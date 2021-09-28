package io.praveen.typenote;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {
    private final Typeface newType;

    CustomTypefaceSpan(String str, Typeface typeface) {
        super(str);
        this.newType = typeface;
    }

    private static void applyCustomTypeFace(@NonNull Paint paint, @NonNull Typeface typeface) {
        int i;
        Typeface typeface2 = paint.getTypeface();
        if (typeface2 == null) {
            i = 0;
        } else {
            i = typeface2.getStyle();
        }
        int i2 = i & (~typeface.getStyle());
        if ((i2 & 1) != 0) {
            paint.setFakeBoldText(true);
        }
        if ((i2 & 2) != 0) {
            paint.setTextSkewX(-0.25f);
        }
        paint.setTypeface(typeface);
    }

    public void updateDrawState(@NonNull TextPaint textPaint) {
        applyCustomTypeFace(textPaint, this.newType);
    }

    public void updateMeasureState(@NonNull TextPaint textPaint) {
        applyCustomTypeFace(textPaint, this.newType);
    }
}
