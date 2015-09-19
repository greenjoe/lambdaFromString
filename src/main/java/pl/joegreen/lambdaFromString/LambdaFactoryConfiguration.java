package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassFactory;
import pl.joegreen.lambdaFromString.classFactory.DefaultClassFactory;

import javax.tools.JavaCompiler;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class LambdaFactoryConfiguration {


    private HelperClassSourceProvider helperClassSourceProvider;
    private ClassFactory classFactory;
    private List<String> staticImports;
    private List<String> imports;
    private Optional<JavaCompiler> javaCompiler;

    public static LambdaFactoryConfiguration get() {
        return new LambdaFactoryConfiguration();
    }

    private LambdaFactoryConfiguration() {
        helperClassSourceProvider = new DefaultHelperClassSourceProvider();
        classFactory = new DefaultClassFactory();
        staticImports = Collections.unmodifiableList(new ArrayList<>());
        imports = Collections.unmodifiableList(new ArrayList<>());
        javaCompiler = Optional.empty();
    }


    private LambdaFactoryConfiguration copy() {
        return new LambdaFactoryConfiguration()
                .setClassFactory(classFactory)
                .setDefaultHelperClassSourceProvider(helperClassSourceProvider)
                .setImports(imports)
                .setStaticImports(staticImports);
    }

    public HelperClassSourceProvider getDefaultHelperClassSourceProvider() {
        return helperClassSourceProvider;
    }

    public ClassFactory getClassFactory() {
        return classFactory;
    }

    public List<String> getStaticImports() {
        return staticImports;
    }

    public List<String> getImports() {
        return imports;
    }

    public Optional<JavaCompiler> getJavaCompiler() {
        return javaCompiler;
    }
    /**
     *  Changes classFactory which is responsible for compiling the helper class and loading it into the memory. <br>
     *  Should be used only in rare cases when you cannot get the exact functionality
     *  you want from the library and want to change some of its inner behavior. The interface accepted by this method
     *  can change between library versions and cause compilation errors if you decide to use it.
     */
    public LambdaFactoryConfiguration withClassFactory(ClassFactory classFactory){
        return copy().setClassFactory(classFactory);
    }

    /**
     *  Changes helperClassSourceProvider which provides a code template for the class to be compiled. <br>
     *  Should be used only in rare cases when you cannot get the exact functionality
     *  you want from the library and want to change some of its inner behavior. The interface accepted by this method
     *  can change between library versions and cause compilation errors if you decide to use it.
     */
    public LambdaFactoryConfiguration withHelperClassSourceProvider(HelperClassSourceProvider helperSourceProvider){
        return copy().setDefaultHelperClassSourceProvider(helperSourceProvider);
    }

    /**
     * Overrides default JavaCompiler instance. <br>It can be used to force using JDK compiler when the Eclipse Compiler
     * is also available. Completely different compiler instance can also be passed but in that case it is possible
     * that some changes will have to be made in the class factory ({@link #withClassFactory(ClassFactory)}).
     */
    public LambdaFactoryConfiguration withJavaCompiler(JavaCompiler javaCompiler){
        return copy().setJavaCompiler(javaCompiler);
    }

    /**
     * Adds imports that will be visible in the lambda code. Each string should consist of what is
     * normally written between "import " and ";" in the import statement. Imports can contain * wildcards.
     */
    public LambdaFactoryConfiguration withImports(String... newImports){
        return copy().setImports(listWithNewElements(imports, newImports));
    }

    /**
     * Adds imports that will be visible in the lambda code. To add an import with * wildcard
     * please use{@link #withImports(String...)}.
     */
    public LambdaFactoryConfiguration withImports(Class<?>... newImports){
        String[] stringImports = Arrays.stream(newImports).map(Class::getCanonicalName).toArray(String[]::new);
        return withImports(stringImports);
    }

    /**
     * Adds static imports that will be visible in the lambda code. Each string should consist of what is
     * normally written between "import static " and ";" in the import statement. Imports can contain * wildcards.
     */
    public LambdaFactoryConfiguration withStaticImports(String... newStaticImports){
        return copy().setStaticImports(listWithNewElements(staticImports, newStaticImports));
    }

    private LambdaFactoryConfiguration setClassFactory(ClassFactory classFactory) {
        this.classFactory = classFactory;
        return this;
    }

    private LambdaFactoryConfiguration setStaticImports(List<String> staticImports) {
        this.staticImports = staticImports;
        return this;
    }

    private LambdaFactoryConfiguration setImports(List<String> imports) {
        this.imports = imports;
        return this;
    }

    private LambdaFactoryConfiguration setDefaultHelperClassSourceProvider(HelperClassSourceProvider helperClassSourceProvider) {
        this.helperClassSourceProvider = helperClassSourceProvider;
        return this;
    }

    private LambdaFactoryConfiguration setJavaCompiler(JavaCompiler javaCompiler) {
        this.javaCompiler = Optional.of(javaCompiler);
        return this;
    }

    private static <T> List<T> listWithNewElements(List<T> oldList, T... newElements){
        return Collections.unmodifiableList(concat(oldList.stream(), Arrays.stream(newElements)).collect(toList()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LambdaFactoryConfiguration that = (LambdaFactoryConfiguration) o;

        if (!helperClassSourceProvider.equals(that.helperClassSourceProvider)) return false;
        if (!classFactory.equals(that.classFactory)) return false;
        if (!staticImports.equals(that.staticImports)) return false;
        if (!imports.equals(that.imports)) return false;
        return javaCompiler.equals(that.javaCompiler);

    }

    @Override
    public int hashCode() {
        int result = helperClassSourceProvider.hashCode();
        result = 31 * result + classFactory.hashCode();
        result = 31 * result + staticImports.hashCode();
        result = 31 * result + imports.hashCode();
        result = 31 * result + javaCompiler.hashCode();
        return result;
    }
}
