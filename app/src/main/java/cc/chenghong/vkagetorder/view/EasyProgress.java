package cc.chenghong.vkagetorder.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import cc.chenghong.vkagetorder.R;

/**
 * @author hubing 
 * @version ����ʱ�䣺2015-5-18 ����10:22:13
 *
 */
public class EasyProgress extends View {

	private final static String TAG = "EasyProgress.java";

	public EasyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttributeset(context , attrs , defStyleAttr) ; 
		init(context);
	}
	public EasyProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttributeset(context  ,attrs , 0) ; 
		init(context);
	}
	public EasyProgress(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		arcPaint = new Paint() ; 
		arcPaint.setColor(arcColor) ;
		arcPaint.setStrokeWidth(borderWidth) ;
		arcPaint.setAntiAlias(true) ; 
		arcPaint.setStyle(Paint.Style.STROKE) ;
		arcRectf = new RectF() ;
	}
	
	
	private  void initAttributeset(Context context ,  AttributeSet attrs , int defStyle){
		Resources resources = getResources();
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyProgressAttr, defStyle, 0);
		arcColor = typedArray.getColor(R.styleable.EasyProgressAttr_EasyProgressArcColor, Color.BLUE ) ;
		borderWidth = typedArray.getDimensionPixelSize(R.styleable.EasyProgressAttr_EasyProgressBorderWidth, 16) ;
		
		typedArray.recycle() ;
		
	}
	
	
	private Paint arcPaint ; 
	private RectF arcRectf ; 
	private float startAngle = -45f; 
	private float sweepAngle = -19f; 
	private float incrementAngele  = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawArc(arcRectf, startAngle + incrementAngele  , sweepAngle, false, arcPaint) ; 
		
		if (animatorSet == null || !animatorSet.isRunning()) {
			startAnimation() ;
		}
	}
	
	
	private int size ; 
	
	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = (w < h) ? w : h;
        stupBound();
    }
	
	private void stupBound(){
		int paddingLeft = getPaddingLeft() ; 
		int paddingTop = getPaddingTop() ;
		arcRectf.set(paddingLeft + borderWidth , paddingTop + borderWidth, size - paddingLeft - borderWidth , size - paddingTop - borderWidth) ;
	}
	
	
	private static float DEFULT_MAX_ANGLE = -305f ;
	private static float DEFULT_MIN_ANGLE = -19f ;
	
	private void startAnimation(){
		
		if (animatorSet != null && animatorSet.isRunning()) {
			animatorSet.cancel() ;
		}
		
		animatorSet = new AnimatorSet() ;
		
		AnimatorSet set = circuAnimator();
		animatorSet.play(set) ;
		animatorSet.addListener(new AnimatorListener() {
			private boolean  isCancel = false ; 
			@Override
			public void onAnimationStart(Animator animation) {
			}
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isCancel) {
					startAnimation() ;
				}
			}
			@Override
			public void onAnimationCancel(Animator animation) {
				isCancel = true ;
			}
		}) ;
		
		animatorSet.start() ;
	}
	
	//Ĭ�ϵĶ���ʱ��
	private  int DEFULT_DURATION = 660 ;
	
	/**
	 * ѭ���Ķ���
	 */
	private AnimatorSet circuAnimator(){
		
		//��СȦ����Ȧ
		ValueAnimator holdAnimator1 = ValueAnimator.ofFloat(incrementAngele + DEFULT_MIN_ANGLE , incrementAngele + 115f) ; 
		holdAnimator1.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				incrementAngele = (float) animation.getAnimatedValue() ;
			}
		}) ; 
		holdAnimator1.setDuration(DEFULT_DURATION ) ; 
		holdAnimator1.setInterpolator(new LinearInterpolator()) ;
		
		
		ValueAnimator expandAnimator = ValueAnimator.ofFloat(DEFULT_MIN_ANGLE , DEFULT_MAX_ANGLE) ;
		expandAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				sweepAngle = (float) animation.getAnimatedValue() ;
				incrementAngele -= sweepAngle ;
				invalidate() ; 
			}
		}) ;
		expandAnimator.setDuration(DEFULT_DURATION) ;
		expandAnimator.setInterpolator(new DecelerateInterpolator(2)) ;
		
		
		//�Ӵ�Ȧ��СȦ
		ValueAnimator holdAnimator = ValueAnimator.ofFloat(startAngle , startAngle + 115f) ;
		holdAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				startAngle =  (float) animation.getAnimatedValue() ;
			}
		});
		
		holdAnimator.setDuration(DEFULT_DURATION ) ; 
		holdAnimator.setInterpolator(new LinearInterpolator()) ;
		
		ValueAnimator narrowAnimator = ValueAnimator.ofFloat(DEFULT_MAX_ANGLE , DEFULT_MIN_ANGLE) ;
		narrowAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				sweepAngle = (float) animation.getAnimatedValue() ;
				invalidate() ; 
			}
		}) ;
		
		narrowAnimator.setDuration(DEFULT_DURATION) ;
		narrowAnimator.setInterpolator(new DecelerateInterpolator(2)) ;
		
		AnimatorSet set = new AnimatorSet() ;
		set.play(holdAnimator1 ).with(expandAnimator) ;
		set.play(holdAnimator).with(narrowAnimator).after(holdAnimator1);
		return set ; 
	}
	
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
	}
	
	
	private int borderWidth ; 
	public  void setBorderWidth(int width){
		this.borderWidth = width ; 
	}
	
	
	private final static int DEFULT_ARC_COLOR = Color.BLUE  ; 
	private int arcColor  = DEFULT_ARC_COLOR ;
	private AnimatorSet animatorSet; 
	
	public void setArcColor(int color){
		this.arcColor = color ; 
	}
	
	
	
	
}
