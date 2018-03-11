package io.github.agaghd.markdownview.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : MarkDown处理类
 */

public class MarkDown {

    /**
     * 解析需要的所有正则表达式
     */
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
        public static final Pattern HYPER_LINK = Pattern.compile("[^!]{0}\\[.*\\]\\(.*\\)");

        /**
         * MarkDown 斜体正则
         */
        public static final Pattern ITALIC = Pattern.compile("[^\\*]{0}\\*[^\\*]+\\*[^\\*]{0}");

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

        /**
         * MarkDown 图片和超链接URL的简单正则
         */
        public static final Pattern URL = Pattern.compile("[^(]{0}\\(.*\\)$");

        private Patterns() {
        }
    }

    /**
     * 各类MarkDown语句的分类,使用int值表示
     */
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
     * @param sourceStr   源文字字符串
     * @param targetTv    目标TextView
     * @param olNumber    有序列表的起始值
     * @param olNumberMap 保存有序列表每条对应的索引，避免因Recyclerview Item复用时导致序号混乱
     */
    public static void setMarkDownText(String sourceStr, TextView targetTv, int[] olNumber, Map<String, Integer> olNumberMap) {
        //解析MarkDown语句，将结果显示在targetTv上
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
                int ol = olNumberMap.containsKey(sourceStr) ? olNumberMap.get(sourceStr) : olNumber[0];
                olNumber[0] = ol;
                olNumberMap.put(sourceStr, ol);
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
            setBoldSpannableString(spannableStringBuilder);
            //解析所有斜体文字
            setItalicSpannableString(spannableStringBuilder);
            //解析所有图片
            setImageSpannableString(spannableStringBuilder, targetTv);
            //解析所有超链接
            setHyperLinkSpannableString(spannableStringBuilder);
            targetTv.setText(spannableStringBuilder);
            //使clickspan点击生效
            targetTv.setMovementMethod(ClickOnlyMovementMethod.getInstance());
        } else {
            targetTv.setText(spannableStringBuilder);
        }
    }

    /**
     * 处理1~3级标题的方法
     * 字体加粗，字号根据标题级别放大
     *
     * @param statement              MarkDown语句所属的类型
     * @param sourceStr              源字符串
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setHSpannableString(int statement, String sourceStr, SpannableStringBuilder spannableStringBuilder) {
        //计算标题内容的字号相对大小
        float proportion = 2 - statement / 4f;
        sourceStr = sourceStr.substring(statement, sourceStr.length()).trim();
        spannableStringBuilder.append(sourceStr);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
        spannableStringBuilder.setSpan(relativeSizeSpan, 0, sourceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //标题都是粗体
        StyleSpan hBoldStyle = new StyleSpan(Typeface.BOLD);
        spannableStringBuilder.setSpan(hBoldStyle, 0, sourceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 处理有序列表的方法
     * 内容 = 序号 + .+ 空格 + 列表内容.trim()
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
     * 内容 = ● + 空格 + 列表内容.trim()
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
        //解析所有斜体文字
        String sourceStr = spannableStringBuilder.toString();
        Matcher matcher = Patterns.ITALIC.matcher(sourceStr);
        while (matcher.find()) {
            String italicStr = matcher.group();
            int start = sourceStr.indexOf(italicStr);
            int end = start + italicStr.length();
            StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
            spannableStringBuilder.setSpan(italicSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //将左右两边的*删掉
            spannableStringBuilder.replace(start, end, spannableStringBuilder.subSequence(start + 1, end - 1));
            sourceStr = spannableStringBuilder.toString();
        }
    }

    /**
     * 设置粗体文字的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setBoldSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //解析所有粗体文字
        String sourceStr = spannableStringBuilder.toString();
        Matcher matcher = Patterns.BOLD.matcher(sourceStr);
        while (matcher.find()) {
            String boldStr = matcher.group();
            int start = sourceStr.indexOf(boldStr);
            int end = start + boldStr.length();
            StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
            spannableStringBuilder.setSpan(boldStyle, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //将左右两边的*删掉
            spannableStringBuilder.replace(start, end, spannableStringBuilder.subSequence(start + 2, end - 2));
            sourceStr = spannableStringBuilder.toString();
        }
    }

    /**
     * 设置超链接的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setHyperLinkSpannableString(SpannableStringBuilder spannableStringBuilder) {
        //解析所有超链接
        String sourceStr = spannableStringBuilder.toString();
        Matcher matcher = Patterns.HYPER_LINK.matcher(sourceStr);
        while (matcher.find()) {
            String hyperLinkStr = matcher.group();
            int start = sourceStr.indexOf(hyperLinkStr);
            int end = start + hyperLinkStr.length();
            int urlStart = 0;
            //找出Url并设置可点击
            String url = "";
            Matcher urlMatcher = Patterns.URL.matcher(hyperLinkStr);
            if (urlMatcher.find()) {
                url = urlMatcher.group();
                urlStart = sourceStr.indexOf(url);
            }
            final String finalUrl = url.length() > 2 ? url.substring(1, url.length() - 1) : "";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(finalUrl));
                    widget.getContext().startActivity(intent);
                }
            };
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //删掉中括号和小括号Url部分
            spannableStringBuilder.replace(urlStart, end, "");
            spannableStringBuilder.replace(urlStart - 1, urlStart, "");
            spannableStringBuilder.replace(start, start + 1, "");
            sourceStr = spannableStringBuilder.toString();
        }
    }

    /**
     * 设置图片的方法
     *
     * @param spannableStringBuilder 保存修改样式的spannablestring
     */
    private static void setImageSpannableString(final SpannableStringBuilder spannableStringBuilder, final TextView targetTv) {
        // 解析所有图片
        String sourceStr = spannableStringBuilder.toString();
        Matcher matcher = Patterns.IMAGE.matcher(sourceStr);
        while (matcher.find()) {
            String hyperLinkStr = matcher.group();
            int start = sourceStr.indexOf(hyperLinkStr);
            int end = start + hyperLinkStr.length();
            int urlStart = 0;
            //找出Url并设置可点击
            String url = "";
            Matcher urlMatcher = Patterns.URL.matcher(hyperLinkStr);
            if (urlMatcher.find()) {
                url = urlMatcher.group();
                urlStart = sourceStr.indexOf(url);
            }
            final String finalUrl = url.length() > 3 ? url.substring(1, url.length() - 1) : "";
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(finalUrl));
                    widget.getContext().startActivity(intent);
                }
            };
            String imageStr = sourceStr.substring(start + 2, urlStart - 1);
            spannableStringBuilder.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //删除感叹号、中括号和小括号Url部分
            spannableStringBuilder.replace(urlStart, end, "");
            spannableStringBuilder.replace(urlStart - 1, urlStart, "");
            spannableStringBuilder.replace(start, start + 2, "");
            sourceStr = spannableStringBuilder.toString();
            final int imageStrStart = sourceStr.indexOf(imageStr);
            final int imageStrEnd = imageStrStart + imageStr.length();
            SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    if (resource != null) {
                        //压缩图片的操作可能较为耗时，放在子线程做
                        AsyncTask<Object, Integer, Bitmap> asyncTask = new AsyncTask<Object, Integer, Bitmap>() {

                            int maxWidth = 0;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                maxWidth = targetTv.getContext().getResources().getDisplayMetrics().widthPixels;
                            }

                            @Override
                            protected Bitmap doInBackground(Object... params) {
                                return PictureUtil.compressBitmapByWidth(resource, maxWidth);
                            }

                            @Override
                            protected void onPostExecute(Bitmap bitmap) {
                                super.onPostExecute(bitmap);
                                ImageSpan imageSpan = new ImageSpan(targetTv.getContext(), bitmap);
                                spannableStringBuilder.setSpan(imageSpan, imageStrStart, imageStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                targetTv.setText(spannableStringBuilder);
                                targetTv.setMovementMethod(ClickOnlyMovementMethod.getInstance());
                            }
                        };
                        asyncTask.execute();
                    }
                }
            };
            //使用Glide加载图片
            Glide.with(targetTv.getContext())
                    .load(finalUrl)
                    .asBitmap()
                    .into(simpleTarget);
        }
    }

    private MarkDown() {

    }
}
