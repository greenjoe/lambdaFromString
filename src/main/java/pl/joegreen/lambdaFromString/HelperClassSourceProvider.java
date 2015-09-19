package pl.joegreen.lambdaFromString;

import java.util.List;


/**
 * <strong>This interface may change between versions</strong>.
 * If you use it your code may not work with the next version of the library.
 */
public interface HelperClassSourceProvider {
    String getHelperClassSource(String lambdaType, String lambdaCode, List<String> imports, List<String> staticImports);

    String getHelperClassName();

    String getLambdaReturningMethodName();
}
