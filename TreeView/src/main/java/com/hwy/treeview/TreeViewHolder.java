package com.hwy.treeview;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者: hewenyu
 * 日期: 2018/11/4 16:17
 * 说明: 树形结构适配器视图辅助类
 */
public class TreeViewHolder {

    private SparseArray<View> mViews;

    private View contentView;

    private TreeViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId) {
        this.contentView = inflater.inflate(layoutId, parent, false);
        mViews = new SparseArray<>();
        this.contentView.setTag(this);
    }

    /**
     * 获取一个ViewHolder对象
     *
     * @param inflater
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static TreeViewHolder get(LayoutInflater inflater, View convertView, ViewGroup parent, int layoutId) {
        if (null == convertView) {
            return new TreeViewHolder(inflater, parent, layoutId);
        } else {
            TreeViewHolder holder = (TreeViewHolder) convertView.getTag();
            return holder;
        }
    }

    /**
     * 获取 item 布局
     *
     * @return
     */
    public View getContentView() {
        return this.contentView;
    }

    /**
     * 获取View
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = contentView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, view);
            } else {
                throw new RuntimeException("no find view in adapter layout");
            }
        }
        return (T) view;
    }

    /**
     * 设置 TextView 文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public TreeViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (!TextUtils.isEmpty(text)) {
            tv.setText(text);
        } else {
            tv.setText("");
        }
        return this;
    }

    /**
     * 设置文本颜色
     *
     * @param viewId
     * @param color
     * @return
     */
    public TreeViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    /**
     * 设置是否选中
     *
     * @param viewId
     * @param isChecked
     * @return
     */
    public TreeViewHolder setChecked(int viewId, boolean isChecked) {
        CheckBox cb = getView(viewId);
        cb.setChecked(isChecked);
        return this;
    }

    public TreeViewHolder setImageRes(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

}
