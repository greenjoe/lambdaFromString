package pl.joegreen.lambdaFromString;

public class JavaCompilerNotFoundException extends RuntimeException {
    private final static String ERROR_MESSAGE = "Java compiler can't be found by the library and it was not provided in the configuration. " +
            "Either Eclipse ECJ has to be available on classpath or the application has to be running with JDK instead of JRE (tools.jar on the classpath). ";

    public JavaCompilerNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
