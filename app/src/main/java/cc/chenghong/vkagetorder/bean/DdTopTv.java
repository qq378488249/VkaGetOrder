package cc.chenghong.vkagetorder.bean;

import java.io.Serializable;

/** 手机版订单顶部textview
 * Created by 何成龙 on 2016/8/8.
 */
public class DdTopTv implements Serializable{
    public String name;
    public boolean isNew = false;
    public boolean isSelect = false;

    public DdTopTv(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
