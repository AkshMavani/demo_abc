package com.lib.customedittext;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.demo_full.R;

import java.util.ArrayList;

public class SlideLayout extends ViewGroup {
    private int mWidth;
    private int mHeight;

    private final ArrayList<View> mMatchParentChildren = new ArrayList<>(1);
    private Scroller mScroller;
    private int mTopBorder;
    private int mBottomBorder;
    private int mTouchSlop;
    private int mSlideSlop;
    private int mDuration;

    private float mTouchX, mTouchY;
    private float mLastTouchY;
    private boolean mIsDragging;
    private boolean mIsOpen;
    private boolean mIsEnable;
    private OnStateChangeListener mOnStateChangeListener;

    private View swipeButton; // Reference to the swipe button

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, com.d.lib.slidelayout.R.styleable.SlideLayout);
        mSlideSlop = (int) typedArray.getDimension(com.d.lib.slidelayout.R.styleable.SlideLayout_sl_slideSlop,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics()));
        mDuration = typedArray.getInteger(com.d.lib.slidelayout.R.styleable.SlideLayout_sl_duration, 250);
        mIsEnable = typedArray.getBoolean(com.d.lib.slidelayout.R.styleable.SlideLayout_sl_enable, true);
        typedArray.recycle();
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new LayoutParams(lp);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        final boolean measureMatchParentChildren = MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final SlideLayout.LayoutParams lp = (SlideLayout.LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                }

                if (measureMatchParentChildren) {
                    if (lp.width == SlideLayout.LayoutParams.MATCH_PARENT ||
                            lp.height == SlideLayout.LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final Drawable drawable = getForeground();
            if (drawable != null) {
                maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
                maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                    resolveSizeAndState(maxHeight, heightMeasureSpec,
                            childState << MEASURED_HEIGHT_STATE_SHIFT));
        } else {
            setMeasuredDimension(mWidth, mHeight);
        }

        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);
                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                final int childWidthMeasureSpec;
                if (lp.width == SlideLayout.LayoutParams.MATCH_PARENT) {
                    final int width = Math.max(0, getMeasuredWidth()
                            - lp.leftMargin - lp.rightMargin);
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                            width, MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                final int childHeightMeasureSpec;
                if (lp.height == SlideLayout.LayoutParams.MATCH_PARENT) {
                    final int height = Math.max(0, getMeasuredHeight()
                            - lp.topMargin - lp.bottomMargin);
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        if (count <= 0) {
            return;
        }

        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();

        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int top = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();

                int childLeft = parentLeft + lp.leftMargin;
                int childTop = parentTop + lp.topMargin;

                // Layout vertically for each child view in the ViewGroup
                child.layout(childLeft, top + childTop, childLeft + width, top + childTop + height);

                top += childTop + height + lp.bottomMargin + getPaddingBottom();
            }
        }
        // Initialize top and bottom boundary values
        mTopBorder = getChildAt(0).getTop();
        mBottomBorder = getChildAt(count - 1).getBottom();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            final boolean intercepted = mOnStateChangeListener != null
                    && mOnStateChangeListener.onInterceptTouchEvent(this);
            if (intercepted) {
                return false;
            }

            final float x = ev.getRawX();
            final float y = ev.getRawY();
            mLastTouchY = mTouchY = y;
            mTouchX = x;
            mIsDragging = false;

            // Check if the touch event is on the swipe button
            swipeButton =findViewById(R.id.swipebtn);
            if (swipeButton != null && isPointInsideView(x, y, swipeButton)) {
                super.dispatchTouchEvent(ev);
                return true;
            }
            return false; // Ignore touch events outside the swipe button
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mIsEnable) {
            return super.onInterceptTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            final float x = ev.getRawX();
            final float y = ev.getRawY();
            // Intercept child event when vertical ACTION_MOVE value is greater than TouchSlop
            if (Math.abs(y - mTouchY) > mTouchSlop
                    && Math.abs(y - mTouchY) > Math.abs(x - mTouchX)) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnable) {
            return super.onTouchEvent(event);
        }

        final float x = event.getRawX();
        final float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!mIsDragging
                        && Math.abs(y - mTouchY) > mTouchSlop
                        && Math.abs(y - mTouchY) > Math.abs(x - mTouchX)) {
                    requestDisallowInterceptTouchEvent(true);
                    mIsDragging = true;
                    mLastTouchY = y;
                    return super.onTouchEvent(event);
                }
                if (mIsDragging) {
                    final int offset = (int) (mLastTouchY - y);
                    if (getScrollY() + offset < 0) {
                        setOpen(false, false);
                        mTouchY = y; // Reset touch y
                    } else if (getScrollY() + offset > mBottomBorder - mHeight) {
                        setOpen(true, false);
                        mTouchY = y; // Reset touch y
                    } else {
                        scrollBy(0, offset);
                    }
                    mLastTouchY = y;
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    if (y - mTouchY < -mSlideSlop) {
                        setOpen(true, true);
                    } else if (y - mTouchY > mSlideSlop) {
                        setOpen(false, true);
                    } else {
                        setOpen(mIsOpen, true);
                    }
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(event);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        return (x >= viewX && x <= viewX + view.getWidth() && y >= viewY && y <= viewY + view.getHeight());
    }

    private void smoothScrollTo(int dstY, int duration) {
        int offset = dstY - getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, offset, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public boolean isEnable() {
        return mIsEnable;
    }

    public void setEnable(boolean enable) {
        this.mIsEnable = enable;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void open() {
        setOpen(true, true);
    }

    public void close() {
        setOpen(false, true);
    }

    public void setOpen(boolean open, boolean withAnim) {
        if (mIsOpen != open && mOnStateChangeListener != null) {
            mOnStateChangeListener.onStateChanged(this, open);
        }
        mIsOpen = open;
        int y = mIsOpen ? mBottomBorder - mHeight : 0;
        if (withAnim) {
            /*ObjectAnimator animator = ObjectAnimator.ofInt(this, "scrollY", getScrollY(), y);
            animator.setDuration(mDuration);
            animator.start();*/
            Log.e("MDURTI", "setOpen: "+mDuration);
            smoothScrollTo(y, mDuration);
        } else {
            scrollTo(0, y);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener listener) {
        this.mOnStateChangeListener = listener;
    }

    public abstract static class OnStateChangeListener {
        public boolean onInterceptTouchEvent(SlideLayout layout) {
            return false;
        }

        public abstract void onStateChanged(SlideLayout layout, boolean open);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}