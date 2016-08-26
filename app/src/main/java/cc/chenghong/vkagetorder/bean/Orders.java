package cc.chenghong.vkagetorder.bean;

import java.util.ArrayList;
import java.util.List;

import cc.chenghong.vkagetorder.response.ListResponse;
import cc.chenghong.vkagetorder.util.StringUtils;

/** 订单实体类
 * Created by 何成龙 on 2016/7/21.
 */
public class Orders extends ListResponse<Orders>{

    /**
     * id : 108672
     * storeId : 232
     * storeName : 小马快乐01
     * cardId : 121678
     * mobile : 18692186540
     * address : 其实
     * amount : 0.02
     * payType : 2
     * transactionId : 312307
     * isTakeaway : 1
     * status : 2
     * discount :
     * tableId :
     * tableNum :
     * itemSubtotal : 0.02
     * orderSource : 1
     * created : 2016-07-21 10:58
     * userId :
     * outTradeNo :
     * payStatus : 1
     * ordersDetailsList : []
     * cardName : 曹操
     * storeMobile :
     */

    private int id;
    private int storeId;
    private String storeName;
    private int cardId;
    private String mobile;
    private String address;
    private double amount;
    private int payType;
    private int transactionId;
    private int isTakeaway;
    private int status;
    private String discount;
    private String tableId;
    private String tableNum;
    private double itemSubtotal;
    private int orderSource;
    private String created;
    private String userId;
    private String outTradeNo;
    private int payStatus;
    private int orderType;
    private List<OrdersDetailsList> ordersDetailsList = new ArrayList<>();
    private String cardName;
    private String storeMobile;
    private String sinceTime;	//自提时间
    private Card card;//会员卡

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getSinceTime() {
        return sinceTime;
    }

    public void setSinceTime(String sinceTime) {
        this.sinceTime = sinceTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    public double getItemSubtotal() {
        return itemSubtotal;
    }

    public void setItemSubtotal(double itemSubtotal) {
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

    public List<OrdersDetailsList> getOrdersDetailsList() {
        return ordersDetailsList;
    }

    public void setOrdersDetailsList(List<OrdersDetailsList> ordersDetailsList) {
        this.ordersDetailsList = ordersDetailsList;
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

    public Orders(int id, int storeId, String storeName, int cardId, String mobile, String address, double amount, int payType, int transactionId, int isTakeaway, int status, String discount, String tableId, String tableNum, double itemSubtotal, int orderSource, String created, String userId, String outTradeNo, int payStatus, List<OrdersDetailsList> ordersDetailsList, String cardName, String storeMobile) {
        this.id = id;
        this.storeId = storeId;
        this.storeName = storeName;
        this.cardId = cardId;
        this.mobile = mobile;
        this.address = address;
        this.amount = amount;
        this.payType = payType;
        this.transactionId = transactionId;
        this.isTakeaway = isTakeaway;
        this.status = status;
        this.discount = discount;
        this.tableId = tableId;
        this.tableNum = tableNum;
        this.itemSubtotal = itemSubtotal;
        this.orderSource = orderSource;
        this.created = created;
        this.userId = userId;
        this.outTradeNo = outTradeNo;
        this.payStatus = payStatus;
        this.ordersDetailsList = ordersDetailsList;
        this.cardName = cardName;
        this.storeMobile = storeMobile;
    }

    public Orders(Order order){
        if (StringUtils.noEmpty(order.getId())){
            this.id = order.getId();
        }
        if (StringUtils.noEmpty(order.getStoreId())){
            this.storeId = order.getStoreId();
        }
        if (StringUtils.noEmpty(order.getStoreName())){
            this.storeName = order.getStoreName();
        }
        if (StringUtils.noEmpty(order.getCardId())){
            this.cardId = order.getCardId();
        }
        if (StringUtils.noEmpty(order.getStoreName())){
            this.storeName = order.getStoreName();
        }
        if (StringUtils.noEmpty(order.getAddress())){
            this.address = order.getAddress();
        }
        if (StringUtils.noEmpty(order.getAmount())){
            this.amount = order.getAmount();
        }
        if (StringUtils.noEmpty(order.getPayType())){
            this.payType = order.getPayType();
        }
        if (StringUtils.noEmpty(order.getTransactionId())){
            this.transactionId = order.getTransactionId();
        }
        if (StringUtils.noEmpty(order.getIsTakeaway())){
            this.isTakeaway = order.getIsTakeaway();
        }
        if (StringUtils.noEmpty(order.getStatus())){
            this.status = order.getStatus();
        }
        if (StringUtils.noEmpty(order.getDiscount())){
            this.discount = order.getDiscount();
        }
        if (StringUtils.noEmpty(order.getTableId())){
            this.tableId = order.getTableId();
        }
        if (StringUtils.noEmpty(order.getTableNum())){
            this.tableNum = order.getTableNum();
        }
        if (StringUtils.noEmpty(order.getItemSubtotal())){
            this.itemSubtotal = order.getItemSubtotal();
        }
        if (StringUtils.noEmpty(order.getOrderSource())){
            this.orderSource = order.getOrderSource();
        }
        if (StringUtils.noEmpty(order.getCardId())){
            this.created = order.getCreated();
        }
        if (StringUtils.noEmpty(order.getUserId())){
            this.userId = order.getUserId();
        }
        if (StringUtils.noEmpty(order.getOutTradeNo())){
            this.outTradeNo = order.getOutTradeNo();
        }
        if (StringUtils.noEmpty(order.getPayStatus())){
            this.payStatus = order.getPayStatus();
        }
        if (StringUtils.noEmpty(order.getOrdersDetailsList())){
            this.ordersDetailsList = order.getOrdersDetailsList();
        }
        if (StringUtils.noEmpty(order.getCardName())){
            this.cardName = order.getCardName();
        }
        if (StringUtils.noEmpty(order.getStoreMobile())){
            this.storeMobile = order.getStoreMobile();
        }
        if (StringUtils.noEmpty(order.getPayType())){
            this.payType = order.getPayType();
        }

    }
}
