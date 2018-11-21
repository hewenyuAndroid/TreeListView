package com.hwy.treelistview.entity;

import com.hwy.treeview.annotation.TreeNodeId;
import com.hwy.treeview.annotation.TreeNodeParentId;

/**
 * 作者: hewenyu
 * 日期: 2018/11/21 13:00
 * 说明: 根据项目需求定制的Bean
 */
public class TreeBean {

    @TreeNodeId
    private String Id;

    @TreeNodeParentId
    private String ParentId;

    private String Name;

    /**
     * 需要初始化ParentId
     */
    public void initParentId() {

        String[] arr = getId().split("\\.");

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length - 1; i++) {
            sb.append(arr[i]);
            sb.append(".");
        }

        ParentId = sb.toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "TreeBean{" +
                "Id='" + Id + '\'' +
                ", ParentId='" + ParentId + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}
