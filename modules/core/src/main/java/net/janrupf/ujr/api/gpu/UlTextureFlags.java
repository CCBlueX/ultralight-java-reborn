package net.janrupf.ujr.api.gpu;

/**
 * The flags of {@link UltralightGPUDriver#createTexture}, combined with bitwise or.
 */
public final class UlTextureFlags {
    /**
     * The texture is the target of a render buffer, to be rendered into and sampled. Its bitmap has no pixels.
     * <p>
     * A render target is BGRA or, with {@link net.janrupf.ujr.api.bitmap.UlBitmapFormat#A8_UNORM}, a single channel
     * that the shaders sample from the red channel.
     */
    public static final int RENDER_TARGET = 1;

    /**
     * The render target is multisampled. Never set, as the driver doesn't report support for it.
     */
    public static final int ANTIALIASED = 1 << 1;

    /**
     * The texture is never updated.
     */
    public static final int IMMUTABLE = 1 << 2;

    private UlTextureFlags() {
        throw new UnsupportedOperationException("Constants class");
    }
}
