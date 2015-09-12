package pl.joegreen.lambdaFromString;

import java.util.List;

public interface HelperClassSourceProvider {
    String getHelperClassSource(String lambdaType, String lambdaCode, List<String> imports, List<String> staticImports);

    String getHelperClassName();

    String getLambdaReturningMethodName();
}
