package pl.joegreen.lambdaFromString;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LambdaFactoryTest {

    public static final String INCORRECT_CODE = "()->a";
    private LambdaFactory defaultFactory = LambdaFactory.get();

    @Test
    public void integerIncrement() {
        Function<Integer, Integer> lambda = defaultFactory.createLambdaUnchecked(
                "i -> i+1", new TypeReference<Function<Integer, Integer>>() {});
        assertTrue(1 == lambda.apply(0));
    }

    @Test
    public void integerMultiply(){
        IntBinaryOperator lambda = defaultFactory.createLambdaUnchecked(
                "(a,b) -> a*b", new TypeReference<IntBinaryOperator>() {});
        assertEquals(1 * 2 * 3 * 4, IntStream.range(1, 5).reduce(lambda).getAsInt());
    }

    @Test
    public void integerToString() {
        Function<Integer, String> lambda = defaultFactory.createLambdaUnchecked(
                "i -> \"ABC\"+i+\"DEF\"", new TypeReference<Function<Integer, String>>() {});
        assertEquals("ABC101DEF", lambda.apply(101));
    }

    @Test
    public void useImportToAddBigDecimals() throws LambdaCreationException {
        LambdaFactory factory = LambdaFactory.get(
                LambdaFactoryConfiguration.get().withImports(BigDecimal.class));
        BiFunction<BigDecimal, BigDecimal, BigDecimal> lambda = factory.createLambda(
                "(a,b) -> a.add(b)", new TypeReference<BiFunction<BigDecimal, BigDecimal, BigDecimal>>() {});
        assertEquals(new BigDecimal("11"), lambda.apply(BigDecimal.ONE, BigDecimal.TEN));
    }

    @Test
    public void useStaticImportToIncrementBigDecimals(){
        LambdaFactory factory = LambdaFactory.get(
                LambdaFactoryConfiguration.get()
                .withImports(BigDecimal.class)
                .withStaticImports("java.math.BigDecimal.ONE"));
        Function<BigDecimal, BigDecimal> lambda = factory.createLambdaUnchecked(
                "a -> a.add(ONE)", new TypeReference< Function<BigDecimal, BigDecimal>>() {});
        assertEquals(new BigDecimal("11"), lambda.apply(BigDecimal.TEN));
    }

    @Test
    public void usingNoArgStringConstructorAsCode(){
        Supplier<String> lambda = defaultFactory.createLambdaUnchecked(
                "String::new", new TypeReference<Supplier<String>>() {});
        String string = lambda.get();
        assertEquals("", string);
    }

    @Test
    public void creatingIntegerInsteadOfLambda(){
        Integer result = defaultFactory.createLambdaUnchecked("1+2", new TypeReference<Integer>() {});
        assertEquals(3, result.intValue());
    }

    @Test
    public void lambdaCreatingAnonymousClass(){
        String code = "() -> new Object(){ public String toString(){return \"test\";}}";
        Supplier<Object> lambda = defaultFactory.createLambdaUnchecked(code, new TypeReference<Supplier<Object>>() {});
        Object object = lambda.get();
        assertEquals("test", object.toString());
    }


    @Test(expected = LambdaCreationException.class)
    public void exceptionContainsCompilationDetailsWhenCompilationFails() throws LambdaCreationException {
        try {
            defaultFactory.createLambda(INCORRECT_CODE, new TypeReference<Supplier<Integer>>() {});
        }catch (LambdaCreationException ex){
            assertTrue(ex.getCompilationDetails().isPresent());
            assertFalse(ex.getCompilationDetails().get().getDiagnostics().isEmpty());
            throw ex;
        }
    }

    @Test(expected = LambdaCreationRuntimeException.class)
    public void runtimeExceptionContainsNestedCheckedException() throws LambdaCreationException {
        try {
            defaultFactory.createLambdaUnchecked(INCORRECT_CODE, new TypeReference<Supplier<Integer>>() {});
        }catch (LambdaCreationRuntimeException ex){
            assertNotNull(ex.getNestedCheckedException());
            throw ex;
        }
    }

    @Test(expected = LambdaCreationRuntimeException.class)
    public void emptyCodeFailsWithException(){
        defaultFactory.createLambdaUnchecked("", new TypeReference<Supplier<Object>>() {});
    }

}
