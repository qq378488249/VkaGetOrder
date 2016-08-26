package cc.chenghong.vkagetorder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;

/**
 * Created by 何成龙 on 2016/7/22.
 */
public class OrdersDetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrdersDetailsList> mList;

    public OrdersDetailAdapter(Context context, List<OrdersDetailsList> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.lv_item_pad_dd_lv, null);
        TextView tv1 = (TextView) linearLayout.findViewById(R.id.tv1);
        TextView tv2 = (TextView) linearLayout.findViewById(R.id.tv2);
        TextView tv3 = (TextView) linearLayout.findViewById(R.id.tv3);
        tv1.setText(mList.get(position).getProductName() + "");
        tv2.setText("x"+mList.get(position).getCount() + "");
        tv3.setText(mList.get(position).getPrice() + "");
        return linearLayout;
    }
}
