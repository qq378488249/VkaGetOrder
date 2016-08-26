package cc.chenghong.vkagetorder.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunmi.controller.ICallback;
import com.sunmi.impl.V1Printer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.adapter.RvAdapter;
import cc.chenghong.vkagetorder.adapter.RvViewHolder;
import cc.chenghong.vkagetorder.adapter.StarAdapter;
import cc.chenghong.vkagetorder.bean.Star;
import cc.chenghong.vkagetorder.decoration.SpaceItemDecoration;
import cc.chenghong.vkagetorder.view.CircleImageView;

/**
 * 门店管理
 * Created by hcl on 2016/6/13.
 */
public class MdFragment extends BaseFragment {

    @Bind(R.id.iv)
    CircleImageView iv;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.iv3)
    ImageView iv3;
    @Bind(R.id.iv4)
    ImageView iv4;
    @Bind(R.id.iv5)
    ImageView iv5;
    @Bind(R.id.tvYy)
    TextView tvYy;
    @Bind(R.id.tvDd)
    TextView tvDd;
    @Bind(R.id.tvZt)
    TextView tvZt;
    @Bind(R.id.llFather)
    LinearLayout ll;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    //    @Bind(R.id.bt)
//    Button bt;
//    @Bind(R.id.btRecharge)
//    Button btRecharge;
    @Bind(R.id.llYy)
    LinearLayout llYy;
    @Bind(R.id.rvStar)
    RecyclerView rv;

    private V1Printer printer;
    private ICallback callback;
    private List<Star> list = new ArrayList<>();
    private StarAdapter adapter;
    private RvAdapter<Star> rvAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_md, container,
                false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        setViewWidth(ll,getScreenWidth());
        ViewTreeObserver vto = tvZt.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {//容器的宽度和高度确定后会调用的方法
                setViewHeigth(tvZt, tvZt.getWidth());
            }
        });
//        printer = new V1Printer(getActivity());
//        callback = new ICallback() {
//            /**
//             * 返回执行结果
//             * @param isSuccess:       true执行成功，false 执行失败
//             */
//            @Override
//            public void onRunResult(boolean isSuccess) {//打印后执行的返回码
//                Log.i(TAG, "onRunResult:" + isSuccess);
//            }
//
//            /**
//             * 返回结果(字符串数据)
//             * @param result:     结果
//             */
//            @Override
//            public void onReturnString(String result) {
//                Log.i(TAG, "onReturnString:" + result);
//            }
//
//            /**
//             * 执行发生异常
//             * code：     异常代码
//             * msg:     异常描述
//             */
//            @Override
//            public void onRaiseException(int code, String msg) {
//                Log.i(TAG, "onRaiseException:" + code + ":" + msg);
//            }
//
//        };
//        printer.setCallback(callback);//设置打印机回调
        getStarData(5);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceItemDecoration(1));//设置item间的分隔距离
        rv.setLayoutManager(new GridLayoutManager(getActivity(),5));//设置RecyclerView布局管理器为5列横向排布

        rvAdapter = new RvAdapter<Star>(getActivity(),list,R.layout.rv_md_star) {
            @Override
            public void convert(RvViewHolder holder, Star item,int position) {
                ImageView iv = holder.getView(R.id.iv);
                if (item.isSelect()){
                   iv.setSelected(true);
                }else{
                    iv.setSelected(false);
                }
            }
        };

        rvAdapter.setOnItemClickListener(new RvAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                toask("点击了"+position+"");
            }
        });
        rvAdapter.setOnItemLongClickListener(new RvAdapter.OnItemLongClickListener() {
            @Override
            public void OnItemLongClickListener(View view, int position) {
                toask("长按了"+position+"");
            }
        });

//        rvAdapter.onit

//        adapter = new StarAdapter(getActivity(), list);
        rv.setAdapter(rvAdapter);
