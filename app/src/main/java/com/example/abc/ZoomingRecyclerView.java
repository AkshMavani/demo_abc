package com.example.abc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import androidx.core.view.ScaleGestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class ZoomingRecyclerView extends RecyclerView {
    private ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener;
    private ScaleGestureDetector scaleGestureDetector;

    public ZoomingRecyclerView(Context context) {
        this(context, null, 0);
    }

    public ZoomingRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomingRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() { // from class: com.example.iphoto.view.ZoomingRecyclerView.1
            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector detector) {
                if (isScaleListenerSet()) {
                    return ZoomingRecyclerView.this.onScaleGestureListener.onScale(detector);
                }
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                if (isScaleListenerSet()) {
                    return ZoomingRecyclerView.this.onScaleGestureListener.onScaleBegin(detector);
                }
                return false;
            }

            @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector detector) {
                if (isScaleListenerSet()) {
                    ZoomingRecyclerView.this.onScaleGestureListener.onScaleEnd(detector);
                }
            }

            private boolean isScaleListenerSet() {
                return ZoomingRecyclerView.this.onScaleGestureListener != null;
            }
        });
        this.scaleGestureDetector = scaleGestureDetector;
        ScaleGestureDetectorCompat.setQuickScaleEnabled(scaleGestureDetector, false);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ItemAnimator itemAnimator = getItemAnimator();
        boolean onTouchEvent = this.scaleGestureDetector.onTouchEvent(ev);
        return (itemAnimator == null || !itemAnimator.isRunning()) ? onTouchEvent | super.dispatchTouchEvent(ev) : onTouchEvent;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setItemAnimator(ItemAnimator animator) {
        super.setItemAnimator(animator);
    }

    public void setOnScaleGestureListener(ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener) {
        this.onScaleGestureListener = onScaleGestureListener;
    }
}
