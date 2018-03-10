package io.github.agaghd.markdownview.utils;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : MarkDown处理类
 */

public class MarkDown {

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
        public static final Pattern IMAGE = Pattern.compile("!\\[.*\\]\\(.*\\)");

        /**
         * MarkDown 超链接正则
         */
        public static final Pattern HYPER_LINK = Pattern.compile("\\[.*\\]\\(.*\\)");

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

        private Patterns() {
        }
    }

    public static class Statement {
        /**
         * MarkDown 1~3级标题
         */
        public static final int H1 = 1;
        public static final int H2 = 2;
        public static final int H3 = 3;

        /**
         * MarkDown 图片
         */
        public static final int IMAGE = 4;

        /**
         * MarkDown 超链接
         */
        public static final int HYPER_LINK = 5;

        /**
         * MarkDown 斜体
         */
        public static final int ITALIC = 6;

        /**
         * MarkDown 粗体
         */
        public static final int BOLD = 7;

        /**
         * MarkDown 有序列表
         */
        public static final int OL = 8;

        /**
         * MarkDown 无序列表
         */
        public static final int UL = 9;

        private Statement() {
        }
    }

    /**
     * 解析MarkDown语句，将结果显示在targetTv上
     * 先先解析作用于整行的类型，再解析每行中的元素
     *
     * @param sourceStr 源文字字符串
     * @param targetTv  目标TextView
     * @param olNumber  有序列表的起始值
     */
    public static void setMarkDownText(String sourceStr, TextView targetTv, int[] olNumber) {
        // TODO: 2018/3/9 解析MarkDown语句，将结果显示在targetTv上
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(sourceStr)) {
            //先解析作用于整行的类型，如1~3级标题，有序列表和无序列表, 有序列表以外作用于整行的类型会重置有序列表的起始值
            if (Patterns.H1.matcher(sourceStr).matches()) {
                setHSpannableString(Statement.H1, sourceStr, spannableStringBuilder);
                olNumber[0] = 1;
            } else if (Patterns.H2.matcher(sourceStr).matches()) {
                setHSpannableString(Statement.H2, sourceStr, spannableStringBuilder);
                olNumber[0] = 1;
            } else if (Patterns.H3.matcher(sourceStr).matches()) {
                setHSpannableString(Statement.H3, sourceStr, spannableStringBuilder);
                olNumber[0] = 1;
            } else if (Patterns.OL.matcher(sourceStr).matches()) {
                setOlSpannableString(sourceStr, spannableStringBuilder, olNumber);
                //有序列表索引自增
                olNumber[0]++;
            } else if (Patterns.UL.matcher(sourceStr).matches()) {
                setUlSpannableString(sourceStr, spannableStringBuilder);
                olNumber[0] = 1;
            } else {
                spannableStringBuilder.append(sourceStr);
            }
            //解析所有粗体文字
            setItalicSpannableString(spannableStringBuilder);
            //解析所有斜体文字
            setBoldSpannableString(spannableStringBuilder);
            //解析所有超链接
            setHyperLinkSpannableString(spannableStringBuilder);
            //解析所有图片
            setImageSpannableString(spannableStringBuilder);
            //TODO 用于测试，完成上述所有方法后这句删除
            targetTv.setText(spannableStringBuilder);
        } else {
            targetTv.setText(spannableStringBuilder);
        }
    }

    /**
     * 处理1~3级标题的方法
     *
     * @param statement              MarkDown语句所属的类型
     * @param sourceStr              源字符串
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setHSpannableString(int statement, String sourceStr, SpannableStringBuilder spannableStringBuilder) {
        float proportion = 2 - statement / 4f;
        sourceStr = sourceStr.substring(statement, sourceStr.length()).trim();
        spannableStringBuilder.append(sourceStr);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
        spannableStringBuilder.setSpan(relativeSizeSpan, 0, sourceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 处理有序列表的方法
     *
     * @param sourceStr              源字符串
     * @param spannableStringBuilder 保存修改样式的spannablestring
     * @param olNumber               序号
     */
    private static void setOlSpannableString(String sourceStr, SpannableStringBuilder spannableStringBuilder, int[] olNumber) {
        sourceStr = sourceStr.substring(2, sourceStr.length()).trim();
        spannableStringBuilder.append(String.valueOf(olNumber[0])).append(". ").append(sourceStr);
    }

    /**
     * 处理无序列表的方法
     *
     * @param sourceStr              源字符串
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setUlSpannableString(String sourceStr, SpannableStringBuilder spannableStringBuilder) {
        sourceStr = sourceStr.substring(2, sourceStr.length()).trim();
        spannableStringBuilder.append("● ").append(sourceStr);
    }

    /**
     * 设置斜体文字的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setItalicSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //TODO 解析所有斜体文字
        String sourceStr = spannableStringBuilder.toString();
        Matcher matcher = Patterns.ITALIC.matcher(sourceStr);
        while (matcher.find()) {
            String italicStr = matcher.group();
            int start = sourceStr.indexOf(italicStr);
            int end = start + italicStr.length();
            StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
            spannableStringBuilder.setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.replace(start, start + 1, "");
            spannableStringBuilder.replace(end - 1, end, "");
        }
    }

    /**
     * 设置粗体文字的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setBoldSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //TODO 解析所有粗体文字
    }

    /**
     * 设置超链接的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setHyperLinkSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //TODO 解析所有超链接
    }

    /**
     * 设置图片的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setImageSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //TODO 解析所有图片
    }


    private MarkDown() {

    }
}
