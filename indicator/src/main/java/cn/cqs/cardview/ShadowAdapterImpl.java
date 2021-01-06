package cn.cqs.cardview;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public ShadowAdapterImpl(List<T> list) {
        this(list,false);
    }

    public ShadowAdapterImpl(List<T> list,boolean isBannerStyle) {
        this.isBannerStyle = isBannerStyle;
        this.mData = list;
        mViews = new HashMap<>();
    }

    public void attachToViewPager(ViewPager viewPager) {
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(viewPager, this);
        viewPager.setAdapter(this);
        viewPager.setPageTransformer(false, mCardShadowTransformer);
        if (isBannerStyle)viewPager.setCurrentItem(1000);
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
}
