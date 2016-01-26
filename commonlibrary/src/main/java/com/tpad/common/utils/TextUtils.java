package com.tpad.common.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by jiajun.jiang on 2015/4/7.
 */
public class TextUtils {

    private static TextUtils mTextUtils;

    public TextUtils() {

    }
    public static TextUtils getInstance() {
        if (mTextUtils == null) {
            mTextUtils = new TextUtils();
        }
        return mTextUtils;
    }

    public void ConverPartOfTextSizeOfString(TextView textview, int size,
                                             int firsrc, int firdst) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textview
                .getText().toString());
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size, true);
        builder.setSpan(sizeSpan, firsrc, firdst,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(builder);
    }


    public void ConverPartOfWordsColorOfString(TextView textview, int color,
                                               int firsrc, int firdst, int secsrc, int secdst) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textview
                .getText().toString());
        ForegroundColorSpan greenSpan1 = new ForegroundColorSpan(color);
        ForegroundColorSpan greenSpan2 = new ForegroundColorSpan(color);
        builder.setSpan(greenSpan1, firsrc, firdst,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(greenSpan2, secsrc, secdst,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(builder);
    }

    public void ConverPartOfWordsColorOfString(TextView textview, int color,
                                               int firsrc, int firdst) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textview
                .getText().toString());
        ForegroundColorSpan greenSpan1 = new ForegroundColorSpan(color);
        builder.setSpan(greenSpan1, firsrc, firdst,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(builder);
    }

    /**
     * 获取设置大小后的字符串
     * @param content
     * @param hintTextSize
     * @return
     */
    public SpannedString getSpannedString(String content ,int hintTextSize){
        SpannableString ss = new SpannableString(content);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(hintTextSize, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return new SpannedString(ss);
    }

}
