package cc.chenghong.vkagetorder.fragment_pad;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.MainActivity;
import cc.chenghong.vkagetorder.activity.StartActivity;
import cc.chenghong.vkagetorder.activity_pad.BluetoothActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.dialog.AskDialog;
import cc.chenghong.vkagetorder.fragment.BaseFragment;
import cc.chenghong.vkagetorder.service.LoginService;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;

/**
 * pad门店
 * Created by 何成龙 on 2016/7/13.
 */
public class PadXtFragment extends BaseFragment {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tvZh)
    TextView tvZh;
    @Bind(R.id.ivXx)
    ImageView ivXx;
    @Bind(R.id.flXx)
    FrameLayout flXx;
    @Bind(R.id.tvJc)
    TextView tvJc;
    @Bind(R.id.tvSy)
    TextView tvSy;
    @Bind(R.id.tvYj)
    TextView tvYj;
    @Bind(R.id.tvQh)
    TextView tvQh;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.ll)
    LinearLayout ll;
    AskDialog dialog;
    String ms = "手机";
    @Bind(R.id.ivLy)
    ImageView ivLy;
    @Bind(R.id.flLy)
    FrameLayout flLy;

    AskDialog exitDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fg_pad_xt, container,
                false);
        ButterKnife.bind(this, view);
        if (!App.isMobile()) {
            tvQh.setText("切换为手机模式");
        } else {
            tvQh.setText("切换为平板模式");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (App.getBluetooth()) {
            ivLy.setSelected(true);
        }else{
            ivLy.setSelected(false);
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

    @OnClick({R.id.tv_title, R.id.tvZh, R.id.flXx, R.id.flLy, R.id.tvJc, R.id.tvSy, R.id.tvYj, R.id.tvQh, R.id.bt, R.id.ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                break;
            case R.id.tvZh:
                break;
            case R.id.flXx:
                if (flXx.isSelected()) {
                    flXx.setSelected(false);
                } else {
                    flXx.setSelected(true);
                }
                break;
            case R.id.tvJc:
                break;
            case R.id.tvSy:
                break;
            case R.id.tvYj:
                break;
            case R.id.tvQh:
                showAsk();
                break;
            case R.id.bt:
                showExitDialog();
                break;
            case R.id.ll:
                break;
            case R.id.flLy:
                if (ivLy.isSelected()) {
                    ivLy.setSelected(false);
                    App.setBluetooth(false);
                } else {
                    ivLy.setSelected(true);
                    App.setBluetooth(true);
//                    getBlueActivity().startActivity(BluetoothActivity.class);
//                    getActivity().sendBroadcast(new Intent(MainActivity.MESSAGE_OPEN_BLUEBOOTH));
                    sendBroadcast(MainActivity.MESSAGE_OPEN_BLUEBOOTH);
                }
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
//                    SharedPreferencesHelper.saveBoolean("isLogin", false);
//                    getActivity().finish();
//                    ((BlueActivity) getActivity()).startActivity(PadLoginActivity.class);
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
}
