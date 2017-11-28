package com.jqk.pdrandpul.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jqk.pdrandpul.R;
import com.jqk.pdrandpullibrary.view.PdrAndPulView;

/**
 * Created by YASCN on 2017/6/21.
 */

public class NestedScrollViewActivity extends AppCompatActivity {

    private PdrAndPulView pdrAndPulView;
    private Button button;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                pdrAndPulView.refreshComplete(true);
            } else if (msg.what == 1001) {
                pdrAndPulView.loadmoreComplete(false);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestedscrollview);

        pdrAndPulView = (PdrAndPulView) findViewById(R.id.pdrAndPulView);

        pdrAndPulView.setOnRefreshFinishListener(new PdrAndPulView.OnRefreshFinishListener() {
            @Override
            public void onReflesh() {
                handler.sendEmptyMessageDelayed(1000, 2000);
            }

            @Override
            public void onLoadmore() {
                handler.sendEmptyMessageDelayed(1001, 2000);
            }
        });
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NestedScrollViewActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
