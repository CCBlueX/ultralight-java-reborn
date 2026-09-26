package net.janrupf.ujr.api.gpu;

/**
 * The layout of vertices.
 */
public enum UlVertexBufferFormat {
    /**
     * Used for paths, 20 bytes: position (2 floats), color (4 unsigned bytes) and object coordinates (2 floats).
     */
    F2_UB4_F2(20),

    /**
     * Used for quads, 140 bytes: position (2 floats), color (4 unsigned bytes), texture coordinates (2 floats),
     * object coordinates (2 floats) and seven data vectors (4 floats each).
     */
    F2_UB4_F2_F2_F28(140);

    private final int stride;

    UlVertexBufferFormat(int stride) {
        this.stride = stride;
    }

    /**
     * Retrieves the size of a vertex.
     *
     * @return the size of a vertex in bytes
     */
    public int stride() {
        return stride;
    }
}
