package net.janrupf.ujr.api.gpu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The commands of a frame, read directly from the memory of Ultralight.
 * <p>
 * Only valid during {@link UltralightGPUDriver#updateCommandList(UlCommandList)}.
 */
public final class UlCommandList {
    /**
     * Size of a command in bytes, the layout of {@code ultralight::Command}.
     */
    public static final int COMMAND_SIZE = 794;

    private final ByteBuffer buffer;
    private final int size;

    /**
     * Wraps the packed commands.
     *
     * @param buffer the packed commands
     * @param size   the number of commands
     */
    public UlCommandList(ByteBuffer buffer, int size) {
        this.buffer = buffer.order(ByteOrder.nativeOrder());
        this.size = size;
    }

    /**
     * Retrieves the number of commands.
     *
     * @return the number of commands
     */
    public int size() {
        return size;
    }

    /**
     * Retrieves a command.
     * <p>
     * The command reads from the list, so it is only valid as long as the list is.
     *
     * @param index the index of the command
     * @return the command
     */
    public UlCommand get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Command " + index + " of " + size);
        }

        return new UlCommand(buffer, index * COMMAND_SIZE);
    }
}
