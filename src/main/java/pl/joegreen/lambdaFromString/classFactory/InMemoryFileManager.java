package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* JavaFileManager that uses in-memory byte arrays for output.
When java compiler is using that file manager it doesn't create output files anywhere, it just puts compiled code
into byte arrays stored in CompiledClassJavaObject instances. The solution was inspired by
http://javapracs.blogspot.com/2011/06/dynamic-in-memory-compilation-using.html
 */
class InMemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> implements StandardJavaFileManager {

    protected InMemoryFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    private Map<String, CompiledClassJavaObject> classes = new HashMap<>();

    public Map<String,CompiledClassJavaObject> getClasses(){
        return new HashMap<>(classes);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            classes.putIfAbsent(className, new CompiledClassJavaObject(className));
            return classes.get(className);
        }
        throw new IOException(this.getClass().getSimpleName() + " cannot open files for writing. " +
                "Only .class output is supported and stored in memory.");
    }

    @Override
    public void close() {
        try {
            super.close();
        }catch (IOException ex){
            // if we only work in-memory so IOException on close should not matter
        }
    }


    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files) {
        return fileManager.getJavaFileObjectsFromFiles(files);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
        return fileManager.getJavaFileObjects(files);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) {
        return fileManager.getJavaFileObjectsFromStrings(names);
    }

    @Override
    public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names) {
        return fileManager.getJavaFileObjects(names);
    }

    @Override
    public void setLocation(Location location, Iterable<? extends File> path) throws IOException {
        fileManager.setLocation(location, path);
    }

    @Override
    public Iterable<? extends File> getLocation(Location location) {
        return fileManager.getLocation(location);
    }
}
