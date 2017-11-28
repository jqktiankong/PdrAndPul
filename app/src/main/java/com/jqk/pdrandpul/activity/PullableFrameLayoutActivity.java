package com.jqk.pdrandpul.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jqk.pdrandpul.R;
import com.jqk.pdrandpullibrary.view.PdrAndPulView;

/**
 * Created by YASCN on 2017/6/21.
 */

public class PullableFrameLayoutActivity extends AppCompatActivity {

    private PdrAndPulView pdrAndPulView;

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
        setContentView(R.layout.activity_framelayout);

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
    }
}
