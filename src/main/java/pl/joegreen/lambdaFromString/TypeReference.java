package pl.joegreen.lambdaFromString;

import org.apache.commons.lang3.reflect.TypeUtils;

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
    public String toString() {
        return fixCommonsLangArrayTypeBug(TypeUtils.toString(type));
    }

    private String fixCommonsLangArrayTypeBug(String typeText) {
        /* The bug is already fixed in Apache Commons master branch:
         * https://github.com/apache/commons-lang/commit/1661e5519c4836a5a940b13b7797263443156fc9
         * Unfortunately it's not fixed in the latest version (3.5) and it doesn't look like there will be a new release soon,
         * so it's better to temporarily fix it here than to rely on snapshot build of Apache Commons Lang3.
         */
        if (typeText.contains("[L")) {
            return typeText.replace("[L", "")
                    .replace(";", "[]")
                    .replace("$", ".");
        } else {
            return typeText;
        }
    }


}

