package io.github.agaghd.markdownview;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : 显示markdown使用的适配器
 */

public class MarkDownAdapter extends RecyclerView.Adapter<MarkDownAdapter.MarkDownAdapterViewHolder> {

    private List<String> sourceList;//数据来源：markDown语句字符串

    public MarkDownAdapter() {
        sourceList = new ArrayList<>();
    }

    @Override
    public MarkDownAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MarkDownAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_markdown_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(MarkDownAdapterViewHolder holder, int position) {
        if (sourceList != null && sourceList.size() > position) {
            String markDownStr = sourceList.get(position);
            if (!TextUtils.isEmpty(markDownStr)) {
                MarkDown.setMarkDownText(markDownStr, holder.itemTv);
            }
        }
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    /**
     * 设置数据源的方法
     *
     * @param sourceList 数据源
     */
    public void setData(List<String> sourceList) {
        this.sourceList = sourceList;
        notifyDataSetChanged();
    }

    /**
     * 添加数据的方法
     *
     * @param data 添加的数据集合
     */
    public void addData(List<String> data) {
        sourceList.addAll(data);
        notifyItemChanged(sourceList.size() - 1);
    }

    class MarkDownAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTv;

        public MarkDownAdapterViewHolder(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView;
        }
    }
}