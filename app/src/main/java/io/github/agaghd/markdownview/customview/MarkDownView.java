package io.github.agaghd.markdownview.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : 封装的MarkDown渲染控件，使用RecyclerView实现
 */

public class MarkDownView extends RecyclerView {

    private MarkDownAdapter markDownAdapter;

    public MarkDownView(Context context) {
        super(context);
        init();
    }

    public MarkDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarkDownView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        markDownAdapter = new MarkDownAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        setLayoutManager(linearLayoutManager);
        setItemAnimator(new DefaultItemAnimator());
        setAdapter(markDownAdapter);
    }

    /**
     * 设置数据的方法
     *
     * @param data 数据 字符串List
     */
    public void setData(List<String> data) {
        markDownAdapter.setData(data);
    }

    /**
     * 添加数据的方法
     *
     * @param data 数据 字符串List
     */
    public void addData(List<String> data) {
        markDownAdapter.addData(data);
    }

    /**
     * 添加单条数据的方法
     *
     * @param data 添加的单条数据
     */
    public void addData(String data) {
        markDownAdapter.addData(data);
    }

}
