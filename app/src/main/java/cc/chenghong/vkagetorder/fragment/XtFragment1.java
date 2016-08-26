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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.MainActivity;
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
public class XtFragment1 extends BaseFragment {

    @Bind(R.id.lv)
    ListView lv;

    AskDialog dialog;
    String ms = "手机";
    LoginService service = new LoginService();
    AskDialog exitDialog;
    Page<Set> page = Page.New();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_xt1, container,
                false);
        ButterKnife.bind(this, view);
        if (App.isMobile()) {
            ms = "切换为平板模式";
        } else {
            ms = "切换为手机模式";
        }
        getData();
        return view;
    }

    private void getData() {
        page.list.add(new Set("账户设置"));
        page.list.add(new Set("消息提醒设置", true, App.getMessage()));
        page.list.add(new Set("蓝牙打印设置", true, App.getBluetooth()));
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
                    iv.setSelected(item.isSelect);
                    if (item.isSwith) {
                        ivJt.setVisibility(View.GONE);
                        iv.setVisibility(View.VISIBLE);
                    } else {
                        ivJt.setVisibility(View.VISIBLE);
                        iv.setVisibility(View.GONE);
                    }
                }
            }
        };
        lv.setAdapter(page.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                          Set item = page.list.get(position);
                                          switch (position) {
                                              case 1:
                                                  if (item.isSelect) {
                                                      item.setSelect(false);
                                                      App.setMessage(false);
                                                  } else {
                                                      item.setSelect(true);
                                                      App.setMessage(true);
                                                  }
                                                  break;
                                              case 2:
                                                  if (item.isSelect) {
                                                      item.setSelect(false);
                                                      App.setBluetooth(false);
                                                  } else {
                                                      item.setSelect(true);
                                                      App.setBluetooth(true);
                                                      getActivity().sendBroadcast(new Intent(MainActivity.MESSAGE_OPEN_BLUEBOOTH));
                                                  }
                                                  break;
                                              case 7://切换为平板模式
                                                  showAsk();
                                                  break;
                                              default:
                                                  toask(item.name);
                                                  break;
                                          }
                                          page.notifyDataSetChanged();
                                      }
                                  }

        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog() {
        if (exitDialog == null) {
            exitDialog = new AskDialog(getActivity(), "提示", "确定退出登陆？");
            exitDialog.setTvYesOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new LoginService().exitLogin(getBlueActivity());
                    exitDialog.dismiss();
                }
            });
            exitDialog.setTvNoOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exitDialog.dismiss();
                }
            });
        }
        exitDialog.show();
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
            dialog = new AskDialog(getActivity(), "提示", ms + "需要重启才能生效");
            dialog.setTvYes("确认").setTvYesOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = SharedPreferencesHelper.getInt("textSize");
                    Configuration configuration = getResources().getConfiguration();
                    if (App.isMobile()) {
                        App.setMobile(false);
                    } else {
                        App.setMobile(true);
                    }
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                    Intent restartIntent = new Intent(getActivity(), StartActivity.class);
                    int pendingId = 1;
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), pendingId, restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, pendingIntent);
                    dialog.dismiss();
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
