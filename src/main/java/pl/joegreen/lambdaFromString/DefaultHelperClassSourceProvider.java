package pl.joegreen.lambdaFromString;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultHelperClassSourceProvider implements HelperClassSourceProvider {
    private final String template;

    public DefaultHelperClassSourceProvider() {
        this.template = getClasspathResourceAsString("helperClassTemplate.txt");
    }

    @Override
    public String getHelperClassSource(String lambdaType, String lambdaCode, List<String> imports, List<String> staticImports) {
        String importStatements = generateImportStatements(imports, staticImports);
        return String.format(template, importStatements, lambdaType, lambdaCode);
    }

    @Override
    public String getHelperClassName(){
        return "LambdaFromStringHelper";
    }

    @Override
    public String getLambdaReturningMethodName(){
        return "getLambda";
    }

    private String generateImportStatements(List<String> imports, List<String> staticImports){
        return Stream.concat(imports.stream(), staticImports.stream().map(s -> "static " + s))
                .map(s -> "import " + s + ";")
                .collect(Collectors.joining("\n"));
    }


    //uses the "stupid scanner trick" https://weblogs.java.net/blog/2004/10/24/stupid-scanner-tricks
    private static String getClasspathResourceAsString(String resourcePath) {
        try (InputStream is = DefaultHelperClassSourceProvider.class.getClassLoader().getResourceAsStream(resourcePath);
             java.util.Scanner s = new java.util.Scanner(is)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
            //should never happen and if it does then all RuntimeExceptions should be caught in LambdaFactory
        }
    }
}
