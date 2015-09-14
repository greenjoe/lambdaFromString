package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationDetails {
    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;
    private final String className;
    private final String sourceCode;

    public CompilationDetails(String className, String sourceCode, List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        this.diagnostics = diagnostics;
        this.className = className;
        this.sourceCode = sourceCode;
    }

    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }

    public String getClassName() {
        return className;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    @Override
    public String toString() {
        return String.format("Class compilation details:\nClass name: %s\nClass source:\n%s\nCompiler messages:\n%s",
                className, sourceCode, diagnosticsListToString(diagnostics));
    }

    private static String diagnosticsListToString(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        return diagnostics.stream().map(CompilationDetails::diagnosticToString).collect(Collectors.joining("\n"));
    }

    private static String diagnosticToString(Diagnostic<?> diagnostic) {
        return String.format("%s: %s", diagnostic.getKind(), diagnostic.getMessage(null));
    }



}
