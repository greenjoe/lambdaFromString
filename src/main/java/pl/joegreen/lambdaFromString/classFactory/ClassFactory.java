package pl.joegreen.lambdaFromString.classFactory;

public interface ClassFactory {
    /**
     * Compiles class from its source code and loads it into JVM.
     * @param fullClassName qualified name of the class
     * @param sourceCode code of the class
     * @throws ClassCompilationException in case of compilation failure, it should contain CompilationDetails
     * instance describing errors if possible
     */
    Class<?> createClass(String fullClassName, String sourceCode) throws ClassCompilationException;
}
