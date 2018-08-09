package com.jet.flowtaglayoutdemo;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @Author：Jet啟思
 * @Date:2018/7/24 15:07
 */
public class Utils {

    public static float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static float spToPx(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    public static float pxToSp(float px) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

}
