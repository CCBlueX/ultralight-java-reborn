package net.janrupf.ujr.platform.jni.wrapper.listener;

import net.janrupf.ujr.api.UltralightView;
import net.janrupf.ujr.api.listener.UltralightLoadListener;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;
import net.janrupf.ujr.platform.jni.impl.JNIUlView;

public class JNIUlLoadListener {
    private final UltralightLoadListener delegate;

    public JNIUlLoadListener(UltralightLoadListener delegate) {
        this.delegate = delegate;
    }

    @NativeAccess
    public void onBeginLoading(JNIUlView view, long frameId, boolean isMainFrame, String url) {
        delegate.onBeginLoading(new UltralightView(view), frameId, isMainFrame, url);
    }

    @NativeAccess
    public void onFinishLoading(JNIUlView view, long frameId, boolean isMainFrame, String url) {
        delegate.onFinishLoading(new UltralightView(view), frameId, isMainFrame, url);
    }

    @NativeAccess
    public void onFailLoading(
            JNIUlView view,
            long frameId,
            boolean isMainFrame,
            String url,
            String description,
            String errorDomain,
            int errorCode
    ) {
        delegate.onFailLoading(new UltralightView(view), frameId, isMainFrame, url, description, errorDomain, errorCode);
    }

    @NativeAccess
    public void onWindowObjectReady(JNIUlView view, long frameId, boolean isMainFrame, String url) {
        delegate.onWindowObjectReady(new UltralightView(view), frameId, isMainFrame, url);
    }

    @NativeAccess
    public void onDOMReady(JNIUlView view, long frameId, boolean isMainFrame, String url) {
        delegate.onDOMReady(new UltralightView(view), frameId, isMainFrame, url);
    }

    @NativeAccess
    public void onUpdateHistory(JNIUlView view) {
        delegate.onUpdateHistory(new UltralightView(view));
    }
}
