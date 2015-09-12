package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassCompilationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaFactory {

    protected final LambdaFactoryConfiguration configuration;

    /**
     * Returns a LambdaFactory instance with default configuration.
     */
    public static LambdaFactory get() {
        return new LambdaFactory(LambdaFactoryConfiguration.get());
    }

    /**
     * Returns a LambdaFactory instance with the given configuration.
     */
    public static LambdaFactory get(LambdaFactoryConfiguration configuration){
        return new LambdaFactory(configuration);
    }

    private LambdaFactory(LambdaFactoryConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates lambda from the given code.
     * @param code source of the lambda as you would write it in Java expression  {TYPE} lambda = ({CODE});
     * @param typeReference a subclass of TypeReference class with the generic argument representing the type of the lambda
     *                      , for example  <br> {@code new TypeReference<Function<Integer,Integer>>(){}; }
     * @param <T> type of the lambda you want to get
     * @throws LambdaCreationException when anything goes wrong (no other exceptions are thrown including runtimes),
     * if the exception was caused by compilation failure it will contain a CompilationDetails instance describing them
     */
    public <T> T createLambda(String code, TypeReference<T> typeReference) throws LambdaCreationException {
        HelperClassSourceProvider helperProvider = configuration.getDefaultHelperClassSourceProvider();
        String helperClassSource = helperProvider.getHelperClassSource(typeReference.toString(), code,
                configuration.getImports(), configuration.getStaticImports());
        try {
            Class<?> helperClass = configuration.getClassFactory()
                    .createClass(helperProvider.getHelperClassName(), helperClassSource);
            Method lambdaReturningMethod = helperClass.getMethod(helperProvider.getLambdaReturningMethodName());
            @SuppressWarnings("unchecked")
            // the whole point of the class template and runtime compilation is to make this cast work well :-)
                    T lambda = (T) lambdaReturningMethod.invoke(null);
            return lambda;
        } catch (ReflectiveOperationException | RuntimeException e) {
            throw new LambdaCreationException(e);
        } catch(ClassCompilationException classCompilationException){
            // knows type of the cause so it get CompilationDetails
            throw new LambdaCreationException(classCompilationException);
        }
    }

    /**
     * Convenience wrapper for {@link #createLambda(String, TypeReference)}
     * which throws unchecked exception instead of checked one.
     * @see #createLambda(String, TypeReference)
     */
    public <T> T createLambdaUnchecked(String code, TypeReference<T> type) {
        try {
            return createLambda(code, type);
        } catch (LambdaCreationException e) {
            throw new LambdaCreationRuntimeException(e);
        }
    }
}
