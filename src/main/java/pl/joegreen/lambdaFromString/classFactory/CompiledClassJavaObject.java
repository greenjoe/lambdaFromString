package pl.joegreen.lambdaFromString.classFactory;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class CompiledClassJavaObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    CompiledClassJavaObject(String className) {
        super(URI.create("mem:///" + className + Kind.CLASS.extension), Kind.CLASS);
    }

    public byte[] getBytes() {
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return byteArrayOutputStream;
    }
}
