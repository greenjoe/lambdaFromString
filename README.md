# Java 8 &ndash; Lambda from String

Sometimes you may want to load a function from your application configuration file instead of a single value. 
In that case you should probably use Nashorn JavaScript engine that comes with Java 8, as advised by [this stackoverflow answer](http://stackoverflow.com/a/22291144). 
However if, for some reason, you would like to stick to Java then this library might be a good choice for you. 

## Reasons to use it
LambdaFromString is a library that can generate a Java 8 lambda object at runtime from its code stored in a String. 
* Runtime behavior change &ndash; you can change the way your applicaton behaves at runtime, without recompiling it or even shutting it down.  
* Quick prototyping &ndash; you can quickly prototype different behaviors by just changing the code of the lambda stored in some file and making your application reload it on demand.
* Wider configuration possibilities &ndash; you can let your users freely specify relation between some X and Y in the configuration file without constraining them by some predefined choices (let them write a code of `Function<Integer,Integer>` for example).
* Simplicity &ndash; you can get what you would normally achieve by manually compiling a new class and loading it with a custom classloader in your application (but the library does it for you). 

The library assumes that the lambda code is not malicious and doesn't validate it. Creating and executing lambdas based on external code can be really harmful so please make sure that it will be secure in your case. 

## Code examples 
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
(You can visit [tests directory](https://github.com/greenjoe/lambdaFromString/tree/master/src/test/java/pl/joegreen/lambdaFromString) to see additional examples.)

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


## Requirements and downloads 
The library works only with __Java 8__.
You can get it from Maven Central:
```xml
<dependency>
	<groupId>pl.joegreen</groupId>
	<artifactId>lambda-from-string</artifactId>
	<version>1.1</version>
</dependency>
```
It has only one external Maven dependency: [Eclipse JDT Core Batch Compiler](http://mvnrepository.com/artifact/org.eclipse.jdt.core.compiler/ecj). That dependency was added because Java compiler is a part of JDK (located in tools.jar) and it's not available in pure JRE. When client applications were running on JRE then no Java compiler was available at runtime and the LambdaFromString library failed to compile lambda code. Eclipse ECJ makes it possible to use LambdaFromString even in cases when only JRE is available at runtime. 

### Running without Eclipse ECJ 
If you are sure that your application will be running on JDK and you want to use default compiler instead of Eclipse ECJ you can exclude that dependency in Maven by adding exclusions inside the `<dependency>` tag:  
```xml
<exclusions>
	<exclusion>
		<groupId>org.eclipse.jdt.core.compiler</groupId>
		<artifactId>ecj</artifactId>
	</exclusion>
</exclusions>
```
In that case you can also use lambdaFromString without Maven by just downloading a [single jar](http://central.maven.org/maven2/pl/joegreen/lambda-from-string/1.1/lambda-from-string-1.1.jar). 

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
