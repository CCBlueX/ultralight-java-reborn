package net.janrupf.ujr.platform.jni.wrapper.logger;

import net.janrupf.ujr.api.logger.UltralightLogLevel;
import net.janrupf.ujr.api.logger.UltralightLogger;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;

public class JNIUlLogger {
    private final UltralightLogger delegate;

    public JNIUlLogger(UltralightLogger delegate) {
        this.delegate = delegate;
    }

    /**
     * Retrieves the Java object this wrapper forwards to.
     *
     * @return the delegate
     */
    public UltralightLogger getDelegate() {
        return delegate;
    }

    @NativeAccess
    public void logMessage(UltralightLogLevel logLevel, String message) {
        delegate.logMessage(logLevel, message);
    }
}
