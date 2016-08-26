package cc.chenghong.vkagetorder.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import cc.chenghong.vkagetorder.R;
import cc.chenghong.vkagetorder.activity_pad.BluetoothActivity;
import cc.chenghong.vkagetorder.api.Api;
import cc.chenghong.vkagetorder.bean.Account;
import cc.chenghong.vkagetorder.param.VolleyParam;
import cc.chenghong.vkagetorder.request.AsyncHttpRequest;
import cc.chenghong.vkagetorder.request.VolleyRequest;
import cc.chenghong.vkagetorder.service.BlueboothPrintService;
import cc.chenghong.vkagetorder.util.SharedPreferencesHelper;
import cc.chenghong.vkagetorder.util.StringUtils;
import cc.chenghong.vkagetorder.util.UITools;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by hcl on 2016/6/13.
 */
public class App extends Application {
    private static final String TAG = "Application";
    // 上下文
    private static Application application;
    //在线语音（3个实体类）
    private static com.iflytek.cloud.SynthesizerListener synthesizerListener;
    private static InitListener myInitListener;
    private static SpeechSynthesizer speechSynthesizer;
    //设置别名时轮循的次数
    private static int setIndex = 0;
    /**
     * 蓝牙打印广播
     */
    public static final String BLUEBOOTH_PRINT = "bluetooth_print";
    private BlueboothPrintService blueboothPrintService = null;
    /**
     * 蓝牙状态
     */
    public static boolean isBluebooth = false;
    /**
     * 蓝牙打印机连接状态
     */
    public static boolean isConnection = false;
    /**
     * 蓝牙搜索页面是否在运行
     */
    public static boolean isBlueboothAcRun = false;
    public static GpService mGpService;
    //蓝牙地址
    public static String blueboothAddress = "";
    /**
     * 蓝牙打印机重试次数
     */
    public static int index = 1;
    /**
     * 自动连接
     */
    public static boolean autoConnection = false;

    @Override
    public void onCreate() {
        super.onCreate();
        this.application = this;
        VolleyRequest.init(this);
        setOfficialApi(true);
        Log.i(TAG, "onCreate: " + Api.MAIN);
        initImageLoader(this);
        UITools.init(this);
        SharedPreferencesHelper.init(this);
        if (App.isLogin()) {
            initJpush();
            String registrationID = JPushInterface.getRegistrationID(App.getInstance());
            System.out.println(registrationID + "xxxxxx");
        }
        initSpeech();
        setDebug(true);
        isConnection = false;
    }

    public static boolean getMessage() {
        return SharedPreferencesHelper.getBoolean("isMessage");
    }

    public static void setMessage(boolean b) {
        SharedPreferencesHelper.saveBoolean("isMessage", b);
    }

    /**
     * 启动蓝牙打印服务
     */
    private void startService() {
        Intent i = new Intent(this, GpPrintService.class);
        startService(i);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setDebug(boolean b) {
        SharedPreferencesHelper.saveBoolean("isDebug", b);
    }

    public static boolean getIsDebug() {
        return SharedPreferencesHelper.getBoolean("isDebug");
    }

    /**
     * 设置是否正式版接口
     */
    private void setOfficialApi(boolean b) {
        if (b) {
            Api.MAIN = Api.MAIN_OFFICIAL;
        } else {
            Api.MAIN = Api.MAIN_TEST;
        }
    }

    public static void initJpush() {
        JPushInterface.init(getInstance());            // 初始化 JPush
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
    }

    private static boolean isLogin() {
        return SharedPreferencesHelper.getBoolean("isLogin");
    }

    /**
     * 初始化在线语音
     */
    private void initSpeech() {
        myInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d("mySynthesiezer:", "InitListener init() code = " + code);
            }
        };
        synthesizerListener = new com.iflytek.cloud.SynthesizerListener() {
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
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=577e0bc8");
        //处理语音合成关键类
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this, myInitListener);
        speechSynthesizer.setParameter(SpeechConstant.SPEED, "60");//设置语速
        //设置发音人
        speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "aisxmeng");
        //设置音调
//        speechSynthesizer.setParameter(SpeechConstant.PITCH,"50");
        //设置音量
        speechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
    }

    /**
     * 新订单语音
     *
     * @param voiceString
     */
    public static void newOrderVoice(String voiceString) {
        if (!StringUtils.isEmpty(voiceString)) {
            //开始合成语音
            speechSynthesizer.startSpeaking(voiceString, synthesizerListener);
        }

    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, context.getString(R.string.app_name) + "/Cache");//获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        //创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.c6)//加载中显示的图片