//        adapter.notifyDataSetChanged();
    }

    private void getStarData(int h) {
        for (int i = 0; i < 5; i++) {
            Star s = new Star(true);
            if (i < h) {
                s.setSelect(true);
            }else{
                s.setSelect(false);
            }
            list.add(s);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.iv, R.id.tvName, R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5, R.id.tvYy, R.id.tvDd, R.id.tvZt, R.id.ll1, R.id.ll2, R.id.ll3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                break;
            case R.id.tvName:
                break;
            case R.id.iv1:
                break;
            case R.id.iv2:
                break;
            case R.id.iv3:
                break;
            case R.id.iv4:
                break;
            case R.id.iv5:
                break;
            case R.id.tvYy:
                break;
            case R.id.tvDd:
                break;
            case R.id.tvZt:
                break;
            case R.id.ll1:
                toask("经营分析");
                break;
            case R.id.ll2:
                toask("店铺管理");
                break;
            case R.id.ll3:
                toask("消息管理");
                break;
//            case R.id.bt://打印小票
////                ThreadPool.getInstance().executeTask(new Runnable() {
////                    @Override
////                    public void run() {
////
////                    }
////                });
//                printTicket("", "澄泓", "0.1", "", "", "", "", "", "", "", "");
//                break;
//            case R.id.btRecharge://打印充值小票
//                ThreadPool.getInstance().executeTask(new Runnable() {
//                    @Override
//                    public void run() {
//                        printRecharge("测试店铺","88888","100","2016-1-1 18:50","10","110","0","充值送10元优惠券");
//                    }
//                });
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        setViewHeigth(tvZt, tvZt.getWidth());
    }

    /**
     * 打印小票
     */
    void printTicket(String brandName, String shopName, String totalFeeText, String cashFeeText, String couponFeeText, String name, String balance, String point, String time, String orderNo, String cashFee) {
        printer.beginTransaction();//开启事务
        printer.setAlignment(0);
        String body = "微信扫码支付";
        StringBuilder sb = new StringBuilder();
        sb.append("   ***" + shopName + "*** \n");
        sb.append("--------------------------------\n");
        sb.append("消费 \n");
        sb.append("项目:" + body + "\n");
        sb.append("应付金额:" + totalFeeText + "\n");
        sb.append("实付金额:" + cashFee + "\n");
        sb.append("折扣金额:" + couponFeeText + "\n");
            /*if (couponFee > 0)
            {
                sb.append("折扣金额:" + couponFeeText + "");
            }*/

        sb.append("订单号:" + orderNo + "\n");
        sb.append("付款用户:" + name + "\n");
        sb.append("时间:" + time + "\n");
        sb.append("状态:下单成功并且支付成功" + "\n");
        sb.append("*" + brandName + "*\n");
        sb.append("--------------------------------\n");
        sb.append("  \n");
        sb.append("*************客户联*************\n");
        sb.append("  \n");
        sb.append("   ***" + shopName + "*** \n");
        sb.append("--------------------------------\n");
        sb.append("消费 \n");
        sb.append("项目:" + body + "\n");
        sb.append("应付金额:" + totalFeeText + "\n");
        sb.append("实付金额:" + cashFee + "\n");
        sb.append("折扣金额:" + couponFeeText + "\n");
            /*if (couponFee > 0)
            {
                sb.append("折扣金额:" + couponFeeText + "");
            }*/

        sb.append("订单号:" + orderNo + "\n");
        sb.append("付款用户:" + name + "\n");
        sb.append("时间:" + time + "\n");
        sb.append("状态:下单成功并且支付成功\n");
        sb.append("*" + brandName + "*\n");
        sb.append("--------------------------------\n");
        printer.printText(sb.toString());
        /**
         * 打印机走纸(强制换行，结束之前的打印内容后走纸n行)
         * @param n:	走纸行数
         */
        printer.lineWrap(2);
        printer.commitTransaction();//提交事务
    }

    /**
     * 打印会员充值小票
     *
     * @param shopName     店铺名
     * @param cardId       卡号
     * @param money        充值金额
     * @param giveMoney    赠送金额
     * @param residueMoney 剩余金额
     * @param cardIntegral 卡内积分
     * @param giveTicket   赠送卡券
     */
    void printRecharge(String shopName, String cardId, String money, String time, String giveMoney, String residueMoney, String cardIntegral, String giveTicket) {
        StringBuilder sb = new StringBuilder();
        sb.append("   ***" + shopName + "*** \n");
        sb.append("--------------------------------\n");
        sb.append("会员卡号:" + cardId + "\n");
        sb.append("充值金额:" + money + "\n");
        sb.append("赠送金额:" + giveMoney + "\n");
        sb.append("卡内余额:" + residueMoney + "\n");
        sb.append("卡内积分:" + cardIntegral + "\n");
        sb.append("赠送券:" + giveTicket + "\n");
        sb.append("充值时间:" + time + "\n");
        sb.append("--------------------------------\n");
        sb.append("\n");
        sb.append("*************客户联*************\n");
        sb.append("   ***" + shopName + "*** \n");
        sb.append("--------------------------------\n");
        sb.append("会员卡号:" + cardId + "\n");
        sb.append("充值金额:" + money + "\n");
        sb.append("赠送金额:" + giveMoney + "\n");
        sb.append("卡内余额:" + residueMoney + "\n");
        sb.append("卡内积分:" + cardIntegral + "\n");
        sb.append("赠送券:" + giveTicket + "\n");
        sb.append("充值时间:" + time + "\n");
        sb.append("--------------------------------\n");
        sb.append("\n");

        printer.beginTransaction();
        printer.printText(sb.toString());
        printer.lineWrap(2);
        printer.commitTransaction();
    }

}
