package com.blurview;

/* loaded from: classes.dex */
public class SizeScaler {
    private static final int ROUNDING_VALUE = 64;
    private final float scaleFactor;

    public SizeScaler(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Size scale(int width, int height) {
        float f = width;
        int roundSize = roundSize(downscaleSize(f));
        return new Size(roundSize, (int) Math.ceil(height / 4), f / roundSize);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isZeroSized(int measuredWidth, int measuredHeight) {
        return downscaleSize((float) measuredHeight) == 0 || downscaleSize((float) measuredWidth) == 0;
    }

    private int roundSize(int value) {
        int i = value % 64;
        return i == 0 ? value : (value - i) + 64;
    }

    private int downscaleSize(float value) {
        return (int) Math.ceil(value / this.scaleFactor);
    }

    /* loaded from: classes.dex */
    static class Size {
        final int height;
        final float scaleFactor;
        final int width;

        Size(int width, int height, float scaleFactor) {
            this.width = width;
            this.height = height;
            this.scaleFactor = scaleFactor;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Size size = (Size) o;
            return this.width == size.width && this.height == size.height && Float.compare(size.scaleFactor, this.scaleFactor) == 0;
        }

        public int hashCode() {
            int i = ((this.width * 31) + this.height) * 31;
            float f = this.scaleFactor;
            return i + (f != 0.0f ? Float.floatToIntBits(f) : 0);
        }

        public String toString() {
            return "Size{width=" + this.width + ", height=" + this.height + ", scaleFactor=" + this.scaleFactor + '}';
        }
    }
}
