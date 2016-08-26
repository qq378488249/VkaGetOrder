package cc.chenghong.vkagetorder.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.request.AsyncHttpParam;
import cc.chenghong.vkagetorder.service.LoginService;

/**
 * 登录
 */
public class LoginActivity extends BlueActivity {

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
    @Bind(R.id.iv)
    ImageView iv;

    public EditText getEt1() {
        return et1;
    }

    public EditText getEt2() {
        return et2;
    }

    public EditText getEt3() {
        return et3;
    }

    public EditText getEt4() {
        return et4;
    }

    public Button getBt() {
        return bt;
    }

    public ImageView getIv() {
        return iv;
    }

    private LoginService service = new LoginService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        et4.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et1.setText("1078");
        et2.setText("CC888");
        et3.setText("oppo01");
        et4.setText("111111");
    }

    @OnClick({R.id.et1, R.id.et2, R.id.et3, R.id.et4, R.id.bt, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et1:
                break;
            case R.id.et2:
                break;
            case R.id.et3:
                break;
            case R.id.et4:
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
//                progress("登陆中...");

                final VolleyParam param = VolleyParam.New();
//                AsyncHttpParam param = AsyncHttpParam.New();
                param.putHeader("parentCode", viewGetValue(et1)).
                        putHeader("code", viewGetValue(et2)).
                        putHeader("Content-Type", "application/json");
                param.putParam("account", viewGetValue(et3)).
                        putParam("password", viewGetValue(et4));

                AsyncHttpParam asyncHttpParam = AsyncHttpParam.New();
                asyncHttpParam.putHeader("parentCode", viewGetValue(et1)).
                        putHeader("code", viewGetValue(et2)).
                        putHeader("Content-Type", "application/json");
                asyncHttpParam.putParam("account", viewGetValue(et3)).
                        putParam("password", viewGetValue(et4));

                service.login(this, param,asyncHttpParam);
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
//                break;
        }
    }
}
