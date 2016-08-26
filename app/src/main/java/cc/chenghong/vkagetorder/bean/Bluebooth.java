package cc.chenghong.vkagetorder.bean;

/**
 * Created by 何成龙 on 2016/7/28.
 */
public class Bluebooth {
    public String name;
    public String address;
    public boolean isConnection;//是否连接

    public Bluebooth(String name, String address, boolean isConnection) {
        this.name = name;
        this.address = address;
        this.isConnection = isConnection;
    }

    public Bluebooth() {
    }
}
