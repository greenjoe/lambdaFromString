package pl.joegreen.lambdaFromString;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LambdaFactoryTest {


    public static final String INCORRECT_CODE = "()->a";
    private LambdaFactory factory = LambdaFactory.get();

    @Test
    public void integerIncrement() {
        Function<Integer, Integer> lambda = factory.createLambdaUnchecked
                ("i -> i+1", new TypeReference<Function<Integer, Integer>>() {});
        assertTrue(1 == lambda.apply(0));
    }

    @Test
    public void integerMultiply(){
        IntBinaryOperator lambda = factory.createLambdaUnchecked
                ("(a,b) -> a*b", new TypeReference<IntBinaryOperator>() {});
        assertEquals(1 * 2 * 3 * 4, IntStream.range(1, 5).reduce(lambda).getAsInt());
    }

    @Test
    public void integerToString() {
        Function<Integer, String> lambda = factory.createLambdaUnchecked
                ("i -> \"ABC\"+i+\"DEF\"", new TypeReference<Function<Integer, String>>() {});
        assertEquals("ABC101DEF", lambda.apply(101));
    }

    @Test
    public void usingNoArgStringConstructorAsCode(){
        Supplier<String> lambda = factory.createLambdaUnchecked(
                "String::new", new TypeReference<Supplier<String>>() {});
        String string = lambda.get();
        assertEquals("", string);
    }

    @Test
    public void creatingIntegerInsteadOfLambda(){
        Integer result = factory.createLambdaUnchecked("1+2", new TypeReference<Integer>() {});
        assertEquals(3, result.intValue());
    }

    @Test
    public void lambdaCreatingAnonymousClass(){
        String code = "() -> new Object(){ public String toString(){return \"test\";}}";
        Supplier<Object> lambda = factory.createLambdaUnchecked(code, new TypeReference<Supplier<Object>>() {});
        Object object = lambda.get();
        assertEquals("test", object.toString());
    }


    @Test(expected = LambdaCreationException.class)
    public void exceptionContainsCompilationDetailsWhenCompilationFails() throws LambdaCreationException {
        try {
            factory.createLambda(INCORRECT_CODE, new TypeReference<Supplier<Integer>>() {});
        }catch (LambdaCreationException ex){
            assertTrue(ex.getCompilationDetails().isPresent());
            assertFalse(ex.getCompilationDetails().get().getDiagnostics().isEmpty());
            throw ex;
        }
    }

    @Test(expected = LambdaCreationRuntimeException.class)
    public void runtimeExceptionContainsNestedCheckedException() throws LambdaCreationException {
        try {
            factory.createLambdaUnchecked(INCORRECT_CODE, new TypeReference<Supplier<Integer>>() {});
        }catch (LambdaCreationRuntimeException ex){
            assertNotNull(ex.getNestedCheckedException());
            throw ex;
        }
    }

    @Test(expected = LambdaCreationRuntimeException.class)
    public void emptyCodeFailsWithException(){
        factory.createLambdaUnchecked("", new TypeReference<Supplier<Object>>() {});
    }

}
