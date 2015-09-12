package pl.joegreen.lambdaFromString;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LambdaFactoryTest {


    private LambdaFactory lambdaFactory = LambdaFactory.get();

    @Test
    public void integerIncrement() {
        Function<Integer, Integer> lambda = lambdaFactory.createLambdaUnchecked
                ("i -> i+1", new TypeReference<Function<Integer, Integer>>(){});
        assertTrue(1 == lambda.apply(0));
    }

    @Test
    public void integerMultiply(){
        IntBinaryOperator lambda = lambdaFactory.createLambdaUnchecked
                ("(a,b) -> a*b", new TypeReference<IntBinaryOperator>() {});
        assertEquals(1*2*3*4, IntStream.range(1,5).reduce(lambda).getAsInt());
    }

    @Test
    public void integerToString() {
        Function<Integer, String> lambda = lambdaFactory.createLambdaUnchecked
                ("i -> \"ABC\"+i+\"DEF\"", new TypeReference<Function<Integer, String>>(){});
        assertEquals("ABC101DEF", lambda.apply(101));
    }

    @Test
    public void usingNoArgStringConstructorAsCode(){
        Supplier<String> lambda = lambdaFactory.createLambdaUnchecked(
                "String::new", new TypeReference<Supplier<String>>(){});
        String string = lambda.get();
        assertEquals("", string);
    }

    @Test
    public void lambdaCreatingAnonymousClass(){
        String code = "() -> new Object(){ public String toString(){return \"test\";}}";
        Supplier<Object> lambda = lambdaFactory.createLambdaUnchecked(code, new TypeReference<Supplier<Object>>() {});
        Object object = lambda.get();
        assertEquals("test", object.toString());
    }

    @Test(expected = LambdaCreationRuntimeException.class)
    public void emptyStringAsCode(){
        Supplier<Object> lambda = lambdaFactory.createLambdaUnchecked("", new TypeReference<Supplier<Object>>() {});
    }



}
