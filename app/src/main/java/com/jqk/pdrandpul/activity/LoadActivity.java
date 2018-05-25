package com.jqk.pdrandpul.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.jqk.pdrandpul.R;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public class LoadActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
    }
}
