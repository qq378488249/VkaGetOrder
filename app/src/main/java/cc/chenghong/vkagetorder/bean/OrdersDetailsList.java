package cc.chenghong.vkagetorder.bean;

import cc.chenghong.vkagetorder.response.ListResponse;

/** 订单菜品列表
 * Created by 何成龙 on 2016/7/21.
 */
public class OrdersDetailsList extends ListResponse<OrdersDetailsList>{

    /**
     * id : 193738
     * ordersId : 108672
     * productId : 71
     * productName : 鸡汤
     * attributeIds :
     * attributeNames :
     * price : 0.01
     * count : 1
     * source : 1
     * categoriesId : 34
     * productId2 : null
     * created : 2016-07-21 10:58
     */

    private int id;
    private int ordersId;
    private int productId;
    private String productName;
    private String attributeIds;
    private String attributeNames;
    private double price;
    private int count;
    private int source;
    private int categoriesId;
    private Object productId2;
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(String attributeIds) {
        this.attributeIds = attributeIds;
    }

    public String getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(String attributeNames) {
        this.attributeNames = attributeNames;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public void setCategoriesId(int categoriesId) {
        this.categoriesId = categoriesId;
    }

    public Object getProductId2() {
        return productId2;
    }

    public void setProductId2(Object productId2) {
        this.productId2 = productId2;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
