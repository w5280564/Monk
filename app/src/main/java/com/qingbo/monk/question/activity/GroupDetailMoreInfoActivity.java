package com.qingbo.monk.question.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gyf.barlibrary.ImmersionBar;
import com.qingbo.monk.HttpSender;
import com.qingbo.monk.R;
import com.qingbo.monk.base.BaseActivity;
import com.qingbo.monk.bean.MyGroupBean;
import com.xunda.lib.common.common.Constants;
import com.xunda.lib.common.common.eventbus.EditGroupEvent;
import com.xunda.lib.common.common.glide.GlideUtils;
import com.xunda.lib.common.common.http.HttpUrl;
import com.xunda.lib.common.common.http.MyOnHttpResListener;
import com.xunda.lib.common.common.utils.GsonUtil;
import com.xunda.lib.common.common.utils.StringUtil;
import org.greenrobot.eventbus.Subscribe;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 社群详情
 */
public class GroupDetailMoreInfoActivity extends BaseActivity {
    @BindView(R.id.iv_head_bag)
    ImageView iv_head_bag;
    @BindView(R.id.tv_shequn_name)
    TextView tv_shequn_name;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    private String id;
    private MyGroupBean sheQunBean;


    public static void actionStart(Context context, String id) {
        Intent intent = new Intent(context, GroupDetailMoreInfoActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_detail_more_info;
    }

    @Override
    protected void initView() {
        setBar();
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
        ImmersionBar.with(this).titleBar(ll_back)
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
                            sheQunBean = GsonUtil.getInstance().json2Bean(data, MyGroupBean.class);
                            handleData();
                        }
                    }
                }, isShowAnimal);
        sender.setContext(mActivity);
        sender.sendGet();
    }

    private void handleData() {
        if (sheQunBean != null) {
            sheQunBean.setId(id);
            String group_header = sheQunBean.getShequnImage();
            if (StringUtil.isBlank(group_header)) {
                iv_head_bag.setImageResource(R.mipmap.bg_group_top);
            }else{
                GlideUtils.loadImage(mContext,iv_head_bag,group_header);
            }
            tv_shequn_name.setText(StringUtil.getStringValue(sheQunBean.getShequnName()));
        }
    }






    @OnClick({R.id.ll_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                back();
                break;
        }
    }

}
