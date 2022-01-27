package com.qingbo.monk.base.baseview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.qingbo.monk.R;

/**
 * 头部下拉放大Behavior
 */

public class AppBarLayoutOverScrollViewBehavior extends AppBarLayout.Behavior {
//        private static final String TAG = "overScroll";
//    //    private static final String TAG_TOOLBAR = "toolbar";
//    private static final String TAG_MIDDLE = "middle"; //对应布局中NestedScrollView的tag
//    private static final float TARGET_HEIGHT = 1500;//放大最大高度
//    private View mTargetView;
//    private int mParentHeight;//记录AppbarLayout原始高度
//    private int mTargetViewHeight;//记录ImageView原始高度
//    private float mTotalDy;//手指在Y轴滑动的总距离
//    private float mLastScale;//图片缩放比例
//    private int mLastBottom;//Appbar的变化高度
//    private boolean isAnimate;
//    //    private Toolbar mToolBar;
//    private ViewGroup middleLayout;//个人信息布局
//    private int mMiddleHeight;
//    private boolean isRecovering = false;//是否正在自动回弹中
//
//    private final float MAX_REFRESH_LIMIT = 0.3f;//达到这个下拉临界值就开始刷新动画
//
//    public AppBarLayoutOverScrollViewBehavior() {
//    }
//
//    public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
//        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
//
////        if (mToolBar == null) {
////            mToolBar = parent.findViewWithTag(TAG_TOOLBAR);
////        }
//        if (middleLayout == null) {
//            middleLayout = (ViewGroup) parent.findViewWithTag(TAG_MIDDLE);
//        }
//        // 需要在调用过super.onLayoutChild()方法之后获取
//        if (mTargetView == null) {
//            mTargetView = parent.findViewById(R.id.iv_img);
//            if (mTargetView != null) {
//                initial(abl);
//            }
//        }
////        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
////            @Override
////            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//////                mToolBar.setAlpha(Float.valueOf(Math.abs(i)) / Float.valueOf(appBarLayout.getTotalScrollRange()));
////
////            }
////
////        });
////        abl.getTotalScrollRange()
////
////        final boolean started = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && (child.isLiftOnScroll() || canScrollChildren(parent, child, directTargetChild));
////
////        abl.SCROLL_AXIS_VERTICAL
////        abl.hasScrollableChildren()
//
//        abl.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                onNestedPreScroll(parent,abl,middleLayout,);
//                scale(abl, v, scrollY);
//            }
//        });
//        middleLayout.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                scale(abl, v, scrollY);
//            }
//        });
//
//
//        return handled;
//    }
//
//
//    /**
//     * 是否处理嵌套滑动
//     *
//     * @param parent
//     * @param child
//     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
//     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
//     * @param nestedScrollAxes  嵌套滚动的方向
//     * @return
//     */
//    @Override
//    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int x) {
//        isAnimate = true;
////        DisInterceptNestedScrollView
////        if (target instanceof NestedScrollView) return true;//这个布局就是middleLayout
//        if (target instanceof DisInterceptNestedScrollView) return true;//这个布局就是middleLayout
//            return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, x);
//
//    }
//
//
//
//    @Override
//    public void onNestedScroll(CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
//        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
//
//    }
//
//
////    @Override
////    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
////        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
////        stopNestedScrollIfNeeded(dy, child, target, type);
////    }
//
//    private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
//        if (type == ViewCompat.TYPE_NON_TOUCH) {
//            final int currOffset = getTopAndBottomOffset();
//            if ((dy < 0 && currOffset == 0)
//                    || (dy > 0 && currOffset == -child.getTotalScrollRange())) {
//                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
//            }
//        }
//    }
//    /**
//     * 在这里做具体的滑动处理
//     *
//     * @param coordinatorLayout
//     * @param child
//     * @param target
//     * @param dx
//     * @param dy
//     * @param consumed
//     */
//    @Override
//    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int x) {
//        if (!isRecovering) {
//            // 1.mTargetView不为null 2.是向下滑动，dy<0表示向下滑动 3.AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
//            if (mTargetView != null && dy < 0 && child.getBottom() >= mParentHeight) {
////            if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight) || (dy > 0 && child.getBottom() > mParentHeight))) {
//                stopNestedScrollIfNeeded(dy, child, target, x);
//                scale(child, target, dy);
//                return;
//            }
//        }
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, x);
//    }
//
//    /**
//     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
//     *
//     * @param abl
//     * @param dy
//     */
//    private void scale(AppBarLayout abl, View target, int dy) {
//        // 累加垂直方向上滑动的像素数
//        mTotalDy += -dy;
//        // 不能大于最大滑动距离
//        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
//        // 计算目标View缩放比例，不能小于1
//        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
//        // 缩放目标View
//        mTargetView.setScaleX(mLastScale);
//        mTargetView.setScaleY(mLastScale);
//        // 计算目标View放大后增加的高度
//        mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
//        // 修改AppBarLayout的高度
//        abl.setBottom(mLastBottom);
////        target.setScrollY(0);
//
//        middleLayout.setTop(mLastBottom - mMiddleHeight);
//        middleLayout.setBottom(mLastBottom);
//
//        if (onProgressChangeListener != null) {
//            float progress = Math.min((mLastScale - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
//            onProgressChangeListener.onProgressChange(progress, false);
//        }
//
//    }
//
//
//    /**
//     * 处理惯性滑动的情况
//     *
//     * @param coordinatorLayout
//     * @param child
//     * @param target
//     * @param velocityX
//     * @param velocityY
//     * @return
//     */
//    @Override
//    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout
//            child, View target, float velocityX, float velocityY) {
//        if (velocityY > 100) {//当y速度>100,就秒弹回
//            isAnimate = false;
//        }
//        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
//    }
//
//    /**
//     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
//     *
//     * @param coordinatorLayout
//     * @param abl
//     * @param target
//     */
//    @Override
//    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View
//            target, int x) {
//        recovery(abl);
//        super.onStopNestedScroll(coordinatorLayout, abl, target, x);
//    }
//
//    /**
//     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
//     *
//     * @param abl
//     */
//    private void initial(AppBarLayout abl) {
//        abl.setClipChildren(false);
//        mParentHeight = abl.getHeight();
//        mTargetViewHeight = mTargetView.getHeight();
//        mMiddleHeight = middleLayout.getHeight();
//    }
//
//
//    public interface onProgressChangeListener {
//        /**
//         * 范围 0~1
//         *
//         * @param progress
//         * @param isRelease 是否是释放状态
//         */
//        void onProgressChange(float progress, boolean isRelease);
//    }
//
//    public void setOnProgressChangeListener  (AppBarLayoutOverScrollViewBehavior.onProgressChangeListener onProgressChangeListener) {
//        this.onProgressChangeListener = onProgressChangeListener;
//    }
//
//    onProgressChangeListener onProgressChangeListener;
//
//    /**
//     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
//     *
//     * @param abl
//     */
//    private void recovery(final AppBarLayout abl) {
//        if (isRecovering) return;
//        if (mTotalDy > 0) {
//            isRecovering = true;
//            mTotalDy = 0;
//            if (isAnimate) {
//                ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
//                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                                           @Override
//                                           public void onAnimationUpdate(ValueAnimator animation) {
//                                               float value = (float) animation.getAnimatedValue();
//                                               ViewCompat.setScaleX(mTargetView, value);
//                                               ViewCompat.setScaleY(mTargetView, value);
//                                               abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
//                                               middleLayout.setTop((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight));
//
//                                               if (onProgressChangeListener != null) {
//                                                   float progress = Math.min((value - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
//                                                   onProgressChangeListener.onProgressChange(progress, true);
//                                               }
//                                           }
//                                       }
//                );
//                anim.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        isRecovering = false;
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                    }
//                });
//                anim.start();
//            } else {
//                ViewCompat.setScaleX(mTargetView, 1f);
//                ViewCompat.setScaleY(mTargetView, 1f);
//                abl.setBottom(mParentHeight);
//                middleLayout.setTop(mParentHeight - mMiddleHeight);
////                middleLayout.setBottom(mParentHeight);
//                isRecovering = false;
//
//                if (onProgressChangeListener != null)
//                    onProgressChangeListener.onProgressChange(0, true);
//            }
//        }
//    }

    private static final String TAG = "overScroll";
    private static final String TAG_TOOLBAR = "toolbar";
    private static final String TAG_MIDDLE = "middle"; //对应布局中NestedScrollView的tag
    private static final float TARGET_HEIGHT = 1500;//放大最大高度
    private View mTargetView;
    private int mParentHeight; //记录AppbarLayout原始高度
    private int mTargetViewHeight; //记录ImageView原始高度
    private float mTotalDy;//手指在Y轴滑动的总距离
    private float mLastScale;//图片缩放比例
    private int mLastBottom;//Appbar的变化高度
    private boolean isAnimate;
    private Toolbar mToolBar;
    private ViewGroup middleLayout;//个人信息布局
    private int mMiddleHeight;
    private boolean isRecovering = false;//是否正在自动回弹中
    private final float MAX_REFRESH_LIMIT = 0.3f;//达到这个下拉临界值就开始刷新动画

    public AppBarLayoutOverScrollViewBehavior() {
    }

    public AppBarLayoutOverScrollViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);

        if (mToolBar == null) {
            mToolBar = parent.findViewWithTag(TAG_TOOLBAR);
        }
        if (middleLayout == null) {
            middleLayout = (ViewGroup) parent.findViewWithTag(TAG_MIDDLE);
        }
        // 需要在调用过super.onLayoutChild()方法之后获取
        if (mTargetView == null) {
            mTargetView = parent.findViewById(R.id.iv_img);
            if (mTargetView != null) {
                initial(abl);
            }
        }
        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
