package net.janrupf.ujr.platform.jni.wrapper.listener;

import net.janrupf.ujr.api.UltralightView;
import net.janrupf.ujr.api.cursor.UlCursor;
import net.janrupf.ujr.api.listener.UlMessageLevel;
import net.janrupf.ujr.api.listener.UlMessageSource;
import net.janrupf.ujr.api.listener.UltralightViewListener;
import net.janrupf.ujr.api.math.IntRect;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;
import net.janrupf.ujr.platform.jni.impl.JNIUlView;

public class JNIUlViewListener {
    private final UltralightViewListener delegate;

    public JNIUlViewListener(UltralightViewListener delegate) {
        this.delegate = delegate;
    }

    @NativeAccess
    public void onChangeTitle(JNIUlView view, String title) {
        delegate.onChangeTitle(new UltralightView(view), title);
    }

    @NativeAccess
    public void onChangeURL(JNIUlView view, String url) {
        delegate.onChangeURL(new UltralightView(view), url);
    }

    @NativeAccess
    public void onChangeTooltip(JNIUlView view, String tooltip) {
        delegate.onChangeTooltip(new UltralightView(view), tooltip);
    }

    @NativeAccess
    public void onChangeCursor(JNIUlView view, UlCursor cursor) {
        delegate.onChangeCursor(new UltralightView(view), cursor);
    }

    @NativeAccess
    public void onAddConsoleMessage(
            JNIUlView view,
            UlMessageSource source,
            UlMessageLevel level,
            String message,
            long lineNumber,
            long columnNumber,
            String sourceId
    ) {
        delegate.onAddConsoleMessage(new UltralightView(view), source, level, message, lineNumber, columnNumber, sourceId);
    }

    @NativeAccess
    public JNIUlView onCreateChildView(
            JNIUlView view,
            String openerUrl,
            String targetUrl,
            boolean isPopup,
            IntRect popupRect
    ) {
        return (JNIUlView) delegate.onCreateChildView(new UltralightView(view), openerUrl, targetUrl, isPopup, popupRect).getImplementation();
    }

    @NativeAccess
    public JNIUlView onCreateInspectorView(JNIUlView view, boolean isLocal, String inspectedUrl) {
        return (JNIUlView) delegate.onCreateInspectorView(new UltralightView(view), isLocal, inspectedUrl).getImplementation();
    }

    @NativeAccess
    public void onRequestClose(JNIUlView view) {
        delegate.onRequestClose(new UltralightView(view));
    }
}
