# 1.7
* @jonathanschilling and @uhoefel made this release possible, thanks! 
* JDK compiler takes precedence over ECJ.
* Support for higher Java versions (tested on Java 15) with JDK compiler. ECJ + JRE still works only for Java 8.
* ECJ dependency marked as optional in Maven.
* Additional compiler arguments can be added in LambdaFactoryConfiguration. 
* DefaultClassFactory interface changes. 
* Tests migrated to Junit 5.
 
# 1.6
* Apache Commons Lang3 version changed to 3.6. Removed array types description generation workaround as it not necessary anymore.

# 1.5
* Additional maven dependency to Apache Commons Lang3. Fixes type description generation in more complicated cases where inner classes are involved(#8).

# 1.4
* Decoding URLs returned by URLClassLoader in ClassPathExtractor#getCurrentContextClassLoaderClassPath to prevent spaces being converted to %20.

# 1.3
* Custom compilation class path. User can now specify a class path and a class loader used when creating lambda. By default, current JVM class path is used. Thanks to that users can now use custom classes and interfaces in lambda codes (previously only standard library classes were available).

# 1.2
* Dynamic type references. User can now pass a String type name to the constructor of the DynamicTypeReference class instead of statically creating a TypeReference instance with an appropriate generic type.

# 1.1
* Adding Maven dependency on Eclipse ECJ compiler and using it by default instead of JDK compiler from tools.jar. Library can now run on pure JRE without JDK. JDK compiler will be still used as fallback if no ECJ is available on classpath for some reason. 
* JavaCompiler choice can be overriden by using LambdaFactoryConfiguration#withJavaCompiler(JavaCompiler) method. 
* If no compiler is available LambdaFactory#get() and LambdaFactory#get(LambdaFactoryConfiguration) are now throwing runtime JavaCompilerNotFoundException. 
* CompilationDetails#getStandardError() can be used to retrieve standard error output from the compiler. 