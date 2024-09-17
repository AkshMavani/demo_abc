package com.blurview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.OverScroller;

import java.util.ArrayList;
import java.util.List;

public class HorizontalWheelView extends View implements GestureDetector.OnGestureListener {

    private List<String> items = new ArrayList<>();
    private int selectedItemIndex = 2; // Set the 3rd item as selected (0-based index)
    private Paint textPaint;
    private float itemWidth;
    private GestureDetector gestureDetector;
    private OverScroller scroller;
    private OnItemSelectedListener listener;
    private int defaultTextColor = Color.BLACK; // Default text color
    private int selectedTextColor = Color.BLUE; // Selected text color

    public HorizontalWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);  // Set text size here
        scroller = new OverScroller(getContext());
        gestureDetector = new GestureDetector(getContext(), this);
    }

    public void setItems(List<String> items) {
        this.items = items;
        requestLayout();
        invalidate();
        // Scroll to the initially selected item
        post(() -> scrollToItem(selectedItemIndex));
    }

    public void setSelectedItemIndex(int index) {
        selectedItemIndex = index;
        scrollToItem(selectedItemIndex);
        invalidate();
    }

    public int getSelectedItemIndex() {
        return selectedItemIndex;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewHeight = getHeight();
        int viewWidth = getWidth();
        itemWidth = viewWidth / 5.0f;  // Display 5 items at once

        float centerX = viewWidth / 2.0f;

        for (int i = 0; i < items.size(); i++) {
            float xPos = centerX + (i - selectedItemIndex) * itemWidth;

            // Set text color: blue if selected, black otherwise
            if (i == selectedItemIndex) {
                textPaint.setColor(selectedTextColor);
            } else {
                textPaint.setColor(defaultTextColor);
            }

            canvas.drawText(items.get(i), xPos, viewHeight / 2.0f, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            smoothScrollToSelected();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void smoothScrollToSelected() {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }

        // Find the closest item
        int selectedItem = Math.round(getScrollX() / itemWidth);
        selectedItem = Math.max(0, Math.min(selectedItem, items.size() - 1));
        selectedItemIndex = selectedItem;

        // Smooth scroll to the selected item
        int targetScrollX = Math.round(selectedItemIndex * itemWidth);
        scroller.startScroll(getScrollX(), 0, targetScrollX - getScrollX(), 0);
        invalidate();

        // Notify listener
        if (listener != null) {
            listener.onItemSelected(selectedItemIndex);
        }
    }

    // Scroll to a specific item index
    private void scrollToItem(int index) {
        int scrollX = Math.round(index * itemWidth);
        scrollTo(scrollX, 0);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        scroller.fling(getScrollX(), 0, (int) -velocityX, 0, 0, (int) ((items.size() - 1) * itemWidth), 0, 0);
        invalidate();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        scrollBy((int) distanceX, 0);
        invalidate();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        playSoundEffect(SoundEffectConstants.CLICK);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        return true;
    }

    // Parcelable state handling for saving/restoring state
    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    // Custom listener interface
    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
