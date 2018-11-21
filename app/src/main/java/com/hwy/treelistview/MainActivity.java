package com.hwy.treelistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hwy.treelistview.entity.TreeBean;
import com.hwy.treeview.TreeAdapter;
import com.hwy.treeview.TreeViewHolder;
import com.hwy.treeview.entity.Node;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String result = "{\"Result\":0,\"Data\":[{\"Id\":\".01.\",\"Name\":\"1号楼\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.01.\",\"Name\":\"1层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.02.\",\"Name\":\"2层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.02.01.\",\"Name\":\"201病房\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.02.02.\",\"Name\":\"202病房\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.02.03.\",\"Name\":\"203病房\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.03.\",\"Name\":\"3层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.04.\",\"Name\":\"4层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.05.\",\"Name\":\"5层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.06.\",\"Name\":\"6层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.07.\",\"Name\":\"7层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.08.\",\"Name\":\"8层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.09.\",\"Name\":\"9层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.10.\",\"Name\":\"10层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.11.\",\"Name\":\"11层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.12.\",\"Name\":\"12层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.13.\",\"Name\":\"13层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".01.14.\",\"Name\":\"14层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".02.\",\"Name\":\"2号楼\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".02.01.\",\"Name\":\"1层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".02.02.\",\"Name\":\"2层\",\"ProjectId\":\".01.01.02.\"},{\"Id\":\".02.03.\",\"Name\":\"3层\",\"ProjectId\":\".01.01.02.\"}]}";

    private ListView mListView;

    private TreeAdapter<TreeBean> mAdapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listview);

        mContext = this;

        JSONObject args = JSONObject.parseObject(result);

        // 获取数据集
        List<TreeBean> list = JSONArray.parseArray(args.getString("Data"), TreeBean.class);

        // 根据项目给的数据结构，初始化ParentId
        for (TreeBean bean : list) {
            bean.initParentId();
        }


        mListView.setAdapter(mAdapter = new TreeAdapter<TreeBean>(this, list, R.layout.adapter_tree_layout) {
            @Override
            protected void convert(TreeViewHolder holder, Node<TreeBean> node, int position, ViewGroup parent) {
                // 操作Item布局
                holder.setText(R.id.tv_name, node.getRawData().getName());


                // 设置选中的图标
                updateCheckedNodeIcon(holder, R.id.iv_checked, node, R.drawable.icon_checked_circle, R.drawable.icon_unchecked_circle);
                // 设置具体点击哪个View来操作选中事件
                setCheckedNodeListener(holder, R.id.ll_convert_view, node);
                // 设置展开/折叠的图标
                updateExpendOrCollapseIcon(holder, R.id.iv_arrow, node, R.drawable.icon_tree_down, R.drawable.icon_tree_right);
                // 设置具体点击哪个View来操作展开/折叠功能
                setExpendOrCollapseListener(holder, R.id.iv_arrow, position);
            }
        });

        // 设置默认选中的数据
        Node<TreeBean> node = (Node<TreeBean>) mAdapter.getItem(0);
        mAdapter.addCheckedNode(node);

        // 自定义单选多选的操作
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
                        Toast.makeText(mContext, "最多只能选择 " + checkedNodes.size() + " 个", Toast.LENGTH_SHORT).show();
                    }
                }

                // 单选
//                if (checkedNodes.contains(currentNode)) {
//                    checkedNodes.remove(currentNode);
//                } else {
//                    if (checkedNodes.isEmpty()) {
//                        checkedNodes.add(currentNode);
//                    } else {
//                        Toast.makeText(mContext, "当前已选: " + checkedNodes.get(0).getRawData().getName(), Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });


    }

}
