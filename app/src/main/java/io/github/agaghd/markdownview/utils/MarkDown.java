package io.github.agaghd.markdownview.utils;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

import java.util.regex.Pattern;

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

    public static class Patterns {
        /**
         * MarkDown 1~3级标题正则
         */
        public static final Pattern H1 = Pattern.compile("^#{1}[^#]+.*$");
        public static final Pattern H2 = Pattern.compile("^#{2}[^#]+.*$");
        public static final Pattern H3 = Pattern.compile("^#{3}[^#]+.*$");

        /**
         * MarkDown 图片正则
         */
        public static final Pattern IMAGE = Pattern.compile("^!\\[.*\\]\\(.*\\)$");

        /**
         * MarkDown 超链接正则
         */
        public static final Pattern HYPER_LINK = Pattern.compile("^\\[.*\\]\\(.*\\)$");

        /**
         * MarkDown 斜体正则
         */
        public static final Pattern ITALIC = Pattern.compile("\\*[^\\*]+\\*$");

        /**
         * MarkDown 粗体正则
         */
        public static final Pattern BOLD = Pattern.compile("\\*{2}[^\\*]+\\*{2}");

        /**
         * MarkDown 有序列表正则
         */
        public static final Pattern OL = Pattern.compile("^\\d\\..*$");

        /**
         * MarkDown 无序列表正则
         */
        public static final Pattern UL = Pattern.compile("^-.*$");
    }

    private MarkDown() {

    }
}
