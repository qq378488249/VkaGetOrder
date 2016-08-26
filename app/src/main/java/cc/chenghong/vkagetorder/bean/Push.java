package cc.chenghong.vkagetorder.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 极光推送实体类
 * Created by 何成龙 on 2016/7/7.
 */
public class Push implements Serializable{
    //    Bundle[{cn.jpush.android.ALERT=blue1我阿斯蒂芬, cn.jpush.android.EXTRA={},
//            cn.jpush.android.NOTIFICATION_ID=1969344771,
//            cn.jpush.android.NOTIFICATION_CONTENT_TITLE=VkaGetOrder,
//            cn.jpush.android.MSG_ID=1969344771}]
    private String ALERT;//普通通知内容
    private List<String> EXTRA;//额外的通知内容
    private int NOTIFICATION_ID;//状态栏通知id
    private String NOTIFICATION_CONTENT_TITLE;//状态栏通知标题
    private String MSG_ID;//极光推送消息id

    public Push(String ALERT, List<String> EXTRA, int NOTIFICATION_ID, String NOTIFICATION_CONTENT_TITLE, String MSG_ID) {
        this.ALERT = ALERT;
        this.EXTRA = EXTRA;
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.NOTIFICATION_CONTENT_TITLE = NOTIFICATION_CONTENT_TITLE;
        this.MSG_ID = MSG_ID;
    }

    public String getALERT() {
        return ALERT;
    }

    public void setALERT(String ALERT) {
        this.ALERT = ALERT;
    }

    public List<String> getEXTRA() {
        return EXTRA;
    }

    public void setEXTRA(List<String> EXTRA) {
        this.EXTRA = EXTRA;
    }

    public int getNOTIFICATION_ID() {
        return NOTIFICATION_ID;
    }

    public void setNOTIFICATION_ID(int NOTIFICATION_ID) {
        this.NOTIFICATION_ID = NOTIFICATION_ID;
    }

    public String getNOTIFICATION_CONTENT_TITLE() {
        return NOTIFICATION_CONTENT_TITLE;
    }

    public void setNOTIFICATION_CONTENT_TITLE(String NOTIFICATION_CONTENT_TITLE) {
        this.NOTIFICATION_CONTENT_TITLE = NOTIFICATION_CONTENT_TITLE;
    }

    public String getMSG_ID() {
        return MSG_ID;
    }

    public void setMSG_ID(String MSG_ID) {
        this.MSG_ID = MSG_ID;
    }
}
