package net.janrupf.ujr.api.bitmap;

/**
 * The various Bitmap formats.
 */
public enum UlBitmapFormat {
    /**
     * Alpha channel only, 8-bits per pixel.
     * <p>
     * Encoding: 8-bits per channel, unsigned normalized.
     * <p>
     * Color-space: Linear (no gamma), alpha-coverage only.
     */
    A8_UNORM,

    /**
     * Blue Green Red Alpha channels, 32-bits per pixel.
     * <p>
     * Encoding: 8-bits per channel, unsigned normalized.
     * <p>
     * Color-space: sRGB gamma with premultiplied linear alpha channel.
     */
    BGRA8_UNORM_SRGB,

    /**
     * Red Green channels, 16-bits per pixel.
     * <p>
     * Encoding: 8-bits per channel, unsigned normalized.
     * <p>
     * Color-space: Linear (no gamma).
     */
    RG8_UNORM,

    /**
     * BC1 block-compressed color (4x4 blocks, 8 bytes per block) with optional 1-bit alpha.
     */
    BC1_UNORM,

    /**
     * BC2 block-compressed color (4x4 blocks, 16 bytes per block) with explicit 4-bit alpha.
     */
    BC2_UNORM,

    /**
     * BC3 block-compressed color (4x4 blocks, 16 bytes per block) with interpolated alpha.
     */
    BC3_UNORM,

    /**
     * BC7 block-compressed color (4x4 blocks, 16 bytes per block).
     */
    BC7_UNORM,

    /**
     * Four half-precision float channels, 64-bits per pixel.
     * <p>
     * Raw data, to be sampled without filtering or mipmaps.
     */
    RGBA16F,

    /**
     * Four unsigned integer channels, 16-bits each, 64-bits per pixel.
     * <p>
     * Raw data, to be sampled without filtering or mipmaps.
     */
    RGBA16UI,

    /**
     * Four float channels, 128-bits per pixel.
     * <p>
     * Raw data, to be sampled without filtering or mipmaps.
     */
    RGBA32F;

    /**
     * Retrieves the number of bytes per pixel for this format.
     *
     * @return the number of bytes per pixel
     * @throws IllegalStateException for block-compressed formats, which have no size per pixel
     */
    public int bytesPerPixel() {
        switch (this) {
            case A8_UNORM:
                return 1;
            case RG8_UNORM:
                return 2;
            case BGRA8_UNORM_SRGB:
                return 4;
            case RGBA16F:
            case RGBA16UI:
                return 8;
            case RGBA32F:
                return 16;
            default:
                throw new IllegalStateException(this + " is block-compressed");
        }
    }
}
