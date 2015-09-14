package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * Gives the possibility to compile a class code stored in a string. <br>
 *
 * Solution inspired by: <br>
 * <a href="http://www.javabeat.net/2007/04/the-java-6-0-compiler-api/" >The
 * Java 6.0 Compiler API</a> <br>
 * and by <a href=
 * "http://www.accordess.com/wpblog/an-overview-of-java-compilation-api-jsr-199/"
 * >Generating Java classes dynamically through Java compiler API</a>
 */
public class ClassSourceJavaObject extends SimpleJavaFileObject {
    private final String className;
    private final String classSource;

    public ClassSourceJavaObject(String className, String classCode) {
        super(URI.create("string:///"   + className.replaceAll("\\.", "/")+ Kind.SOURCE.extension), Kind.SOURCE);
        this.className = className;
        this.classSource = classCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return classSource;
    }

    public String getClassName() {
        return className;
    }
}
