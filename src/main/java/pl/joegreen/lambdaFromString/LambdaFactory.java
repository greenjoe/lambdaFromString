package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassCompilationException;
import pl.joegreen.lambdaFromString.classFactory.ClassFactory;
import pl.joegreen.lambdaFromString.classFactory.DefaultClassFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaFactory {

    public static LambdaFactory get() {
        return new LambdaFactory();
    }

    private final HelperClassSourceCreator helperClassSourceCreator;
    private final ClassFactory classFactory;


    private LambdaFactory() {
        this.helperClassSourceCreator = new HelperClassSourceCreator();
        this.classFactory = new DefaultClassFactory();
    }

    public <T> T createLambda(String code, TypeReference<T> typeReference) throws LambdaCreationException {
        String helperClassSource = helperClassSourceCreator.getHelperClassSource(typeReference.toString(), code);
        try {
            Class<?> helperClass = classFactory.createClass(
                    helperClassSourceCreator.getHelperClassName(), helperClassSource
            );
            Method lambdaReturningMethod = helperClass.getMethod(helperClassSourceCreator.getLambdaReturningMethodName());
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
