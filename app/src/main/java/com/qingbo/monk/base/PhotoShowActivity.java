package com.qingbo.monk.base;

import android.view.WindowManager;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.xunda.lib.common.common.eventbus.ClickImageFinishEvent;
import com.xunda.lib.common.common.utils.ListUtils;
import com.qingbo.monk.R;
import com.xunda.lib.common.view.ViewPagerPhoto;
import com.gyf.barlibrary.ImmersionBar;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 查看图片的公共页面
 */
public class PhotoShowActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPagerPhoto viewPager;
    @BindView(R.id.tv_num)
    TextView tvNum;
    private List<String> imageUrlList = new ArrayList<>();
    private int index;


    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        return R.layout.activity_photo_show;
    }

    @Subscribe
    public void onFinishEvent(ClickImageFinishEvent event) {
        if(event.type == ClickImageFinishEvent.CLICK_IMAGE){
            back();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

    }


        @Override
    protected void setStatusBar() {
        ImmersionBar.with(this).init();
    }



    @Override
    protected void initView() {
        registerEventBus();
    }

    @Override
    protected void initLocalData() {
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        index = getIntent().getIntExtra("index",0);
        List<String> tempList = (List<String>) getIntent().getSerializableExtra("imgList");
        if(!ListUtils.isEmpty(tempList)){
            imageUrlList.addAll(tempList);
            tvNum.setText((index + 1) + "/" + imageUrlList.size());
            initPagerAdapter();
        }
    }

    private void initPagerAdapter() {
        PhotoPagerAdapter viewPagerAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), imageUrlList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvNum.setText((position + 1) + "/" + imageUrlList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(index);
    }



}
