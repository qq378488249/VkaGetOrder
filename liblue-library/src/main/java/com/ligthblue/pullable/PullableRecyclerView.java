package com.ligthblue.pullable;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by 何成龙 on 2016/7/18.
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {
    private String TAG = getClass().getSimpleName();

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        int count = getChildCount();
//        Log.i(TAG, first + "," + last + "," + count);
//        System.out.println(first + "\n" + last + "\n");
        if (getChildCount() == 0) {
            // û��item��ʱ��Ҳ��������ˢ��
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0) {
            // ����ListView�Ķ�����
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp() {
        int first = getFirstVisiblePosition();
        int last = getLastVisiblePosition();
        int count = getChildCount();
//        Log.i(TAG, first + "," + last + "," + count);
//        System.out.println(first + "\n" + last+"\n");
        int lineNum = 3;
        if (getChildCount() == 0) {
//        if (getChildCount() == 0 || getChildCount() == 1 || getChildCount() == 2) {
//            Toast.makeText(getContext(),"a"+first + "\n" + last + "\n" + count,Toast.LENGTH_LONG).show();
            return true;
//        } else if (getFirstVisiblePosition() == -1) {
//        } else if (getFirstVisiblePosition() == -1 &&
//                getChildAt(getLastVisiblePosition() - 0) != null &&
//                getChildAt(getLastVisiblePosition() - 0).getBottom() <= getMeasuredHeight()) {
//            Toast.makeText(getContext(), "b"+first + "\n" + last + "\n" + count,Toast.LENGTH_LONG).show();
//            return true;
//        } else if (getChildCount() <= lineNum) {
//            return true;
        } else if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null &&
                getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight()) {
//            Toast.makeText(getContext(), "c"+first + "\n" + last + "\n" + count,Toast.LENGTH_LONG).show();
            return true;
//        } else if (getFirstVisiblePosition() == -1 &&
//                getChildAt(getLastVisiblePosition()) != null &&
//                getChildAt(getLastVisiblePosition()).getBottom() <= getMeasuredHeight()) {
//            return true;
        }
        return false;
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的位置（第一列）
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int minPosition = positions[0];
        for (int i = 0; i < positions.length; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置（最后一列）
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
//            StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) getLayoutManager();
//            int[] lastPositions = new int[((StaggeredGridLayoutManager) lm).getSpanCount()];
//            int[] positions = ((StaggeredGridLayoutManager) lm).findLastVisibleItemPositions(lastPositions);
//            position = getMaxPosition(positions);

            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            if (getLayoutManager() != null && getLayoutManager().getChildCount() > 0) {
                position = getLayoutManager().getItemCount() - 1;
            } else {
                position = 0;
            }
        }
        return position;
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int maxPosition = positions[0];
        for (int i = 0; i < positions.length; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

}
