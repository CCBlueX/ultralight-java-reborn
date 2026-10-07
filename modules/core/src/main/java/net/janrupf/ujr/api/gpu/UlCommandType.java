package net.janrupf.ujr.api.gpu;

/**
 * The type of a command.
 */
public enum UlCommandType {
    /**
     * Clears the render buffer to transparent.
     */
    CLEAR_RENDER_BUFFER,

    /**
     * Draws geometry into the render buffer.
     */
    DRAW_GEOMETRY,

    /**
     * Submits the pending work, before drawing into a render buffer that earlier draws of the list sampled. Its state
     * holds nothing.
     */
    FLUSH
}
