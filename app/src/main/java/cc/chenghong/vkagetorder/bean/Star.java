package cc.chenghong.vkagetorder.bean;

/**
 * Created by hcl on 2016/7/6.
 */
public class Star {
    private boolean isSelect;//是否选中

    public Star(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
