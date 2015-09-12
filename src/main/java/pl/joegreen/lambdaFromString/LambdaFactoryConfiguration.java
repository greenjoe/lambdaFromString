package pl.joegreen.lambdaFromString;

import pl.joegreen.lambdaFromString.classFactory.ClassFactory;
import pl.joegreen.lambdaFromString.classFactory.DefaultClassFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

public class LambdaFactoryConfiguration {


    private HelperClassSourceProvider helperClassSourceProvider;
    private ClassFactory classFactory;
    private List<String> staticImports;
    private List<String> imports;


    public static LambdaFactoryConfiguration get() {
        return new LambdaFactoryConfiguration();
    }

    private LambdaFactoryConfiguration() {
        helperClassSourceProvider = new DefaultHelperClassSourceProvider();
        classFactory = new DefaultClassFactory();
        staticImports = Collections.unmodifiableList(new ArrayList<>());
        imports = Collections.unmodifiableList(new ArrayList<>());
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

    public LambdaFactoryConfiguration withClassFactory(ClassFactory classFactory){
        return copy().setClassFactory(classFactory);
    }

    public LambdaFactoryConfiguration withHelperClassSourceCreator(HelperClassSourceProvider creator){
        return copy().setDefaultHelperClassSourceProvider(creator);
    }

    public LambdaFactoryConfiguration withImports(String... newImports){
        return copy().setImports(listWithNewElements(imports, newImports));
    }

    public LambdaFactoryConfiguration withImports(Class<?>... newImports){
        String[] stringImports = Arrays.stream(newImports).map(Class::getCanonicalName).toArray(String[]::new);
        return withImports(stringImports);
    }



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
        return imports.equals(that.imports);

    }

    @Override
    public int hashCode() {
        int result = helperClassSourceProvider.hashCode();
        result = 31 * result + classFactory.hashCode();
        result = 31 * result + staticImports.hashCode();
        result = 31 * result + imports.hashCode();
        return result;
    }
}
