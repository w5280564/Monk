package com.qingbo.monk.question.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.qingbo.monk.R;
import com.qingbo.monk.base.PhotoShowActivity;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.qingbo.monk.person.adapter.My_MoreItem_Adapter;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 社群话题  转发文章
 */
public class Group_MoreItem_Article extends BaseItemProvider<OwnPublishBean, BaseViewHolder> {
    private int type;
    private String role_self;
    private String id_self;

    @Override
    public int viewType() {
        return My_MoreItem_Adapter.TYPE_ARTICLE;
    }

    @Override
    public int layout() {
        return R.layout.group_moreitem_artilce;
    }

    public Group_MoreItem_Article(int type, String role) {
        this.type = type;
        this.role_self = role;
        id_self = SharePref.user().getUserId();
    }


    @Override
    public void convert(@NonNull BaseViewHolder helper, OwnPublishBean item, int position) {
        ImageView more_Img = helper.getView(R.id.more_Img);
        LinearLayout ll_bottom = helper.getView(R.id.ll_bottom);
        TextView report_Tv = helper.getView(R.id.report_Tv);
        TextView follow_Tv = helper.getView(R.id.follow_Tv);
        TextView send_Mes = helper.getView(R.id.send_Mes);
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView tv_role = helper.getView(R.id.tv_role);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView tv_status = helper.getView(R.id.tv_status);
        TextView tv_answer = helper.getView(R.id.tv_answer);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView read_number_Tv = helper.getView(R.id.read_number_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img, 100);
        ImageView iv_delete = helper.getView(R.id.iv_delete);
        viewTouchDelegate.expandViewTouchDelegate(iv_delete, 100);
        helper.addOnClickListener(R.id.iv_delete);
        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.send_Mes);
        helper.addOnClickListener(R.id.follow_Img);

        viewTouchDelegate.expandViewTouchDelegate(more_Img, 50);

