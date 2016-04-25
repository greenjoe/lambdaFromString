package pl.joegreen.lambdaFromString;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassPathExtractor {
    public static String getJavaPropertyClassPath(){
        return Optional.ofNullable(System.getProperty("java.class.path")).orElse("");
    }

    public static String getCurrentContextClassLoaderClassPath(){
        URLClassLoader contextClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        return Arrays.stream(contextClassLoader.getURLs())
                .map(URL::getFile)
                .collect(Collectors.joining(File.pathSeparator));
    }
}
