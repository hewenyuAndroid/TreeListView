package com.hwy.treeview;

import com.hwy.treeview.annotation.TreeNodeId;
import com.hwy.treeview.annotation.TreeNodeParentId;
import com.hwy.treeview.entity.Node;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: hewenyu
 * 日期: 2018/11/4 13:13
 * 说明: 树形结构工具类
 */
public class TreeHelper {

    /**
     * 将原始的数据转换为排序后的Node
     *
     * @param datas
     * @param defaultExpendLevel
     * @param <E>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <E, T> List<Node<T>> getSortNodes(List<E> datas, int defaultExpendLevel) throws IllegalAccessException {
        List<Node<T>> result = new ArrayList<>();

        // 将原始数据列表转换为Node数据列表
        List<Node<T>> nodes = convertData2Node(datas);
        // 获取根目录
        List<Node<T>> rootNodes = getRootNodes(nodes);

        // 排序
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpendLevel, 1);
        }

        return result;
    }

    /**
     * 递归挂在所有的子节点
     *
     * @param nodes
     * @param node
     * @param defaultExpendLevel
     * @param currentLevel
     */
    private static <T> void addNode(List<Node<T>> nodes, Node node, int defaultExpendLevel, int currentLevel) {

        nodes.add(node);
        if (defaultExpendLevel >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf()) {
            return;
        }

        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, (Node) node.getChildren().get(i), defaultExpendLevel, currentLevel + 1);
        }

    }

    /**
     * 获取根目录
     *
     * @param nodes
     * @param <T>
     * @return
     */
    private static <T> List<Node<T>> getRootNodes(List<Node<T>> nodes) {
        List<Node<T>> root = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

    /**
     * 将原始的数据转换为List<Node>，同时设置Node之间的关系
     *
     * @param datas
     * @param <E>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <E, T> List<Node<T>> convertData2Node(List<E> datas) throws IllegalAccessException {

        List<Node<T>> nodes = new ArrayList<>();

        Node<T> node = null;

        // 将原始数据转换为Node对象

        for (E data : datas) {
            // 当前节点的ID
            String id = null;
            // 当前节点的父节点的ID
            String parentId = null;

            Class<?> clazz = data.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    id = (String) field.get(data);
                }
                if (field.getAnnotation(TreeNodeParentId.class) != null) {
                    parentId = (String) field.get(data);
                }

            }

            if (null == id) {
                throw new RuntimeException("please add tree annotation for raw data");
            }

            node = new Node(id, parentId, data);
            nodes.add(node);
        }


        // 设置Node之间的父子关系
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getParentId().equals(n.getId())) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId().equals(n.getParentId())) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        return nodes;
    }

    /**
     * 过滤出可见的Node
     *
     * @param allNodes
     * @return
     */
    public static <T> List<Node<T>> filterVisibleNodes(List<Node<T>> allNodes) {

        List<Node<T>> result = new ArrayList<>();
        for (Node node : allNodes) {
            // 如果为根节点/上层目录为展开状态
            if (node.isRoot() || node.isParentExpend()) {
                result.add(node);
            }
        }

        return result;
    }
}
