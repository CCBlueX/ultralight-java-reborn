package net.janrupf.ujr.platform.jni.impl;

import net.janrupf.ujr.core.platform.PlatformIdentification;
import net.janrupf.ujr.core.platform.abstraction.UlResourceProvider;
import net.janrupf.ujr.platform.jni.bundled.BundledResources;
import net.janrupf.ujr.platform.jni.bundled.HashedResource;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class JNIUlResourceProvider implements UlResourceProvider {
    private final PlatformIdentification platformIdentification;
    private final BundledResources bundledResources;
    private final Path ultralightDirectory;

    public JNIUlResourceProvider(
            PlatformIdentification platformIdentification,
            BundledResources bundledResources,
            Path ultralightDirectory
    ) {
        this.platformIdentification = platformIdentification;
        this.bundledResources = bundledResources;
        this.ultralightDirectory = ultralightDirectory;
    }

    @Override
    public URI getResource(String path) {
        // Resources of the Ultralight runtime, such as the ICU data and the CA certificates
        if (ultralightDirectory != null) {
            Path runtimeResource = ultralightDirectory.resolve("resources").resolve(path);
            if (Files.isRegularFile(runtimeResource)) {
                return runtimeResource.toUri();
            }
        }

        return bundledResources.getForPlatform(platformIdentification)
                .stream()
                .filter(nativeResource -> nativeResource.names().contains(path))
                .map(HashedResource::bundledLocation)
                .findFirst()
                .orElse(null);
    }
}
