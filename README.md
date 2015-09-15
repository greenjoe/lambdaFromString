# Java 8 Lambda From String

Sometimes you may want to load a function from your application configuration file instead of a single value. 
In that case you should probably use Nashorn JavaScript engine that comes with Java 8, as advised by [this stackoverflow answer](http://stackoverflow.com/a/22291144). 
However if, for some reason, you would like to stick to Java then this library might be a good choice for you. 

Let's go straight to the code examples: 
```java
LambdaFactory lambdaFactory = LambdaFactory.get();

Function<Integer, Integer> increase = lambdaFactory.createLambda(
        "i -> i+1", new TypeReference<Function<Integer, Integer>>(){});
assertTrue(1 == increase.apply(0));

IntBinaryOperator multiply = lambdaFactory.createLambda(
        "(a,b) -> a*b", new TypeReference<IntBinaryOperator>(){});
assertEquals(1*2*3*4, IntStream.range(1,5).reduce(multiply).getAsInt());

Function<Integer, String> decorate = lambdaFactory.createLambda(
        "i -> \"ABC\"+i+\"DEF\"", new TypeReference<Function<Integer, String>>(){});
assertEquals("ABC101DEF", decorate.apply(101));
```
By default only java.util.function.* is imported by the class, as it is needed by the library itself. If you would like to import additional classes, you can specify imports in the configuration, as shown below. Please note that only Java standard library classes are available on the compilation classpath in the current version of library, so you cannot import your own classes. 

Imports can be passed as `Class<?>` instances or strings (string form is the only way to use * wildcard). Static imports are also supported and can be passed as strings. 

```java
LambdaFactory factory = LambdaFactory.get(
        LambdaFactoryConfiguration.get().withImports(BigDecimal.class));
BiFunction<BigDecimal, BigDecimal, BigDecimal> lambda = factory.createLambda(
        "(a,b) -> a.add(b)", new TypeReference<BiFunction<BigDecimal, BigDecimal, BigDecimal>>() {});
assertEquals(new BigDecimal("11"), lambda.apply(BigDecimal.ONE, BigDecimal.TEN));
```

`createLambda` throws a checked `LambdaCreationException`. If the exception is caused by compilation errors, it will contain a `CompilationDetails` instance with all messages from the compiler. Class source and compilation errors are also provided as the exception message so they will appear in the stacktrace. It is the only exception that can be thrown by that method, all other runtime exceptions are being caught and wrapped by this one. If you don't like checked exceptions you can call `createLambdaUnchecked` which is a proxy to `createLambda` that throws `LambdaCreationRuntimeException` instead. 

Unfortunately the TypeReference class has to be subclassed when creating an instance. It looks like it's the only way to get generic type 
information at runtime and it's called [super type tokens](http://gafter.blogspot.com/2006/12/super-type-tokens.html). 
Information about the expected type is needed so that the compiler can use it's type inference
when compiling lambda code and users don't have to write those types inside the actual lambda code. 

The compilation process takes time (on my laptop: first call ~1s, subsequent calls ~0.1s) so it probably should not be used in places where performance matters.
The library is rather intended to be used once during the configuration reading process when the application starts. 
LambdaFactory instances are threadsafe. 


## Requirements 
* Java 8
* Using JDK to run the application, JRE is not enough

JDK provides a tools.jar file that is required to use the Java Compiler API. 

## Download

You can get the library from Maven Central:
```xml
<dependency>
	<groupId>pl.joegreen</groupId>
	<artifactId>lambda-from-string</artifactId>
	<version>1.0</version>
</dependency>
```
It has **no external dependencies** so you can also just [download the jar](http://repo1.maven.org/maven2/pl/joegreen/lambda-from-string/1.0/lambda-from-string-1.0.jar) and add it to your classpath. 

## How it works? 

It actually compiles a new class using the Java Compiler API and some tricks to perform the whole
compilation process in memory. The source that is compiled looks like this:

```java
{IMPORTS}
public class LambdaFromStringHelper {
    public static {TYPE} getLambda() {return ({LAMBDA_CODE});}
}
```
The class is loaded by a custom class loader and then reflection is used to call 'getLambda' to get the actual lambda. 


## Contribution
Issues and pull requests are welcome. By contributing, you agree to allow the project owner to license your work under the the terms of the [MIT license](LICENSE). 
