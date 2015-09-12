package pl.joegreen.lambdaFromString;

import java.io.IOException;
import java.io.InputStream;

class HelperClassSourceCreator {
    private final String template;

    public HelperClassSourceCreator() {
        this.template = getClasspathResourceAsString("helperClassTemplate.txt");
    }

    public String getHelperClassSource(String lambdaType, String lambdaCode) {
        return String.format(template, lambdaType, lambdaCode);
    }

    public String getHelperClassName(){
        return "LambdaFromStringHelper";
    }

    public String getLambdaReturningMethodName(){
        return "getLambda";
    }

    //uses the "stupid scanner trick" https://weblogs.java.net/blog/2004/10/24/stupid-scanner-tricks
    private static String getClasspathResourceAsString(String resourcePath) {
        try (InputStream is = HelperClassSourceCreator.class.getClassLoader().getResourceAsStream(resourcePath);
             java.util.Scanner s = new java.util.Scanner(is)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        } catch (IOException e) {
            throw new RuntimeException(e);
            //should never happen and if it does then all RuntimeExceptions should be caught in LambdaFactory
        }
    }
}
