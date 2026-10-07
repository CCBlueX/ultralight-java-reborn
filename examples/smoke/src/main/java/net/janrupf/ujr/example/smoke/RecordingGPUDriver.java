package net.janrupf.ujr.example.smoke;

import net.janrupf.ujr.api.bitmap.UltralightBitmap;
import net.janrupf.ujr.api.gpu.UlCommand;
import net.janrupf.ujr.api.gpu.UlCommandList;
import net.janrupf.ujr.api.gpu.UlCommandType;
import net.janrupf.ujr.api.gpu.UlRenderBuffer;
import net.janrupf.ujr.api.gpu.UlTextureFlags;
import net.janrupf.ujr.api.gpu.UlVertexBufferFormat;
import net.janrupf.ujr.api.gpu.UltralightGPUDriver;
import net.janrupf.ujr.api.math.IntRect;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * GPU driver which doesn't draw anything, but checks that the calls of Ultralight make sense.
 */
final class RecordingGPUDriver implements UltralightGPUDriver {
    private int nextId = 1;

    final Set<Integer> textures = new HashSet<>();
    final Map<Integer, UlRenderBuffer> renderBuffers = new HashMap<>();
    final Map<Integer, Integer> geometryIndices = new HashMap<>();

    int uploadedTextures;
    int draws;
    int clears;

    @Override
    public void beginSynchronize() {
    }

    @Override
    public void endSynchronize() {
    }

    @Override
    public int nextTextureId() {
        return nextId++;
    }

    @Override
    public void createTexture(int textureId, UltralightBitmap bitmap, int flags) {
        check(textures.add(textureId), "texture " + textureId + " was created twice");
        check(bitmap.width() > 0 && bitmap.height() > 0, "texture " + textureId + " has no size");
        check((flags & UlTextureFlags.ANTIALIASED) == 0, "texture " + textureId + " is antialiased");

        if ((flags & UlTextureFlags.RENDER_TARGET) == 0) {
            uploadedTextures++;
        }
    }

    @Override
    public void updateTexture(int textureId, UltralightBitmap bitmap, IntRect dirtyRect) {
        check(textures.contains(textureId), "texture " + textureId + " was updated before it was created");
        check(dirtyRect.getLeft() >= 0 && dirtyRect.getTop() >= 0 && dirtyRect.getRight() <= bitmap.width() &&
                        dirtyRect.getBottom() <= bitmap.height() && dirtyRect.getLeft() < dirtyRect.getRight() &&
                        dirtyRect.getTop() < dirtyRect.getBottom(),
                "texture " + textureId + " was updated outside of its bitmap");
    }

    @Override
    public void destroyTexture(int textureId) {
        check(textures.remove(textureId), "texture " + textureId + " was destroyed but doesn't exist");
    }

    @Override
    public int nextRenderBufferId() {
        return nextId++;
    }

    @Override
    public void createRenderBuffer(int renderBufferId, UlRenderBuffer renderBuffer) {
        check(textures.contains(renderBuffer.textureId()), "render buffer " + renderBufferId + " has no texture");
        check(renderBuffers.put(renderBufferId, renderBuffer) == null, "render buffer " + renderBufferId + " was created twice");
    }

    @Override
    public void destroyRenderBuffer(int renderBufferId) {
        check(renderBuffers.remove(renderBufferId) != null, "render buffer " + renderBufferId + " was destroyed but doesn't exist");
    }

    @Override
    public int nextGeometryId() {
        return nextId++;
    }

    @Override
    public void createGeometry(int geometryId, UlVertexBufferFormat format, ByteBuffer vertices, ByteBuffer indices) {
        check(!geometryIndices.containsKey(geometryId), "geometry " + geometryId + " was created twice");
        updateGeometry(geometryId, format, vertices, indices);
    }

    @Override
    public void updateGeometry(int geometryId, UlVertexBufferFormat format, ByteBuffer vertices, ByteBuffer indices) {
        check(vertices.isDirect() && indices.isDirect(), "geometry buffers are not direct");
        check(vertices.remaining() > 0 && vertices.remaining() % format.stride() == 0,
                "geometry " + geometryId + " has " + vertices.remaining() + " bytes of " + format + " vertices");
        check(indices.remaining() % Integer.BYTES == 0, "geometry " + geometryId + " has partial indices");

        geometryIndices.put(geometryId, indices.remaining() / Integer.BYTES);
    }

    @Override
    public void destroyGeometry(int geometryId) {
        check(geometryIndices.remove(geometryId) != null, "geometry " + geometryId + " was destroyed but doesn't exist");
    }

    @Override
    public void updateCommandList(UlCommandList commands) {
        for (int i = 0; i < commands.size(); i++) {
            UlCommand command = commands.get(i);
            if (command.type() == UlCommandType.FLUSH) {
                continue;
            }

            int renderBufferId = command.state().renderBufferId();
            check(renderBufferId == 0 || renderBuffers.containsKey(renderBufferId),
                    "command " + i + " targets the unknown render buffer " + renderBufferId);

            if (command.type() == UlCommandType.CLEAR_RENDER_BUFFER) {
                clears++;
                continue;
            }

            Integer indices = geometryIndices.get(command.geometryId());
            check(indices != null, "command " + i + " draws the unknown geometry " + command.geometryId());
            check(command.indicesCount() > 0 && command.indicesOffset() + command.indicesCount() <= indices,
                    "command " + i + " draws indices outside of geometry " + command.geometryId());
            check(command.state().viewportWidth() > 0 && command.state().viewportHeight() > 0,
                    "command " + i + " has an empty viewport");
            draws++;
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