//                .showImageOnFail(R.drawable.c6)//加载失败显示的图片
                .cacheInMemory(true)//在内存中缓存
                .cacheOnDisk(true)//在磁盘（内存卡）中缓存
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null) // Can slow ImageLoader, use it carefully (Better don't use it)设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(5)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                //.discCacheFileNameGenerator(new HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存File最大数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(displayImageOptions)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }

    /**
     * 返回当前App的上下文实例
     *
     * @return application
     */
    public static Application getInstance() {
        return application;
    }

    /**
     * 返回是否是手机模式
     *
     * @return
     */
    public static boolean isMobile() {
        return SharedPreferencesHelper.getBoolean("isMobile");
    }

    /**
     * 设置是否是手机模式
     */
    public static void setMobile(boolean b) {
        SharedPreferencesHelper.saveBoolean("isMobile", b);
    }

    /**
     * 获取string.xml里对应id的值
     *
     * @param id
     * @return
     */
    public static String getResourcesString(int id) {
        return getInstance().getApplicationContext().getResources().getString(id);
    }

    /**
     * 保存登陆的账户
     *
     * @param account
     */
    public static void putAccount(Account account) {
        SharedPreferencesHelper.saveJSON("account", account);
    }

    /**
     * 获取登陆的用户
     *
     * @return
     */
    public static Account getAccount() {
        return SharedPreferencesHelper.getJSON("account", Account.class);
    }

    /**
     * 设置登陆状态
     *
     * @return
     */
    public static void setLogin(boolean b) {
        SharedPreferencesHelper.saveBoolean("isLogin", b);
    }

    static int code = 0;

    /**
     * 设置极光推送标签线程
     *
     * @param index 轮循的次数
     */
    public static void setJPushInterfaceTagThread(final int index) {
        setIndex = index;
        hander.sendMessage(hander.obtainMessage(1));
//        new Thread() {
//            @Override
//            public void run() {
//                System.out.println(index + "轮循次数");
//                if (index < 10 && code != 200) {
//                    try {
//                        Thread.sleep(10000);
//                        setJPushInterfaceTag();
//                        setJPushInterfaceTagThread(index + 1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    hander.sendEmptyMessage(code);
//                    return;
//                }
//            }
//        }.start();
    }

    static Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setIndex++;
                    setJPushInterfaceTag();
                    break;
                case 200:

                    break;
                case 329://
                    if (isBluebooth == false) {
                        toask("请手动打开蓝牙");
                    } else {
                        if (!isConnection) {
                            Intent intent = new Intent(App.getInstance(), BluetoothActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getInstance().startActivity(intent);
                        }
                    }
                    break;
                default:
                    if (setIndex < 6) {
                        setJPushInterfaceTag();
                    } else {
                        toask("设置别名失败");
                    }
                    setIndex++;
                    break;
            }
        }
    };

    /**
     * 设置极光推送标签
     *
     * @param strs
     */
    public static void setJPushInterfaceTag(String... strs) {
        if (setIndex > 2) {
            toask("获取订单推送消息失败，请重新登陆");
            return;
        }
        Set<String> set = new HashSet<>();
        for (String str : strs) {
            set.add(getAccount().getStoreId() + "");
        }
        JPushInterface.setAlias(getInstance(), getAccount().getStoreId() + "", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                if (i == 0) {
                    Log.i(TAG, "setAlias：" + i + getAccount().getStoreId());
                    System.out.println("code+" + i + "，setAlias：" + getAccount().getStoreId());
                    code = 200;
                } else {
                    if (setIndex < 3) {
                        hander.sendEmptyMessageDelayed(1, 3000);
                    } else {
                        toask("获取订单推送消息失败，请重新登陆");
                    }
                }
            }
        });
//        JPushInterface.setTags(getInstance(), set, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                if (i == 0){
//                    System.out.println(i + "，setTags：" + getAccount().getStoreId());
//                    hander.sendEmptyMessage(200);
//                    code = 200;
//                }else{
//                    hander.sendEmptyMessage(i);
//                }
//            }
//        });
    }

    public static void toask(String s) {
        Toast.makeText(getInstance(), s, Toast.LENGTH_LONG).show();
    }

    public static void setBluetooth(boolean b) {
        SharedPreferencesHelper.saveBoolean("isBluetooth", b);
    }

    public static boolean getBluetooth() {
        return SharedPreferencesHelper.getBoolean("isBluetooth");
    }

    public static VolleyParam getVolleyParam() {
        return new VolleyParam().putHeader("parentCode", App.getAccount().getParentCode() + "").
                putHeader("code", App.getAccount().getStoreCode() + "").
                putHeader("accessToken", App.getAccount().getAccessToken() + "").
                putHeader("Content-Type", "application/json");
    }

    /**
     * 初始化AsyncHttpRequest头部参数
     */
    public static void initAsyncHttpRequestHeader() {
        AsyncHttpRequest.getClient().addHeader("parentCode", App.getAccount().getParentCode() + "");
        AsyncHttpRequest.getClient().addHeader("code", App.getAccount().getStoreCode() + "");
        AsyncHttpRequest.getClient().addHeader("accessToken", App.getAccount().getAccessToken() + "");
        AsyncHttpRequest.getClient().addHeader("Content-Type", "application/json");
    }

    /**
     * 打印机未连接
     */
    public static void falseConnection() {
        isConnection = false;
    }

    /**
     * 打印机已连接
     */
    public static void trueConnection() {
        isConnection = true;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.e(TAG, "onTerminate");
    }
}
