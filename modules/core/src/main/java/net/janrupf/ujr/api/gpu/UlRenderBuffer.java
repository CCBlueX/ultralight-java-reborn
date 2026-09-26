package net.janrupf.ujr.api.gpu;

/**
 * A render buffer, which draws into a texture.
 */
public final class UlRenderBuffer {
    private final int textureId;
    private final int width;
    private final int height;

    public UlRenderBuffer(int textureId, int width, int height) {
        this.textureId = textureId;
        this.width = width;
        this.height = height;
    }

    /**
     * Retrieves the texture the render buffer draws into.
     *
     * @return the id of the texture
     */
    public int textureId() {
        return textureId;
    }

    /**
     * Retrieves the width of the render buffer.
     *
     * @return the width in pixels
     */
    public int width() {
        return width;
    }

    /**
     * Retrieves the height of the render buffer.
     *
     * @return the height in pixels
     */
    public int height() {
        return height;
    }
}
