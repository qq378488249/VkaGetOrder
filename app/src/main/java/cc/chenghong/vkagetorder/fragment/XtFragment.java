package cc.chenghong.vkagetorder.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.StartActivity;
import cc.chenghong.vkagetorder.adapter.LvAdapter;
import cc.chenghong.vkagetorder.adapter.LvViewHolder;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.bean.Page;
import cc.chenghong.vkagetorder.bean.Set;
import cc.chenghong.vkagetorder.dialog.AskDialog;
import cc.chenghong.vkagetorder.service.LoginService;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;

/**
 * 系统设置
 * Created by hcl on 2016/6/13.
 */
public class XtFragment extends BaseFragment {

    @Bind(R.id.ivKg)
    ImageView ivKg;
    @Bind(R.id.ll2)
    LinearLayout ll2;
    @Bind(R.id.ll3)
    LinearLayout ll3;
    @Bind(R.id.ll4)
    LinearLayout ll4;
    @Bind(R.id.ll5)
    LinearLayout ll5;
    @Bind(R.id.ll6)
    LinearLayout ll6;
    @Bind(R.id.ll1)
    LinearLayout ll1;
    @Bind(R.id.tv6)
    TextView tv6;
    @Bind(R.id.lv)
    ListView lv;

    AskDialog dialog;
    String ms = "手机";
    /**
     * 是否为平板模式
     */
    boolean isPad = false;
    LoginService service = new LoginService();
    Page<Set> page = Page.New();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_xt, container,
                false);
        ButterKnife.bind(this, view);
        if (App.isMobile()) {
            ms = "切换为平板模式";
        } else {
            ms = "切换为手机模式";
        }
        tv6.setText(ms);
        getData();
//        int i = SharedPreferencesHelper.getInt("textSize");
//        switch (i) {
//            case 0:
//                ms = "平板";
//                tv6.setText("切换为平板模式");
//                break;
//            case 1:
//                ms = "手机";
//                tv6.setText("切换为手机模式");
//                break;
//        }
        return view;
    }

    private void getData() {
        page.list.add(new Set("账户设置"));
        page.list.add(new Set("消息提醒设置", true,App.getMessage()));
        page.list.add(new Set("蓝牙打印设置", true,App.getBluetooth()));
        page.list.add(new Set(""));
        page.list.add(new Set("检查更新"));
        page.list.add(new Set("使用说明"));
        page.list.add(new Set("意见反馈"));
        page.list.add(new Set(ms));

        page.adapter = new LvAdapter<Set>(getActivity(), page.list, R.layout.lv_item_xt_set) {
            @Override
            public void convert(LvViewHolder helper, final Set item, final int position) {
                LinearLayout ll = helper.getView(R.id.ll);
                final TextView tv = helper.getView(R.id.tv);
                View v = helper.getView(R.id.v);
                final ImageView iv = helper.getView(R.id.iv);
                ImageView ivJt = helper.getView(R.id.ivJt);
                if (item.name.equals("")) {
                    ll.setVisibility(View.GONE);
                    v.setVisibility(View.VISIBLE);
                } else {
                    ll.setVisibility(View.VISIBLE);
                    v.setVisibility(View.GONE);
                    tv.setText(item.name);
                    if (item.isSwith) {
                        ivJt.setVisibility(View.GONE);
                        iv.setVisibility(View.VISIBLE);
                    } else {
                        ivJt.setVisibility(View.VISIBLE);
                        iv.setVisibility(View.GONE);
                    }
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position == 1) {//消息提醒设置
                                if (item.isSelect) {
                                    item.setSelect(false);
                                    App.setMessage(false);
                                } else {
                                    item.setSelect(true);
                                    App.setMessage(true);
                                }
                            } else if (position == 2) {//蓝牙打印设置
                                if (item.isSelect) {
                                    item.setSelect(false);
                                    App.setBluetooth(false);
                                } else {
                                    item.setSelect(true);
                                    App.setBluetooth(true);
                                }
                            } else {
                                return;
                            }
                            page.adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };
        lv.setAdapter(page.adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll1, R.id.ivKg, R.id.ll2, R.id.ll3, R.id.ll4, R.id.ll5, R.id.ll6, R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll1:
                break;
            case R.id.ivKg:
                autoSelectView(ivKg);
                break;
            case R.id.ll2:
                break;
            case R.id.ll3:
                break;
            case R.id.ll4:
                break;
            case R.id.ll5:
                break;
            case R.id.ll6:
                showAsk();
                break;
            case R.id.bt:
                service.exitLogin(getBlueActivity());
                break;
        }
    }

    private void autoSelectView(View view) {
        if (view.isSelected()) {
            view.setSelected(false);
        } else {
            view.setSelected(true);
        }
    }

    private void showAsk() {
        if (dialog == null) {
            dialog = new AskDialog(getActivity(), "提示", "切换" + ms + "模式需要重启才能生效");
            dialog.setTvYes("确认").setTvYesOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = SharedPreferencesHelper.getInt("textSize");
                    Configuration configuration = getResources().getConfiguration();
                    if (App.isMobile()) {
//                        configuration.fontScale = 1.0f;
                        App.setMobile(false);
//                        SharedPreferencesHelper.saveBoolean(ISPAD, false);
                    } else {
                        App.setMobile(true);
//                        configuration.fontScale = 1.5f;
//                        SharedPreferencesHelper.saveBoolean(ISPAD, true);
                    }
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                    Intent restartIntent = new Intent(getActivity(), StartActivity.class);
                    int pendingId = 1;
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), pendingId, restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
                    dialog.dismiss();
//                    SharedPreferencesHelper.saveBoolean("isPad",true);
                    getActivity().finish();
                }
            });
            dialog.setTvNo("取消").setTvNoOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

}
