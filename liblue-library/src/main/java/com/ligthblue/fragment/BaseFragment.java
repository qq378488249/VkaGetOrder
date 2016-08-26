package com.ligthblue.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * fragment基类
 * @author guozhiwei 2015-6-15
 *
 */
public class BaseFragment extends Fragment {
	public  String TAG = getClass().getSimpleName();
	public static final String ISPAD = "isPad";
	/**
	 * Fragment视图
	 */
	public View contentView;

	/**
	 * 设置视图
	 * @param inflater
	 * @param layout
	 */
	public void setContentView(LayoutInflater inflater, int layout){
		contentView = inflater.inflate(layout, null);
	}
	/**
	 * 找到view
	 * @param resId
	 * @return
	 */
	public View findViewById(int resId){
		return contentView.findViewById(resId);
	}
	/**
	 * 找到TextView
	 * @param resId
	 * @return
	 */
	public TextView findTextViewById(int resId){
		return (TextView) findViewById(resId);
	}
	/**
	 * 找到EditView
	 * @param resId
	 * @return
	 */
	public EditText findEditTextViewById(int resId){
		return (EditText) findViewById(resId);
	}
	/**
	 * 找到Button
	 * @param resId
	 * @return
	 */
	public Button findButtonById(int resId){
		return (Button) findViewById(resId);
	}
	/**
	 * 找到Button
	 * @param view
	 * @param resId
	 * @return
	 */
	public Button findButtonById(View view, int resId){
		return (Button) view.findViewById(resId);
	}
	/**
	 * 找到LinearLayout
	 * @param resId
	 * @return
	 */
	public LinearLayout findLinearLayoutById(int resId){
		return (LinearLayout) findViewById(resId);
	}
	/**
	 * 找到RelativeLayout
	 * @param resId
	 * @return
	 */
	public RelativeLayout findRelativeLayoutById(int resId){
		return (RelativeLayout) findViewById(resId);
	}

	/**
	 * 找到ListView
	 * @param resId
	 * @return
	 */
	public ListView findListViewById(int resId){
		return (ListView) findViewById(resId);
	}
	/**
	 * 找到ImageView
	 * @param resId
	 * @return
	 */
	public ImageView findImageViewById(int resId){
		return (ImageView) findViewById(resId);
	}
	
	/**
	 * 阻拦onBackPressed事件
	 * @return
	 */
	public boolean onBackPressed(){
		return false;
	}
	
	/**
	 * 右边第一个按钮点击
	 */
	public void onIconRight1Click(View v){
		
	}
	/**
	 * 右边第二个按钮点击
	 */
	public void onIconRight2Click(View v){
		
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		return false;
//	}

	public void toask(Object str){
		Toast.makeText(getActivity(),str+"",Toast.LENGTH_LONG).show();
	}
	/**
	 * 设置控件的宽度
	 *
	 * @param view
	 * @param width
	 */
	public void setViewWidth(View view, int width) {
		setViewLayoutParams(view, width, 0);
	}

	/**
	 * 设置控件的高度
	 *
	 * @param view
	 * @param height
	 */
	public void setViewHeigth(View view, int height) {
		setViewLayoutParams(view, 0, height);
	}

	/**
	 * 设置控件的宽度和高度
	 *
	 * @param view
	 * @param width
	 * @param height
	 */
	public void setViewLayoutParams(View view, int width, int height) {
		if (view == null) {
			throw new NullPointerException("view can't be null");
		}
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (width > 1) {
			lp.width = width;
		}
		if (height > 1) {
			lp.height = height;
		}
		view.setLayoutParams(lp);
	}

	// 设置背景透明度
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getActivity().getWindow()
				.getAttributes();
		lp.alpha = bgAlpha;
		getActivity().getWindow().setAttributes(lp);
	}
}
