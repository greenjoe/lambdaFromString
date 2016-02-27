package pl.joegreen.lambdaFromString;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * This class implements the idea of <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html">super type tokens</a>
 * in Java. The only correct usage is to create an anonymous subclass that has the generic type set to the type
 * it should represent. For example, to represent function from Integer to Integer type: <br>
 *   <pre>
 *   {@code
 *    TypeReference<Function<Integer,Integer>> ref = new TypeReference<Function<Integer,Integer>>(){};

 *   }
 *   </pre>
 *
 */
public abstract class TypeReference<T> {
    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (!(superClass instanceof ParameterizedType)) {
            throw new IllegalArgumentException("TypeReference defined without actual generic type argument");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    protected TypeReference(Type type) {
        this.type = type;
    }

    public final Type getType() {
        return type;
    }

    @Override
    public final String toString() {
        return type.getTypeName();
    }


}

