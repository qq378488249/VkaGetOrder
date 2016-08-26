package cc.chenghong.vkagetorder.bean;

/**
 * 系统设置实体类
 * Created by 何成龙 on 2016/8/9.
 */
public class Set {
    public String name;
    public boolean isSelect = false;
    public boolean isSwith = false;

    public Set(String name, boolean isSwith) {
        this.name = name;
        this.isSwith = isSwith;
    }

    public Set(String name, boolean isSwith,boolean isSelect) {
        this.name = name;
        this.isSwith = isSwith;
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSwith() {
        return isSwith;
    }

    public void setSwith(boolean swith) {
        isSwith = swith;
    }

    public Set(String name) {
        this.name = name;
    }
}
