package cc.chenghong.vkagetorder.bean;

import cc.chenghong.vkagetorder.response.ObjectResponse;

/**
 * Created by 何成龙 on 2016/7/14.
 */
public class Account extends ObjectResponse<Account> {

    /**
     * storeId : 282
     * storeName : 万达店
     * employeeId : 366
     * employeeName : 何成龙
     * accessToken : a9bcdd5140ba50a0a2bb80c2b07a1939bd0e55
     * roleName : 店长
     */

    private int storeId;
    private String storeName;
    private int employeeId;
    private String employeeName;
    private String accessToken;
    private String roleName;
    private String parentCode;//总店编号

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    private String storeCode;//门店编号

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
