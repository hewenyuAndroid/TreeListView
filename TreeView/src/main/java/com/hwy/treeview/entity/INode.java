package com.hwy.treeview.entity;

/**
 * 作者: hewenyu
 * 日期: 2018/11/4 13:04
 * 说明: 节点
 */
public interface INode {

    /**
     * 是否是根节点
     *
     * @return
     */
    boolean isRoot();

    /**
     * 父节点是否展开
     *
     * @return
     */
    boolean isParentExpend();

    /**
     * 是否是叶子节点（没有子节点了）
     *
     * @return
     */
    boolean isLeaf();

}
