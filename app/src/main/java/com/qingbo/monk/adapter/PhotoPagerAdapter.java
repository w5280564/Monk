package com.qingbo.monk.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.qingbo.monk.base.PhotoFragment;

import java.util.List;

/**
 * 图片查看适配器
 * Created by ouyang
 */

public class PhotoPagerAdapter extends FragmentPagerAdapter {

    private List<String> urlList;

    public PhotoPagerAdapter(FragmentManager fm, List<String> urlList) {
        super(fm);
        this.urlList=urlList;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}
