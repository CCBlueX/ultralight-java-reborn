package net.janrupf.ujr.platform.jni;

import net.janrupf.ujr.core.platform.InvalidPlatformEnvironmentException;
import net.janrupf.ujr.core.platform.abstraction.*;
import net.janrupf.ujr.core.platform.abstraction.javascript.JSCJSClassFactory;
import net.janrupf.ujr.core.platform.abstraction.javascript.JSCJSContextGroupFactory;
import net.janrupf.ujr.core.platform.abstraction.javascript.JSCJSGlobalContextFactory;
import net.janrupf.ujr.core.platform.option.PlatformEnvironmentOptionContainer;
import net.janrupf.ujr.core.platform.option.std.CommonPlatformOptions;
import net.janrupf.ujr.core.platform.provider.PlatformEnvironmentProvider;
import net.janrupf.ujr.platform.jni.bundled.BundledResources;
import net.janrupf.ujr.platform.jni.bundled.HashedResource;
import net.janrupf.ujr.platform.jni.gc.ObjectCollector;
import net.janrupf.ujr.platform.jni.impl.*;
import net.janrupf.ujr.platform.jni.impl.javascript.JNIJSCJSClassFactory;
import net.janrupf.ujr.platform.jni.impl.javascript.JNIJSCJSContextGroupFactory;
import net.janrupf.ujr.platform.jni.impl.javascript.JNIJSCJSGlobalContextFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * The JNI platform provider.
 * <p>
 * This provider is responsible for loading the JNI based Ultralight library and providing the JNI
 * Ultralight API.
 */
public class UJRJniPlatformProvider implements PlatformEnvironmentProvider {
    /**
     * The Ultralight libraries in the order they depend on each other.
     */
    private static final List<String> ULTRALIGHT_LIBRARIES = Arrays.asList(
            "UltralightCore", // Core library, no dependencies
            "WebCore", // WebCore, depends on UltralightCore
            "Ultralight", // Main library, depends on all previous libraries
            "AppCore" // AppCore, depends on all previous libraries
    );

    private final CommonPlatformOptions commonPlatformOptions;
    private final JniPlatformOptions jniPlatformOptions;
    private final BundledResources bundledResources;

    UJRJniPlatformProvider(PlatformEnvironmentOptionContainer options) {
        this.commonPlatformOptions = options.require(CommonPlatformOptions.class); // Always present

        JniPlatformOptions jniPlatformOptions = options.use(JniPlatformOptions.class);
        this.jniPlatformOptions = jniPlatformOptions != null ? jniPlatformOptions : new JniPlatformOptions();
        this.bundledResources = new BundledResources(this.jniPlatformOptions.nativesClassLoader());
    }

    @Override
    public boolean supportsThisEnvironment() {
        return bundledResources.supports(this.commonPlatformOptions.platformIdentification());
    }

    @Override
    public void performLoading() throws InvalidPlatformEnvironmentException {
        Path ultralightDirectory = jniPlatformOptions.ultralightDirectory();
        if (ultralightDirectory == null) {
            throw new InvalidPlatformEnvironmentException(
                    "No Ultralight runtime given, set it with JniPlatformOptions.ultralightDirectory, " +
                            "the ujr.ultralightDirectory property or ULTRALIGHT_DIR");
        }

        // The Ultralight runtime comes first, our library depends on it
        Path ultralightLibraries = ultralightDirectory.resolve("bin");
        for (String library : ULTRALIGHT_LIBRARIES) {
            Path libraryPath = ultralightLibraries.resolve(System.mapLibraryName(library));
            if (!Files.isRegularFile(libraryPath)) {
                throw new InvalidPlatformEnvironmentException("The Ultralight runtime is missing " + libraryPath);
            }

            try {
                System.load(libraryPath.toAbsolutePath().toString());
            } catch (Throwable e) {
                throw new InvalidPlatformEnvironmentException("Failed to load " + libraryPath, e);
            }
        }

        List<HashedResource> natives = bundledResources.getForPlatform(this.commonPlatformOptions.platformIdentification());

        // Our own library has to be extracted to be loaded. It goes into a directory named after its hash, so an
        // extracted copy can be reused, even while another process has it loaded.
        String libraryName = System.mapLibraryName("ultralight-java-reborn");
        HashedResource library = natives.stream()
                .filter(nat -> nat.type().equals("library") && nat.names().contains(libraryName))
                .findFirst()
                .orElseThrow(() -> new InvalidPlatformEnvironmentException("The natives don't contain " + libraryName));

        Path libraryPath = this.commonPlatformOptions.temporaryDirectory()
                .resolve("natives")
                .resolve(library.hash())
                .resolve(libraryName);

        if (!Files.isRegularFile(libraryPath)) {
            try (InputStream in = library.bundledLocation().toURL().openStream()) {
                Files.createDirectories(libraryPath.getParent());

                Path partialPath = libraryPath.resolveSibling(libraryName + ".part");
                Files.copy(in, partialPath, StandardCopyOption.REPLACE_EXISTING);
                Files.move(partialPath, libraryPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new InvalidPlatformEnvironmentException("Failed to extract " + libraryName, e);
            }
        }

        try {
            System.load(libraryPath.toAbsolutePath().toString());
        } catch (Throwable e) {
            throw new InvalidPlatformEnvironmentException("Failed to load native library", e);
        }
    }

    @Override
    public <T> T tryProvideApi(Class<T> interfaceClass) {
        if (interfaceClass == UlPlatformProvider.class) {
            return interfaceClass.cast(new JNIUlPlatformProvider());
        } else if (interfaceClass == UlResourceProvider.class) {
            return interfaceClass.cast(new JNIUlResourceProvider(
                    this.commonPlatformOptions.platformIdentification(),
                    bundledResources,
                    jniPlatformOptions.ultralightDirectory()
            ));
        } else if (interfaceClass == UlKeyboard.class) {
            return interfaceClass.cast(new JNIUlKeyboard());
        } else if (interfaceClass == UlBitmapSurfaceFactoryProvider.class) {
            return interfaceClass.cast(new JNIUlBitmapSurfaceFactoryProvider());
        } else if (interfaceClass == UlBitmapFactory.class) {
            return interfaceClass.cast(new JNIUlBitmapFactory());
        } else if (interfaceClass == JSCJSContextGroupFactory.class) {
            return interfaceClass.cast(new JNIJSCJSContextGroupFactory());
        } else if (interfaceClass == JSCJSGlobalContextFactory.class) {
            return interfaceClass.cast(new JNIJSCJSGlobalContextFactory());
        } else if (interfaceClass == JSCJSClassFactory.class) {
            return interfaceClass.cast(new JNIJSCJSClassFactory());
        } else {
            return null;
        }
    }

    @Override
    public void cleanup() {
        // This is a rather fragile attempt at cleaning up the native memory
        System.gc();
        ObjectCollector.processRound();

        try (Stream<Path> files = Files.walk(this.commonPlatformOptions.temporaryDirectory())) {
            //noinspection ResultOfMethodCallIgnored
            files.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (IOException e) {
            // Ignore
        }
    }
}
