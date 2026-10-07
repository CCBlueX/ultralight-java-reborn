package net.janrupf.ujr.api.gpu;

/**
 * How the weighted source and destination colors are combined when blending.
 */
public enum UlBlendEquation {
    /**
     * {@code source + destination}.
     */
    ADD,

    /**
     * {@code source - destination}.
     */
    SUBTRACT,

    /**
     * {@code destination - source}.
     */
    REV_SUBTRACT,

    /**
     * The smaller of both colors, ignoring the blend factors.
     */
    MIN,

    /**
     * The larger of both colors, ignoring the blend factors.
     */
    MAX
}
