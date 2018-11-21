package com.hwy.treeview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hwy.treeview.entity.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hewenyu
 * 日期: 2018/11/21 9:52
 * 说明: 树形结构适配器
 */
public abstract class TreeAdapter<T> extends BaseAdapter {

    protected Context mContext;

    protected LayoutInflater mInflater;

    /**
     * 当前显示的数据集
     */
    protected List<Node<T>> mNodes;

    /**
     * 所有的数据集
     */
    protected List<Node<T>> mAllNodes;

    /**
     * 子节点同父节点起始位置的间距
     */
    private int mLevelPadding = 30;

    /**
     * 布局文件
     */
    private int mLayoutId;

    /**
     * 选中的节点
     */
    private List<Node<T>> mCheckedNodes;

    // region ------------ 构造 -------------------

    public TreeAdapter(Context context, List<T> datas, int layoutId) {
        this(context, datas, 0, layoutId);
    }

    /**
     * @param context
     * @param datas
     * @param defaultExpendLevel 默认展开的层级
     * @param layoutId
     */
    public TreeAdapter(Context context, List<T> datas, int defaultExpendLevel, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        this.mLayoutId = layoutId;

        try {
            mAllNodes = TreeHelper.getSortNodes(datas, defaultExpendLevel);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 过滤出可见的Node
        mNodes = TreeHelper.filterVisibleNodes(mAllNodes);
        mCheckedNodes = new ArrayList<>();

    }

    // endregion ----------------------------

    // region ------------ 适配器方法 -----------

    @Override
    public int getCount() {
        return mNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mNodes.get(position);
        TreeViewHolder viewHolder = TreeViewHolder.get(mInflater, convertView, parent, mLayoutId);

        convert(viewHolder, node, position, parent);

        // 通过设置内边距来区分父节点和子节点的缩进
        viewHolder.getContentView().setPadding(node.getLevel() * mLevelPadding,
                viewHolder.getContentView().getPaddingTop(),
                viewHolder.getContentView().getPaddingRight(),
                viewHolder.getContentView().getPaddingBottom());

        return viewHolder.getContentView();
    }

    // endregion -------------------------------------

    /**
     * 外部重写方法
     *
     * @param holder
     * @param node
     * @param position
     * @param parent
     */
    protected abstract void convert(TreeViewHolder holder, Node<T> node, int position, ViewGroup parent);

    /**
     * 添加选中的节点
     *
     * @param node
     */
    public void addCheckedNode(Node<T> node) {
        if (node != null) {
            if (!mCheckedNodes.contains(node)) {
                mCheckedNodes.add(node);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 添加多个选中的节点
     *
     * @param nodes
     */
    public void addCheckedNodes(Node<T>... nodes) {
        for (Node<T> node : nodes) {
            if (!mCheckedNodes.contains(node)) {
                mCheckedNodes.add(node);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 添加多个选中的节点
     *
     * @param nodes
     */
    public void addCheckedNodes(List<Node<T>> nodes) {
        for (Node<T> node : nodes) {
            if (!mCheckedNodes.contains(node)) {
                mCheckedNodes.add(node);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清空选中的节点
     */
    public void clearCheckedNode() {
        if (!mCheckedNodes.isEmpty()) {
            mCheckedNodes.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 获取选中的节点
     *
     * @return
     */
    public List<Node<T>> getCheckedNodes() {
        return mCheckedNodes;
    }

    /**
     * 设置选中的节点的图标(convert 方法中调用)
     *
     * @param holder
     * @param viewId
     * @param node
     * @param checkedResId
     * @param normalResId
     */
    protected void updateCheckedNodeIcon(TreeViewHolder holder, int viewId, Node<T> node, int checkedResId, int normalResId) {
        ImageView iv = holder.getView(viewId);
        if (mCheckedNodes.contains(node)) {
            iv.setImageResource(checkedResId);
        } else {
            iv.setImageResource(normalResId);
        }
    }

    /**
     * 设置具体的View的点击事件来操作是否选中item
     *
     * @param holder
     * @param viewId
     */
    protected void setCheckedNodeListener(TreeViewHolder holder, int viewId, final Node<T> node) {
        View view = holder.getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemCheck(mCheckedNodes, node);
                }

                notifyDataSetChanged();
            }
        });
    }

    /**
     * 设置展开/关闭/没有子节点的图标(convert 方法中调用)
     *
     * @param holder
     * @param viewId        ImageView的Id
     * @param node          对应的节点
     * @param expendResId   展开的图标
     * @param collapseResId 关闭的图标
     */
    protected void updateExpendOrCollapseIcon(TreeViewHolder holder, int viewId, Node<T> node, int expendResId, int collapseResId) {
        ImageView iv = holder.getView(viewId);
        iv.setVisibility(View.INVISIBLE);

        if (!node.isLeaf() && node.isExpand()) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(expendResId);
        } else if (!node.isLeaf() && !node.isExpand()) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(collapseResId);
        }

    }

    /**
     * 设置展开/折叠点击事件监听(convert 方法中调用)
     *
     * @param holder
     * @param viewId
     * @param position
     */
    protected void setExpendOrCollapseListener(TreeViewHolder holder, int viewId, final int position) {

        View view = holder.getView(viewId);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expendOrCollapse(position);
                }
            });
        }
    }

    /**
     * 具体操作对应位置展开/关闭的具体操作
     *
     * @param position
     */
    protected void expendOrCollapse(int position) {
        Node<T> node = mNodes.get(position);
        if (node != null) {
            if (!node.isLeaf()) {
                node.setExpand(!node.isExpand());
                mNodes = TreeHelper.filterVisibleNodes(mAllNodes);
                // 刷新视图
                notifyDataSetChanged();
            }
        }
    }


    public int getLevelPadding() {
        return mLevelPadding;
    }

    /**
     * 设置子View同parent缩进的距离
     *
     * @param levelPadding
     */
    public void setLevelPadding(int levelPadding) {
        this.mLevelPadding = levelPadding;
        notifyDataSetChanged();
    }

    // region ------- 回调监听 ----------------

    private OnItemCheckListener onItemCheckListener;

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    /**
     * Item选中事件的回调监听
     */
    public interface OnItemCheckListener<T> {

        void onItemCheck(List<Node<T>> checkedNodes, Node<T> currentNode);

    }

    // endregion ------------------------

}
