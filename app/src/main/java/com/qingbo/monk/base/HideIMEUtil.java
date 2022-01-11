package com.qingbo.monk.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * 点击收起键盘工具类
 */
public class HideIMEUtil {

    public static void wrap(Activity activity) {
        ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        wrap(contentParent);
    }

    public static void wrap(Fragment fragment) {
        ViewGroup contentParent = (ViewGroup) fragment.getView().getParent();
        wrap(contentParent);
    }
    public static void wrap(Activity activity,EditText editText) {
//        editText.setHint("");
        ViewGroup contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        wrap(contentParent);
    }

    public static void wrap(ViewGroup contentParent) {
        View content = contentParent.getChildAt(0);
        contentParent.removeView(content);
        ViewGroup.LayoutParams p = content.getLayoutParams();
        AutoHideIMELayout layout = new AutoHideIMELayout(content.getContext());
        layout.addView(content);

        contentParent.addView(layout, new ViewGroup.LayoutParams(p.width, p.height));
    }


}
