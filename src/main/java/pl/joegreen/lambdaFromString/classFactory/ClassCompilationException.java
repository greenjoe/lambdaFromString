package pl.joegreen.lambdaFromString.classFactory;


import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.stream.Collectors;

public class ClassCompilationException extends Exception {
    public ClassCompilationException( String className, String classCode,List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super(String.format("Cannot compile class %s with code:\n%s\nCompiler messages:\n%s",
                className, classCode, diagnosticsListToString(diagnostics)));
    }

    private static String diagnosticsListToString(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        return diagnostics.stream().map(ClassCompilationException::diagnosticToString).collect(Collectors.joining("\n"));
    }

    private static String diagnosticToString(Diagnostic<?> diagnostic) {
        return String.format("%s: %s", diagnostic.getKind(), diagnostic.getMessage(null));
    }


    public ClassCompilationException(Throwable cause) {
        super(cause);
    }
}
