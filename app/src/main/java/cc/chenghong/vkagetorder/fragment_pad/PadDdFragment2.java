package cc.chenghong.vkagetorder.fragment_pad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.activity_pad.PadOrderDetailsAc;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.adapter.OrderAdapter;
import cc.chenghong.vkagetorder.api.Api;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Card;
import cc.chenghong.vkagetorder.bean.OrderState;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;
import cc.chenghong.vkagetorder.bean.Page;
import cc.chenghong.vkagetorder.bean.Push;
import cc.chenghong.vkagetorder.dialog.AskDialog;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.pullable.PullToRefreshLayout;
import cc.chenghong.vkagetorder.pullable.PullableGridView;
import cc.chenghong.vkagetorder.request.VolleyRequest;
import cc.chenghong.vkagetorder.response.BaseResponse;
import cc.chenghong.vkagetorder.util.StringUtils;
import cn.jpush.android.api.JPushInterface;

/**
 * pad订单
 * Created by hcl on 2016/7/5.
 */
public class PadDdFragment2 extends BaseFragment {

    @Bind(R.id.tvDd)
    TextView tvDd;
    @Bind(R.id.tvTd)
    TextView tvTd;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.flTd)
    FrameLayout flTd;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.flDd)
    FrameLayout flDd;
    @Bind(R.id.rlDd)
    RelativeLayout rlDd;
    @Bind(R.id.ivJt)//上/下箭头
            ImageView ivJt;
    @Bind(R.id.prl)
    PullToRefreshLayout prl;
    //    @Bind(R.id.pgv)
//    PullableRecyclerView pgv;
    @Bind(R.id.pgv)
    PullableGridView pgv;

    //    Page<Orders> page = new Page(0, 5);
    PopupWindow popupWindow;
    View popupWindowView;
    ListView lv;
    List<OrderState> stateList = new ArrayList<>();
    LvAdapter<OrderState> stateAdapter;
    OrderAdapter adapter;
    int index = 0;
    Page<Orders> page = new Page<>(5);//总列表
    List<Orders> showList = new ArrayList<>();//显示的列表
    List<Orders> temporaryList = new ArrayList<>();//临时列表，用来切换需要显示的订单

    MessageReceiver mMessageReceiver;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.iv7)
    ImageView iv7;
    @Bind(R.id.tv7)
    TextView tv7;
    @Bind(R.id.fl7)
    FrameLayout fl7;
    @Bind(R.id.tv8)
    TextView tv8;
    @Bind(R.id.tv9)
    TextView tv9;
    @Bind(R.id.tv10)
    TextView tv10;
    @Bind(R.id.tv11)
    TextView tv11;
//    @Bind(R.id.bt)
//    Button bt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_pad_dd1, container,
                false);
        ButterKnife.bind(this, view);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PrinterStatusBroadcastReceiver;
