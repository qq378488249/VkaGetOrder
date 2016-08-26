package com.ligthblue.bean;

import com.ligthblue.adapter.LvAdapter;

import java.util.ArrayList;
import java.util.List;

/** 分页工具类
 * Created by hcl on 2016/3/16.
 */

public class Page<T> {
    public List<T> list = new ArrayList<>();//数据集合
    public LvAdapter<T> adapter;//适配器
    public int pageIndex;//页数
    public int pageSize;//每页个数

    public Page(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Page() {
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
