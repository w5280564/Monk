package com.qingbo.monk.base;


/**
 * 取消预加载的Fragment基类
 */
public abstract class BaseLazyFragment extends BaseFragment{


    protected boolean isFirstLoad = true; // 是否第一次加载



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            loadData();
            isFirstLoad = false;
        }
    }



    protected abstract void loadData();
}
