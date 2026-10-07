package net.janrupf.ujr.api.gpu;

import java.nio.ByteBuffer;

/**
 * A command of a {@link UlCommandList}, read directly from the memory of Ultralight.
 */
public final class UlCommand {
    private static final int COMMAND_TYPE = 0;
    private static final int GPU_STATE = 1;
    private static final int GEOMETRY_ID = 821;
    private static final int INDICES_COUNT = 825;
    private static final int INDICES_OFFSET = 829;

    private final ByteBuffer buffer;
    private final int offset;

    UlCommand(ByteBuffer buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    /**
     * Retrieves the type of the command.
     *
     * @return the type
     */
    public UlCommandType type() {
        return UlCommandType.values()[buffer.get(offset + COMMAND_TYPE)];
    }

    /**
     * Retrieves the state to execute the command with.
     *
     * @return the state
     */
    public UlGPUState state() {
        return new UlGPUState(buffer, offset + GPU_STATE);
    }

    /**
     * Retrieves the geometry to draw, for {@link UlCommandType#DRAW_GEOMETRY}.
     *
     * @return the id of the geometry
     */
    public int geometryId() {
        return buffer.getInt(offset + GEOMETRY_ID);
    }

    /**
     * Retrieves the number of indices to draw, for {@link UlCommandType#DRAW_GEOMETRY}.
     *
     * @return the number of indices
     */
    public int indicesCount() {
        return buffer.getInt(offset + INDICES_COUNT);
    }

    /**
     * Retrieves the first index to draw, for {@link UlCommandType#DRAW_GEOMETRY}.
     *
     * @return the index of the first index
     */
    public int indicesOffset() {
        return buffer.getInt(offset + INDICES_OFFSET);
    }
}
