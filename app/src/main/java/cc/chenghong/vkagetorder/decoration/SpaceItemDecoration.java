package cc.chenghong.vkagetorder.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hcl on 2016/7/5.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        outRect.top = space;
//        if (parent.getChildAdapterPosition(view) != 0||parent.getChildAdapterPosition(view) != 1||parent.getChildAdapterPosition(view) != 2) {
//            outRect.top = space;
//        }
    }
}
