package com.qingbo.monk.person.adapter;

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
public class My_MoreItem_Adapter extends MultipleItemRvAdapter<MyDynamic_MoreItem_Bean, BaseViewHolder> {
    public static final int TYPE_MY = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_INFORMATION = 3;
    public static final int TYPE_FORWARD = 4;
    public static final int TYPE_Combination = 5;

    public My_MoreItem_Adapter(List<MyDynamic_MoreItem_Bean> data) {
        super(data);
        finishInitialize();
    }


    @Override
    protected int getViewType(MyDynamic_MoreItem_Bean entity) {
        if (entity != null) {
            String isReprint = entity.getIsReprint();//0-原创 1-转发
            if (isReprint.equals("1")) {
                String reprintType = entity.getReprintType();
                if (reprintType.equals("0")) { //0-文章 1-资讯 2-转发评论 4-是仓位组合
                    return TYPE_ARTICLE;
                } else if (reprintType.equals("1")) {
                    return TYPE_INFORMATION;
                }else if (reprintType.equals("2")) {
                    return TYPE_FORWARD;
                }else if (reprintType.equals("4")) {
                    return TYPE_Combination;
                }
            } else {
                return TYPE_MY;
            }
        }
        return TYPE_MY;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Adapter());
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Article());
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Information());
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_ForWard());
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Combination());
    }


}
