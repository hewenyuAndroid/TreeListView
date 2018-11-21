# TreeListView
Android 树形结构控件参考[鸿洋](https://blog.csdn.net/lmj623565791/article/details/40212367)大神的文章实现的树形结构功能；

### 效果图
![单选](https://github.com/hewenyuAndroid/TreeListView/blob/master/screen/single.gif)
![多选](https://github.com/hewenyuAndroid/TreeListView/blob/master/screen/multi.gif)

### 引用方式

> compile 'com.hewenyu:TreeView:1.0'

### 使用方式
1. 在具体的项目的对象中标注Id和ParentId;
```Java
@TreeNodeId
private String Id;

@TreeNodeParentId
private String ParentId;

/**
 * 在拿到数据后，计算ParentId
 * 很多项目可能只给出了Id，ParentId需要自己计算，
 * 这个可以根据不同的项目来进行不同的设置
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

```

2. 正常流程设置适配器，在适配器中进行相对应的操作
```Java
mListView.setAdapter(
        mAdapter = new TreeAdapter<TreeBean>(this, list, R.layout.adapter_tree_layout) {
            @Override
            protected void convert(TreeViewHolder holder, Node<TreeBean> node,
                                   int position, ViewGroup parent) {
                // 操作Item布局
                holder.setText(R.id.tv_name, node.getRawData().getName());


                // 设置选中的图标到具体的View
                updateCheckedNodeIcon(holder, R.id.iv_checked, node,
                        R.drawable.icon_checked_circle, R.drawable.icon_unchecked_circle);
                // 设置具体点击哪个View来操作选中事件
                setCheckedNodeListener(holder, R.id.ll_convert_view, node);
                // 设置展开/折叠的图标到具体的View
                updateExpendOrCollapseIcon(holder, R.id.iv_arrow, node,
                        R.drawable.icon_tree_down, R.drawable.icon_tree_right);
                // 设置具体点击哪个View来操作展开/折叠功能
                setExpendOrCollapseListener(holder, R.id.iv_arrow, position);
            }
        });
```

3. 设置默认选中的节点(可以省略)
```Java
// 设置默认选中的数据
Node<TreeBean> node = (Node<TreeBean>) mAdapter.getItem(0);
mAdapter.addCheckedNode(node);
```

4. 监听选中的操作（自由定制单选/多选）
```Java
mAdapter.setOnItemCheckListener(new TreeAdapter.OnItemCheckListener<TreeBean>() {

    @Override
    public void onItemCheck(List<Node<TreeBean>> checkedNodes, Node<TreeBean> currentNode) {

        // 多选 (3个)
        if (checkedNodes.contains(currentNode)) {
            checkedNodes.remove(currentNode);
        } else {
            if (checkedNodes.size() < 3) {
                checkedNodes.add(currentNode);
            } else {
                Toast.makeText(mContext,
                        "最多只能选择 " + checkedNodes.size() + " 个",
                        Toast.LENGTH_SHORT).show();
            }
        }

        // 单选
//        if (checkedNodes.contains(currentNode)) {
//            checkedNodes.remove(currentNode);
//        } else {
//            if (checkedNodes.isEmpty()) {
//                checkedNodes.add(currentNode);
//            } else {
//                Toast.makeText(mContext,
//                        "当前已选: " + checkedNodes.get(0).getRawData().getName(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
    }
});
```

5. 获取选中的节点的数据
```Java
// ListView中使用的是Node节点，原始的数据缓存在Node的RawData对象中
List<TreeBean> checkedNodes = mAdapter.getCheckedNodes().get(0).getRawData();
```
