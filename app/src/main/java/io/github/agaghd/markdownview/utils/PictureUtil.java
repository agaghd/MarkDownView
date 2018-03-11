package io.github.agaghd.markdownview.utils;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : 图片处理工具类
 */

public class PictureUtil {

    /**
     * 压缩宽度大于最大宽度限定的图片
     * 耗时方法，建议运行在子线程
     *
     * @param bitmap   原图片
     * @param maxWidth 最大宽度限定
     * @return 以给定的最大宽度限定按比例缩小的图片
     */
    @WorkerThread
    public static Bitmap compressBitmapByWidth(
            @NonNull Bitmap bitmap,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int maxWidth) {
        if (maxWidth == 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        if (width <= maxWidth) {
            return bitmap;
        } else {
            int height = bitmap.getHeight() * maxWidth / width;
            return ThumbnailUtils.extractThumbnail(bitmap, maxWidth, height);
        }
    }

    private PictureUtil() {
    }
}
