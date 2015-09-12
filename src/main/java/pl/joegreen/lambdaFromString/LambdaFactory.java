package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassCompilationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaFactory {

    protected final LambdaFactoryConfiguration configuration;

    public static LambdaFactory get() {
        return new LambdaFactory(LambdaFactoryConfiguration.get());
    }

    public static LambdaFactory get(LambdaFactoryConfiguration configuration){
        return new LambdaFactory(configuration);
    }

    private LambdaFactory(LambdaFactoryConfiguration configuration) {
        this.configuration = configuration;
    }

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
        } catch (NoSuchMethodException | IllegalAccessException  | InvocationTargetException e) {
            throw new LambdaCreationException(e);
        } catch(ClassCompilationException classCompilationException){
            // knows type of the cause so it get CompilationDetails
            throw new LambdaCreationException(classCompilationException);
        }
    }

    public <T> T createLambdaUnchecked(String code, TypeReference<T> type) {
        try {
            return createLambda(code, type);
        } catch (LambdaCreationException e) {
            throw new LambdaCreationRuntimeException(e);
        }
    }
}
