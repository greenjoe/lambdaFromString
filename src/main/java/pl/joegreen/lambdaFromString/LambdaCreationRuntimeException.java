package pl.joegreen.lambdaFromString;

public class LambdaCreationRuntimeException extends RuntimeException {
    private final LambdaCreationException nestedCheckedException;

    public LambdaCreationRuntimeException(LambdaCreationException ex) {
        super(ex.getCause());
        nestedCheckedException = ex;
    }

    public LambdaCreationException getNestedCheckedException() {
        return nestedCheckedException;
    }
}
