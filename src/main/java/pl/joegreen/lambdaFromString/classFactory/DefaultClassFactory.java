package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.Map;

public class DefaultClassFactory implements ClassFactory {
    @Override
    public Class<?> createClass(String fullClassName, String sourceCode) throws ClassCompilationException {
        try {
            JavaCompiler compiler = findJavaCompiler();
            Map<String, CompiledClassJavaObject> compiledClassesBytes = compileClasses(compiler, fullClassName, sourceCode);
            return loadClass(fullClassName, compiledClassesBytes);
        } catch (ClassNotFoundException | CannotFindJavaCompilerException | RuntimeException e) {
            throw new ClassCompilationException(e);
        }
    }

    protected Class<?> loadClass(String fullClassName, Map<String, CompiledClassJavaObject> compiledClassesBytes) throws ClassNotFoundException {
        return (new InMemoryClassLoader(compiledClassesBytes)).loadClass(fullClassName);
    }

    protected Map<String, CompiledClassJavaObject> compileClasses(JavaCompiler compiler, String fullClassName, String sourceCode) throws ClassCompilationException {
        ClassSourceJavaObject classSourceObject = new ClassSourceJavaObject(fullClassName, sourceCode);
        Map<String, CompiledClassJavaObject> compiledClassesBytes;
               /*
         * diagnosticListener = null -> compiler's default reporting
		 * diagnostics; locale = null -> default locale to format diagnostics;
		 * charset = null -> uses platform default charset
		 */
        try (InMemoryFileManager stdFileManager = new InMemoryFileManager(compiler.getStandardFileManager(null, null, null))) {

            DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
            JavaCompiler.CompilationTask compilationTask = compiler.getTask(null,
                    stdFileManager, diagnosticsCollector,
                    Collections.emptyList(), null, Collections.singletonList(classSourceObject));

            boolean status = compilationTask.call();
            if (!status) {
                throw new ClassCompilationException(new CompilationDetails(fullClassName, sourceCode, diagnosticsCollector.getDiagnostics()));
            }
            return stdFileManager.getClasses();
        }
    }

    private JavaCompiler findJavaCompiler() throws CannotFindJavaCompilerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new CannotFindJavaCompilerException();
        }
        return compiler;
    }

    static class CannotFindJavaCompilerException extends Exception {
        CannotFindJavaCompilerException() {
            super("ToolProvider.getSystemJavaCompiler() returned null - please check your JDK version and/or tools.jar availability.");
        }
    }
}


