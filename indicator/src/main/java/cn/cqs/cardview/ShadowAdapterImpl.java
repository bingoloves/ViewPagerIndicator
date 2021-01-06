package cn.cqs.cardview;

import android.annotation.SuppressLint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tmall.ultraviewpager.TimerHandler;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ShadowAdapterImpl<T> extends PagerAdapter implements IShadowAdapter {
    /*使用Map缓存，避免误解*/
    private Map<Integer,View> mViews;
    private List<T> mData;
    private float mBaseElevation;
    private boolean isBannerStyle = false;
    private TimerHandler timer;
    private ViewPager viewPager;

    public ShadowAdapterImpl(List<T> list) {
        this(list,false);
    }

    public ShadowAdapterImpl(List<T> list,boolean isBannerStyle) {
        this.isBannerStyle = isBannerStyle;
        this.mData = list;
        mViews = new HashMap<>();
    }

    /**
     * 提供布局Id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 数据绑定
     * @param item
     * @param view
     */
    public abstract void bindView(T item, View view);

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public View getCardViewAt(int position) {
        return mViews.get(position % mData.size());
    }

    @Override
    public int getCount() {
        if (isBannerStyle) return Integer.MAX_VALUE;
        return mData.size();
    }

    public int getRealCount(){
        return mData.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(getLayoutId(), container, false);
        container.addView(view);
        int newPosition = position % mData.size();
        bindView(mData.get(newPosition), view);
        setCardViewConfig(view,newPosition);
        return view;
    }

    /**
     * 设置缓存数据
     * @param view
     * @param position
     */
    public void setCardViewConfig(View view,int position){
        if (view != null){
            if (view instanceof CardView){
                CardView cardView = (CardView) view;
                if (mBaseElevation == 0) {
                    mBaseElevation = cardView.getCardElevation();
                }
                Log.e("TAG","mBaseElevation = "+mBaseElevation);
                //默认CardView mBaseElevation CardView间距可以根据实际情况调整
                //cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            }
            mViews.put(position, view);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.remove(position % mData.size());
    }


    public void attachToViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(viewPager, this);
        viewPager.setAdapter(this);
        viewPager.setPageTransformer(false, mCardShadowTransformer);
        if (isBannerStyle){
            viewPager.setCurrentItem(1000);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (timer != null) {
                        final int action = event.getAction();
                        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE ) {
                            Log.e("TAG","ACTION_DOWN | ACTION_MOVE");
                            stopTimer();
                        }
                        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                            Log.e("TAG","ACTION_UP | ACTION_CANCEL");
                            startTimer();
                        }
                    }
                    return false;
                }
            });
        }
    }

    private TimerHandler.TimerHandlerListener mTimerHandlerListener = new TimerHandler.TimerHandlerListener() {
        @Override
        public int getNextItem() {
            return (viewPager.getCurrentItem()+1) % getRealCount();
        }

        @Override
        public void callBack() {
            if (viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0) {
                final int curr = viewPager.getCurrentItem();
                int nextPage = 0;
                if (curr < viewPager.getAdapter().getCount() - 1) {
                    nextPage = curr + 1;
                }
                viewPager.setCurrentItem(nextPage, true);
            }
        }
    };
    public void setAutoScroll(int intervalInMillis) {
        if (0 == intervalInMillis) {
            return;
        }
        if (timer != null) {
            disableAutoScroll();
        }
        timer = new TimerHandler(mTimerHandlerListener, intervalInMillis);
        startTimer();
    }

    public void disableAutoScroll() {
        stopTimer();
        timer = null;
    }
    private void startTimer() {
        if (timer == null || viewPager == null || !timer.isStopped) {
            return;
        }
        timer.listener = mTimerHandlerListener;
        timer.removeCallbacksAndMessages(null);
        timer.tick(0);
        timer.isStopped = false;
    }

    private void stopTimer() {
        if (timer == null || viewPager == null || timer.isStopped) {
            return;
        }
        timer.removeCallbacksAndMessages(null);
        timer.listener = null;
        timer.isStopped = true;
    }
}
