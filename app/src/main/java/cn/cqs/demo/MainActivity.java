package cn.cqs.demo;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cqs.cardview.ShadowAdapterImpl;
import cn.cqs.cardview.ShadowTransformer;
import cn.cqs.indicator.UIndicator;

public class MainActivity extends AppCompatActivity {
    //普通的ViewPager模式
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.indicator1)
    UIndicator indicator1;
    //UltraViewPager
    @BindView(R.id.ultra_viewpager)
    UltraViewPager ultraViewPager;
    @BindView(R.id.indicator2)
    UIndicator indicator2;
    //带阴影的ViewPager
    @BindView(R.id.shadow_viewPager)
    ViewPager shadowViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DemoPagerAdapter adapter = new DemoPagerAdapter(getList());
        viewPager.setAdapter(adapter);
        indicator1.attachToViewPager(viewPager);
        viewPager.setPageMargin(10);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(false,new CustomTransformer());

        DemoPagerAdapter ultraAdapter = new DemoPagerAdapter(getList());
        ultraViewPager.setAdapter(ultraAdapter);

        ultraViewPager.setOffscreenPageLimit(3);
        indicator2.attachToViewPager(ultraViewPager.getViewPager());
        ultraViewPager.setMultiScreen(0.75f);
//        ultraViewPager.setItemRatio(1.0f);
//        ultraViewPager.setAutoMeasureHeight(true);
        ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());

        ShadowAdapterImpl shadowAdapter = new ShadowAdapterImpl<String>() {
            @Override
            public int getLayoutId() {
                return R.layout.layout_cardview_item;
            }

            @Override
            public void bindView(String item, View view) {

            }
        };
        shadowAdapter.addCardItem("");
        shadowAdapter.addCardItem("");
        shadowAdapter.addCardItem("");
        shadowAdapter.addCardItem("");
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(shadowViewPager, shadowAdapter);
        shadowViewPager.setAdapter(shadowAdapter);
        shadowViewPager.setPageTransformer(false, mCardShadowTransformer);
    }
    public List<View> getList() {
        List<View> list = new ArrayList<>();
        Random rm = new Random();
        for (int i = 0; i < 5; i++) {
            int ranColor = 0xff000000 | rm.nextInt(0x00ffffff);
            list.add(generateView(ranColor));
        }
        return list;
    }

    public View generateView(int color) {
        View tv = new View(this);
        ViewPager.LayoutParams lp = new ViewPager.LayoutParams();
        lp.width = ViewPager.LayoutParams.MATCH_PARENT;
        lp.height = ViewPager.LayoutParams.MATCH_PARENT;
        tv.setBackgroundColor(color);
        tv.setLayoutParams(lp);
        return tv;
    }
    public class DemoPagerAdapter extends PagerAdapter {
        private List<View> views;

        public DemoPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeAllViews();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