//                mToolBar.setAlpha(Float.valueOf(Math.abs(i)) / Float.valueOf(appBarLayout.getTotalScrollRange()));

            }

        });
        return handled;
    }

    /**
     * 是否处理嵌套滑动
     *
     * @param parent
     * @param child
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes  嵌套滚动的方向
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int x) {
        isAnimate = true;
        if (target instanceof NestedScrollView) return true;//这个布局就是middleLayout
//        if (target instanceof DisInterceptNestedScrollView) return true;//这个布局就是middleLayout
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, x);
    }

    /**
     * 在这里做具体的滑动处理
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int x) {
        if (!isRecovering) {
            if (mTargetView != null && ((dy < 0 && child.getBottom() >= mParentHeight)
                    || (dy > 0 && child.getBottom() > mParentHeight))) {
                scale(child, target, dy);
                return;
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, x);

    }

    /**
     * 处理惯性滑动的情况
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY > 100) {//当y速度>100,就秒弹回
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
     *
     * @param coordinatorLayout
     * @param abl
     * @param target
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int x) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, x);
    }

    /**
     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     *
     * @param abl
     */
    private void initial(AppBarLayout abl) {
        abl.setClipChildren(false);
        mParentHeight = abl.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
        mMiddleHeight = middleLayout.getHeight();
    }

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     *
     * @param abl
     */
    private void scale(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT);
        ViewCompat.setScaleX(mTargetView, mLastScale);
        ViewCompat.setScaleY(mTargetView, mLastScale);
        mLastBottom = mParentHeight + (int) (mTargetViewHeight / 2 * (mLastScale - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);

        middleLayout.setTop(mLastBottom - mMiddleHeight);
        middleLayout.setBottom(mLastBottom);

        if (onProgressChangeListener != null) {
            float progress = Math.min((mLastScale - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
            onProgressChangeListener.onProgressChange(progress, false);
        }

    }

    public interface onProgressChangeListener {
        /**
         * 范围 0~1
         *
         * @param progress
         * @param isRelease 是否是释放状态
         */
        void onProgressChange(float progress, boolean isRelease);
    }

    public void setOnProgressChangeListener(AppBarLayoutOverScrollViewBehavior.onProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    onProgressChangeListener onProgressChangeListener;

    private void recovery(final AppBarLayout abl) {
        if (isRecovering) return;
        if (mTotalDy > 0) {
            isRecovering = true;
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {

                                float value = (float) animation.getAnimatedValue();
                                ViewCompat.setScaleX(mTargetView, value);
                                ViewCompat.setScaleY(mTargetView, value);
                                abl.setBottom((int) (mLastBottom - (mLastBottom - mParentHeight) * animation.getAnimatedFraction()));
                                middleLayout.setTop((int) (mLastBottom -
                                        (mLastBottom - mParentHeight) * animation.getAnimatedFraction() - mMiddleHeight));

                                if (onProgressChangeListener != null) {
                                    float progress = Math.min((value - 1) / MAX_REFRESH_LIMIT, 1);//计算0~1的进度
                                    onProgressChangeListener.onProgressChange(progress, true);
                                }
                            }
                        }
                );
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRecovering = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                anim.start();
            } else {
                ViewCompat.setScaleX(mTargetView, 1f);
                ViewCompat.setScaleY(mTargetView, 1f);
                abl.setBottom(mParentHeight);
                middleLayout.setTop(mParentHeight - mMiddleHeight);
//                middleLayout.setBottom(mParentHeight);
                isRecovering = false;

                if (onProgressChangeListener != null)
                    onProgressChangeListener.onProgressChange(0, true);
            }
        }
    }


}
