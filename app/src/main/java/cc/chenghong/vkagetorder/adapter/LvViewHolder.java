package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cc.chenghong.vkagetorder.util.StringUtils;


public class LvViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private LvViewHolder(Context context, ViewGroup parent, int layoutId,
                         int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static LvViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new LvViewHolder(context, parent, layoutId, position);
        }
        return (LvViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public LvViewHolder setText(int viewId, Object text) {
        TextView view = getView(viewId);
        if (text == null) {
            view.setText("");
        } else {
            view.setText(text + "");
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
    public LvViewHolder setDefaultText(int viewId, Object text, Object defauleText) {
        TextView view = getView(viewId);
        if (StringUtils.noEmpty(text)) {
            view.setText(text + "");
        } else {
            view.setText(defauleText + "");
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
    public LvViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @return
     */
    public LvViewHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    //	/**
//	 * 为ImageView设置图片
//	 * 
//	 * @param viewId
//	 * @param drawableId
//	 * @return
//	 */
    public LvViewHolder setImageByUrl(int viewId, String url) {
        ImageLoader.getInstance().displayImage(url, (ImageView) getView(viewId));
        return this;
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    /**
     * 显示view
     *
     * @param viewId
     */
    public void visibleView(int viewId) {
        getView(viewId).setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏view
     *
     * @param viewId
     */
    public void goneView(int viewId) {
        getView(viewId).setVisibility(View.GONE);
    }

}
