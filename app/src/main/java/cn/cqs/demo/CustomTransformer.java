package cn.cqs.demo;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by bingo on 2021/1/5.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/1/5
 */

public class CustomTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.9F;
    @Override
    public void transformPage(View view, float position) {
        Log.e("TAG","position = "+position);
        if(position < -1){
            view.setScaleY(MIN_SCALE);
        }else if(position <= 1){
            float scale = Math.max(MIN_SCALE,1 - Math.abs(position));
            view.setScaleY(scale);
        } else{
            view.setScaleY(MIN_SCALE);
        }
    }
}