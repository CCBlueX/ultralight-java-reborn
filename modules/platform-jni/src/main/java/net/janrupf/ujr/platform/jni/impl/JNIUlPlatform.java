package net.janrupf.ujr.platform.jni.impl;

import net.janrupf.ujr.api.clipboard.UltralightClipboard;
import net.janrupf.ujr.api.filesystem.UltralightFilesystem;
import net.janrupf.ujr.api.logger.UltralightLogger;
import net.janrupf.ujr.api.surface.UltralightSurfaceFactory;
import net.janrupf.ujr.core.platform.abstraction.UlPlatform;
import net.janrupf.ujr.api.config.UlConfig;
import net.janrupf.ujr.core.platform.abstraction.UlRenderer;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;
import net.janrupf.ujr.platform.jni.wrapper.clipboard.JNIUlClipboard;
import net.janrupf.ujr.platform.jni.wrapper.filesystem.JNIUlFilesystem;
import net.janrupf.ujr.platform.jni.wrapper.logger.JNIUlLogger;
import net.janrupf.ujr.platform.jni.wrapper.surface.JNIUlSurfaceFactory;

import java.util.Objects;

public class JNIUlPlatform implements UlPlatform {
    @NativeAccess
    private final long handle;

    @NativeAccess
    private final long nativeCollector;

    private JNIUlPlatform() {
        throw new RuntimeException("Allocate in native code without calling constructor");
    }

    @Override
    public void setConfig(UlConfig config) {
        nativeSetConfig(config);
    }

    private native void nativeSetConfig(UlConfig config);

    @Override
    public void usePlatformFontLoader() {
        nativeUsePlatformFontLoader();
    }

    private native void nativeUsePlatformFontLoader();

    @Override
    public void setLogger(UltralightLogger logger) {
        nativeSetLogger(logger == null ? null : new JNIUlLogger(logger));
    }

    private native void nativeSetLogger(JNIUlLogger logger);

    @Override
    public UltralightLogger getLogger() {
        Object logger = nativeGetLogger();

        // Our own wrappers are unwrapped, native implementations are returned as they are
        return logger instanceof JNIUlLogger
                ? ((JNIUlLogger) logger).getDelegate()
                : (UltralightLogger) logger;
    }

    private native Object nativeGetLogger();

    @Override
    public void setFilesystem(UltralightFilesystem filesystem) {
        nativeSetFilesystem(filesystem == null ? null : new JNIUlFilesystem(filesystem));
    }

    private native void nativeSetFilesystem(JNIUlFilesystem filesystem);

    @Override
    public UltralightFilesystem getFilesystem() {
        Object filesystem = nativeGetFilesystem();

        // Our own wrappers are unwrapped, native implementations are returned as they are
        return filesystem instanceof JNIUlFilesystem
                ? ((JNIUlFilesystem) filesystem).getDelegate()
                : (UltralightFilesystem) filesystem;
    }

    private native Object nativeGetFilesystem();

    @Override
    public void setClipboard(UltralightClipboard clipboard) {
        nativeSetClipboard(clipboard == null ? null : new JNIUlClipboard(clipboard));
    }

    private native void nativeSetClipboard(JNIUlClipboard clipboard);

    @Override
    public UltralightClipboard getClipboard() {
        Object clipboard = nativeGetClipboard();

        // Our own wrappers are unwrapped, native implementations are returned as they are
        return clipboard instanceof JNIUlClipboard
                ? ((JNIUlClipboard) clipboard).getDelegate()
                : (UltralightClipboard) clipboard;
    }

    private native Object nativeGetClipboard();

    @Override
    public void setSurfaceFactory(UltralightSurfaceFactory surfaceFactory) {
        nativeSetSurfaceFactory(surfaceFactory == null ? null : new JNIUlSurfaceFactory(surfaceFactory));
    }

    private native void nativeSetSurfaceFactory(JNIUlSurfaceFactory surfaceFactory);

    @Override
    public UltralightSurfaceFactory surfaceFactory() {
        Object surfaceFactory = nativeSurfaceFactory();

        // Our own wrappers are unwrapped, native implementations are returned as they are
        return surfaceFactory instanceof JNIUlSurfaceFactory
                ? ((JNIUlSurfaceFactory) surfaceFactory).getDelegate()
                : (UltralightSurfaceFactory) surfaceFactory;
    }

    private native Object nativeSurfaceFactory();

    @Override
    public UlRenderer createRenderer() {
        return nativeCreateRenderer();
    }

    private native UlRenderer nativeCreateRenderer();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JNIUlPlatform)) return false;
        JNIUlPlatform that = (JNIUlPlatform) o;
        return handle == that.handle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(handle);
    }
}
