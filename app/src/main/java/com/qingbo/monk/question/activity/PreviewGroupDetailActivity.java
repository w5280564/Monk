package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;

/**
 * 查看未加入的社群详情
 */
public class PreviewGroupDetailActivity extends BaseActivity {
    private String id;

    @Override
    protected int getLayoutId() {
        return R.layout.actiivty_preview_group_detail;
    }

    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, PreviewGroupDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }
}
