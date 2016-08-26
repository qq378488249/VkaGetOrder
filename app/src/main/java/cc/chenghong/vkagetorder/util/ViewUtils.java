package cc.chenghong.vkagetorder.util;

import android.view.View;
import android.widget.TextView;

/**
 * 控件工具类
 * Created by 何成龙 on 2016/7/8.
 */
public class ViewUtils {
    /**
     * 判断控件是否为空
     * @param views
     * @return true为空，false非空
     */
    public static boolean viewisEmpty(View... views) {
        for (View view : views) {
            if (view == null) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断textView或editView是否为空或者值是否为null
     * @param views
     * @return true为空或null，false非空
     */
    public static boolean textViewisEmpty(View... views) {
        for (View view : views) {
            TextView tv = (TextView) view;
            if (tv == null || tv.getText().toString().equals("")) {
                return true;
            }
        }
        return false;
    }
}
