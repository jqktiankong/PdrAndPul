package com.jqk.pdrandpul.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jqk.pdrandpul.R;
import com.jqk.pdrandpullibrary.view.RippleView;

public class MainActivity extends AppCompatActivity implements RippleView.OnRippleCompleteListener {

    private RippleView frameLayout, nestedScrollView, recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (RippleView) findViewById(R.id.frameLayout);
        nestedScrollView = (RippleView) findViewById(R.id.nestedscrollview);
        recyclerView = (RippleView) findViewById(R.id.recyclerview);

        frameLayout.setOnRippleCompleteListener(this);
        nestedScrollView.setOnRippleCompleteListener(this);
        recyclerView.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        switch (rippleView.getId()) {
            case R.id.frameLayout:
                Intent intent = new Intent(this, PullableFrameLayoutActivity.class);
                startActivity(intent);
                break;
            case R.id.nestedscrollview:
                Intent intent1 = new Intent(this, NestedScrollViewActivity.class);
                startActivity(intent1);
                break;
            case R.id.recyclerview:
                Intent intent2 = new Intent(this, RecyclerViewActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
