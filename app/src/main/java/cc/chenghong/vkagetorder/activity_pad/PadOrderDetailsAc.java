package cc.chenghong.vkagetorder.activity_pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.BlueActivity;
import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.api.Api;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Card;
import cc.chenghong.vkagetorder.bean.Orders;
import cc.chenghong.vkagetorder.bean.OrdersDetailsList;
import cc.chenghong.vkagetorder.dialog.AskDialog;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.request.VolleyRequest;
import cc.chenghong.vkagetorder.response.BaseResponse;
import cc.chenghong.vkagetorder.util.StringUtils;
import cc.chenghong.vkagetorder.view.JustifyTextView;

/**
 * pad订单详情页面
 * 何成龙
 * 160801
 */
public class PadOrderDetailsAc extends BlueActivity {
    Orders item;
    LvAdapter<OrdersDetailsList> adapter;
//    @Bind(R.id.llBack)
//    LinearLayout llBack;
//    @Bind(R.id.tvBack)
//    TextView tvBack;
    @Bind(R.id.tvDd)
    TextView tvDd;
    @Bind(R.id.ivZt)
    ImageView ivZt;
    @Bind(R.id.tvZt)
    TextView tvZt;
    @Bind(R.id.tvLx)
    TextView tvLx;
    @Bind(R.id.tvSp)
    TextView tvSp;
    @Bind(R.id.tvJe)
    TextView tvJe;
    @Bind(R.id.tvXm)
    TextView tvXm;
    @Bind(R.id.tvHm)
    TextView tvHm;
    @Bind(R.id.tvBz)
    JustifyTextView tvBz;
    @Bind(R.id.llDz)
    LinearLayout llDz;
    @Bind(R.id.tvDz)
    TextView tvDz;
    @Bind(R.id.tvSj)
    TextView tvSj;
    @Bind(R.id.tvDdh)
    TextView tvDdh;
    @Bind(R.id.llBz)
    LinearLayout llBz;
    @Bind(R.id.tvQx)
    TextView tvQx;
    @Bind(R.id.tvJd)
    TextView tvJd;
    @Bind(R.id.tvQr)
    TextView tvQr;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.ll)
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView(R.layout.activity_pad_order_details);
        setTitleName("订单详情");
        vBar.setBackgroundResource(R.color.blackVka);
        ButterKnife.bind(this);
        item = (Orders) getIntent().getSerializableExtra("data");
        logI(item.toString());
        initView();
        if (item != null) {
            getOrderDetail();
        }
    }

    int position = 0;

    /**
     * 修改订单状态
     * 0：已下单 1：已接单，2，派送中 3：已成功，4：已退单
     *
     * @param index
     * @param state
     */
    void updateOrderState(final int index, final int state) {
        progress("修改中...");
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.postHeaders(Api.UPDATE_ORDER_STATE + item.getId() + "/" + state, volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                Card obj = fromJson(o, Card.class);
                if (obj.isSuccess()) {
                    item.setStatus(state);
                    initView();
                    getActivity().sendBroadcast(new Intent(MainActivity.MESSAGE_REFRESH));//发送刷新广播
                    if (state == 3) {
                        if (obj.data != null) {
                            item.setCard(obj.data);
                        }
                        getActivity().sendBroadcast(new Intent(App.BLUEBOOTH_PRINT).putExtra("data", item));//发送蓝牙打印广播
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

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    private void initView() {
        if (cc.chenghong.vkagetorder.util.StringUtils.noEmpty(item.getAddress())) {
            tvDz.setText(item.getAddress());
        } else {
            tvDz.setText("未填写地址");
        }
        if (cc.chenghong.vkagetorder.util.StringUtils.noEmpty(item.getSinceTime())) {
            tvBz.setText(item.getSinceTime());
        } else {
            tvBz.setText("未填写备注");
        }
        switch (item.getOrderType()) {
            case 0:
                tvDd.setText("自提订单");
                tvDd.setTextColor(getResources().getColor(R.color.zs));
                llDz.setVisibility(View.GONE);
                llBz.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvDd.setText("外卖订单");
                tvDd.setTextColor(getResources().getColor(R.color.bz));
                llDz.setVisibility(View.VISIBLE);
                llBz.setVisibility(View.GONE);
                break;
            case 2:
                tvDd.setText("堂食订单");
                tvDd.setTextColor(getResources().getColor(R.color.bz));
                llDz.setVisibility(View.VISIBLE);
                llBz.setVisibility(View.GONE);
                break;
        }
        if (item.getOrdersDetailsList() != null) {
            tvSp.setText(item.getOrdersDetailsList().size() + "");
        }
        tvSj.setText(item.getCreated());
        if (item.getCardName() == null || item.getCardName().equals("")) {
            tvXm.setText("未填写姓名");
        } else {
            tvXm.setText(item.getCardName());
        }
        tvHm.setText(item.getMobile());

//        if (cc.chenghong.vkagetorder.util.StringUtils.noEmpty(item.get)) {
//            tvDz.setText(item.getAddress());
//        } else {
//            tvDz.setText("未填写地址");
//        }
        tvJe.setText("￥" + item.getAmount() + "");
        tvDdh.setText(item.getId() + "");

        switch (item.getPayType()) {
            case 1:
                tvLx.setText("微信支付");
                tvLx.setTextColor(getResources().getColor(R.color.ls));
                break;
            case 2:
                tvLx.setText("会员支付");
                tvLx.setTextColor(getResources().getColor(R.color.bz));
                break;
            case 3:
                tvLx.setText("货到付款");
                tvLx.setTextColor(getResources().getColor(R.color.lanse));
                break;
            case 4:
                tvLx.setText("混合支付");
                break;
            default:
                tvLx.setText("未知支付类型");
                break;
        }
        //除了已完成，其他的都能退单
        tvQx.setVisibility(View.VISIBLE);
        tvQx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelOrder();
            }
        });

        switch (item.getStatus()) {
            case 0://已下单
                if (item.getPayType() == 3) {//货到付款
                    tvZt.setVisibility(View.VISIBLE);
                    tvZt.setText("货到付款");
                    tvZt.setBackgroundResource(R.drawable.shape_round_zs_white);
                    tvZt.setTextColor(getResources().getColor(R.color.zs));
                    tvJd.setVisibility(View.GONE);
                    tvQr.setVisibility(View.VISIBLE);

                    tvQr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateOrderState(0, 3);
                        }
                    });
                } else {
                    tvQr.setVisibility(View.GONE);
                    if (item.getPayStatus() == 0) {
                        tvZt.setVisibility(View.VISIBLE);
                        tvZt.setText("未支付");
                        tvZt.setTextColor(getResources().getColor(R.color.bz));
                        tvZt.setBackgroundResource(R.drawable.shape_round_bz_white);
//                        ivZt.setVisibility(View.GONE);
                        tvJd.setVisibility(View.GONE);
                        tvQx.setVisibility(View.GONE);
                    } else if (item.getPayStatus() == 1) {
                        tvZt.setVisibility(View.VISIBLE);
                        tvZt.setText("已支付");
                        tvZt.setTextColor(getResources().getColor(R.color.bz));
                        tvZt.setBackgroundResource(R.drawable.shape_round_bz_white);
                        tvJd.setVisibility(View.VISIBLE);
                        tvJd.setText("接单");
                        tvJd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateOrderState(0, 1);
                            }
                        });
                    } else if (item.getPayStatus() == 3) {
                        tvZt.setText("已退单");
                        tvQx.setVisibility(View.GONE);
                    }
                }
                break;
            case 1://已接单
                tvZt.setText("已接单");
                tvZt.setVisibility(View.VISIBLE);
                tvZt.setTextColor(getResources().getColor(R.color.zs));
                tvZt.setBackgroundResource(R.drawable.shape_round_zs_white);

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
                tvZt.setVisibility(View.VISIBLE);
                tvZt.setText("派送中");
                tvZt.setTextColor(getResources().getColor(R.color.ls));
                tvZt.setBackgroundResource(R.drawable.shape_round_ls_white);
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
                tvZt.setVisibility(View.VISIBLE);
                tvZt.setText("已退单");
                tvZt.setTextColor(getResources().getColor(R.color.bz));
                tvZt.setBackgroundResource(R.drawable.shape_round_bz_white);

                tvQx.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvJd.setVisibility(View.GONE);
                break;
            default://已完成订单,不能进行任何操作
                tvZt.setVisibility(View.GONE);
                tvZt.setTextColor(getResources().getColor(R.color.bz));
                tvZt.setBackgroundResource(R.drawable.shape_round_bz_white);
                ivZt.setImageResource(R.drawable.yiwancheng);
                ivZt.setVisibility(View.VISIBLE);
                tvQx.setVisibility(View.GONE);
                tvQr.setVisibility(View.GONE);
                tvJd.setVisibility(View.GONE);
                break;
        }

        ll.setVisibility(View.VISIBLE);
    }

    AskDialog cancelOrder;

    public void showCancelOrder() {
        if (cancelOrder == null) {
            cancelOrder = new AskDialog(this, "是否取消该订单？", "取消后将不可恢复", "是", "否");
            cancelOrder.setTvNoOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOrder.dismiss();
                }
            });
        }
        cancelOrder.setAskDialogWidth(getWidth() / 2);
        cancelOrder.setTvYesOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderById();
                cancelOrder.dismiss();
            }
        });
        cancelOrder.show();
    }

    /**
     * 取消订单
     */
    private void cancelOrderById() {
        progress("操作中...");
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.getHeaders(Api.CANCEL_ORDER + item.getId(), volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                BaseResponse data = fromJson(o, BaseResponse.class);
                if (data.isSuccess()) {
                    toask("取消订单成功");
                    item.setStatus(4);
                    getActivity().sendBroadcast(new Intent(MainActivity.MESSAGE_REFRESH));//发送刷新广播
                    initView();
                } else {
                    toask("取消订单失败," + data.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError(volleyError);
            }
        });

    }

    public void getOrderDetail() {
//        progress("加载中...");
        VolleyParam volleyParam = App.getVolleyParam();
        VolleyRequest.getHeaders(Api.GET_ORDER_DETAIL + item.getId(), volleyParam, new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                hideProgress();
                OrdersDetailsList obj = fromJson(o, OrdersDetailsList.class);
                if (obj.data.size() > 0) {
                    item.setOrdersDetailsList(obj.data);
                    if (adapter == null) {
                        initView();
                        initAdapter();
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
        if (adapter == null && item.getOrdersDetailsList() != null) {
            adapter = new LvAdapter<OrdersDetailsList>(this, item.getOrdersDetailsList(), R.layout.lv_item_pad_order_details) {
                @Override
                public void convert(LvViewHolder helper, OrdersDetailsList item, int position) {
                    helper.setText(R.id.tv1, item.getProductName());
                    helper.setText(R.id.tv2, "x1");
                    helper.setText(R.id.tv3, "￥" + item.getPrice());
                    helper.getView(R.id.tv4).setVisibility(View.GONE);
                    if (StringUtils.noEmpty(item.getAttributeNames())) {
                        helper.setText(R.id.tv4, "口味：" + item.getAttributeNames() + "");
                        helper.getView(R.id.tv4).setVisibility(View.VISIBLE);
                    } else {
                        helper.goneView(R.id.tv4);
                    }
                }
            };
            lv.setAdapter(adapter);
        }
    }

    @OnClick(R.id.llBack)
    public void click(View v) {
        switch (v.getId()) {
            case R.id.llBack:
                finish();
                break;
        }
    }
}
