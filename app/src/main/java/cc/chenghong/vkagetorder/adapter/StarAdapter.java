package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.bean.Star;

/**
 * Created by hcl on 2016/7/4.
 */
public class StarAdapter extends RecyclerView.Adapter<StarAdapter.OrderHolder> {
    private List<Star> mList;
    private Context mContext;
    public StarAdapter(Context context, List<Star> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_md_star,parent,false);
        OrderHolder viewHolder = new OrderHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        Star star = mList.get(position);
        if (star.isSelect()){
            holder.iv.setSelected(true);
        }else{
            holder.iv.setSelected(false);
        }
    }


    class OrderHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        public OrderHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
