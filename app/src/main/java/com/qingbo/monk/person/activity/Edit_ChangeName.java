package com.qingbo.monk.person.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;

public class Edit_ChangeName extends BaseActivity {


    public static void actionStart(Context context, String nickname) {
        Intent intent = new Intent(context, Edit_ChangeName.class);
        intent.putExtra("nickname", nickname);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_changename;
    }
}