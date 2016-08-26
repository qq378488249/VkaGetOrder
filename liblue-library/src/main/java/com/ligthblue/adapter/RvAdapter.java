package com.ligthblue.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RecyclerView万能适配器，可直接设置点击事件跟长按事件
 * Created by 何成龙 on 2015/7/6.
 */
public abstract class RvAdapter<T> extends RecyclerView.Adapter<RvViewHolder> {
    private Context mContext;
    private List<T> list;
    protected LayoutInflater mInflater;
    private int mItemLayoutId;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;

    public RvAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = new ArrayList<T>();

    }

    public RvAdapter(Context context, List<T> list) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = new LinearLayout(mContext).getId();
        this.list = list;

    }

    /**
     * @param context
     * @param list         数据集合
     * @param itemLayoutId item编号
     */
    public RvAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        this.list = list;

    }

    public RvAdapter(Context context, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        this.list = new ArrayList<T>();
    }

    public void setitemLayoutId(int itemLayoutId) {
        this.mItemLayoutId = itemLayoutId;
    }

    public List<T> getList() {
        return this.list;
    }

    public void appendList(List<T> list) {
        // TODO Auto-generated method stub
        this.list = list;
        notifyDataSetChanged();
    }

    public void addAllList(List<T> list2) {
        // TODO Auto-generated method stub
        this.list.addAll((Collection<? extends T>) list2);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    boolean hasHeader = false;
    boolean hasFooter = false;
    View headerView;
    View footerView;

    public void setHeaderView(View headerView) {
        hasHeader = true;
        this.headerView = headerView;
    }

    public void setFooterView(View footerView) {
        hasFooter = true;
        this.footerView = footerView;
    }

    public View getHeaderView() {
        return headerView;
    }

    public View getFooterView() {
        return footerView;
    }

//    @Override
//    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.d("TAG", "onCreateViewHolder() called with: " + "parent = [" + parent + "], viewType = [" + viewType + "]"+",times="+"["+(++times)+"]");
//        final RvViewHolder holder=new RvViewHolder(mInflater.inflate(mItemLayoutId,parent,false));
//
//        //设置点击事件监听
//        if (mListener!=null){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.OnItemClickListener(v,holder.getLayoutPosition());
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mLongClickListener.OnItemLongClickListener(v,holder.getLayoutPosition());
//                    //返回true，拦截点击事件继续往下传递，不触发单击事件的响应
//                    return true;
//                }
//            });
//        }
//        return holder;
//    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (hasHeader && position == 0) {
            return;
        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
            return;
        } else {
            convert(holder, (T) list.get(position));
        }
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int position) {
//        RvViewHolder viewHolder = new RvViewHolder()
//        if (hasHeader && position == 0) {
//            return new RvViewHolder(headerView);
//        } else if (hasFooter && position == (list.size() + (hasHeader ? 1 : 0))) {
//            return new RvViewHolder(footerView);
//        } else {
//            return RvViewHolder.get(mContext, parent, mItemLayoutId, position);
//        }

        final RvViewHolder holder = new RvViewHolder(mInflater.inflate(mItemLayoutId, parent, false));
        //设置点击事件监听
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.OnItemClickListener(v, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.OnItemLongClickListener(v, holder.getLayoutPosition());
                    //返回true，拦截点击事件继续往下传递，不触发单击事件的响应
                    return true;
                }
            });
        }
        return holder;

    }

    //这里定义抽象方法，我们在匿名内部类实现的时候实现此方法来调用控件
    public abstract void convert(RvViewHolder holder, T item);

    /**
     * 自定义RecyclerView item的点击事件
     */
    public interface OnItemClickListener {
        void OnItemClickListener(View view, int position);
    }

    /**
     * 自定义RecyclerView item的长点击事件
     */
    public interface OnItemLongClickListener {
        void OnItemLongClickListener(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }
}
