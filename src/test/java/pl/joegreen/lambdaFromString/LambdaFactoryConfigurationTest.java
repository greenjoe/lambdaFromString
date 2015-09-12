package pl.joegreen.lambdaFromString;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LambdaFactoryConfigurationTest {

    @Test
    public void addingImportsAndStaticImportsWithMultipleSteps() {
        List<String> imports = Arrays.asList("i1", "i2", "i3");
        List<String> staticImports = Arrays.asList("si1", "si2", "si3");

        LambdaFactoryConfiguration conf = LambdaFactoryConfiguration.get()
                .withStaticImports(staticImports.get(0))
                .withImports(imports.get(0), imports.get(1))
                .withImports(imports.get(2))
                .withStaticImports(staticImports.get(1), staticImports.get(2));
        assertEquals(imports, conf.getImports());
        assertEquals(staticImports, conf.getStaticImports());
    }
    @Test
    public void addingImportsCreatesNewConfiguration() {
        LambdaFactoryConfiguration defaultConf = LambdaFactoryConfiguration.get();
        LambdaFactoryConfiguration withImport = defaultConf.withImports("abc");
        LambdaFactoryConfiguration withStaticImport = defaultConf.withStaticImports("def");
        assertNotEquals(defaultConf, withImport);
        assertNotEquals(defaultConf, withStaticImport);
    }

}
