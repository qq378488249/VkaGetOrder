package cc.chenghong.vkagetorder.fragment_pad;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.bean.Page;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.layout.RefreshFrameLayout;
import cc.chenghong.vkagetorder.pullable.PullToRefreshLayout;
import cc.chenghong.vkagetorder.pullable.PullableListView;
import cc.chenghong.vkagetorder.view.CircleImageView;

/**
 * pad门店
 * Created by 何成龙 on 2016/7/13.
 */
public class PadMdFragment extends BaseFragment {
    @Bind(R.id.iv)
    CircleImageView iv;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvZt)
    TextView tvZt;
    @Bind(R.id.rvStar)
    RecyclerView rvStar;
    @Bind(R.id.tvYy)
    TextView tvYy;
    @Bind(R.id.tvDd)
    TextView tvDd;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.rv)
    RefreshFrameLayout rv;
    @Bind(R.id.tv_nodata)
    TextView tvNodata;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.prl)
    PullToRefreshLayout prl;
    @Bind(R.id.plv)
    PullableListView plv;

    Page<String> page = new Page<>();
    int index = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_pad_md, container,
                false);
        ButterKnife.bind(this, view);
        getData(true, 1);
        initView();
        return view;
    }

    private void getData(boolean b, int start) {
        if (b) page.list.clear();
        for (int i = 0; i < 10; i++) {
            page.list.add((index++) + "订单");
        }
        if (page.adapter != null) {
            page.adapter.notifyDataSetChanged();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getData(true,1);
                    prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    break;
                case 2:
                    getData(false,2);
                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    break;
            }
        }
    };

    private void initView() {
        page.adapter = new LvAdapter<String>(getActivity(), page.list, R.layout.lv_item_pad_dd_lv) {
            @Override
            public void convert(LvViewHolder helper, String item, int position) {
                helper.setText(R.id.tv1, item);
            }
        };
        plv.setAdapter(page.adapter);
        prl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                Message message = Message.obtain();
                message.what = 1;
                handler.sendMessageDelayed(message,2000);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessageDelayed(message,2000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv, R.id.tvName, R.id.tvZt, R.id.tvYy, R.id.tvDd, R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv_nodata, R.id.ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                break;
            case R.id.tvName:
                break;
            case R.id.tvZt:
                break;
            case R.id.tvYy:
                break;
            case R.id.tvDd:
                break;
            case R.id.tv1:
                setSelectTv(tv1);
                break;
            case R.id.tv2:
                setSelectTv(tv2);
                break;
            case R.id.tv3:
                setSelectTv(tv3);
                break;
            case R.id.tv_nodata:
                break;
            case R.id.ll:
                break;
        }
    }

    void setSelectTv(View view) {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        view.setSelected(true);
    }
}
