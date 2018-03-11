package io.github.agaghd.markdownview.utils;

/**
 * author : wjy
 * time   : 2018/03/11
 * desc   : 各类MarkDown语句的分类,使用int值表示
 */

public class MarkDownStatements {

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

    private MarkDownStatements() {

    }
}
