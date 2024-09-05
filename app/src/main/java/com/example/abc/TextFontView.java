package com.example.abc;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

/* loaded from: classes2.dex */
public class TextFontView extends AppCompatTextView {
    public TextFontView(Context context) {
        super(context);
     //   setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf"));
    }

    public TextFontView(Context context, AttributeSet attrs) {
        super(context, attrs);
      //  setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf"));
    }

    public TextFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
      //  setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/font.ttf"));
    }
}
