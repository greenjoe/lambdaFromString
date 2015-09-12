package pl.joegreen.lambdaFromString.classFactory;


import java.util.Optional;

public class ClassCompilationException extends Exception {
    private final Optional<CompilationDetails> compilationDetails;

    public ClassCompilationException(CompilationDetails compilationDetails) {
        super(compilationDetails.toString());
        this.compilationDetails = Optional.of(compilationDetails);
    }

    public ClassCompilationException(Throwable cause) {
        super(cause);
        this.compilationDetails = Optional.empty();
    }

    public Optional<CompilationDetails> getCompilationDetails() {
        return compilationDetails;
    }


}
