package cc.chenghong.vkagetorder.bean;

import java.util.List;

import cc.chenghong.vkagetorder.response.ObjectResponse;

/**
 * 单条订单实体类
 * Created by hcl on 2016/7/4.
 */
public class Order extends ObjectResponse<Order> {
    /**
     * id : 108750
     * storeId : 235
     * storeName : 小马快乐04
     * cardId : 121676
     * mobile : 15200000000
     * address : 666666666
     * amount : 12
     * payType : 3
     * transactionId :
     * isTakeaway : 1
     * isSince :
     * status : 4
     * discount :
     * tableId :
     * tableNum :
     * itemSubtotal : 12
     * orderSource : 1
     * created : 2016-07-25 17:42
     * userId :
     * outTradeNo :
     * payStatus : 0
     * orderType : 1
     * ordersDetailsList : [{"id":193860,"ordersId":108750,"productId":67,"productName":"桂花菜","attributeIds":"","attributeNames":"","price":12,"count":1,"source":1,"categoriesId":33,"productId2":"","created":"2016-07-25 17:42"}]
     * cardName :
     * storeMobile :
     */

    private int id;
    private int storeId;
    private String storeName;
    private int cardId;
    private String mobile;
    private String address;
    private int amount;
    private int payType;
    private int transactionId;
    private int isTakeaway;
    private String isSince;
    private int status;
    private String discount;
    private String tableId;
    private String tableNum;
    private int itemSubtotal;
    private int orderSource;
    private String created;
    private String userId;
    private String outTradeNo;
    private int payStatus;
    private int orderType;
    private String cardName;
    private String storeMobile;
    /**
     * id : 193860
     * ordersId : 108750
     * productId : 67
     * productName : 桂花菜
     * attributeIds :
     * attributeNames :
     * price : 12
     * count : 1
     * source : 1
     * categoriesId : 33
     * productId2 :
     * created : 2016-07-25 17:42
     */

    private List<OrdersDetailsList> ordersDetailsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getIsTakeaway() {
        return isTakeaway;
    }

    public void setIsTakeaway(int isTakeaway) {
        this.isTakeaway = isTakeaway;
    }

    public String getIsSince() {
        return isSince;
    }

    public void setIsSince(String isSince) {
        this.isSince = isSince;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableNum() {
        return tableNum;
    }

    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }

    public int getItemSubtotal() {
        return itemSubtotal;
    }

    public void setItemSubtotal(int itemSubtotal) {
        this.itemSubtotal = itemSubtotal;
    }

    public int getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        this.orderSource = orderSource;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public List<OrdersDetailsList> getOrdersDetailsList() {
        return ordersDetailsList;
    }

    public void setOrdersDetailsList(List<OrdersDetailsList> ordersDetailsList) {
        this.ordersDetailsList = ordersDetailsList;
    }

}
