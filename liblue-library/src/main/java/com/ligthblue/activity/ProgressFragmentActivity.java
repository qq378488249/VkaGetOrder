package com.ligthblue.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ligthblue.R;
import com.ligthblue.dialog.ProgressDialog;


/**
 * 进度条Activity,继承FragmentActivity
 * hcl 2016-6-24
 */
public class ProgressFragmentActivity extends FragmentActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        progressDialog = new ProgressDialog(this);
    }

    /**
     * 显示进度弹窗
     * @param message 弹窗文字
     */
    public void progress(String message){
        if (progressDialog != null) {
            progressDialog.show(message);
        }
    }
    /**
     * 显示进度弹窗
     */
    public void progress(){
        if (progressDialog != null) {
            progressDialog.show("");
        }
    }
    /**
     * 隐藏进度弹窗
     */
    public void hideProgress(){

        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
