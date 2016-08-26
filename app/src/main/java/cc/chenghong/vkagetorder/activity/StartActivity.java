package cc.chenghong.vkagetorder.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity_pad.PadLoginActivity;
import cc.chenghong.vkagetorder.activity_pad.PadMainActivity;
import cc.chenghong.vkagetorder.app.App;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;

/**
 * 开始页面
 */
public class StartActivity extends BlueActivity {
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.tv)
    TextView tv;

    private Animation alphaAnimation = null;
    /**
     * 是否为平板模式
     */
    boolean isPad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        tv.setText("欢迎使用" + getResourcesString(R.string.app_name));
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.start_alpha);
        alphaAnimation.setFillEnabled(true); //启动Fill保持
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面
        rl.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {//为动画设置监听
            @Override
            public void onAnimationStart(Animation animation) {//动画开始

            }

            @Override
            public void onAnimationEnd(Animation animation) {//动画结束
//                startActivity(LoginActivity.class);
                if (!App.isMobile()) {
                    if (SharedPreferencesHelper.getBoolean("isLogin")){
                        startActivity(PadMainActivity.class);
                    }else{
                        startActivity(PadLoginActivity.class);
//                        startActivity(PadMainActivity.class);
                    }
                } else {
                    if (SharedPreferencesHelper.getBoolean("isLogin")){
                        startActivity(MainActivity.class);
                    }else{
                        startActivity(LoginActivity.class);
//                        startActivity(MainActivity.class);
                    }
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {//动画重复

            }
        });
    }
}
