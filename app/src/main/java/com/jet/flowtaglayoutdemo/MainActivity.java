package com.jet.flowtaglayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jet.flowtaglayout.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FlowTagLayout flowTagLayout;
    private List<String> dataList;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<>();

        flowTagLayout = findViewById(R.id.flowTagLayout);
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
//                flowTagLayout.addTag("Kotlin");
                flowTagLayout.addTagOfIndex(i, "测试" + i);
                i = i + 1;
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                flowTagLayout.removeTag();
                flowTagLayout.removeTagOfIndex(3);
                i++;
            }
        });

    }

    private void setDatas() {
        dataList.add("数据结构");
        dataList.add("算法");
        dataList.add("Java");
        dataList.add("多线程编程");
        dataList.add("自定义view");
        dataList.add("JVM");
        dataList.add("TCP/IP");
        dataList.add("设计模式");
        dataList.add("gradle强化");
        dataList.add("git");
    }

}
