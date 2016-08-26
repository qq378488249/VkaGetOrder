package cc.chenghong.vkagetorder.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.renderer.LevelLoadingRenderer;
import cc.chenghong.vkagetorder.renderer.LoadingDrawable;

/** 圆形进度条对话框
 * Created by hcl on 2016/6/24.
 */
public class ProgressDialog extends Dialog {
    private ImageView ivProgress;
    private TextView tvProgress;
    private ProgressBar pb;
    private Context mContext;
    private LoadingDrawable loadingDrawable;

    private String strMessage = "";

    public ProgressDialog(Context context) {
        super(context, R.style.TransparencyDialog);
        mContext = context;
        loadingDrawable = new LoadingDrawable(new LevelLoadingRenderer(context));
    }

    public ProgressDialog(Context context, int theme) {
        super(context, R.style.TransparencyDialog);
        mContext = context;
        loadingDrawable = new LoadingDrawable(new LevelLoadingRenderer(context));
    }

    protected ProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        loadingDrawable = new LoadingDrawable(new LevelLoadingRenderer(context));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_progress);
        ivProgress = (ImageView) findViewById(R.id.ivProgress);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
//        pb = (ProgressBar) findViewById(R.id.pb);
        if (ivProgress != null) {
            ivProgress.setImageDrawable(loadingDrawable);
        }
        if (tvProgress != null) {
            tvProgress.setText(strMessage);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadingDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingDrawable.stop();
    }

    public void show(String message) {
        if (message != null) {
            strMessage = message;
        }
        super.show();
    }

}
