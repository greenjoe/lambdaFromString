package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassCompilationException;
import pl.joegreen.lambdaFromString.classFactory.CompilationDetails;

import java.util.Optional;

public class LambdaCreationException extends Exception {
    private final Optional<CompilationDetails> compilationDetails;

    public LambdaCreationException(ClassCompilationException classCompilationException){
        super(classCompilationException);
        compilationDetails = classCompilationException.getCompilationDetails();
    }

    public LambdaCreationException(Throwable t) {
        super(t);
        compilationDetails = Optional.empty();
    }

    public Optional<CompilationDetails> getCompilationDetails() {
        return compilationDetails;
    }
}
