package com.qingbo.monk.base;

import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.xunda.lib.common.common.utils.ListUtils;
import com.qingbo.monk.R;
import com.xunda.lib.common.view.ViewPagerPhoto;
import com.gyf.barlibrary.ImmersionBar;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 查看图片的公共页面
 */
public class PhotoShowActivity extends BaseActivity implements PhotoFragment.FragmentCallBack {
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



    @Override
    protected void setStatusBar() {
        ImmersionBar.with(this).init();
    }


    @Override
    protected void initLocalData() {
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        boolean single = getIntent().getBooleanExtra("single",false);//单张图片
        if (single) {
            String image_url = getIntent().getStringExtra("image_url");
            imageUrlList.add(image_url);
            tvNum.setVisibility(View.GONE);
        }else{
            index = getIntent().getIntExtra("index",0);
            List<String> tempList = (List<String>) getIntent().getSerializableExtra("imgList");
            if(!ListUtils.isEmpty(tempList)){
                imageUrlList.addAll(tempList);
                tvNum.setText((index + 1) + "/" + imageUrlList.size());
            }
        }
        initPagerAdapter();
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



    @Override
    public void onViewClick() {
        back();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
