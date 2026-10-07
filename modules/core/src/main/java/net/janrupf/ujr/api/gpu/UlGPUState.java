package net.janrupf.ujr.api.gpu;

import net.janrupf.ujr.api.math.IntRect;

import java.nio.ByteBuffer;

/**
 * The state a command is executed with, read directly from the memory of Ultralight.
 * <p>
 * Matrices are column-major, as the shaders of Ultralight expect them.
 */
public final class UlGPUState {
    private static final int VIEWPORT_WIDTH = 0;
    private static final int VIEWPORT_HEIGHT = 4;
    private static final int TRANSFORM = 8;
    private static final int ENABLE_TEXTURING = 72;
    private static final int ENABLE_BLEND = 73;
    private static final int BLEND_SRC_FACTOR = 74;
    private static final int BLEND_DST_FACTOR = 75;
    private static final int BLEND_EQUATION = 76;
    private static final int SHADER_TYPE = 77;
    private static final int RENDER_BUFFER_ID = 78;
    private static final int TEXTURE_1_ID = 82;
    private static final int TEXTURE_2_ID = 86;
    private static final int TEXTURE_3_ID = 90;
    private static final int TEXTURE_4_ID = 94;
    private static final int UNIFORM_INTEGER = 98;
    private static final int UNIFORM_SCALAR = 130;
    private static final int UNIFORM_VECTOR = 162;
    private static final int CLIP_SIZE = 290;
    private static final int CLIP = 291;
    private static final int ENABLE_SCISSOR = 803;
    private static final int SCISSOR_RECT = 804;

    /**
     * Number of uniform integers.
     */
    public static final int UNIFORM_INTEGERS = 8;

    /**
     * Number of uniform scalars.
     */
    public static final int UNIFORM_SCALARS = 8;

    /**
     * Number of uniform vectors.
     */
    public static final int UNIFORM_VECTORS = 8;

    /**
     * Maximum number of clip matrices.
     */
    public static final int MAX_CLIPS = 8;

    private final ByteBuffer buffer;
    private final int offset;

    UlGPUState(ByteBuffer buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;
    }

    /**
     * Retrieves the width of the viewport.
     *
     * @return the width in pixels
     */
    public int viewportWidth() {
        return buffer.getInt(offset + VIEWPORT_WIDTH);
    }

    /**
     * Retrieves the height of the viewport.
     *
     * @return the height in pixels
     */
    public int viewportHeight() {
        return buffer.getInt(offset + VIEWPORT_HEIGHT);
    }

    /**
     * Retrieves an element of the transformation of the geometry.
     *
     * @param index the index of the element, column-major
     * @return the element
     */
    public float transform(int index) {
        return buffer.getFloat(offset + TRANSFORM + checkIndex(index, 16) * 4);
    }

    /**
     * Retrieves whether textures are sampled.
     *
     * @return true if textures are sampled
     */
    public boolean enableTexturing() {
        return buffer.get(offset + ENABLE_TEXTURING) != 0;
    }

    /**
     * Retrieves whether to blend, with {@link #blendSrcFactor()}, {@link #blendDstFactor()} and
     * {@link #blendEquation()}.
     *
     * @return true to blend
     */
    public boolean enableBlend() {
        return buffer.get(offset + ENABLE_BLEND) != 0;
    }

    /**
     * Retrieves the factor of the color the shader outputs, {@link UlBlendFactor#ONE} for premultiplied alpha.
     *
     * @return the factor
     */
    public UlBlendFactor blendSrcFactor() {
        return UlBlendFactor.values()[buffer.get(offset + BLEND_SRC_FACTOR)];
    }

    /**
     * Retrieves the factor of the color in the render buffer, {@link UlBlendFactor#INV_SRC_ALPHA} for premultiplied
     * alpha.
     *
     * @return the factor
     */
    public UlBlendFactor blendDstFactor() {
        return UlBlendFactor.values()[buffer.get(offset + BLEND_DST_FACTOR)];
    }

