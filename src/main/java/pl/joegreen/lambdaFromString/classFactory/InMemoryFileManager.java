package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* JavaFileManager that uses in-memory byte arrays for output.
When java compiler is using that file manager it doesn't create output files anywhere, it just puts compiled code
into byte arrays stored in CompiledClassJavaObject instances. The solution was inspired by
http://javapracs.blogspot.com/2011/06/dynamic-in-memory-compilation-using.html
 */
class InMemoryFileManager extends ForwardingJavaFileManager implements JavaFileManager {

    protected InMemoryFileManager(JavaFileManager fileManager) {
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
}
