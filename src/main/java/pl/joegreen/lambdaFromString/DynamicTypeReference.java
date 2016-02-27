package pl.joegreen.lambdaFromString;

import java.lang.reflect.Type;

public class DynamicTypeReference extends TypeReference<Object> {

    public DynamicTypeReference(String typeName) {
        super(createTypeFromName(typeName));
    }

    private static Type createTypeFromName(String typeName) {
        return new Type() {
            @Override
            public String getTypeName() {
                return typeName;
            }
        };
    }
}