    /**
     * Retrieves how the weighted colors are combined.
     *
     * @return the equation
     */
    public UlBlendEquation blendEquation() {
        return UlBlendEquation.values()[buffer.get(offset + BLEND_EQUATION)];
    }

    /**
     * Retrieves the shader to draw with.
     *
     * @return the shader
     */
    public UlShaderType shaderType() {
        return UlShaderType.values()[buffer.get(offset + SHADER_TYPE)];
    }

    /**
     * Retrieves the render buffer to draw into.
     *
     * @return the id of the render buffer
     */
    public int renderBufferId() {
        return buffer.getInt(offset + RENDER_BUFFER_ID);
    }

    /**
     * Retrieves the first texture.
     *
     * @return the id of the texture, or 0 if none
     */
    public int texture1Id() {
        return buffer.getInt(offset + TEXTURE_1_ID);
    }

    /**
     * Retrieves the second texture.
     *
     * @return the id of the texture, or 0 if none
     */
    public int texture2Id() {
        return buffer.getInt(offset + TEXTURE_2_ID);
    }

    /**
     * Retrieves the third texture.
     *
     * @return the id of the texture, or 0 if none
     */
    public int texture3Id() {
        return buffer.getInt(offset + TEXTURE_3_ID);
    }

    /**
     * Retrieves the fourth texture, the integer index page of Photon draws.
     *
     * @return the id of the texture, or 0 if none
     */
    public int texture4Id() {
        return buffer.getInt(offset + TEXTURE_4_ID);
    }

    /**
     * Retrieves a uniform integer.
     *
     * @param index the index of the integer
     * @return the integer
     */
    public int uniformInteger(int index) {
        return buffer.getInt(offset + UNIFORM_INTEGER + checkIndex(index, UNIFORM_INTEGERS) * 4);
    }

    /**
     * Retrieves a uniform scalar.
     *
     * @param index the index of the scalar
     * @return the scalar
     */
    public float uniformScalar(int index) {
        return buffer.getFloat(offset + UNIFORM_SCALAR + checkIndex(index, UNIFORM_SCALARS) * 4);
    }

    /**
     * Retrieves a component of a uniform vector.
     *
     * @param index     the index of the vector
     * @param component the component, 0 to 3
     * @return the component
     */
    public float uniformVector(int index, int component) {
        return buffer.getFloat(
                offset + UNIFORM_VECTOR + checkIndex(index, UNIFORM_VECTORS) * 16 + checkIndex(component, 4) * 4
        );
    }

    /**
     * Retrieves the number of clip matrices.
     *
     * @return the number of clip matrices
     */
    public int clipSize() {
        return Byte.toUnsignedInt(buffer.get(offset + CLIP_SIZE));
    }

    /**
     * Retrieves an element of a clip matrix.
     *
     * @param clip  the index of the clip matrix
     * @param index the index of the element, column-major
     * @return the element
     */
    public float clip(int clip, int index) {
        return buffer.getFloat(offset + CLIP + checkIndex(clip, MAX_CLIPS) * 64 + checkIndex(index, 16) * 4);
    }

    /**
     * Retrieves whether to clip to {@link #scissorRect()}.
     *
     * @return true to clip
     */
    public boolean enableScissor() {
        return buffer.get(offset + ENABLE_SCISSOR) != 0;
    }

    /**
     * Retrieves the rectangle to clip to, from the top left of the render buffer.
     *
     * @return the rectangle
     */
    public IntRect scissorRect() {
        return new IntRect(
                buffer.getInt(offset + SCISSOR_RECT),
                buffer.getInt(offset + SCISSOR_RECT + 4),
                buffer.getInt(offset + SCISSOR_RECT + 8),
                buffer.getInt(offset + SCISSOR_RECT + 12)
        );
    }

    private static int checkIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + " of " + size);
        }

        return index;
    }
}
