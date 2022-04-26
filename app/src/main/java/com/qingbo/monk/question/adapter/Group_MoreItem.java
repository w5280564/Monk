package com.qingbo.monk.question.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.qingbo.monk.bean.OwnPublishBean;

import java.util.List;

/**
 * ================================================
 * <p>
 * 我的动态item 多种布局
 * <p>
 * ================================================
 */
public class Group_MoreItem extends MultipleItemRvAdapter<OwnPublishBean, BaseViewHolder> {
    public static final int TYPE_MY = 1;
    public static final int TYPE_ARTICLE = 2;
    public static final int TYPE_INFORMATION = 3;
    public static final int TYPE_FORWARD = 4;
    public static final int TYPE_Combination = 5;
    int fragmentType;
    String role;

    public Group_MoreItem(List<OwnPublishBean> data,int fragmentType,String role) {
        super(data);
        this.fragmentType = fragmentType;
        this.role = role;
        finishInitialize();
    }

    @Override
    protected int getViewType(OwnPublishBean entity) {
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
                } else if (reprintType.equals("3")){

                    String source_type = entity.getSourceType(); //1社群 2问答 3创作者中心文章 4仓位组合策略 5资讯
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
        mProviderDelegate.registerProvider(new GroupDetailTopicListAdapter(fragmentType, role));
        mProviderDelegate.registerProvider(new Group_MoreItem_Article(fragmentType, role));
        mProviderDelegate.registerProvider(new Group_MoreItem_Information(fragmentType, role));
        mProviderDelegate.registerProvider(new Group_MoreItem_Combination(fragmentType, role));
    }


}
