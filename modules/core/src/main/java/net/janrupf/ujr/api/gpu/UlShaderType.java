package net.janrupf.ujr.api.gpu;

/**
 * The shader program to draw geometry with.
 * <p>
 * A driver has to support all of them, the stock shaders are in the {@code platform/shaders} folder of the SDK.
 */
public enum UlShaderType {
    /**
     * Fills quads, geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     */
    FILL,

    /**
     * Fills tessellated paths, geometry in {@link UlVertexBufferFormat#F2_UB4_F2}.
     */
    FILL_PATH,

    /**
     * Basic CSS and SVG filters, geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     */
    FILTER_BASIC,

    /**
     * Blur filters, geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     */
    FILTER_BLUR,

    /**
     * Drop-shadow filters, geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     */
    FILTER_DROP_SHADOW,

    /**
     * Draws paths analytically (Photon), geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     * <p>
     * The data pages are in texture slots 1 (RGBA16F curves), 3 (RGBA32F constants) and 4 (RGBA16UI index), read with
     * texel fetches only: bind them unfiltered and without mipmaps.
     */
    FILL_PHOTON,

    /**
     * Draws paths analytically in cells, geometry in {@link UlVertexBufferFormat#F2_UI2}.
     * <p>
     * Binds the same data pages as {@link #FILL_PHOTON}, which the vertex shader reads as well.
     */
    FILL_PHOTON_GRID
}