//            }
//        });
        getData(0);
        initAdapter();
        initView();
        registerMessageReceiver();
        return view;
    }

    AskDialog cancelOrder;

    public void showCancelOrder(final int index) {
        if (cancelOrder == null) {
            cancelOrder = new AskDialog(getActivity(), "是否取消该订单？", "取消后将不可恢复", "是", "否");
            cancelOrder.setTvNoOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOrder.dismiss();
                }
            });
        }
        cancelOrder.setAskDialogWidth(getScreenWidth() / 2);
        cancelOrder.setTvYesOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderById(index);
                cancelOrder.dismiss();
            }
        });
        cancelOrder.show();
    }

    private void initAdapter() {
        if (page.adapter == null) {
            page.adapter = new LvAdapter<Orders>(getActivity(), showList, R.layout.lv_item_pad_dd_rv1) {
                @Override
                public void convert(LvViewHolder holder, Orders item, final int position) {
                    holder.setText(R.id.tvSj, item.getCreated());
                    holder.setDefaultText(R.id.tvXm, item.getCardName(), "未填写姓名");
                    holder.setDefaultText(R.id.tvDz, item.getAddress(), "未填写地址");
                    holder.setDefaultText(R.id.tvBz, item.getSinceTime(), "未填写备注");
                    holder.setText(R.id.tvHm, item.getMobile());
                    holder.setText(R.id.tvZj, item.getAmount());
                    holder.setText(R.id.tvDdh, item.getId());
                    if (item.getOrdersDetailsList() != null) {
                        holder.setText(R.id.tvSps, item.getOrdersDetailsList().size());
                    }
                    ImageView ivTop = holder.getView(R.id.ivLeftTop);//订单类型 0自提1外卖2堂食
                    TextView tvDd = holder.getView(R.id.tvDd);
//                   状态 0：已下单 1：已接单，2，派送中 3：已成功，4：已取消
                    TextView tvJd = holder.getView(R.id.tvJd);//接单
                    TextView tvQx = holder.getView(R.id.tvQx);//取消订单
                    TextView tvQr = holder.getView(R.id.tvQr);//确认完成
                    ImageView ivZt = holder.getView(R.id.ivZt);//订单状态
                    TextView tvZf = holder.getView(R.id.tvZf);//支付类型3货到付款，2会员支付，1微信支付,4混合支付
                    TextView tvHd = holder.getView(R.id.tvHd);//货到付款
                    switch (item.getOrderType()) {
                        case 0:
                            ivTop.setImageResource(R.drawable.zs);
                            holder.setText(R.id.tvDd, "自提订单");
                            tvDd.setTextColor(getResources().getColor(R.color.zs));
                            holder.getView(R.id.llBz).setVisibility(View.VISIBLE);
                            holder.getView(R.id.llDz).setVisibility(View.GONE);
                            break;
                        case 1:
                            ivTop.setImageResource(R.drawable.cs);
                            holder.setText(R.id.tvDd, "外卖订单");
                            tvDd.setTextColor(getResources().getColor(R.color.bz));
                            holder.getView(R.id.llBz).setVisibility(View.GONE);
                            holder.getView(R.id.llDz).setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ivTop.setImageResource(R.drawable.cs);
                            holder.setText(R.id.tvDd, "堂食订单");
                            tvDd.setTextColor(getResources().getColor(R.color.bz));
                            break;
                    }
                    switch (item.getPayType()) {
                        case 1:
                            tvZf.setText("微信支付");
                            tvZf.setTextColor(getResources().getColor(R.color.ls));
                            break;
                        case 2:
                            tvZf.setText("会员支付");
                            tvZf.setTextColor(getResources().getColor(R.color.bz));
                            break;
                        case 3:
                            tvZf.setText("货到付款");
                            tvZf.setTextColor(getResources().getColor(R.color.lanse));
                            break;
                        case 4:
                            tvZf.setText("混合支付");
                            tvZf.setTextColor(getResources().getColor(R.color.bz));
                            break;
                        default:
                            tvZf.setText("未知支付类型");
                            tvZf.setTextColor(getResources().getColor(R.color.bz));
                            break;
                    }
                    //除了已完成，其他的都能退单
                    tvQx.setVisibility(View.VISIBLE);
                    tvQx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCancelOrder(position);
                        }
                    });
                    switch (item.getStatus()) {
                        case 0://已下单
                            if (item.getPayType() == 3) {//货到付款
                                tvHd.setVisibility(View.VISIBLE);
                                tvHd.setText("货到付款");
                                ivZt.setVisibility(View.GONE);
                                tvJd.setVisibility(View.GONE);
                                tvQr.setVisibility(View.VISIBLE);

                                tvQr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateOrderState(position, 3);
                                    }
                                });
                            } else {
                                tvQr.setVisibility(View.GONE);
                                if (item.getPayStatus() == 0) {
                                    tvHd.setText("未支付");
                                    tvHd.setVisibility(View.VISIBLE);
                                    ivZt.setVisibility(View.GONE);
                                    tvJd.setVisibility(View.GONE);
                                    tvQx.setVisibility(View.GONE);
                                } else if (item.getPayStatus() == 1) {
                                    tvHd.setVisibility(View.GONE);
                                    ivZt.setVisibility(View.VISIBLE);
                                    ivZt.setImageResource(R.drawable.yizhifu);

                                    tvJd.setVisibility(View.VISIBLE);
                                    tvJd.setText("接单");
                                    tvJd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            updateOrderState(position, 1);
                                        }
                                    });
                                } else if (item.getPayStatus() == 3) {
                                    tvHd.setVisibility(View.VISIBLE);
                                    tvHd.setText("已退单");
                                    ivZt.setVisibility(View.GONE);
                                    tvQx.setVisibility(View.GONE);
                                }
                            }
                            break;
                        case 1://已接单
                            tvHd.setVisibility(View.GONE);
                            ivZt.setImageResource(R.drawable.yijiedan);
                            ivZt.setVisibility(View.VISIBLE);
                            tvJd.setText("派送");
                            tvJd.setVisibility(View.VISIBLE);
                            tvQr.setVisibility(View.GONE);

                            tvJd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateOrderState(position, 2);
                                }
                            });
                            break;
                        case 2://派送中
                            tvHd.setVisibility(View.GONE);
                            ivZt.setImageResource(R.drawable.paisongzhong);
                            ivZt.setVisibility(View.VISIBLE);
                            tvQr.setVisibility(View.VISIBLE);
                            tvJd.setVisibility(View.GONE);

                            tvQr.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updateOrderState(position, 3);
                                }
                            });
                            break;
                        case 4://已退单
                            tvHd.setVisibility(View.VISIBLE);
                            tvHd.setText("已退单");
                            ivZt.setVisibility(View.GONE);
                            tvQx.setVisibility(View.GONE);
                            tvQr.setVisibility(View.GONE);
                            tvJd.setVisibility(View.GONE);
                            break;
                        default://已完成订单,不能进行任何操作
                            tvHd.setVisibility(View.GONE);
                            ivZt.setImageResource(R.drawable.yiwancheng);
                            ivZt.setVisibility(View.VISIBLE);
                            tvQx.setVisibility(View.GONE);
                            tvQr.setVisibility(View.GONE);
                            tvJd.setVisibility(View.GONE);
                            break;
                    }
                    ListView lv = holder.getView(R.id.lv);
                    if (item != null && item.getOrdersDetailsList() != null && item.getOrdersDetailsList().size() > 0) {
                        LvAdapter<OrdersDetailsList> lvAdapter = new LvAdapter<OrdersDetailsList>(getActivity(), item.getOrdersDetailsList(), R.layout.lv_item_pad_dd_lv) {
                            @Override
                            public void convert(LvViewHolder helper, OrdersDetailsList item, int position) {
                                if (item.getOrdersId() == 108639) {
                                    System.out.println(page.list.toString());
                                }
                                helper.setText(R.id.tv1, item.getProductName());
                                helper.setText(R.id.tv2, "x" + item.getCount());
                                helper.setText(R.id.tv3, item.getPrice());
                            }
                        };
                        lv.setAdapter(lvAdapter);
                    }
                }
            };
        }
        pgv.setAdapter(page.adapter);
        pgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = newIntent(PadOrderDetailsAc.class);
                intent.putExtra("data", showList.get(i));
                startActivity(intent);
            }
        });
    }

    /**
     * 修改订单状态
     * 0：已下单 1：已接单，2，派送中 3：已成功，4：已退单
     *
     * @param index
     * @param state
     */
    void updateOrderState(final int index, final int state) {
        progress("修改中...");
        final Orders orders = showList.get(index);
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.postHeaders(Api.UPDATE_ORDER_STATE + orders.getId() + "/" + state, volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                Card obj = fromJson(o, Card.class);
                if (obj.isSuccess()) {
                    if (state == 3) {//确认完成的时候需要打印小票
                        Orders orders1 = showList.get(index);
                        if (obj.data != null) {
                            orders1.setCard(obj.data);
                        }
                        getActivity().sendBroadcast(new Intent(App.BLUEBOOTH_PRINT).putExtra("data", orders1));
                    }
                    showList.get(index).setStatus(state);
//                    orders.setStatus(state);
                    if (page.adapter != null) {
                        page.adapter.notifyDataSetChanged();
                    }
                } else {
                    toask("修改失败" + obj.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
            }
        });

    }

    /**
     * start（0第一次加载，1下拉刷新，2上拉加载更多）
     * isOrder true查询订单，false查询退单
     *
     * @param start
     */
    private void getData(final int start) {
        if (start == 0) {
            page.firstPage();
            progress("加载中...");
            stateList.clear();
            stateList.add(new OrderState("所有订单", true));
            stateList.add(new OrderState("新订单", false));
            stateList.add(new OrderState("已接单", false));
            stateList.add(new OrderState("派送中", false));
            stateList.add(new OrderState("已完成", false));
        }
        VolleyParam volleyParam = App.getVolleyParam();
        String url = "";
        if (getOrderState() == -1) {
            url = Api.GET_ORDER + page.pageIndex + "/" + page.limit;
        } else {
            url = Api.GET_CANCEL_ORDER + page.pageIndex + "/" + page.limit + "/" + getOrderState();
        }
        VolleyRequest.postHeaders(url, volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Orders data = fromJson(o, Orders.class);
                hideProgress();
                if (data.isSuccess()) {
                    if (start == 1) {
                        prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else if (start == 2) {
                        prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if (page.adapter == null) {
                        initAdapter();
                    }
                    if (page.pageIndex == 0) {
                        page.list.clear();
                        showList.clear();
                        page.adapter.notifyDataSetChanged();
                    }
                    if (data.data.size() < 1) {
                        System.out.println("");
                        toask("暂无订单信息");
                        return;
                    }
                    page.list.addAll(data.data);
                    showList.addAll(data.data);
                    getOrdersDetailByID(0);
                    if (StringUtils.noEmpty(page.adapter)) {
                        page.adapter.notifyDataSetChanged();
                    }
                } else {
                    if (App.getIsDebug()) {
                        toask("加载数据失败," + data.getMessage());
                    } else {
                        toask("加载数据失败");
                    }
                    if (start == 1) {
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                    } else if (start == 2) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
                if (start == 1) {
                    prl.refreshFinish(PullToRefreshLayout.FAIL);
                } else if (start == 2) {
                    prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        });
        if (page.adapter != null) {
            page.adapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据订单id查询订单详情
     */
    private void getOrdersDetailByID(final int index) {
        if (index > 2) {//最多轮循查3次
            return;
        }
        Log.i(TAG, "轮循次数: " + index);
        for (int i = 0; i < showList.size(); i++) {
//            Orders order = showList.get(i);
            if (showList.get(i).getOrdersDetailsList() != null && showList.get(i).getOrdersDetailsList().size() > 0) {//若该订单已查有详情，则无需在查
                continue;
            }
            final int postion = i;
            VolleyParam volleyParam = App.getVolleyParam();
            VolleyRequest.getHeaders(Api.GET_ORDER_DETAIL + showList.get(i).getId(), volleyParam, new Response.Listener() {
                @Override
                public void onResponse(Object o) {
                    OrdersDetailsList obj = fromJson(o, OrdersDetailsList.class);
                    if (obj.isSuccess()) {
                        if (obj.data.size() > 0) {
                            showList.get(postion).setOrdersDetailsList(obj.data);
                            if (page.adapter != null) {//查询一次就刷新一次适配器
                                page.adapter.notifyDataSetChanged();
                            }
                        }
                        if (postion == showList.size() - 1) {//查到了最后一个订单详情时，统计有多少个订单的详情查到了
                            int isDetail = 0;//判断是否有订单详情
                            for (Orders orders : showList) {
                                if (orders.getOrdersDetailsList() != null && orders.getOrdersDetailsList().size() > 0) {
                                    isDetail++;
                                }
                            }
                            if (isDetail != (showList.size() - 1)) {//如果有订单详情未查到，则轮循查询最多3次
                                getOrdersDetailByID(index + 1);
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (postion == showList.size() - 1) {//查到了最后一个订单详情时，统计有多少个订单的详情查到了
                        int isDetail = 0;//判断是否有订单详情
                        for (Orders orders : showList) {
                            if (orders.getOrdersDetailsList() != null && orders.getOrdersDetailsList().size() > 0) {
                                isDetail++;
                            }
                        }
                        if (isDetail != (showList.size() - 1)) {//如果有订单详情未查到，则轮循查询
                            getOrdersDetailByID(index + 1);
                        }
                    }
                }
            });
        }
    }

    /**
     * 判断recyclerView是否滚到到了底部
     *
     * @param recyclerView
     * @return
     */
    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    getData(true, 1);
//                    prl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    break;
//                case 2:
//                    getData(false, 2);
//                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                    break;
//            }
//        }
//    };

    private void initView() {
        selectTdTv(tv1);
        selectTv(tv6);
//        iv7.setVisibility(View.VISIBLE);
        tvDd.setSelected(true);
//        pgv.setItemAnimator(new DefaultItemAnimator());
//        pgv.addItemDecoration(new SpaceItemDecoration(10));//设置分割线
//        pgv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        pgv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为2列垂直排布
//        adapter = new OrderAdapter(getActivity(), page.list);
//        pgv.setAdapter(adapter);
        prl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                page.firstPage();
                getData(1);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                page.nextPage();
                getData(2);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(mMessageReceiver);//解除广播接收器
    }

    @OnClick({R.id.tv4, R.id.tv5, R.id.tvTd, R.id.flTd, R.id.tv1, R.id.tv2, R.id.tv3, R.id.flDd, R.id.rlDd,
            R.id.tv6, R.id.fl7, R.id.tv8, R.id.tv9, R.id.tv10, R.id.tv11})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlDd:
                if (tvDd.isSelected()) {
                    showDd(view);
                }
                tvDd.setSelected(true);
                flDd.setVisibility(View.VISIBLE);
                tvTd.setSelected(false);
                flTd.setVisibility(View.GONE);
                break;
            case R.id.tvTd:
                toask("退单");
                tvTd.setSelected(true);
                flTd.setVisibility(View.VISIBLE);
                tvDd.setSelected(false);
                flDd.setVisibility(View.GONE);
                break;
            case R.id.flTd:
                break;
            case R.id.tv1:
                if (page != null && page.adapter != null && temporaryList != null) {
                    page.adapter.showNewList(temporaryList);
                }
                selectDdTv(tv1);
                break;
            case R.id.tv2:
                showOrderTypeList(0);
                selectDdTv(tv2);
                break;
            case R.id.tv3:
                showOrderTypeList(1);
                selectDdTv(tv3);
                break;
            case R.id.tv4:
                selectTdTv(tv4);
                break;
            case R.id.tv5:
                selectTdTv(tv5);
                break;
            case R.id.flDd:
                break;
            case R.id.rv:
                break;
            case R.id.tv6://所有订单
                selectTv(tv6);
//                page.firstPage();
//                getData(0);
//                page.adapter.showNewList(page.list);
                break;
//            case R.id.iv7:
//                selectTv(tv6);
//                break;
//            case R.id.tv7:
//                break;
            case R.id.fl7:
                selectTv(fl7);
                iv7.setVisibility(View.GONE);
//                page.firstPage();
//                getData(0);
//                showOrderState(0);
                break;
            case R.id.tv8:
//                showOrderState(1);
                selectTv(tv8);
                page.firstPage();
                getData(0);
                break;
            case R.id.tv9:
//                showOrderState(2);
                selectTv(tv9);
//                page.firstPage();
//                getData(0);
                break;
            case R.id.tv10:
//                showOrderState(3);
                selectTv(tv10);
//                page.firstPage();
//                getData(0);
                break;
            case R.id.tv11:
//                page.firstPage();
//                getCancelOrders(0);
                selectTv(tv11);
//                page.firstPage();
//                getData(0);
                break;
        }
    }

    private void selectTv(View view) {
        unSelectTv(tv6, fl7, tv8, tv9, tv10, tv11);
        view.setSelected(true);
        getData(0);
        if (view.getId() == tv11.getId()) {
            flDd.setVisibility(View.GONE);
        } else {
            flDd.setVisibility(View.VISIBLE);
            flTd.setVisibility(View.GONE);
        }
    }

    void unSelectTv(View... views) {
        for (View view : views) {
            view.setSelected(false);
        }
    }

    /**
     * 显示相应的订单类型列表
     *
     * @param state
     */
    private void showOrderTypeList(int state) {
        temporaryList.clear();
        for (Orders orders : page.list) {
            if (orders.getOrderType() == state) {
                temporaryList.add(orders);
            }
        }
        if (page != null && page.adapter != null && temporaryList != null) {
            page.adapter.showNewList(temporaryList);
        }
    }

    private void showDd(View view) {
        ivJt.setSelected(true);
        if (popupWindow == null) {
            WindowManager windowManager = (WindowManager) getActivity()
                    .getSystemService(Context.WINDOW_SERVICE);
            popupWindowView = LayoutInflater.from(getActivity()).inflate(R.layout.pw_pad_order, null);
            lv = (ListView) popupWindowView.findViewById(R.id.lv);
            popupWindow = new PopupWindow(popupWindowView, windowManager.getDefaultDisplay()
                    .getWidth() / 5,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // 使其获取焦点
            popupWindow.setFocusable(true);
            // 设置允许在外点击消失
            popupWindow.setOutsideTouchable(true);
            // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            stateAdapter = new LvAdapter<OrderState>(getActivity(), stateList, R.layout.lv_item_pad_dd_pw_lv) {
                @Override
                public void convert(LvViewHolder helper, OrderState item, int position) {
                    System.out.println(stateList.size());
                    System.out.println(stateList);
                    TextView tv = helper.getView(R.id.tv);
                    if (item.isSelect) {
                        tv.setSelected(true);
                    } else {
                        tv.setSelected(false);
                    }
                    tv.setText(item.getName());
                }
            };
            lv.setAdapter(stateAdapter);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                    ivJt.setSelected(false);
                }
            });
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                toask(list.get(position));
                List<Orders> list = new ArrayList<Orders>();
                for (OrderState orderState : stateList) {
                    orderState.isSelect = false;
                }
                stateList.get(position).isSelect = true;
                stateAdapter.notifyDataSetChanged();
                switch (position) {
                    case 0://所有订单
                        page.adapter.showNewList(page.list);
                        break;
                    case 1:
                        for (Orders orders : page.list) {
                            if (orders.getStatus() == 0) {
                                list.add(orders);
                            }
                        }
                        page.adapter.showNewList(list);
                        break;
                    case 2:
                        for (Orders orders : page.list) {
                            if (orders.getStatus() == 1) {
                                list.add(orders);
                            }
                        }
                        page.adapter.showNewList(list);
                        break;
                    case 3:
                        for (Orders orders : page.list) {
                            if (orders.getStatus() == 2) {
                                list.add(orders);
                            }
                        }
                        page.adapter.showNewList(list);
                        break;
                    case 4:
                        for (Orders orders : page.list) {
                            if (orders.getStatus() == 3) {
                                list.add(orders);
                            }
                        }
                        page.adapter.showNewList(list);
                        break;
                }
                popupWindow.dismiss();
            }
        });
        backgroundAlpha(0.5f);
//        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
        popupWindow.showAsDropDown(view);
//        popupWindow.showAsDropDown(view, 0, 0);
    }

    /**
     * 显示相应订单状态列表
     *
     * @param state
     */
    void showOrderState(int state) {
        List<Orders> list = new ArrayList<Orders>();
        for (Orders orders : page.list) {
            if (orders.getStatus() == state) {
                list.add(orders);
            }
        }
        page.adapter.showNewList(list);
    }

    void selectDdTv(View view) {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        view.setSelected(true);
    }

    /**
     * 取消订单
     *
     * @param i 位置
     */
    private void cancelOrderById(int i) {
        progress("操作中...");
        final int postion = i;
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.getHeaders(Api.CANCEL_ORDER + page.list.get(postion).getId(), volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                BaseResponse data = fromJson(o, BaseResponse.class);
                if (data.isSuccess()) {
                    if (data.isSuccess()) {
                        toask("取消订单成功");
                        page.adapter.remove(postion);
                    } else {
                        toask("取消订单失败," + data.getMessage());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
            }
        });

    }

    void selectTdTv(View view) {
        tv4.setSelected(false);
        tv5.setSelected(false);
        view.setSelected(true);
    }

    /**
     * 注册极光推送接听器
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MainActivity.MESSAGE_RECEIVED_ACTION);
        filter.addAction(MainActivity.MESSAGE_REFRESH);
        filter.addAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MainActivity.MESSAGE_REFRESH.equals(intent.getAction())) {
                getData(0);
            }
            //接受到推送订单或者用户点击了通知栏都显示订单页面
            if (MainActivity.MESSAGE_RECEIVED_ACTION.equals(intent.getAction()) || JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                String messge = intent.getStringExtra(KEY_MESSAGE);
//                String extras = intent.getStringExtra(KEY_EXTRAS);
//                StringBuilder showMsg = new StringBuilder();
//                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
//                if (!ExampleUtil.isEmpty(extras)) {
//                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
//                }
//                toask(messge + "\n" + extras);
                Push push = (Push) intent.getSerializableExtra("push");
                if (StringUtils.noEmpty(push, push.getALERT())) {
                    getOrderId(push.getALERT());
                }
//                if (StringUtils.noEmpty(push.getNOTIFICATION_ID())) {
//                    try {
//                        JPushInterface.clearNotificationById(App.getInstance(), push.getNOTIFICATION_ID());//清除通知栏信息
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        }
    }

    public void getOrderId(String id) {
        if (!fl7.isSelected()) {//未选中新订单按钮
            iv7.setVisibility(View.VISIBLE);
        }
        progress("加载中...");
        VolleyRequest.getHeaders(Api.GET_ORDER_BY_ID + id, App.getVolleyParam(), new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                Orders obj = fromJson(o, Orders.class);
                if (obj.isSuccess()) {
//                    page.list.add(0, obj.data.get(0));
                    page.adapter.add(0, obj.data.get(0));
                } else {
                    if (App.getIsDebug()) {
                        toask("查询订单失败," + obj.getMessage());
                    } else {
                        toask("查询订单失败");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
            }
        });
//        App.initAsyncHttpRequestHeader();
//        AsyncHttpRequest.get(Api.GET_ORDER_BY_ID + id, new AsyncHttpResponseHandler<Orders>() {
//            @Override
//            public void onSuccess(int statusCode, Orders obj, Header[] headers) {
//                hideProgress();
//                if (obj.isSuccess()) {
//                    page.list.add(0, obj.data.get(0));
//                    page.adapter.add(0, obj.data.get(0));
//                    if (!fl7.isSelected()) {//未选中新订单按钮
//                        iv7.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    toask("查询订单失败,请稍后刷新订单列表");
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
//                hideProgress();
//                toask("连接错误");
//            }
//        });
//        VolleyParam volleyParam = App.getVolleyParam();
//        VolleyRequest.getHeaders(Api.GET_ORDER_BY_ID + id, volleyParam, new Response.Listener() {
//            @Override
//            public void onResponse(Object o) {
//                hideProgress();
//                Order data = fromJson(o, Order.class);
//                if (data.isSuccess()) {
//                    page.list.add(0, new Orders(data.data));
//                    page.adapter.add(0, new Orders(data.data));
//                } else {
//                    toask("查询订单失败,请稍后刷新订单列表");
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                volleyError(volleyError);
//            }
//        });

    }

    /**
     * 获取退单列表
     */
    void getCancelOrders(final int start) {
        progress("加载中...");
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.getHeaders(Api.GET_CANCEL_ORDER + page.pageIndex + "/" + page.limit, volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                Orders data = fromJson(o, Orders.class);
                hideProgress();
                if (data.isSuccess()) {
                    if (page.adapter == null) {
                        initAdapter();
                    }
                    if (data.data.size() < 1) {
                        toask("暂无订单信息");
                        page.adapter.clear();
                        return;
                    }
                    if (page.pageIndex == 0) {
                        page.list.clear();
                        showList.clear();
                    }
                    page.list.addAll(data.data);
                    showList.addAll(data.data);
                    getOrdersDetailByID(0);
                    if (start == 1) {
                        prl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } else if (start == 2) {
                        prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if (StringUtils.noEmpty(page.adapter)) {
                        page.adapter.notifyDataSetChanged();
                    }
                } else {
                    if (start == 1) {
                        prl.refreshFinish(PullToRefreshLayout.FAIL);
                    } else if (start == 2) {
                        prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
                if (start == 1) {
                    prl.refreshFinish(PullToRefreshLayout.FAIL);
                } else if (start == 2) {
                    prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        });
    }

    /**
     * 返回选中的订单状态
     *
     * @return -1所有订单，0新订单，1已接单，2派送中，3已完成，4已退单
     */
    public int getOrderState() {
        int result = -1;
        if (tv6.isSelected()) {
            result = -1;
        }
        if (tv7.isSelected()) {
            result = 0;
        }
        if (tv8.isSelected()) {
            result = 1;
        }
        if (tv9.isSelected()) {
            result = 2;
        }
        if (tv10.isSelected()) {
            result = 3;
        }
        if (tv11.isSelected()) {
            result = 4;
        }
        return result;
    }

}
