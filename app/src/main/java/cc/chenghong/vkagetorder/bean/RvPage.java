package cc.chenghong.vkagetorder.bean;


import java.util.ArrayList;
import java.util.List;

import cc.chenghong.vkagetorder.adapter.RvAdapter;

/** rv分页工具类
 * Created by hcl on 2016/3/16.
 */

public class RvPage<T> {
    public List<T> list = new ArrayList<>();//数据集合
    public RvAdapter<T> adapter ;//适配器
    public int pageIndex;//页数
    public int limit;//每页个数

    public RvPage(int pageIndex, int limit) {
        this.pageIndex = pageIndex;
        this.limit = limit;
    }
    public RvPage(int limit) {
        this.pageIndex = 0;
        this.limit = limit;
    }

    public RvPage() {
        this.pageIndex = 0;
        this.limit = 10;
    }

    /**
     * 下一页
     */
    public int nextPage(){
        return pageIndex++;
    }

    /**
     * 第一页
     * @return
     */
    public int firstPage(){
        return pageIndex=0;
    }

    /**
     * 是否是第一页
     * @return
     */
    public boolean isFirstPage(){
        return pageIndex == 0;
    }


}
