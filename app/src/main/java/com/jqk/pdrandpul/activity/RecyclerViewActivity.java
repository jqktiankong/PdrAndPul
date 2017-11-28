package com.jqk.pdrandpul.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.jqk.pdrandpul.MyAdapter;
import com.jqk.pdrandpul.R;
import com.jqk.pdrandpullibrary.pullview.PullableRecyclerView;
import com.jqk.pdrandpullibrary.view.PdrAndPulView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YASCN on 2017/6/21.
 */

public class RecyclerViewActivity extends AppCompatActivity {

    private PdrAndPulView pdrAndPulView;
    private PullableRecyclerView recyclerView;
    private MyAdapter myAdapter;

    private List<String> strings;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                pdrAndPulView.refreshComplete(true);
                strings.clear();
                for (int i = 0; i < 10; i++) {
                    strings.add(i + "");
                }
                myAdapter.notifyDataSetChanged();
            } else if (msg.what == 1001) {
                pdrAndPulView.loadmoreComplete(true);

                for (int i = 0; i < 10; i++) {
                    strings.add(i + "新增加");
                }
                myAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        pdrAndPulView = (PdrAndPulView) findViewById(R.id.pdrAndPulView);
        recyclerView = (PullableRecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        strings = new ArrayList<String>();

        for (int i = 0; i < 10; i++) {
            strings.add(i + "");
        }
        myAdapter = new MyAdapter(strings, pdrAndPulView);
        recyclerView.setAdapter(myAdapter);

        pdrAndPulView.setOnRefreshFinishListener(new PdrAndPulView.OnRefreshFinishListener() {
            @Override
            public void onReflesh() {
                handler.sendEmptyMessageDelayed(1000, 5000);
            }

            @Override
            public void onLoadmore() {
                handler.sendEmptyMessageDelayed(1001, 5000);
            }
        });

//        pdrAndPulView.setOnScrollListener(new PdrAndPulViewNew.OnScrollListener() {
//            @Override
//            public void onScroll(float y) {
//                Log.d("scroll", "y = " + y);
//            }
//        });
    }
}
