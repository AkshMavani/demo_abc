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
public class DragHorizontalLayout extends FrameLayout implements View.OnTouchListener, View.OnLongClickListener {
    private static final float DEFAULT_FRICTION = 20.0f;
    private static final boolean DEFAULT_INTERCEPT_TOUCH_EVENT = true;
    private static final float DEFAULT_SCALE = 0.25f;
    private static final float DEFAULT_TENSION = 800.0f;
    private static final String TAG = "DragLayout";
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
    private float upX;
    private float upY;
    float x1;
    float y1;

    /* loaded from: classes2.dex */
    public interface DragListener {
        void onDrag(float x, float y);

        void onDragFinished(float x, float y);

        void onDragStarted(float x, float y);
    }

    public DragHorizontalLayout(Context context) {
        this(context, null);
    }

    public DragHorizontalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragHorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.tension = DEFAULT_TENSION;
        this.friction = DEFAULT_FRICTION;
        this.scale = DEFAULT_SCALE;
        this.isDraggable = new AtomicBoolean(false);
        this.longClickDefined = false;
        this.min_distance = 100;
        init(context, attrs);
    }

    public DragHorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.tension = DEFAULT_TENSION;
        this.friction = DEFAULT_FRICTION;
        this.scale = DEFAULT_SCALE;
        this.isDraggable = new AtomicBoolean(false);
        this.longClickDefined = false;
        this.min_distance = 100;
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
        return motionEvent.getPointerCount() == 1 && motionEvent.getAction() == 2 && this.intercepting;
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x000b, code lost:            if (r0 != 2) goto L26;     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchTouchEvent(MotionEvent r7) {
        /*
            r6 = this;
            r6.lastTouchEvent = r7
            int r0 = r7.getAction()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto Le
            r3 = 2
            if (r0 == r3) goto L29
            goto L79
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
            r6.upX = r0
            float r0 = r7.getY()
            r6.upY = r0
            float r3 = r6.downX
            float r4 = r6.upX
            float r3 = r3 - r4
            float r4 = r6.downY
            float r4 = r4 - r0
            float r0 = java.lang.Math.abs(r3)
            float r5 = java.lang.Math.abs(r4)
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            r5 = 0
            if (r0 <= 0) goto L62
            float r0 = java.lang.Math.abs(r3)
            int r4 = r6.min_distance
            float r4 = (float) r4
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L79
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 >= 0) goto L5b
            r6.intercepting = r1
        L5b:
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 <= 0) goto L79
            r6.intercepting = r2
            goto L79
        L62:
            float r0 = java.lang.Math.abs(r4)
            int r1 = r6.min_distance
            float r1 = (float) r1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L79
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r0 >= 0) goto L73
            r6.intercepting = r2
        L73:
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r0 <= 0) goto L79
            r6.intercepting = r2
        L79:
            boolean r7 = super.dispatchTouchEvent(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.iphoto.view.DragHorizontalLayout.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View.OnTouchListener
    public synchronized boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG, "onTouch()");
        this.lastTouchEvent = event;
        if (this.isDraggable.get()) {
            int action = event.getAction();
            if (action == 0) {
                this.initialX = getX();
                this.initialY = getY();
                this.x1 = event.getX();
                this.initialTouchX = event.getRawX();
                this.initialTouchY = event.getRawY();
                DragListener dragListener = this.dragListener;
                if (dragListener != null) {
                    dragListener.onDragStarted(this.initialX, this.initialY);
                }
                return true;
            }
            if (action != 1) {
                if (action == 2) {
                    this.y1 = event.getX();
                    float rawX = this.initialX + ((int) (event.getRawX() - this.initialTouchX));
                    float rawY = this.initialY + ((int) (event.getRawY() - this.initialTouchY));
                    setX(rawX);
                    DragListener dragListener2 = this.dragListener;
                    if (dragListener2 != null) {
                        dragListener2.onDrag(rawX, rawY);
                    }
                    return true;
                }
                if (action != 3) {
                }
            }
            this.isDraggable.set(false);
            this.intercepting = false;
            DragListener dragListener3 = this.dragListener;
            if (dragListener3 != null) {
                dragListener3.onDragFinished(getX(), getY());
            }
            if (getY() <= 50.0f && getY() >= -100.0f) {
                setX(this.initialX);
            }
            return true;
        }
        return false;
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
    public void setOnLongClickListener(OnLongClickListener l) {
        if (this.longClickDefined) {
            return;
        }
        super.setOnLongClickListener(l);
        this.longClickDefined = true;
    }
}
