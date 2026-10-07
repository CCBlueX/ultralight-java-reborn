package net.janrupf.ujr.api.gpu;

import net.janrupf.ujr.api.bitmap.UltralightBitmap;
import net.janrupf.ujr.api.math.IntRect;

import java.nio.ByteBuffer;

/**
 * Renders accelerated views on the GPU of the application.
 * <p>
 * Ultralight creates textures, render buffers and geometry through the driver and then hands it the commands to draw
 * a frame. The driver is called during {@link net.janrupf.ujr.api.UltralightRenderer#update()} and
 * {@link net.janrupf.ujr.api.UltralightRenderer#render()}, on the thread that calls them.
 * <p>
 * Buffers and bitmaps passed to the driver point into memory of Ultralight and are only valid during the call.
 * <p>
 * The methods must not throw: an exception unwinds through Ultralight's renderer, which can't draw anymore afterwards.
 *
 * @see <a href="https://docs.ultralig.ht/docs/using-a-custom-gpudriver">Using a custom GPUDriver</a>
 */
public interface UltralightGPUDriver {
    /**
     * Called before Ultralight starts to create resources and to update the command list.
     */
    void beginSynchronize();

    /**
     * Called after Ultralight finished creating resources and updating the command list.
     */
    void endSynchronize();

    /**
     * Retrieves the next free texture id.
     *
     * @return the next texture id, which must not be 0
     */
    int nextTextureId();

    /**
     * Creates a texture.
     * <p>
     * A render target, see {@link UlTextureFlags#RENDER_TARGET}, gets a bitmap with its size and format but no
     * pixels.
     *
     * @param textureId the id of the texture
     * @param bitmap    the content of the texture
     * @param flags     the {@link UlTextureFlags} of the texture
     */
    void createTexture(int textureId, UltralightBitmap bitmap, int flags);

    /**
     * Replaces the content of a texture.
     *
     * @param textureId the id of the texture
     * @param bitmap    the whole new content of the texture
     * @param dirtyRect the part that changed, which is enough to upload
     */
    void updateTexture(int textureId, UltralightBitmap bitmap, IntRect dirtyRect);

    /**
     * Destroys a texture.
     *
     * @param textureId the id of the texture
     */
    void destroyTexture(int textureId);

    /**
     * Retrieves the next free render buffer id.
     *
     * @return the next render buffer id, which must not be 0
     */
    int nextRenderBufferId();

    /**
     * Creates a render buffer, which draws into a texture.
     *
     * @param renderBufferId the id of the render buffer
     * @param renderBuffer   the texture and size of the render buffer
     */
    void createRenderBuffer(int renderBufferId, UlRenderBuffer renderBuffer);

    /**
     * Destroys a render buffer.
     *
     * @param renderBufferId the id of the render buffer
     */
    void destroyRenderBuffer(int renderBufferId);

    /**
     * Retrieves the next free geometry id.
     *
     * @return the next geometry id, which must not be 0
     */
    int nextGeometryId();

    /**
     * Creates geometry.
     *
     * @param geometryId the id of the geometry
     * @param format     the format of the vertices
     * @param vertices   the vertices, in native byte order
     * @param indices    the indices, 32 bit unsigned integers in native byte order
     */
    void createGeometry(int geometryId, UlVertexBufferFormat format, ByteBuffer vertices, ByteBuffer indices);

    /**
     * Replaces the vertices and indices of geometry.
     *
     * @param geometryId the id of the geometry
     * @param format     the format of the vertices
     * @param vertices   the vertices, in native byte order
     * @param indices    the indices, 32 bit unsigned integers in native byte order
     */
    void updateGeometry(int geometryId, UlVertexBufferFormat format, ByteBuffer vertices, ByteBuffer indices);

    /**
     * Destroys geometry.
     *
     * @param geometryId the id of the geometry
     */
    void destroyGeometry(int geometryId);

    /**
     * Hands the driver the commands to draw.
     * <p>
     * They may be executed right away or be copied to be executed later.
     *
     * @param commands the commands
     */
    void updateCommandList(UlCommandList commands);
}
