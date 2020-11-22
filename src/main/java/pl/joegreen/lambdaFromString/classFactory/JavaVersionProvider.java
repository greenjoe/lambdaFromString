package pl.joegreen.lambdaFromString.classFactory;

public class JavaVersionProvider {

    /**
     * Query the feature version of the JVM this is running on based on the
     * {@code java.version} system property.
     *
     * @return e.g. 6 for {@code java.version}="1.6.0_23" and 9 for "9.0.1"
     * @see <a href="https://stackoverflow.com/a/2591122">https://stackoverflow.com/a/2591122</a>
     */
    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }
}
