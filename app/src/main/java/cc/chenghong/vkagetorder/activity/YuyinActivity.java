package cc.chenghong.vkagetorder.activity;

import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import cc.chenghong.vkagetorder.R;

public class YuyinActivity extends BlueActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuyin);
        //语音初始化，在使用应用使用时需要初始化一次就好，如果没有这句会出现10111初始化失败
        SpeechUtility.createUtility(this, "appid=577e0bc8");
        //处理语音合成关键类
        SpeechSynthesizer speechSynthesizer = SpeechSynthesizer.createSynthesizer(this, myInitListener);
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "60");//设置语速
        //设置发音人
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,"xiaomeng");
        //设置音调
        speechSynthesizer.setParameter(SpeechConstant.PITCH,"50");
        //设置音量
        speechSynthesizer.setParameter(SpeechConstant.VOLUME,"100");
        int code = speechSynthesizer.startSpeaking("您有新的订单，请及时处理", synthesizerListener);
//        toask(code);
    }

    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d("mySynthesiezer:", "InitListener init() code = " + code);
        }
    };

    private SynthesizerListener synthesizerListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {

        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
