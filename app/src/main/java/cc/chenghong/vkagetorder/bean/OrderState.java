package cc.chenghong.vkagetorder.bean;

/**
 * Created by 何成龙 on 2016/7/27.
 */
public class OrderState {
    public String name;
    public boolean isSelect;

    public OrderState(String name, boolean isSelect) {
        this.name = name;
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
}
