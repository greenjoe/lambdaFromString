package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.JavaCompiler;
import java.util.List;


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
     * @param compilationClassPath class path that should be used when compiling the class code
     * @param javaVersion java version to be used
     * @param additionalCompilerOptions additional options to pass to the compiler
     * @param parentClassLoader parent class loader that should be used to load external classes
     * @throws ClassCompilationException in case of compilation failure, it should contain CompilationDetails
     *                                   instance describing errors if possible
     */
    Class<?> createClass(String fullClassName, String sourceCode, JavaCompiler compiler,
                         int javaVersion, String compilationClassPath,
                         List<String> additionalCompilerOptions, ClassLoader parentClassLoader) throws ClassCompilationException;
}
