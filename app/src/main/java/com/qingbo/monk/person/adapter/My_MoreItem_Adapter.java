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
public class My_MoreItem_Adapter extends MultipleItemRvAdapter<MyDynamic_MoreItem_Bean, BaseViewHolder> {
    public static final int TYPE_MY = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_INFORMATION = 3;
    public static final int TYPE_FORWARD = 4;
    public static final int TYPE_Combination = 5;
    private String isMe;


    public My_MoreItem_Adapter(List<MyDynamic_MoreItem_Bean> data) {
        super(data);
        finishInitialize();
    }
    public My_MoreItem_Adapter(List<MyDynamic_MoreItem_Bean> data,String isMe) {
        super(data);
        this.isMe = isMe;
        finishInitialize();
    }


    @Override
    protected int getViewType(MyDynamic_MoreItem_Bean entity) {
        if (entity != null) {
            String isReprint = entity.getIsReprint();//0-原创 1-转发
            if (isReprint.equals("1")) {
                String reprintType = entity.getReprintType(); //0-文章 1-资讯 3-转发评论 4-是仓位组合
                if (reprintType.equals("0")){
                    return TYPE_ARTICLE;
                }else if (reprintType.equals("1")){
                    return TYPE_INFORMATION;
                }else if (reprintType.equals("4")) {
                    return TYPE_Combination;
                }else if (reprintType.equals("3")){

                    String source_type = entity.getSource_type(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
                    if (TextUtils.equals(source_type,"4")){
                        return TYPE_Combination;
                    }else if (TextUtils.equals(source_type,"5")) {
                        return TYPE_INFORMATION;
                    }else {
                        return TYPE_ARTICLE;
                    }

                }

            } else {
                return TYPE_MY;
            }
        }
        return TYPE_MY;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Adapter(isMe));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Article(isMe));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Information(isMe));
        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_Combination(isMe));

//        mProviderDelegate.registerProvider(new MyDynamic_MoreItem_ForWard());
    }


}
