package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cc.chenghong.vkagetorder.util.StringUtils;

/** RecyclerView万能viewHolder
 * Created by 何成龙 on 2016/7/6.
 */
public class RvViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews;

    public RvViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        this.mViews = new SparseArray<View>();
    }

    public static RvViewHolder get(Context context, ViewGroup parent, int layoutId, int position) {
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RvViewHolder(view);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符
     *
     * @param viewId
     * @param text
     * @return
     */
    public RvViewHolder setText(int viewId, Object text) {
        TextView view = getView(viewId);
        if (StringUtils.noEmpty(text)) {
            view.setText(text+"");
        }
        return this;
    }
    /**
     * 为TextView设置默认字符
     *
     * @param viewId
     * @param text
     * @param defauleText
     * @return
     */
    public RvViewHolder setDefaultText(int viewId, Object text,Object defauleText) {
        TextView view = getView(viewId);
        if (StringUtils.noEmpty(text)) {
            view.setText(text+"");
        }else{
            view.setText(defauleText+"");
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public RvViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     * @param viewId
     * @param bm
     * @return
     */
    public RvViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    public View getConvertView() {
        return mConvertView;
    }
}
