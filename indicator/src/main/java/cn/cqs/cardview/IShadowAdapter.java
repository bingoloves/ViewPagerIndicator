package cn.cqs.cardview;

import android.support.v7.widget.CardView;
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

public interface IShadowAdapter {
    int MAX_ELEVATION_FACTOR = 8;
    float getBaseElevation();
    View getCardViewAt(int position);
    int getCount();
}
