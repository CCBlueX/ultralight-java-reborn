package net.janrupf.ujr.api.gpu;

import net.janrupf.ujr.api.bitmap.UlBitmapFormat;
import net.janrupf.ujr.api.math.FloatRect;

/**
 * Where an accelerated view is rendered to by the {@link UltralightGPUDriver}.
 */
public final class UlRenderTarget {
    private boolean isEmpty;
    private int width;
    private int height;
    private int textureId;
    private int textureWidth;
    private int textureHeight;
    private UlBitmapFormat textureFormat;
    private FloatRect uvCoords;
    private int renderBufferId;

    private UlRenderTarget() {
    }

    /**
     * Retrieves whether the view has no render target, for example because it is not accelerated.
     *
     * @return true if there is no render target
     */
    public boolean isEmpty() {
        return isEmpty;
    }

    /**
     * Retrieves the width of the view.
     *
     * @return the width in pixels
     */
    public int width() {
        return width;
    }

    /**
     * Retrieves the height of the view.
     *
     * @return the height in pixels
     */
    public int height() {
        return height;
    }

    /**
     * Retrieves the texture the view is rendered to.
     *
     * @return the id of the texture
     */
    public int textureId() {
        return textureId;
    }

    /**
     * Retrieves the width of the texture, which may be larger than the view.
     *
     * @return the width in pixels
     */
    public int textureWidth() {
        return textureWidth;
    }

    /**
     * Retrieves the height of the texture, which may be larger than the view.
     *
     * @return the height in pixels
     */
    public int textureHeight() {
        return textureHeight;
    }

    /**
     * Retrieves the format of the texture.
     *
     * @return the format
     */
    public UlBitmapFormat textureFormat() {
        return textureFormat;
    }

    /**
     * Retrieves the part of the texture holding the view, in texture coordinates from the top left.
     *
     * @return the texture coordinates of the view
     */
    public FloatRect uvCoords() {
        return uvCoords;
    }

    /**
     * Retrieves the render buffer the view is rendered with.
     *
     * @return the id of the render buffer
     */
    public int renderBufferId() {
        return renderBufferId;
    }
}
