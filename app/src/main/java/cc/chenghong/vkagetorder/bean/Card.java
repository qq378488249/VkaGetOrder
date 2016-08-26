package cc.chenghong.vkagetorder.bean;

import cc.chenghong.vkagetorder.response.ObjectResponse;

/** 会员卡实体类
 * Created by 何成龙 on 2016/8/5.
 */
public class Card extends ObjectResponse<Card>{

    /**
     * id : 121678
     * mobile : 18692186540
     * name : 曹操
     * otherMobile : 18692186540
     * storeId : 231
     * storeName : 小马快乐
     * userId :
     * idCard :
     * birthday :
     * birthday2 :
     * totalRmb : 400
     * balance : 9.72
     * point : 102
     * locked : 0
     * sex : 0
     * activated :
     * activationStore :
     * ticketName :
     * type : 1
     * province : 湖南省
     * city : 岳阳市
     * district : 岳阳楼区
     * openId : oTUIst3zr3U35ufeliCJ4uwkrPgQ
     * lastDay :
     * totalTimes : 61
     * totalAmount : 133.49
     * bindOpenId : 0
     * wxCardCode :
     * attribute : 0
     * discount : 10
     * version : 196
     * created : 2016-07-08 11:27
     * ticketsCount :
     * experience :
     * levelName :
     * levelId :
     */

    private int id;
    private String mobile;
    private String name;
    private String otherMobile;
    private int storeId;
    private String storeName;
    private String userId;
    private String idCard;
    private String birthday;
    private String birthday2;
    private double totalRmb;
    private double balance;
    private int point;
    private int locked;
    private int sex;
    private String activated;
    private String activationStore;
    private String ticketName;
    private int type;
    private String province;
    private String city;
    private String district;
    private String openId;
    private String lastDay;
    private int totalTimes;
    private double totalAmount;
    private int bindOpenId;
    private String wxCardCode;
    private int attribute;
    private int discount;
    private int version;
    private String created;
    private String ticketsCount;
    private String experience;
    private String levelName;
    private String levelId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherMobile() {
        return otherMobile;
    }

    public void setOtherMobile(String otherMobile) {
        this.otherMobile = otherMobile;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday2() {
        return birthday2;
    }

    public void setBirthday2(String birthday2) {
        this.birthday2 = birthday2;
    }

    public double getTotalRmb() {
        return totalRmb;
    }

    public void setTotalRmb(int totalRmb) {
        this.totalRmb = totalRmb;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public String getActivationStore() {
        return activationStore;
    }

    public void setActivationStore(String activationStore) {
        this.activationStore = activationStore;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLastDay() {
        return lastDay;
    }

    public void setLastDay(String lastDay) {
        this.lastDay = lastDay;
    }

    public int getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(int totalTimes) {
        this.totalTimes = totalTimes;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getBindOpenId() {
        return bindOpenId;
    }

    public void setBindOpenId(int bindOpenId) {
        this.bindOpenId = bindOpenId;
    }

    public String getWxCardCode() {
        return wxCardCode;
    }

    public void setWxCardCode(String wxCardCode) {
        this.wxCardCode = wxCardCode;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTicketsCount() {
        return ticketsCount;
    }

    public void setTicketsCount(String ticketsCount) {
        this.ticketsCount = ticketsCount;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }
}
