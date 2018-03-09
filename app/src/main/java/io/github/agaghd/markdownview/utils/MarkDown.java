package io.github.agaghd.markdownview.utils;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : MarkDown处理类
 */

public class MarkDown {

    public static CharSequence setMarkDownText(String sourceStr, TextView targetTv) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        // TODO: 2018/3/9 解析MarkDown语句，将结果显示在targetTv上
        spannableStringBuilder.append(sourceStr);
        targetTv.setText(spannableStringBuilder.toString());
        return spannableStringBuilder;
    }

    private MarkDown(){

    }
}
