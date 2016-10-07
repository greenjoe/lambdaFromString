package pl.joegreen.lambdaFromString;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassPathExtractor {
    public static String getJavaPropertyClassPath(){
        return Optional.ofNullable(System.getProperty("java.class.path")).orElse("");
    }

    /**
     * Assumes that the context class loader of the current thread is a URLClassLoader and will throw a runtime exception if it is not.
     */
    public static String getCurrentContextClassLoaderClassPath(){
        URLClassLoader contextClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        return getUrlClassLoaderClassPath(contextClassLoader);
    }

    public static String getUrlClassLoaderClassPath(URLClassLoader classLoader) {
        return Arrays.stream(classLoader.getURLs())
                .map(URL::getFile)
                .map(ClassPathExtractor::urlDecode)
                .collect(Collectors.joining(File.pathSeparator));
    }

    private static String urlDecode(String s){
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            //Too improbable to bother user with checked exceptions
            throw new RuntimeException("Platform does not support UTF-8", e);
        }
    }
}
