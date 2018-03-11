package io.github.agaghd.markdownview.customview;

import android.annotation.SuppressLint;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.agaghd.markdownview.R;
import io.github.agaghd.markdownview.utils.MarkDown;

/**
 * author : wjy
 * time   : 2018/03/09
 * desc   : MarkDownView使用的适配器
 */
@SuppressLint("UseSparseArrays")
public class MarkDownAdapter extends RecyclerView.Adapter<MarkDownAdapter.MarkDownAdapterViewHolder> {

    private List<String> sourceList;//数据来源：markDown语句字符串
    private int[] olNumber;//有序列表的序号, 基本类型不能传递引用，所以使用数组
    private Map<String, Integer> olNumberMap;//保存序号的散列表,避免在item复用是造成有序列表序号的混乱


    public MarkDownAdapter() {
        olNumber = new int[]{1};
        olNumberMap = new ArrayMap<>();
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
                MarkDown.setMarkDownText(markDownStr, holder.itemTv, olNumber, olNumberMap);
            } else {
                MarkDown.setMarkDownText("", holder.itemTv, olNumber, olNumberMap);
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
        olNumber[0] = 1;
        olNumberMap = new ArrayMap<>();
        this.sourceList = sourceList;
        notifyDataSetChanged();
    }

    /**
     * 添加数据的方法
     *
     * @param data 添加的数据集合
     */
    public void addData(List<String> data) {
        olNumber[0] = 1;
        int start = sourceList.size();
        sourceList.addAll(data);
        notifyItemRangeChanged(start, data.size());
    }

    /**
     * 添加单条数据的方法
     *
     * @param data 添加的单条数据
     */
    public void addData(String data) {
        olNumber[0] = 1;
        sourceList.add(data);
        notifyItemInserted(sourceList.size() - 1);
    }

    class MarkDownAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTv;

        public MarkDownAdapterViewHolder(View itemView) {
            super(itemView);
            itemTv = (TextView) itemView;
        }
    }
}
