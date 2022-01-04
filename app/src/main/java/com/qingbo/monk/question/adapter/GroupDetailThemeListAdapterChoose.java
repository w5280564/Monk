package com.qingbo.monk.question.adapter;

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
import com.xunda.lib.common.common.utils.DateUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 社群预览主题列表(选择)
 */
public class GroupDetailThemeListAdapterChoose extends BaseQuickAdapter<OwnPublishBean, BaseViewHolder> {

    public GroupDetailThemeListAdapterChoose() {
        super(R.layout.item_group_detail_theme_choose);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, OwnPublishBean item) {
        ImageView group_Img = helper.getView(R.id.group_Img);
        TextView group_Name = helper.getView(R.id.group_Name);
        TextView tv_role = helper.getView(R.id.tv_role);
        TextView content_Tv = helper.getView(R.id.content_Tv);
        TextView time_Tv = helper.getView(R.id.time_Tv);
        RecyclerView mNineView = helper.getView(R.id.nine_grid);



        if (!TextUtils.isEmpty(item.getCreateTime())) {
            String userDate = DateUtil.getUserDate(item.getCreateTime());
            time_Tv.setText(userDate);
        }


        LinearLayout ll_container_answer = helper.getView(R.id.ll_container_answer);
        String topicType = item.getTopicType();
        if ("1".equals(topicType)) {//1是话题2是问答
            handleCommonData(item.getAvatar(),item.getNickname(),item.getContent(),item.getRole()
                    ,group_Img,group_Name,content_Tv,tv_role);
            handleImageList(item, mNineView);
            ll_container_answer.setVisibility(View.GONE);
        }else{
            List<OwnPublishBean.DetailDTO> details = item.getDetail();
            if (!ListUtils.isEmpty(details)) {
                ll_container_answer.setVisibility(View.VISIBLE);
                OwnPublishBean.DetailDTO answerObj = details.get(0);
                handleCommonData(answerObj.getAvatar(),answerObj.getNickname(),answerObj.getAnswerContent(),answerObj.getRole()
                ,group_Img,group_Name,content_Tv,tv_role);
                createQuestionList(ll_container_answer,item);
            }else{
                ll_container_answer.setVisibility(View.GONE);
            }
        }
        helper.addOnClickListener(R.id.iv_delete);
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


