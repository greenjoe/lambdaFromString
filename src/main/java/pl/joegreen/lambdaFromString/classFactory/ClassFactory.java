package pl.joegreen.lambdaFromString.classFactory;

public interface ClassFactory {
    Class<?> createClass(String fullClassName, String sourceCode) throws ClassCompilationException;
}
