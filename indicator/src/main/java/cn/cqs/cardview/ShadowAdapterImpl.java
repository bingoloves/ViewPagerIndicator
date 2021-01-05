package cn.cqs.cardview;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public abstract class ShadowAdapterImpl<T> extends PagerAdapter implements IShadowAdapter {

    private List<View> mViews;
    private List<T> mData;
    private float mBaseElevation;

    public ShadowAdapterImpl() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(T item) {
        mViews.add(null);
        mData.add(item);
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
        return mViews.get(position);
    }

    @Override
    public int getCount() {
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
        bindView(mData.get(position), view);
        setCardViewConfig(view,position);
        return view;
    }
    public void setCardViewConfig(View view,int position){
        if (view != null){
            if (view instanceof CardView){
                CardView cardView = (CardView) view;
                if (mBaseElevation == 0) {
                    mBaseElevation = cardView.getCardElevation();
                }
                cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
            }
            mViews.set(position, view);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }
}
