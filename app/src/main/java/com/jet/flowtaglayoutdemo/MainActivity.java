package com.jet.flowtaglayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jet.flowtaglayout.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowTagLayout flowTagLayout;
    private List<String> dataList;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        flowTagLayout = findViewById(R.id.flowTagLayout);
        editText = findViewById(R.id.edit);

        setDatas();
        flowTagLayout.addTags(dataList);

        flowTagLayout.setTagClickListener(new FlowTagLayout.OnTagClickListener() {
            @Override
            public void tagClick(int position) {
                flowTagLayout.getChildAt(position).setSelected(!flowTagLayout.getChildAt(position).isSelected());
                Toast.makeText(MainActivity.this, dataList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowTagLayout.addTag(editText.getText().toString());
                dataList.add(editText.getText().toString());
                Toast.makeText(MainActivity.this, "添加了“" + editText.getText().toString() + "”", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flowTagLayout.removeTag();
                Toast.makeText(MainActivity.this, "移除了“" + dataList.get(dataList.size() - 1) + "”", Toast.LENGTH_SHORT).show();
                dataList.remove(dataList.size() - 1);
            }
        });

    }

    private void setDatas() {
        dataList.add("数据结构");
        dataList.add("算法");
        dataList.add("多线程编程");
        dataList.add("JVM");
        dataList.add("自定义view");
        dataList.add("TCP/IP");
        dataList.add("gradle强化");
        dataList.add("设计模式");
        dataList.add("git");
    }

}
