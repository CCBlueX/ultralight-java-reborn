package net.janrupf.ujr.api.gpu;

/**
 * How much the source or destination color is weighted when blending.
 */
public enum UlBlendFactor {
    ZERO,
    ONE,
    SRC_COLOR,
    INV_SRC_COLOR,
    SRC_ALPHA,
    INV_SRC_ALPHA,
    DEST_COLOR,
    INV_DEST_COLOR,
    DEST_ALPHA,
    INV_DEST_ALPHA,

    /**
     * {@code (f, f, f, 1)} with {@code f = min(source alpha, 1 - destination alpha)}.
     */
    SRC_ALPHA_SATURATE
}
