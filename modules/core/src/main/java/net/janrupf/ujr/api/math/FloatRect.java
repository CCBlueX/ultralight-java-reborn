package net.janrupf.ujr.api.math;

/**
 * A rectangle with float coordinates.
 */
public final class FloatRect {
    private float left;
    private float top;
    private float right;
    private float bottom;

    /**
     * Constructs a new rectangle.
     *
     * @param left   the left edge
     * @param top    the top edge
     * @param right  the right edge
     * @param bottom the bottom edge
     */
    public FloatRect(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    @Override
    public String toString() {
        return "FloatRect{left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + "}";
    }
}
