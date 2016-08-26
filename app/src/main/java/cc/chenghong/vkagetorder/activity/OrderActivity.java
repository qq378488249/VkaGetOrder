package cc.chenghong.vkagetorder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.adapter.OrderAdapter;
import cc.chenghong.vkagetorder.bean.Order;
import cc.chenghong.vkagetorder.bean.OrderDishes;
import cc.chenghong.vkagetorder.bean.Page;
import cc.chenghong.vkagetorder.decoration.SpaceItemDecoration;

public class OrderActivity extends AppCompatActivity {

    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.rv)
    RecyclerView rv;
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
    @Bind(R.id.flDd)
    FrameLayout flDd;

    Page<Order> page = new Page(0, 5);
    PopupWindow popupWindow;
    View popupWindowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getAttributes().gravity = Gravity.LEFT;
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            page.list.add(new Order());
        }
        System.out.println(page.list.size());
        for (int i = 0; i < page.list.size(); i++) {
            int j = (int) (Math.random() * 10 + 1);//获取1到10的随机数，模拟顾客点击的菜品数量
            List<OrderDishes> itemList = new ArrayList<>();
            for (int i1 = 0; i1 < j; i1++) {
                OrderDishes od = new OrderDishes("菜品" + i1, "x" + i1, ((int) (Math.random() * 100 + 1)) + "");
                itemList.add(od);
            }
//            page.list.get(i).getList().addAll(itemList);
//            page.list.get(i).getOrdersDetailsList().addAll(itemList);
        }
//        for (int i1 = 0; i1 < j; i1++) {
//
//        }
//            Order order = page.list.get(i);
//            order.getList().addAll(itemList);

    }

    private void initView() {
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceItemDecoration(10));//设置分割线
        rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));//设置RecyclerView布局管理器为2列垂直排布
        rv.setAdapter(new OrderAdapter(this, page.list));
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tvDd, R.id.tvTd, R.id.tv4, R.id.tv5, R.id.flTd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                selectTv(tv1);
                break;
            case R.id.tv2:
                selectTv(tv2);
                break;
            case R.id.tv3:
                selectTv(tv3);
                break;
            case R.id.tvDd://订单
                showDd();
                break;
            case R.id.tvTd://退单
                break;
            case R.id.tv4:
                break;
            case R.id.tv5:
                break;
            case R.id.flTd:
                break;
        }
    }

    private void showDd() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow();
        }
    }

    void selectTv(View view) {
        tv1.setSelected(false);
        tv2.setSelected(false);
        tv3.setSelected(false);
        view.setSelected(true);
    }

}
