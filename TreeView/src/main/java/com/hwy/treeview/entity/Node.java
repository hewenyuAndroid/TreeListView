package com.hwy.treeview.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hewenyu
 * 日期: 2018/11/4 13:04
 * 说明: 树形结构节点类型
 *
 * @param <T>
 */
public class Node<T> implements INode {

    /**
     * 当前节点的ID
     */
    private String id;

    /**
     * 当前节点的父节点的ID
     */
    private String parentId;

    /**
     * 原始的数据，即我们实际需要展示的数据
     */
    private T rawData;

    /**
     * 当前节点的层次
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    /**
     * 子节点的集合
     */
    private List<Node> children = new ArrayList<>();

    /**
     * 父节点
     */
    private Node parent;

    /**
     * 构造方法中初始化必要的数据
     *
     * @param id
     * @param parentId
     * @param rawData
     */
    public Node(String id, String parentId, T rawData) {
        this.id = id;
        this.parentId = parentId;
        this.rawData = rawData;
    }

    // region -------------- 重写的方法 --------------

    /**
     * 判断是否是根节点
     *
     * @return
     */
    @Override
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    @Override
    public boolean isParentExpend() {
        if (parent == null) {
            return false;
        }
        return parent.isExpand;
    }

    /**
     * 判断当前节点是否是叶子节点
     *
     * @return
     */
    @Override
    public boolean isLeaf() {
        return children.size() == 0;
    }

    // endregion -------------------------------------

    // region ---------- get/set ----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取原始的数据
     *
     * @return
     */
    public T getRawData() {
        return rawData;
    }

    public void setRawData(T rawData) {
        this.rawData = rawData;
    }

    /**
     * 获取当前的层级
     *
     * @return
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置展开/折叠时的子节点变化
     *
     * @param expand
     */
    public void setExpand(boolean expand) {
        this.isExpand = expand;
        if (!isExpand) {
            for (Node child : children) {
                child.setExpand(false);
            }
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // endregion --------------------------

}
