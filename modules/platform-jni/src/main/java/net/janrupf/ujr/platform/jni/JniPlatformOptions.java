package net.janrupf.ujr.platform.jni;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Options of the JNI platform provider.
 * <p>
 * The Ultralight runtime is not bundled: its license only allows applications to pass it on to their users, so
 * applications ship it themselves and tell Ultralight Java Reborn where it is with
 * {@link #ultralightDirectory(Path)}.
 */
public final class JniPlatformOptions {
    private Path ultralightDirectory; // If null, taken from the ujr.ultralightDirectory property or ULTRALIGHT_DIR
    private ClassLoader nativesClassLoader; // If null, the class loader of Ultralight Java Reborn is used

    /**
     * Sets the directory of the Ultralight runtime.
     * <p>
     * The directory contains the Ultralight shared libraries in {@code bin/} and its resources in
     * {@code resources/}, laid out like the Ultralight SDK.
     *
     * @param ultralightDirectory the directory of the Ultralight runtime
     * @return this
     */
    public JniPlatformOptions ultralightDirectory(Path ultralightDirectory) {
        this.ultralightDirectory = ultralightDirectory;
        return this;
    }

    /**
     * Retrieves the directory of the Ultralight runtime.
     *
     * @return the directory of the Ultralight runtime, or null, if none is known
     */
    public Path ultralightDirectory() {
        if (ultralightDirectory != null) {
            return ultralightDirectory;
        }

        String fromProperty = System.getProperty("ujr.ultralightDirectory");
        if (fromProperty != null && !fromProperty.isEmpty()) {
            return Paths.get(fromProperty);
        }

        String fromEnvironment = System.getenv("ULTRALIGHT_DIR");
        if (fromEnvironment != null && !fromEnvironment.isEmpty()) {
            return Paths.get(fromEnvironment);
        }

        return null;
    }

    /**
     * Sets the class loader the natives of Ultralight Java Reborn are loaded with.
     * <p>
     * This is useful when the natives jar is not on the class path, for example because it is downloaded at
     * runtime.
     *
     * @param nativesClassLoader the class loader to load the natives with
     * @return this
     */
    public JniPlatformOptions nativesClassLoader(ClassLoader nativesClassLoader) {
        this.nativesClassLoader = nativesClassLoader;
        return this;
    }

    /**
     * Retrieves the class loader the natives of Ultralight Java Reborn are loaded with.
     *
     * @return the class loader to load the natives with
     */
    public ClassLoader nativesClassLoader() {
        return nativesClassLoader != null ? nativesClassLoader : JniPlatformOptions.class.getClassLoader();
    }
}
