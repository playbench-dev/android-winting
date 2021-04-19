package com.playbench.winting.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.playbench.winting.R;

public class RegularTextView extends TextView {

    Context context;

    public RegularTextView(Context context) {
        super(context);
        this.context = context;
    }

    public RegularTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RegularTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void setTypeface(Typeface tf) {
        tf = ResourcesCompat.getFont(getContext(), R.font.notosanscjkkr_medium_regular);
        super.setTypeface(tf);
    }

    @Override
    public void setIncludeFontPadding(boolean includepad) {
        super.setIncludeFontPadding(false);
    }
}
