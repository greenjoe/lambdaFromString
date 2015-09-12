package pl.joegreen.lambdaFromString;

public class LambdaCreationRuntimeException extends RuntimeException {
    public LambdaCreationRuntimeException(LambdaCreationException ex) {
        super(ex.getCause());
    }
}
