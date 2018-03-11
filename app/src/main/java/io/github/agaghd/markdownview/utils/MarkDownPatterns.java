package io.github.agaghd.markdownview.utils;

import java.util.regex.Pattern;

/**
 * author : wjy
 * time   : 2018/03/11
 * desc   : 解析需要的正则表达式
 */

public class MarkDownPatterns {
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

    private MarkDownPatterns() {

    }
}
