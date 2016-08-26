package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.bean.Order;
import cc.chenghong.vkagetorder.view.NestedListView;

/**
 * Created by hcl on 2016/7/4.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private List<Order> mList;
    private Context mContext;

    public OrderAdapter(Context context, List<Order> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lv_item_pad_dd_rv, parent, false);
        OrderHolder viewHolder = new OrderHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
//        final List<OrderDishes> list = mList.get(position).getList();
//        LvAdapter<OrderDishes> adapter = new LvAdapter<OrderDishes>(mContext, list, R.layout.lv_item_pad_dd_lv) {
//            @Override
//            public void convert(LvViewHolder helper, OrderDishes item, int position) {
//                helper.setText(R.id.tv1, item.getName());
//                helper.setText(R.id.tv2, item.getNumber());
//                helper.setText(R.id.tv3, item.getPrice());
//            }
//        };
//        holder.lv.setAdapter(adapter);
//        holder.lv.setAdapter(adapter);
    }

    class OrderHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivZt)
        ImageView ivZt;
        @Bind(R.id.tvDd)
        TextView tvDd;
        @Bind(R.id.tvSj)
        TextView tvSj;
        @Bind(R.id.tvXm)
        TextView tvXm;
        @Bind(R.id.tvHm)
        TextView tvHm;
        @Bind(R.id.tvDz)
        TextView tvDz;
        @Bind(R.id.tvSpm)
        TextView tvSpm;
        @Bind(R.id.tvZj)
        TextView tvZj;
        @Bind(R.id.lv)
        NestedListView lv;
        @Bind(R.id.tvDdh)
        TextView tvDdh;
        @Bind(R.id.tvJd)
        TextView tvJd;
        @Bind(R.id.tvQx)
        TextView tvQx;
        @Bind(R.id.tvQr)
        TextView tvQr;
//        NestedListView lv;
        public OrderHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(mContext,itemView);
            lv = (NestedListView) itemView.findViewById(R.id.lv);
        }
    }
}
