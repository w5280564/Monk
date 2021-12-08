package com.qingbo.monk.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qingbo.monk.webview.WebviewActivity;
import com.xunda.lib.common.bean.BaseSplitIndexBean;
import com.xunda.lib.common.common.Constants;
import com.qingbo.monk.HttpSender;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.common.utils.T;
import com.xunda.lib.common.dialog.ToastDialog;
import com.qingbo.monk.R;
import com.gyf.barlibrary.SimpleImmersionFragment;
import com.xunda.lib.common.view.CountDownTextView;

import org.greenrobot.eventbus.EventBus;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends SimpleImmersionFragment{
    protected MonkApplication mApplication;
    protected Context mContext;
    protected BaseActivity mActivity;
    private Unbinder unbinder;
    protected int page = 1;
    protected int limit = 10;
    protected String fileName;/* 图片上传图片名称 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mApplication = (MonkApplication) MonkApplication.getInstance();
        mContext = mApplication.getContext();
        mActivity = (BaseActivity) getActivity();
        initLocalData();
        //view与数据绑定
        initView();
        initView(mRootView);
        //设置监听
        initEvent();
        //请求服务端接口数据
        getServerData();
        return mRootView;

    }



    /**
     * 绑定布局文件
     */
    protected abstract int getLayoutId();


    /**
     * 初始化本地数据
     */
    protected void initLocalData() {

    }


    /**
     * 初始化view
     */
    protected void initView() {

    }

    /**
     * 初始化view
     */
    protected void initView(View mRootView) {

    }


    /**
     * 设置监听
     */
    protected void initEvent() {

    }


    /**
     * 请求服务端接口数据
     */
    protected void getServerData() {

    }





    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 建议在`自定义页面`的页面结束函数中调用
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }






    /**
     * 功能描述:简单地Activity的跳转(不携带任何数据)
     */
    protected void skipAnotherActivity(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(mActivity, targetActivity);
        startActivity(intent);
    }











    /**
     * 跳转到默认H5展示页面
     */
    protected void jumpToWebView(String title, String url) {
        Intent intent = new Intent(mActivity, WebviewActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        startActivity(intent);
    }





    protected void showCommonToastDialog(String description) {
        ToastDialog mDialog = new ToastDialog(mActivity, getString(R.string.toast_warm_prompt), description, getString(R.string.Sure), new ToastDialog.DialogConfirmListener() {
            @Override
            public void onConfirmClick() {

            }
        });
        mDialog.show();
    }



    /**
     * 跳转到查看图片页
     *
     * @param position
     * @param mImageList
     */
    public void jumpToPhotoShowActivity(int position, List<String> mImageList) {
        Intent intent = new Intent(mActivity, PhotoShowActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("imgList", (Serializable) mImageList);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }





    protected View addEmptyView(String text, int imgResource) {
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.empty_view_layout, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView iv_noData = emptyView.findViewById(R.id.iv_noData);

        if (!StringUtil.isBlank(text)) {
            TextView tv_noData = emptyView.findViewById(R.id.tv_noData);
            tv_noData.setText(text);
        }

        if (imgResource != 0) {
            iv_noData.setImageResource(imgResource);
        } else {
            iv_noData.setImageResource(R.mipmap.icon_ty);
        }

        return emptyView;
    }



    @Override
    public void initImmersionBar() {

    }


    /**
     * 获取验证码
     */
    protected void getSmsCode(String area_code,String phone, CountDownTextView tvGetCode) {
        HashMap<String, String> baseMap = new HashMap<String, String>();
        baseMap.put("area", area_code);
        baseMap.put("mobile", phone);
        HttpSender sender = new HttpSender(HttpUrl.sendCode, "获取验证码", baseMap,
                new MyOnHttpResListener() {

                    @Override
                    public void onComplete(String json_root, int code, String msg, String json_data) {
                        if (code == Constants.REQUEST_SUCCESS_CODE) {//成功
                            T.ss(getString(R.string.Base_yzmyfs));
                            tvGetCode.start();
                        }

                    }
                }, true);
        sender.setContext(mActivity);
        sender.sendGet();
    }





    /**
     * 全局处理分页的公共方法
     * @param obj  具体的分页对象  列表适配器
     * @param mAdapter
     */
    protected void handleSplitListData(BaseSplitIndexBean obj, BaseQuickAdapter mAdapter, int mPageSize) {
        if(obj!=null){
            int allCount = StringUtil.isBlank(obj.getCount())?0:Integer.parseInt(obj.getCount());
            int bigPage = 0;//最大页
            if(allCount%mPageSize!=0){
                bigPage=allCount/mPageSize+1;
            }else{
                bigPage=allCount/mPageSize;
            }
            if(page==bigPage){
                mAdapter.loadMoreEnd();//显示“没有更多数据”
            }

            boolean isRefresh = page==1?true:false;
            if(!ListUtils.isEmpty(obj.getList())){
                int size = obj.getList().size();

                if (isRefresh) {
                    mAdapter.setNewData(obj.getList());
                } else {
                    mAdapter.addData(obj.getList());
                }


                if (size < mPageSize) {
                    mAdapter.loadMoreEnd(isRefresh);//第一页的话隐藏“没有更多数据”，否则显示“没有更多数据”
                } else {
                    mAdapter.loadMoreComplete();
                }
            }else{

                if (isRefresh) {
                    mAdapter.setNewData(null);//刷新列表
                } else {
                    mAdapter.loadMoreEnd(false);//显示“没有更多数据”
                }
            }
        }
    }




}

