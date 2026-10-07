package net.janrupf.ujr.platform.jni.wrapper.gpu;

import net.janrupf.ujr.api.bitmap.UltralightBitmap;
import net.janrupf.ujr.api.gpu.UlCommandList;
import net.janrupf.ujr.api.gpu.UlRenderBuffer;
import net.janrupf.ujr.api.gpu.UlVertexBufferFormat;
import net.janrupf.ujr.api.gpu.UltralightGPUDriver;
import net.janrupf.ujr.api.math.IntRect;
import net.janrupf.ujr.platform.jni.ffi.NativeAccess;
import net.janrupf.ujr.platform.jni.impl.JNIUlBitmap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Calls a {@link UltralightGPUDriver} from native code.
 */
public class JNIUlGPUDriver {
    private final UltralightGPUDriver delegate;

    public JNIUlGPUDriver(UltralightGPUDriver delegate) {
        this.delegate = delegate;
    }

    /**
     * Retrieves the Java object this wrapper forwards to.
     *
     * @return the delegate
     */
    public UltralightGPUDriver getDelegate() {
        return delegate;
    }

    @NativeAccess
    public void beginSynchronize() {
        delegate.beginSynchronize();
    }

    @NativeAccess
    public void endSynchronize() {
        delegate.endSynchronize();
    }

    @NativeAccess
    public int nextTextureId() {
        return delegate.nextTextureId();
    }

    @NativeAccess
    public void createTexture(int textureId, JNIUlBitmap bitmap, int flags) {
        delegate.createTexture(textureId, new UltralightBitmap(bitmap), flags);
    }

    @NativeAccess
    public void updateTexture(int textureId, JNIUlBitmap bitmap, int left, int top, int right, int bottom) {
        delegate.updateTexture(textureId, new UltralightBitmap(bitmap), new IntRect(left, top, right, bottom));
    }

    @NativeAccess
    public void destroyTexture(int textureId) {
        delegate.destroyTexture(textureId);
    }

    @NativeAccess
    public int nextRenderBufferId() {
        return delegate.nextRenderBufferId();
    }

    @NativeAccess
    public void createRenderBuffer(int renderBufferId, int textureId, int width, int height) {
        delegate.createRenderBuffer(renderBufferId, new UlRenderBuffer(textureId, width, height));
    }

    @NativeAccess
    public void destroyRenderBuffer(int renderBufferId) {
        delegate.destroyRenderBuffer(renderBufferId);
    }

    @NativeAccess
    public int nextGeometryId() {
        return delegate.nextGeometryId();
    }

    @NativeAccess
    public void createGeometry(int geometryId, int format, ByteBuffer vertices, ByteBuffer indices) {
        delegate.createGeometry(
                geometryId,
                UlVertexBufferFormat.values()[format],
                vertices.order(ByteOrder.nativeOrder()),
                indices.order(ByteOrder.nativeOrder())
        );
    }

    @NativeAccess
    public void updateGeometry(int geometryId, int format, ByteBuffer vertices, ByteBuffer indices) {
        delegate.updateGeometry(
                geometryId,
                UlVertexBufferFormat.values()[format],
                vertices.order(ByteOrder.nativeOrder()),
                indices.order(ByteOrder.nativeOrder())
        );
    }

    @NativeAccess
    public void destroyGeometry(int geometryId) {
        delegate.destroyGeometry(geometryId);
    }

    @NativeAccess
    public void updateCommandList(int size, ByteBuffer commands) {
        delegate.updateCommandList(new UlCommandList(commands, size));
    }
}
