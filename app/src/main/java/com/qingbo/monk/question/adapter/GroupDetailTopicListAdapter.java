package com.qingbo.monk.question.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qingbo.monk.R;
import com.qingbo.monk.base.viewTouchDelegate;
import com.qingbo.monk.bean.OwnPublishBean;
import com.qingbo.monk.home.NineGrid.NineGridAdapter;
import com.qingbo.monk.home.NineGrid.NineGridLayoutManager;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.preferences.SharePref;
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.L;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 社群话题全部列表
 */
public class GroupDetailTopicListAdapter extends BaseQuickAdapter<OwnPublishBean, BaseViewHolder> {
    private int type;
    private String role_self;
    private String id_self;

    public GroupDetailTopicListAdapter(int type,String role) {
        super(R.layout.item_group_detail_topic);
        this.type = type;
        this.role_self = role;
        id_self = SharePref.user().getUserId();
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, OwnPublishBean item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView tv_role = helper.getView(R.id.tv_role);
        TextView title_Tv = helper.getView(R.id.title_Tv);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView tv_status = helper.getView(R.id.tv_status);
        TextView tv_answer = helper.getView(R.id.tv_answer);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        TextView follow_Count = helper.getView(R.id.follow_Count);
        TextView mes_Count = helper.getView(R.id.mes_Count);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);
        ImageView follow_Img = helper.getView(R.id.follow_Img);
        viewTouchDelegate.expandViewTouchDelegate(follow_Img,100);
        ImageView iv_delete = helper.getView(R.id.iv_delete);

        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }

        follow_Count.setText(item.getLikecount());
        mes_Count.setText(item.getCommentcount());
        isLike(item.getLike(), item.getLikecount(), follow_Img, follow_Count);



        if (type==0) {
            tv_status.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);

            String publish_user_id = item.getAuthorId();
            if (id_self.equals(publish_user_id)) {
                iv_delete.setVisibility(View.VISIBLE);
                viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
                helper.addOnClickListener(R.id.iv_delete);
            }else{
                String publish_role = item.getRole();
                if ("3".equals(role_self)) {//1管理员2合伙人0一般用户3群主
                    iv_delete.setVisibility(View.VISIBLE);
                    viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
                    helper.addOnClickListener(R.id.iv_delete);
                }else if ("2".equals(role_self)) {
                    if ("1".equals(publish_role)||"0".equals(publish_role)) {
                        iv_delete.setVisibility(View.VISIBLE);
                        viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
                        helper.addOnClickListener(R.id.iv_delete);
                    }
                }else if ("1".equals(role_self)) {//1管理员2合伙人0一般用户3群主
                    if ("0".equals(publish_role)) {
                        iv_delete.setVisibility(View.VISIBLE);
                        viewTouchDelegate.expandViewTouchDelegate(iv_delete,100);
                        helper.addOnClickListener(R.id.iv_delete);
                    }
                }else{
                    iv_delete.setVisibility(View.GONE);
                }
            }
        }else{
            iv_delete.setVisibility(View.GONE);
            tv_answer.setVisibility(View.GONE);
            String status = item.getStatus();//0待审核 1通过 2未通过
            if (TextUtils.equals(status, "0")) {
                tv_status.setVisibility(View.VISIBLE);
                tv_status.setText("待审核");
                setDrawableLeft(R.mipmap.weishenhe,tv_status);
            } else if(TextUtils.equals(status, "1")){
                tv_status.setVisibility(View.VISIBLE);
                tv_status.setText("审核通过");
                setDrawableLeft(R.mipmap.shenhetongguo,tv_status);
            } else if(TextUtils.equals(status, "2")){
                tv_status.setVisibility(View.VISIBLE);
                setDrawableLeft(R.mipmap.weitongguo,tv_status);
                tv_status.setText("未通过");
            } else{
                tv_status.setVisibility(View.GONE);
            }
        }


        LinearLayout ll_container_answer = helper.getView(R.id.ll_container_answer);
        String topicType = item.getTopicType();
        if ("1".equals(topicType)) {//1是话题2是问答
            if (!StringUtil.isBlank(item.getTitle())) {
                title_Tv.setVisibility(View.VISIBLE);
                title_Tv.setText(item.getTitle());
            }else{
                title_Tv.setVisibility(View.GONE);
            }
            handleCommonData(item.getAvatar(),item.getNickname(),item.getContent(),item.getRole()
                    ,group_Img,group_Name,content_Tv,tv_role);
            handleImageList(item, mNineView);
            ll_container_answer.setVisibility(View.GONE);
        }else{
            title_Tv.setVisibility(View.GONE);
            List<OwnPublishBean.DetailDTO> details = item.getDetail();
            if (!ListUtils.isEmpty(details)) {
                ll_container_answer.setVisibility(View.VISIBLE);
                OwnPublishBean.DetailDTO answerObj = details.get(0);
                handleCommonData(answerObj.getAvatar(),answerObj.getNickname(),answerObj.getAnswerContent(),answerObj.getRole()
                ,group_Img,group_Name,content_Tv,tv_role);
                createQuestionList(ll_container_answer,item);
            }else{
                handleCommonData(item.getAvatar(),item.getNickname(),item.getContent(),item.getRole()
                        ,group_Img,group_Name,content_Tv,tv_role);
                handleImageList(item, mNineView);
                ll_container_answer.setVisibility(View.GONE);
            }
        }
        helper.addOnClickListener(R.id.follow_Tv);
        helper.addOnClickListener(R.id.follow_Img);
    }

    private void handleCommonData(String headImg,String headName,String content,String role
            ,ImageView iv_headImg ,TextView tv_headName,TextView tv_content,TextView tv_role) {
        GlideUtils.loadCircleImage(mContext, iv_headImg, headImg);
        tv_headName.setText(headName);

        if (!StringUtil.isBlank(content)) {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }else{
            tv_content.setVisibility(View.GONE);
        }

        if ("1".equals(role)) {//1管理员2合伙人0一般用户3群主
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("管理员");
            tv_headName.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else if("2".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("合伙人");
            tv_headName.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else if("3".equals(role)) {
            tv_role.setVisibility(View.VISIBLE);
            tv_role.setText("群主");
            tv_headName.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_ff5f2e));
        }else{
            tv_role.setVisibility(View.GONE);
            tv_headName.setTextColor(ContextCompat.getColor(mContext,R.color.text_color_444444));
        }
    }


    /**
     * 提问
     */
    private void createQuestionList(LinearLayout ll_container_answer, OwnPublishBean item ) {
        ll_container_answer.removeAllViews();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_answer, null);
        TextView tv_answer_name = itemView.findViewById(R.id.tv_answer_name);
        TextView tv_answer_content = itemView.findViewById(R.id.tv_answer_content);
        RecyclerView mNineView = itemView.findViewById(R.id.nine_grid_answer);
        tv_answer_name.setText(item.getNickname());
        tv_answer_content.setText("提问："+item.getContent());

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
            nineGridAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if (onItemImgClickLister != null) {
                        onItemImgClickLister.OnItemImgClickLister(position, strings);
                    }
                }
            });
        } else {
            mNineView.setVisibility(View.GONE);
            nineGridAdapter.setNewData(null);
        }
    }


    private void setDrawableLeft(int mipmap,TextView status) {
        Drawable drawableLeft = mContext.getResources().getDrawable(
                mipmap);
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


    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }

    public interface OnItemImgClickLister {
        void OnItemImgClickLister(int position, List<String> strings);
    }

    private QuestionListAdapterMy.OnItemImgClickLister onItemImgClickLister;

    public void setOnItemImgClickLister(QuestionListAdapterMy.OnItemImgClickLister ItemListener) {
        onItemImgClickLister = ItemListener;
    }


}

