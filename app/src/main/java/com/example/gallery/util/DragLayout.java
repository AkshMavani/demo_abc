package com.example.gallery.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.demo_full.R;

import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class DragLayout extends FrameLayout implements View.OnTouchListener, View.OnLongClickListener {
    private static final float DEFAULT_FRICTION = 20.0f;
    private static final boolean DEFAULT_INTERCEPT_TOUCH_EVENT = true;
    private static final float DEFAULT_SCALE = 0.25f;
    private static final float DEFAULT_TENSION = 800.0f;
    private static final String TAG = "HuyAnhP";
    private float downX;
    private float downY;
    private DragListener dragListener;
    private float friction;
    private float initialTouchX;
    private float initialTouchY;
    private float initialX;
    private float initialY;
    private boolean intercepting;
    private volatile AtomicBoolean isDraggable;
    private MotionEvent lastTouchEvent;
    private boolean longClickDefined;
    private int min_distance;
    private float scale;
    private float tension;

    /* loaded from: classes2.dex */
    public interface DragListener {
        void onDrag(float x, float y);

        void onDragFinished(float x, float y);

        void onDragStarted(boolean check);
    }

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tension = DEFAULT_TENSION;
        this.friction = DEFAULT_FRICTION;
        this.scale = DEFAULT_SCALE;
        this.isDraggable = new AtomicBoolean(false);
        this.longClickDefined = false;
        this.min_distance = 60;
        init(context, attrs);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.tension = DEFAULT_TENSION;
        this.friction = DEFAULT_FRICTION;
        this.scale = DEFAULT_SCALE;
        this.isDraggable = new AtomicBoolean(false);
        this.longClickDefined = false;
        this.min_distance = 60;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.DragLayout);
            for (int i = 0; i < obtainStyledAttributes.getIndexCount(); i++) {
                int index = obtainStyledAttributes.getIndex(i);
                if (index == 1) {
                    this.scale = obtainStyledAttributes.getFloat(index, DEFAULT_SCALE);
                } else if (index == 2) {
                    this.friction = obtainStyledAttributes.getFloat(index, DEFAULT_FRICTION);
                } else if (index == 3) {
                    this.tension = obtainStyledAttributes.getFloat(index, DEFAULT_TENSION);
                }
            }
            obtainStyledAttributes.recycle();
        }
        setClickable(true);
        setOnTouchListener(this);
        setOnLongClickListener(this);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getPointerCount() == 1 && motionEvent.getAction() == 2 && this.intercepting) {
            return true;
        }
        if (motionEvent.getPointerCount() == 1) {
            motionEvent.getAction();
        }
        return false;
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        super.setOnClickListener(l);
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x000b, code lost:            if (r0 != 2) goto L19;     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            r6.lastTouchEvent = r7
            int r0 = r7.getAction()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto Le
            r3 = 2
            if (r0 == r3) goto L29
            goto L5a
        Le:
            float r0 = r7.getX()
            r6.downX = r0
            float r0 = r7.getY()
            r6.downY = r0
            java.util.concurrent.atomic.AtomicBoolean r0 = r6.isDraggable
            r0.set(r1)
            android.view.MotionEvent r0 = r6.lastTouchEvent
            r0.setAction(r2)
            android.view.MotionEvent r0 = r6.lastTouchEvent
            r6.onTouch(r6, r0)
        L29:
            float r0 = r7.getX()
            float r3 = r7.getY()
            float r4 = r6.downX
            float r0 = r0 - r4
            float r4 = r6.downY
            float r3 = r3 - r4
            float r4 = java.lang.Math.abs(r0)
            float r5 = java.lang.Math.abs(r3)
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto L51
            float r0 = java.lang.Math.abs(r0)
            int r1 = r6.min_distance
            float r1 = (float) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L5a
            r6.intercepting = r2
            goto L5a
        L51:
            int r0 = r6.min_distance
            float r0 = (float) r0
            int r0 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1))
            if (r0 < 0) goto L5a
            r6.intercepting = r1
        L5a:
            boolean r7 = super.dispatchTouchEvent(r7)     // Catch: java.lang.Exception -> L5f
            return r7
        L5f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.iphoto.view.DragLayout.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View.OnTouchListener
    public synchronized boolean onTouch(View v, MotionEvent event) {
        this.lastTouchEvent = event;
        if (this.isDraggable.get()) {
            int action = event.getAction();
            if (action == 0) {
                this.initialX = getX();
                this.initialY = getY();
                this.initialTouchX = event.getRawX();
                this.initialTouchY = event.getRawY();
                setPivotX(getWidth() / 2.0f);
                setPivotY(getHeight() / 2.0f);
                return true;
            }
            if (action == 1) {
                this.isDraggable.set(false);
                this.intercepting = false;
                setPivotX(0.0f);
                setPivotY(0.0f);
                DragListener dragListener = this.dragListener;
                if (dragListener != null) {
                    dragListener.onDragFinished(getX(), getY());
                }
                if (getY() <= 50.0f && getY() >= -100.0f) {
                    setX(this.initialX);
                    setY(this.initialY);
                }
                return true;
            }
            if (action == 2) {
                float rawX = this.initialX + ((int) (event.getRawX() - this.initialTouchX));
                float rawY = this.initialY + ((int) (event.getRawY() - this.initialTouchY));
                setX(rawX);
                setY(rawY);
                float rawY2 = event.getRawY() - this.initialTouchY;
                int i = this.min_distance;
                if (rawY2 >= i) {
                    float min = Math.min(1.0f, Math.max(0.76f, 1.0f - ((rawY2 - i) / 2000.0f)));
                    setScaleX(min);
                    setScaleY(min);
                } else {
                    setScaleX(1.0f);
                    setScaleY(1.0f);
                }
                DragListener dragListener2 = this.dragListener;
                if (dragListener2 != null) {
                    dragListener2.onDrag(rawX, rawY);
                }
                return true;
            }
        }
        return true;
    }

    public void setDragListener(DragListener listener) {
        this.dragListener = listener;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isIntercepting() {
        return this.intercepting;
    }

    public void setIntercepting(boolean intercepting) {
        this.intercepting = intercepting;
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View v) {
        Log.i(TAG, "onLongClick()");
        this.isDraggable.set(true);
        this.lastTouchEvent.setAction(0);
        onTouch(this, this.lastTouchEvent);
        return true;
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener l) {
        if (this.longClickDefined) {
            return;
        }
        super.setOnLongClickListener(l);
        this.longClickDefined = true;
    }
}
