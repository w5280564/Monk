package com.qingbo.monk.base.status;

import android.app.Activity;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.bean.HomeFllowBean;

import java.util.List;

/**
 * 修改数据源，并且绑定刷新
 * 刷新列表状态
 */
public class OnSheQuDataChangeImpl implements SheQuDataChangeListener {

    private BaseQuickAdapter<HomeFllowBean, BaseViewHolder> mAdapter;
    private Activity activity;

    public OnSheQuDataChangeImpl(Activity activity, BaseQuickAdapter<HomeFllowBean, BaseViewHolder> mAdapter) {
        this.activity = activity;
        this.mAdapter = mAdapter;
    }

    /**
     * 数据变更处理 ，处理所有状态改变Adapter 数据
     *
     * @param id    唯一ID
     * @param type  变更类型 删除 点赞、留言、关注、收藏
     * @param flag  操作flag 点赞、关注 true：我点赞了 false：取消点赞或关注
     * @param value 最新点赞、留言数量、最新关注状态（1：已关注 0：未关注）
     */
    @Override
    public void onDataChange(String id, int type, boolean flag, long value) {
        if (id == null || mAdapter == null || activity == null) {
            return;
        }

        try {
            List<HomeFllowBean> items = mAdapter.getData();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) instanceof HomeFllowBean) {
                    HomeFllowBean itemBean = items.get(i);
                    if (TextUtils.equals(id, (items.get(i)).getArticleId())) {

                        if (type == SheQuDataChangeListener.TYPE_FOLLOW) { //关注用户
                            itemBean.setFollow_status((int) value);
                            mAdapter.setData(i, itemBean);
                        } else if (type == SheQuDataChangeListener.TYPE_DELETE) {//删除数据
                            items.remove(i);
                            mAdapter.notifyItemChanged(i);
                        } else if (type == SheQuDataChangeListener.TYPE_COMMENT) { //留言变更数量
                            itemBean.setCommentNum(value + "");
                            mAdapter.setData(i, itemBean);
                        } else if (type == SheQuDataChangeListener.TYPE_PRAISE) { //点赞变更数量
                            itemBean.setLikedNum(value + "");
                            int status = flag ? 1 : 0;
                            itemBean.setLiked_status(status);
                            mAdapter.setData(i, itemBean);
                        } else if (type == SheQuDataChangeListener.TYPE_COLLECT) { //收藏修改状态
                            int status = flag ? 1 : 0;
                            itemBean.setIs_collect(status + "");
                            mAdapter.setData(i, itemBean);
                        }


                    }
                }
            }

        } catch (Exception e) {

        }
    }
}
