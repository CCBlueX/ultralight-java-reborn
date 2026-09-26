package net.janrupf.ujr.api.config;

/**
 * The quality of effects such as blurs and shadows.
 */
public enum UlEffectQuality {
    /**
     * Fastest effect quality-- uses the lowest quality effects (half-resolution, fewer passes, etc.)
     */
    LOW,

    /**
     * Default effect quality-- strikes a good balance between quality and performance.
     */
    MEDIUM,

    /**
     * Highest effect quality-- favors quality over performance.
     */
    HIGH
}
