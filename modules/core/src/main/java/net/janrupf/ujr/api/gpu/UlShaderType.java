package net.janrupf.ujr.api.gpu;

/**
 * The shader program to draw geometry with.
 */
public enum UlShaderType {
    /**
     * Fills quads, geometry in {@link UlVertexBufferFormat#F2_UB4_F2_F2_F28}.
     */
    FILL,

    /**
     * Fills tesselated paths, geometry in {@link UlVertexBufferFormat#F2_UB4_F2}.
     */
    FILL_PATH
}
