package com.qingbo.monk.person.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.xunda.lib.common.bean.ApkBean;
import com.xunda.lib.common.bean.AppMarketBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.OnHttpResListener;
import com.xunda.lib.common.common.utils.AndroidUtil;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.ListUtils;
import com.xunda.lib.common.common.utils.StringUtil;
import com.xunda.lib.common.dialog.ChooseAppMarketDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class MySet_AboutMe_Update extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.changeUpdate_Tv)
    TextView changeUpdate_Tv;
    @BindView(R.id.updateContent_Tv)
    TextView updateContent_Tv;
    @BindView(R.id.next_Tv)
    TextView next_Tv;

    @Override
    protected int getLayoutId() {
        return R.layout.myset_aboutme_update;
    }

    @Override
    protected void initView() {
        String versionName = "当前版本" + AndroidUtil.getVersionName(mContext);
        tv_version.setText(versionName);
    }

    @Override
    protected void initEvent() {
        next_Tv.setOnClickListener(this);
    }

    @Override
    protected void getServerData() {
        checkUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_Tv:
                if (apkObj != null){
                    int isForceUpdate = Integer.parseInt(apkObj.getIs_force_update());
                    isChange(apkObj.getRemark(),isForceUpdate,apkObj.getPlatform());
                }
                break;
        }
    }
    ApkBean apkObj;
    /**
     * 版本更新检测
     */
    private void checkUpdate() {
        HashMap<String, String> baseMap = new HashMap<>();
        baseMap.put("version", String.valueOf(AndroidUtil.getVersionName(mContext)));
        HttpSender sender = new HttpSender(HttpUrl.appVersion, "版本更新检测",
                baseMap, new OnHttpResListener() {
            @Override
            public void onComplete(String json, int status, String description, String data) {
                if (status == Constants.REQUEST_SUCCESS_CODE) {
                     apkObj = GsonUtil.getInstance().json2Bean(data, ApkBean.class);
                    if (apkObj != null) {
                        String tempIsForceUpdate = apkObj.getIs_force_update();//0不需要强制更新，1强制更新，2不需要更新
                        if (!StringUtil.isBlank(tempIsForceUpdate)) {
                            String format = String.format("是否升级到%1$s新版本", apkObj.getVersion());
                            changeUpdate_Tv.setText(format);
                            updateContent_Tv.setText(apkObj.getRemark());
                            int isForceUpdate = Integer.parseInt(tempIsForceUpdate);
                            if (isForceUpdate != 2) {
//                                isChange(apkObj.getRemark(),isForceUpdate,apkObj.getPlatform());
                            }
                        }

                    }
                }
            }
        }, false);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void isChange(String remark,int isForceUpdate,String platform){
        if (!StringUtil.isBlank(platform)){
            List<String> mPlatformList = StringUtil.stringToList(platform);
            if (!ListUtils.isEmpty(mPlatformList)) {
                if (mPlatformList.size()==1) {
                    jumpToWebsite();//只有一个平台时，点击下载直接跳官网
                }else{//否则弹出平台选择弹窗
                    handlePlatformList(isForceUpdate,mPlatformList);
                }
            }
        }
    }

    private void jumpToWebsite(){
        Uri uri = Uri.parse(Constants.WEB_SITE_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    private void handlePlatformList(int isForceUpdate,List<String> mPlatformList){
        String deviceBrandName = android.os.Build.BRAND;
        List<AppMarketBean> mMarketList = new ArrayList<>();
        for (int i = 0; i < mPlatformList.size(); i++) {
            String name = mPlatformList.get(i);
            AppMarketBean obj = new AppMarketBean();
            obj.setMarketName(name);

            if(deviceBrandName.equalsIgnoreCase(Constants.BRAND_OPPO)&&"OPPO".equals(name)){
                obj.setMarketPakageName("com.heytap.market");
                obj.setIconResource(R.mipmap.icon_oppo);
                obj.setBrandName(Constants.BRAND_OPPO);
                mMarketList.add(obj);
                break;
            }else if(deviceBrandName.equalsIgnoreCase(Constants.BRAND_VIVO)&&"VIVO".equals(name)){
                obj.setMarketPakageName("com.bbk.appstore");
                obj.setIconResource(R.mipmap.icon_vivo);
                obj.setBrandName(Constants.BRAND_VIVO);
                mMarketList.add(obj);
                break;
            }else if(deviceBrandName.equalsIgnoreCase(Constants.BRAND_HUAWEI)&&"华为".equals(name)){
                obj.setMarketPakageName("com.huawei.appmarket");
                obj.setIconResource(R.mipmap.icon_huawei);
                obj.setBrandName(Constants.BRAND_HUAWEI);
                mMarketList.add(obj);
                break;
            }else if(deviceBrandName.equalsIgnoreCase(Constants.BRAND_HONOR)&&"华为".equals(name)){
                obj.setMarketPakageName("com.huawei.appmarket");
                obj.setIconResource(R.mipmap.icon_huawei);
                obj.setBrandName(Constants.BRAND_HUAWEI);
                mMarketList.add(obj);
                break;
            }else if(deviceBrandName.equalsIgnoreCase(Constants.BRAND_XIAOMI)&&"小米".equals(name)){
                obj.setMarketPakageName("com.xiaomi.market");
                obj.setIconResource(R.mipmap.icon_xiaomi);
                obj.setBrandName(Constants.BRAND_XIAOMI);
                mMarketList.add(obj);
                break;
            }
        }

        if (mPlatformList.contains("应用宝")) {
            AppMarketBean obj = new AppMarketBean();
            obj.setMarketName("应用宝");
            obj.setMarketPakageName("com.tencent.android.qqdownloader");
            obj.setIconResource(R.mipmap.icon_yyb);
            mMarketList.add(obj);
        }

        showChooseMarketDialog(isForceUpdate,mMarketList);
    }


    private ChooseAppMarketDialog mChooseAppMarketDialog;
    /**
     * 弹出选择市场框
     */
    public void showChooseMarketDialog(int isForceUpdate,List<AppMarketBean> mMarketList) {
        if (mChooseAppMarketDialog == null) {
            mChooseAppMarketDialog = new ChooseAppMarketDialog(this,mMarketList , isForceUpdate,new ChooseAppMarketDialog.DialogItemChooseListener() {
                @Override
                public void onItemChooseClick(AppMarketBean obj) {
                    if (obj != null) {
                        if ("官网".equals(obj.getMarketName())) {
                            jumpToWebsite();
                        } else if ("应用宝".equals(obj.getMarketName())) {
                            openTencentYingYongBao(obj.getMarketPakageName());
                        } else {
                            launchAppDetail(obj.getMarketPakageName());
                        }
                    }

                }
            });
        }

        if (!mChooseAppMarketDialog.isShowing()) {
            mChooseAppMarketDialog.show();
        }
    }

    //打开应用宝
    private void openTencentYingYongBao(String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + Constants.APP_PKG);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!StringUtil.isBlank(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "请先安装应用宝", Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("https://a.app.qq.com/o/simple.jsp?pkgname=com.qingbo.monk");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    /**
     * 跳转到应用市场app详情界面
     * @param marketPkg 应用市场包名
     */
    public void launchAppDetail(String marketPkg) {
        try {
            Uri uri = Uri.parse("market://details?id=" + Constants.APP_PKG);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!StringUtil.isBlank(marketPkg)){
                intent.setPackage(marketPkg);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}