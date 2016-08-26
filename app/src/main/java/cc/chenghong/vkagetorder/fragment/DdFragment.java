package cc.chenghong.vkagetorder.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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
import cc.chenghong.vkagetorder.activity.OrderDetailsAc;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.api.Api;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Card;
import cc.chenghong.vkagetorder.bean.DdTopTv;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;
import cc.chenghong.vkagetorder.bean.Page;
import cc.chenghong.vkagetorder.bean.Push;
import cc.chenghong.vkagetorder.dialog.AskDialog;
import cc.chenghong.vkagetorder.layout.RefreshFrameLayout;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.pullable.PullToRefreshLayout;
import cc.chenghong.vkagetorder.pullable.PullableListView;
import cc.chenghong.vkagetorder.receiver.JpushReceiver;
import cc.chenghong.vkagetorder.request.VolleyRequest;
import cc.chenghong.vkagetorder.response.BaseResponse;
import cc.chenghong.vkagetorder.util.StringUtils;
import cc.chenghong.vkagetorder.view.HorizontialListView;

/**
 * 订单处理
 * Created by hcl on 2016/6/13.
 */
public class DdFragment extends BaseFragment {

    @Bind(R.id.tvTop1)
    TextView tvTop1;
    @Bind(R.id.rlTop1)
    RelativeLayout rlTop1;
    @Bind(R.id.tvTop2)
    TextView tvTop2;
    @Bind(R.id.rlTop2)
    RelativeLayout rlTop2;
    @Bind(R.id.tvTop3)
    TextView tvTop3;
    @Bind(R.id.rlTop3)
    RelativeLayout rlTop3;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.ivTop1)
    ImageView ivTop1;
    @Bind(R.id.ivTop2)
    ImageView ivTop2;
    @Bind(R.id.ivTop3)
    ImageView ivTop3;
    @Bind(R.id.rfl)
    RefreshFrameLayout rfl;
    @Bind(R.id.prl)
    PullToRefreshLayout prl;
    @Bind(R.id.plv)
    PullableListView plv;
    @Bind(R.id.gv)
    GridView gv;
    @Bind(R.id.hlv)
    HorizontialListView hlv;

    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.tv6)
    TextView tv6;

    Page<Orders> page = new Page<>(5);//总列表
    List<Orders> showList = new ArrayList<>();//显示的列表
    List<Orders> temporaryList = new ArrayList<>();//临时列表，用来切换需要显示的订单
    Page<DdTopTv> pageGv = new Page<>();
    JpushReceiver mMessageReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_dd, container,
                false);
        ButterKnife.bind(this, view);
        initGv();
        getData(0);
        initAdapter();
        registerMessageReceiver();
//        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_item);
//        /**布局动画加载器，构建每个子view的动画效果*/
//        LayoutAnimationController lac = new LayoutAnimationController(animation);
//        lac.setDelay(0.5f);//设置子项动画时间间隔
//        lac.setInterpolator(new AccelerateInterpolator());//设置加速
//        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);//设置是正序(有三个值noraml(正序)reverse(逆序)random(随机) )
//        lv.setLayoutAnimation(lac);//设置给ListView
//        lv.startLayoutAnimation();//启动
        return view;
    }

    public void getOrderId(String id) {
//        if (!fl7.isSelected()) {//未选中新订单按钮
//            iv7.setVisibility(View.VISIBLE);
//        }
        progress("加载中...");
        VolleyRequest.getHeaders(Api.GET_ORDER_BY_ID + id, App.getVolleyParam(), new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                Orders obj = fromJson(o, Orders.class);
                if (obj.isSuccess()) {
                    page.list.add(0, obj.data.get(0));
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

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    isNewOrder(true);
                    Push push = (Push) msg.obj;
                    getOrderId(push.getALERT());
                    break;
                case 329:
                    getData(0);
                    break;
            }
        }
    };

    private void isNewOrder(boolean b) {
        if (b) {
            pageGv.list.get(1).isNew = true;
        } else {
            pageGv.list.get(1).isNew = false;
        }
        pageGv.adapter.notifyDataSetChanged();
    }


    /**
     * 注册极光推送接听器
     */
    public void registerMessageReceiver() {
        mMessageReceiver = new JpushReceiver(handler);
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MainActivity.MESSAGE_RECEIVED_ACTION);
        filter.addAction(MainActivity.MESSAGE_REFRESH);
        getActivity().registerReceiver(mMessageReceiver, filter);
    }

    private void initGv() {
        pageGv.list.add(new DdTopTv("所有订单"));
        pageGv.list.add(new DdTopTv("新订单"));
        pageGv.list.add(new DdTopTv("已接单"));
        pageGv.list.add(new DdTopTv("派送中"));
        pageGv.list.add(new DdTopTv("已完成"));
        pageGv.list.add(new DdTopTv("退单"));
        pageGv.adapter = new LvAdapter<DdTopTv>(getActivity(), pageGv.list, R.layout.gv_item_dd) {
            @Override
            public void convert(LvViewHolder helper, DdTopTv item, int position) {
                TextView tv = helper.getView(R.id.tv);
                ImageView iv = helper.getView(R.id.iv);
                helper.setText(R.id.tv, item.name);
                tv.setSelected(item.isSelect);
                if (item.isNew) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.GONE);
                }
                if (position == pageGv.list.size() - 1) {
                    helper.getView(R.id.v).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.v).setVisibility(View.VISIBLE);
                }
            }
        };
        pageGv.list.get(0).isSelect = true;
        hlv.setAdapter(pageGv.adapter);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectState(i);
                getData(0);
                if (i == 1) {
                    isNewOrder(false);
                }
            }
        });
