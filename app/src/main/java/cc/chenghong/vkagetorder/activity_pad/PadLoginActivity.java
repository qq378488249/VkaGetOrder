package cc.chenghong.vkagetorder.activity_pad;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity.BlueActivity;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.request.AsyncHttpParam;
import cc.chenghong.vkagetorder.service.LoginService;

public class PadLoginActivity extends BlueActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et1)
    EditText et1;
    @Bind(R.id.et2)
    EditText et2;
    @Bind(R.id.et3)
    EditText et3;
    @Bind(R.id.et4)
    EditText et4;
    @Bind(R.id.bt)
    Button bt;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.iv)
    ImageView iv;

    LoginService service = new LoginService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg_login_pad);
        ButterKnife.bind(this);
//        et1.setText("1070");
//        et2.setText("CC880");
//        et3.setText("xmkl04");
//        et4.setText("111111");

        et1.setText("1078");
        et2.setText("CC888");
        et3.setText("oppo01");
        et4.setText("111111");
    }

    @OnClick({R.id.bt, R.id.ll, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt:
                if (viewIsNull(et1)) {
                    toask("请输入总店编号");
                    return;
                }
                if (viewIsNull(et2)) {
                    toask("请输入门店编号");
                    return;
                }
                if (viewIsNull(et3)) {
                    toask("请输入登陆账户");
                    return;
                }
                if (viewIsNull(et4)) {
                    toask("请输入登陆密码");
                    return;
                }
                final VolleyParam param = VolleyParam.New();
                param.putHeader("parentCode", viewGetValue(et1)).
                        putHeader("code", viewGetValue(et2)).
                        putHeader("Content-Type", "application/json");
                param.putParam("account", viewGetValue(et3)).putParam("password", viewGetValue(et4));

//                progress("登陆中...");
                AsyncHttpParam asyncHttpParam = AsyncHttpParam.New();
                asyncHttpParam.putHeader("parentCode", viewGetValue(et1)).
                        putHeader("code", viewGetValue(et2));
//                        putHeader("Content-Type", "application/json");
                asyncHttpParam.putParam("account", viewGetValue(et3)).putParam("password", viewGetValue(et4));

                service.login(this,param,asyncHttpParam);
//                SharedPreferencesHelper.saveBoolean("isLogin",true);
//                startActivity(PadMainActivity.class);
//                finish();
//                VolleyRequest.postJson(Api.LOGIN, param, new Response.Listener() {
//                    @Override
//                    public void onResponse(Object o) {
//                        hideProgress();
//                        Account data = fromJson(o.toString(), Account.class);
//                        if (data.isSuccess()) {
//                            SharedPreferencesHelper.saveString("parentCode", param.getHeaders().get("parentCode") + "");
//                            App.initJpush();
//                            JPushInterface.resumePush(App.getInstance());
//                            String registrationID = JPushInterface.getRegistrationID(App.getInstance());
//                            System.out.println(registrationID + "xxxxxxxxx");
//                            data.data.setParentCode(param.getHeaders().get("parentCode") + "");
//                            data.data.setStoreCode(param.getHeaders().get("code") + "");
//                            App.putAccount(data.data);
//                            App.setJPushInterfaceTagThread(0);
//                            App.initAsyncHttpRequestHeader();
//
//                            toask("登陆成功");
//                            setLogin(true);
//                            if (!App.isMobile()) {
//                                startActivity(PadMainActivity.class);
//                            } else {
//                                startActivity(MainActivity.class);
//                            }
//                            finish();
//                        } else {
//                            toask("登陆失败" + data.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        hideProgress();
//                        toask(volleyError.toString());
//                    }
//                });
                break;
            case R.id.ll:
                break;
            case R.id.iv:
                if (iv.isSelected()) {//隐藏密码
                    iv.setSelected(false);
                    et4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et4.setSelection(viewGetValue(et4).length());
                } else {
                    // 显示密码
                    et4.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv.setSelected(true);
                    et4.setSelection(viewGetValue(et4).length());
                }
                break;
        }
    }
}
