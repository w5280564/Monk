package com.qingbo.monk.person.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.qingbo.monk.bean.MyDynamic_MoreItem_Bean;

import java.util.List;

/**
 * ================================================
 * <p>
 * 我的动态item 多种布局
 * <p>
 * ================================================
 */
public class MyCollect_MoreItem extends MultipleItemRvAdapter<MyDynamic_MoreItem_Bean, BaseViewHolder> {
    public static final int TYPE_MY = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_INFORMATION = 3;
    public static final int TYPE_FORWARD = 4;
    public static final int TYPE_Combination = 5;

    private boolean isCollect;

    public MyCollect_MoreItem(List<MyDynamic_MoreItem_Bean> data) {
        super(data);
        finishInitialize();
    }

    public MyCollect_MoreItem(List<MyDynamic_MoreItem_Bean> data, boolean isCollect) {
        super(data);
        this.isCollect = isCollect;
        finishInitialize();
    }


    @Override
    protected int getViewType(MyDynamic_MoreItem_Bean entity) {
        if (entity != null) {
            String collect_type = entity.getCollect_type();//0：文章【默认】1：评论 2：仓位组合 3：资讯
            if (collect_type.equals("2")) {
                return TYPE_Combination;
            } else if (collect_type.equals("3")) {
                return TYPE_INFORMATION;
            } else if (collect_type.equals("0")) {
                return TYPE_MY;
            } else if (collect_type.equals("1")) {

                String source_type = entity.getSource_type(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
                if (TextUtils.equals(source_type, "4")) {
                    return TYPE_Combination;
                } else if (TextUtils.equals(source_type, "5")) {
                    return TYPE_INFORMATION;
                } else {
                    return TYPE_ARTICLE;
                }

            } else {
                return TYPE_MY;
            }

        }
        return TYPE_MY;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Adapter(true));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Article(true));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Information(true));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Combination());

//        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_ForWard());
    }


}