//        gv.setAdapter(pageGv.adapter);
    }

    /**
     * 获取当前选择的订单状态
     *
     * @return
     */
    private int getOrderState() {
        int state = -1;
        for (int i1 = 0; i1 < pageGv.list.size(); i1++) {
            if (pageGv.list.get(i1).isSelect) {
                if (i1 == 0) {
                    state = -1;
                } else {
                    state = i1 - 1;
                }
            }
        }
        return state;
    }

    /**
     * 设置选中的状态
     *
     * @return
     */
    private void setSelectState(int i) {
        for (DdTopTv ddTopTv : pageGv.list) {
            ddTopTv.isSelect = false;
        }
        pageGv.list.get(i).isSelect = true;
        pageGv.adapter.notifyDataSetChanged();
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
        if (showList != null) {
            for (int i = 0; i < showList.size(); i++) {
                if (showList.get(i).getOrdersDetailsList() != null && showList.get(i).getOrdersDetailsList().size() > 0) {//若该订单已查有详情，则无需在查
                    continue;
                }
                final int postion1 = i;
                VolleyParam volleyParam = App.getVolleyParam();
                VolleyRequest.getHeaders(Api.GET_ORDER_DETAIL + showList.get(i).getId(), volleyParam, new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                        OrdersDetailsList obj = fromJson(o, OrdersDetailsList.class);
                        if (obj.isSuccess()) {
                            if (obj.data.size() > 0) {
                                showList.get(postion1).setOrdersDetailsList(obj.data);
                                if (page.adapter != null) {//查询一次就刷新一次适配器
                                    page.adapter.notifyDataSetChanged();
                                }
                            }
                            if (postion1 == showList.size() - 1) {//查到了最后一个订单详情时，统计有多少个订单的详情查到了
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
                        if (postion1 == showList.size() - 1) {//查到了最后一个订单详情时，统计有多少个订单的详情查到了
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
    }


    private void getData() {
        rfl.openPullUp();
        rfl.openPullDown();
        rfl.setListViewScrollListener(lv);
        rfl.addOnSnapListener(new RefreshFrameLayout.OnSnapListener() {
            @Override
            public void onSnapToTop(int distance) {
                getData(1);
            }

            @Override
            public void onSnapToBottom(int distance) {
                getData(2);
            }
        });
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

    AskDialog cancelOrder = null;

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

    private void initAdapter() {
        if (page.adapter == null) {
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
        plv.setAdapter(page.adapter);
        plv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), OrderDetailsAc.class);
                intent.putExtra("data", showList.get(i));
                startActivity(intent);
//                setSelectState(i);
//                getData(0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (mMessageReceiver != null) {
            getActivity().unregisterReceiver(mMessageReceiver);
        }
    }

    @OnClick({R.id.rlTop1, R.id.rlTop2, R.id.rlTop3, R.id.tv1, R.id.tv2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlTop1:
                setSelect(rlTop1);
                break;
            case R.id.rlTop2:
                setSelect(rlTop2);
                break;
            case R.id.rlTop3:
                setSelect(rlTop3);
                break;
            case R.id.tv1:
                toask(1);
                break;
            case R.id.tv2:
                toask(2);
                break;
        }
        getData();
        page.adapter.notifyDataSetChanged();
    }

    void setSelect(View view) {
        rlTop1.setSelected(false);
        rlTop2.setSelected(false);
        rlTop3.setSelected(false);
        view.setSelected(true);
    }
}
