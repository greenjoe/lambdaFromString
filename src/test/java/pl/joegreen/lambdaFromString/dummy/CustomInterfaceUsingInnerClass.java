package pl.joegreen.lambdaFromString.dummy;
@FunctionalInterface
public interface CustomInterfaceUsingInnerClass {
    InnerClass createInnerClass();

    class InnerClass{
    }
}
