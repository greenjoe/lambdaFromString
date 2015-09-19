package pl.joegreen.lambdaFromString;

import javax.tools.JavaCompiler;
import java.lang.reflect.Field;
import java.util.Optional;

public class DefaultCompilerSetter {

    public static void setDefaultCompiler(JavaCompiler compiler) {
        try {
            Field defaultCompilerField = LambdaFactory.class.getDeclaredField("DEFAULT_COMPILER");
            defaultCompilerField.setAccessible(true);
            defaultCompilerField.set(null, Optional.of(compiler));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
