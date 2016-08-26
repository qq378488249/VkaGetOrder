package cc.chenghong.vkagetorder.comat;

import android.view.MotionEvent;

/**
 * Created by 何成龙 on 2016/7/18.
 */
public class MotionEventCompatEclair {
    public static int findPointerIndex(MotionEvent event, int pointerId) {
        return event.findPointerIndex(pointerId);
    }
    public static int getPointerId(MotionEvent event, int pointerIndex) {
        return event.getPointerId(pointerIndex);
    }
    public static float getX(MotionEvent event, int pointerIndex) {
        return event.getX(pointerIndex);
    }
    public static float getY(MotionEvent event, int pointerIndex) {
        return event.getY(pointerIndex);
    }
}
