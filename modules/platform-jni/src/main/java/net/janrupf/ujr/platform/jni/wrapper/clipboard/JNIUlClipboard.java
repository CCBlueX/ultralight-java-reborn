package net.janrupf.ujr.platform.jni.wrapper.clipboard;

import net.janrupf.ujr.api.clipboard.UltralightClipboard;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;

public class JNIUlClipboard {
    private final UltralightClipboard delegate;

    public JNIUlClipboard(UltralightClipboard delegate) {
        this.delegate = delegate;
    }

    /**
     * Retrieves the Java object this wrapper forwards to.
     *
     * @return the delegate
     */
    public UltralightClipboard getDelegate() {
        return delegate;
    }

    @NativeAccess
    public void clear() {
        delegate.clear();
    }

    @NativeAccess
    public String readPlainText() {
        return delegate.readPlainText();
    }

    @NativeAccess
    public void writePlainText(String text) {
        delegate.writePlainText(text);
    }
}
