package pl.joegreen.lambdaFromString.classFactory;

import java.util.Map;

class InMemoryClassLoader extends ClassLoader {

    private final Map<String, CompiledClassJavaObject> classes;

    public InMemoryClassLoader(Map<String, CompiledClassJavaObject> classes) {
        this.classes = classes;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            return this.getParent().loadClass(name);
        } catch (ClassNotFoundException e) {
            return findClass(name);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = classes.get(name).getBytes();
        return defineClass(name,classBytes,0, classBytes.length);
    }
}
