package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.GroupMoreInfoBean;
import com.xunda.lib.common.base.BaseApplication;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.eventbus.FinishEvent;
import com.xunda.lib.common.common.eventbus.GroupManagerEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.imgloader.ImgLoader;
import com.xunda.lib.common.common.utils.FastBlurUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.ShareDialog;
import com.xunda.lib.common.view.MyArrowItemView;
import com.xunda.lib.common.view.RoundImageView;

import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 社群详情
 */
public class GroupDetailMoreInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_top)
    LinearLayout ll_top;
    @BindView(R.id.iv_head)
    RoundImageView ivHead;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.arrowItemView_edit)
    MyArrowItemView arrowItemViewEdit;
    @BindView(R.id.arrowItemView_number)
    MyArrowItemView arrowItemViewNumber;
    @BindView(R.id.arrowItemView_manager)
    MyArrowItemView arrowItemViewManager;
    @BindView(R.id.arrowItemView_invite_partner)
    MyArrowItemView arrowItemViewInvitePartner;
    @BindView(R.id.arrowItemView_invite_member)
    MyArrowItemView arrowItemViewInviteMember;
    @BindView(R.id.arrowItemView_apply_exit)
    MyArrowItemView arrowItemViewApplyExit;
    @BindView(R.id.ll_invite)
    LinearLayout ll_invite;
    private String id,back,role,shareUrl_group,shareUrl_partner,groupImage;
    private GroupMoreInfoBean sheQunBean;


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupDetailMoreInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail_more_info;
    }

    @Override
    protected void initView() {
        setBar();
        registerEventBus();
    }

    @Subscribe
    public void onEditGroupEvent(EditGroupEvent event) {
        if(event.type == EditGroupEvent.EDIT_GROUP){
            getGroupDetail(false);
        }
    }

    @Subscribe
    public void onGroupManagerEvent(GroupManagerEvent event) {
        if(event.type == GroupManagerEvent.UPDATE_FEE){
            sheQunBean.setShequnFee(event.value);
        }else if(event.type == GroupManagerEvent.UPDATE_THEME){
            sheQunBean.setShowTheme(event.value);
        }
    }

    @Subscribe
    public void onFinishEvent(FinishEvent event) {
        if(event.type == FinishEvent.EXIT_GROUP){
            back();
        }
    }

    @Override
    protected void initLocalData() {
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void getServerData() {
        getGroupDetail(true);
    }


    @Override
    protected void setStatusBar() {

    }

    /**
     * 设置状态栏
     */
    private void setBar() {
        ImmersionBar.with(this).titleBar(ll_top)
                .statusBarDarkFont(false)
                .init();
    }


    /**
     * 获取社群详情
     */
    private void getGroupDetail(boolean isShowAnimal) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        HttpSender sender = new HttpSender(HttpUrl.shequnInfo, "获取社群详情", map,
                new MyOnHttpResListener() {
                    @Override
                    public void onComplete(String json, int status, String description, String data) {
                        if (status == Constants.REQUEST_SUCCESS_CODE) {
                            sheQunBean = GsonUtil.getInstance().json2Bean(data, GroupMoreInfoBean.class);
                            handleData();
                        }
                    }
                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData() {
        if (sheQunBean != null) {
            sheQunBean.setSqId(id);
            tvId.setText(String.format("群ID：%s", id));
            tvName.setText(sheQunBean.getShequnName());
            arrowItemViewEdit.getTvContent().setText(sheQunBean.getShequnDes());
            arrowItemViewNumber.getTvContent().setText(sheQunBean.getCount());
            String group_header = sheQunBean.getShequnImage();
            ImgLoader.getInstance().displayCrop(mContext,ivHead,group_header,R.mipmap.bg_group_top);
            handleGaussianBlur(group_header);


            role = sheQunBean.getRole();
            groupImage = sheQunBean.getShequnImage();
            shareUrl_group = sheQunBean.getShareUrl();
            shareUrl_partner = sheQunBean.getPartnerUrl();
            if ("1".equals(role)||"2".equals(role)) {//1管理员2合伙人0一般用户3群主
                arrowItemViewManager.setVisibility(View.GONE);
                ll_invite.setVisibility(View.GONE);
            }else if ("3".equals(role)) {
                arrowItemViewManager.setVisibility(View.VISIBLE);
                ll_invite.setVisibility(View.VISIBLE);
            } else {
                arrowItemViewManager.setVisibility(View.GONE);
                ll_invite.setVisibility(View.GONE);
            }

            back = sheQunBean.getBack();

            if ("1".equals(back)) {//1可以退款0不可以退款
                arrowItemViewApplyExit.getTvTitle().setText("申请退款");
                arrowItemViewApplyExit.getTvTitle().setTextColor(ContextCompat.getColor(mContext, R.color.text_color_444444));
            } else {
                arrowItemViewApplyExit.getTvTitle().setText("退出社群");
                arrowItemViewApplyExit.getTvTitle().setTextColor(ContextCompat.getColor(mContext, R.color.text_color_FC0000));
            }
        }
    }


    /**
     * 处理高斯模糊
     */
    private void handleGaussianBlur(String group_header) {
        if (StringUtil.isBlank(group_header)) {
            iv_head_bag.setImageResource(R.mipmap.bg_group_top);
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int scaleRatio = 2;
                    //下面的这个方法必须在子线程中执行
                    final Bitmap blurBitmap = FastBlurUtil.GetUrlBitmap(group_header, scaleRatio);
                    //刷新ui必须在主线程中执行
                    BaseApplication.runOnUIThread(new Runnable() {//这个是我自己封装的在主线程中刷新ui的方法。
                        @Override
                        public void run() {
                            iv_head_bag.setImageBitmap(blurBitmap);
                        }
                    });
                }
            }).start();
        }
    }


    @OnClick({R.id.arrowItemView_edit, R.id.arrowItemView_number, R.id.arrowItemView_manager, R.id.arrowItemView_invite_partner,
            R.id.arrowItemView_invite_member, R.id.arrowItemView_apply_exit,R.id.ll_back, R.id.tv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrowItemView_edit:
                GroupSettingActivity.actionStart(mActivity, sheQunBean);
                break;
            case R.id.arrowItemView_number:
                GroupMemberListActivity.actionStart(mActivity,id,role);
                break;
            case R.id.arrowItemView_manager:
                GroupManagerActivity.actionStart(mActivity,sheQunBean);
                break;
            case R.id.arrowItemView_invite_partner:
                InvitePartnerActivity.actionStart(mActivity,shareUrl_partner);
                break;
            case R.id.arrowItemView_invite_member:
                SelectMemberListActivity.actionStart(mActivity,id,1);
                break;
            case R.id.arrowItemView_apply_exit:
                ApplyExitGroupOrMoneyActivity.actionStart(mActivity,back,id);
                break;
            case R.id.ll_back:
                back();
                break;
            case R.id.tv_share:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        if (sheQunBean==null) {
            return;
        }
        ShareDialog mShareDialog = new ShareDialog(this,shareUrl_group,groupImage,sheQunBean.getShequnName(),sheQunBean.getShequnDes(),"分享社群");
        mShareDialog.show();
    }
}
