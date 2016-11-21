package com.fabinpaul.project_1_popularmovies.framework.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.fabinpaul.project_1_popularmovies.R;

/**
 * Created by Fabin Paul, Eous Solutions Delivery on 11/7/2016 3:24 PM.
 */

public class ProportionalImageView extends ImageView {

    private float mAspectRatio = 1.5f;

    public ProportionalImageView(Context context) {
        super(context);
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ProportionalImageView,
                0, 0);
        try {
            mAspectRatio = typedArray.getFloat(R.styleable.ProportionalImageView_aspectRatio, 1.5f);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * mAspectRatio);
        setMeasuredDimension(width, height);
    }
}
