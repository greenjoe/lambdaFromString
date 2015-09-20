# 1.1
* Adding Maven dependency on Eclipse ECJ compiler and using it by default instead of JDK compiler from tools.jar. Library can now run on pure JRE without JDK. JDK compiler will be still used as fallback if no ECJ is available on classpath for some reason. 
* JavaCompiler choice can be overriden by using LambdaFactoryConfiguration#withJavaCompiler(JavaCompiler) method. 
* If no compiler is available LambdaFactory#get() and LambdaFactory#get(LambdaFactoryConfiguration) are now throwing runtime JavaCompilerNotFoundException. 
* CompilationDetails#getStandardError() can be used to retrieve standard error output from the compiler. 