        report_Tv.setText("转发动态");
        if (!TextUtils.isEmpty(item.getExtraContent())) {
            String name = TextUtils.isEmpty(item.getCommentAuthorName()) ? "" : item.getCommentAuthorName();
            String comment = TextUtils.isEmpty(item.getCommentComment()) ? "" : item.getCommentComment();
            String format = String.format("转发评论//@%1$s：%2$s", name, comment);
            int startLength = "转发评论//".length();
            int endLength = (String.format("转发评论//@%1$s：", name)).length();
            String extraContent = item.getExtraContent();
            if (TextUtils.isEmpty(extraContent) || !extraContent.contains("转发评论")) {
                setName(format, startLength, startLength, endLength, report_Tv);
            } else {
                setName(extraContent, startLength, startLength, endLength, report_Tv);
            }
        }


        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }

        follow_Count.setText(item.getLikecount());
        mes_Count.setText(item.getCommentcount());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);


        if (type == 0) {
            more_Img.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);
            read_number_Tv.setVisibility(View.VISIBLE);
            read_number_Tv.setText(String.format("阅读人数：%s", item.getReadNum()));
        } else {
            more_Img.setVisibility(View.VISIBLE);
            helper.addOnClickListener(R.id.more_Img);
            iv_delete.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);
            String status = item.getStatus();//0待审核 1通过 2未通过
            if (TextUtils.equals(status, "0")) {
                ll_bottom.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
                read_number_Tv.setVisibility(View.GONE);
                tv_status.setText("待审核");
                setDrawableLeft(R.mipmap.weishenhe, tv_status);
            } else if (TextUtils.equals(status, "1")) {
                ll_bottom.setVisibility(View.VISIBLE);
                tv_status.setVisibility(View.VISIBLE);
                read_number_Tv.setVisibility(View.VISIBLE);
                tv_status.setText("审核通过");
                setDrawableLeft(R.mipmap.shenhetongguo, tv_status);
                read_number_Tv.setText(String.format("阅读人数：%s", item.getReadNum()));
            } else if (TextUtils.equals(status, "2")) {
                ll_bottom.setVisibility(View.GONE);
                tv_status.setVisibility(View.VISIBLE);
                read_number_Tv.setVisibility(View.GONE);
                setDrawableLeft(R.mipmap.weitongguo, tv_status);
                tv_status.setText("未通过");
            } else {
                ll_bottom.setVisibility(View.GONE);
                tv_status.setVisibility(View.GONE);
                read_number_Tv.setVisibility(View.GONE);
            }
        }


        LinearLayout ll_container_answer = helper.getView(R.id.ll_container_answer);
        String topicType = item.getTopicType();
        if ("1".equals(topicType)) {//1是话题2是问答
            if (!StringUtil.isBlank(item.getTitle())) {
                title_Tv.setVisibility(View.VISIBLE);
                title_Tv.setText(item.getTitle());
            } else {
                title_Tv.setVisibility(View.GONE);
            }
            handleCommonData(item.getAvatar(), item.getNickname(), item.getContent(), item.getRole(), item.getAuthorId(), item.getStatusNum()
                    , group_Img, group_Name, content_Tv, tv_role, iv_delete, follow_Tv, send_Mes);
            handleImageList(item, mNineView);
            ll_container_answer.setVisibility(View.GONE);
        } else {
            title_Tv.setVisibility(View.GONE);
            List<OwnPublishBean.DetailDTO> details = item.getDetail();
            if (!ListUtils.isEmpty(details)) {
                ll_container_answer.setVisibility(View.VISIBLE);
                OwnPublishBean.DetailDTO answerObj = details.get(0);
                handleCommonData(answerObj.getAvatar(), answerObj.getNickname(), answerObj.getAnswerContent(), answerObj.getRole(), answerObj.getAuthorId(), answerObj.getStatusNum()
                        , group_Img, group_Name, content_Tv, tv_role, iv_delete, follow_Tv, send_Mes);
                createQuestionList(ll_container_answer, item);
            } else {
                handleCommonData(item.getAvatar(), item.getNickname(), item.getContent(), item.getRole(), item.getAuthorId(), item.getStatusNum()
                        , group_Img, group_Name, content_Tv, tv_role, iv_delete, follow_Tv, send_Mes);
                handleImageList(item, mNineView);
                ll_container_answer.setVisibility(View.GONE);
            }
        }

    }

    private void handleCommonData(String headImg, String headName, String content, String role, String publish_user_id, String follow_status
            , ImageView iv_headImg, TextView tv_headName, TextView tv_content, TextView tv_role, ImageView iv_delete, TextView tv_follow, TextView tv_send_mes) {
        GlideUtils.loadCircleImage(mContext, iv_headImg, headImg);
        tv_headName.setText(headName);

        if (!StringUtil.isBlank(content)) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        } else {
            tv_content.setVisibility(View.GONE);
        }

        if ("1".equals(role)) {//1管理员2合伙人0一般用户3群主
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("管理员");
            tv_headName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_ff5f2e));
        } else if ("2".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("合伙人");
            tv_headName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_ff5f2e));
        } else if ("3".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("群主");
            tv_headName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_ff5f2e));
        } else {
            tv_role.setVisibility(View.GONE);
            tv_headName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
        }

        if (type == 0) {
            if (id_self.equals(publish_user_id)) {
                iv_delete.setVisibility(View.VISIBLE);
            } else {
                if ("3".equals(role_self)) {//1管理员2合伙人0一般用户3群主
                    iv_delete.setVisibility(View.VISIBLE);
                } else if ("2".equals(role_self)) {
                    if ("1".equals(role) || "0".equals(role)) {
                        iv_delete.setVisibility(View.VISIBLE);
                    }
                } else if ("1".equals(role_self)) {//1管理员2合伙人0一般用户3群主
                    if ("0".equals(role)) {
                        iv_delete.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
            }

            isFollow(follow_status, tv_follow, tv_send_mes);
        }
    }

    /**
     * @param follow_status 0是没关系 1是自己 2已关注 3当前用户粉丝 4互相关注
     * @param follow_Tv
     * @param send_Mes
     */
    public void isFollow(String follow_status, TextView follow_Tv, View send_Mes) {
        String s = String.valueOf(follow_status);
        if (TextUtils.equals(s, "0") || TextUtils.equals(s, "3")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.app_main_color));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "1")) {
            follow_Tv.setVisibility(View.GONE);
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "2")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("已关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.GONE);
        } else if (TextUtils.equals(s, "4")) {
            follow_Tv.setVisibility(View.VISIBLE);
            follow_Tv.setText("互相关注");
            follow_Tv.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_a1a1a1));
            StringUtil.changeShapColor(follow_Tv, ContextCompat.getColor(mContext, R.color.text_color_F5F5F5));
            send_Mes.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 提问
     */
    private void createQuestionList(LinearLayout ll_container_answer, OwnPublishBean item) {
        ll_container_answer.removeAllViews();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_answer, null);
        TextView tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
        TextView tv_answer_content = itemView.findViewById(R.id.tv_answer_content);
        RecyclerView mNineView = itemView.findViewById(R.id.nine_grid_answer);
        tv_answer_name.setText(item.getNickname());
        tv_answer_content.setText("提问：" + item.getContent());

        handleImageList(item, mNineView);
        ll_container_answer.addView(itemView);
    }

    private void handleImageList(OwnPublishBean item, RecyclerView mNineView) {
        NineGridAdapter nineGridAdapter = new NineGridAdapter();
        List<String> strings = new ArrayList<>();
        mNineView.setLayoutManager(new NineGridLayoutManager(mContext));
        mNineView.setAdapter(nineGridAdapter);
        //多张图片
        if (!TextUtils.isEmpty(item.getImages())) {
            mNineView.setVisibility(View.VISIBLE);
            String[] imgS = item.getImages().split(",");
            strings.addAll(Arrays.asList(imgS));
            nineGridAdapter.setNewData(strings);
            nineGridAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    jumpToPhotoShowActivity(position, strings);
                }
            });
        } else {
            mNineView.setVisibility(View.GONE);
            nineGridAdapter.setNewData(null);
        }
    }


    private void setDrawableLeft(int mipmap, TextView status) {
        Drawable drawableLeft = mContext.getResources().getDrawable(mipmap);
        status.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }


    private void isLike(int isLike, String likes, ImageView follow_Img, TextView follow_Count) {
        int nowLike;
        nowLike = TextUtils.isEmpty(follow_Count.getText().toString()) ? 0 : Integer.parseInt(follow_Count.getText().toString());
        if (isLike == 0) {
            follow_Img.setBackgroundResource(R.mipmap.icon_dainzan);
        } else if (isLike == 1) {
            follow_Img.setBackgroundResource(R.mipmap.dianzan);
        }
        follow_Count.setText(nowLike + "");
    }


//    @Override
//    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
//        super.setOnItemClickListener(listener);
//    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private QuestionListAdapterMy.OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(QuestionListAdapterMy.OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }

    /**
     * @param name        要显示的数据
     * @param nameLength  要改颜色的字体长度
     * @param startLength 改色起始位置
     * @param endLength   改色结束位置
     * @param viewName
     */
    private void setName(String name, int nameLength, int startLength, int endLength, TextView viewName) {
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.text_color_1F8FE5)), startLength, endLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewName.setText(spannableString);
    }

    /**
     * 跳转到查看图片页
     *
     * @param position
     * @param mImageList
     */
    public void jumpToPhotoShowActivity(int position, List<String> mImageList) {
        Intent intent = new Intent(mContext, PhotoShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgList", (Serializable) mImageList);
        mContext.startActivity(intent);
        ((Activity) mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}


