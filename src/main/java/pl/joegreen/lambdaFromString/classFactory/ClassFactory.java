package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.JavaCompiler;
import java.util.Collection;


/**
 * <strong>This interface may change between versions</strong>.
 * If you use it your code may not work with the next version of the library.
 */
public interface ClassFactory {
    /**
     * Compiles class from its source code and loads it into JVM.
     *
     * @param fullClassName qualified name of the class
     * @param sourceCode    code of the class
     * @param compiler      java compiler that should be used to compile class code
     * @throws ClassCompilationException in case of compilation failure, it should contain CompilationDetails
     *                                   instance describing errors if possible
     */
    Class<?> createClass(String fullClassName, String sourceCode, JavaCompiler compiler) throws ClassCompilationException;
}
