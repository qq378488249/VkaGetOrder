package cc.chenghong.vkagetorder.api;

/**
 * 接口工具类
 * Created by 何成龙 on 2016/7/12.
 */
public class Api {
    /**
     * 主接口
     */
    public static String MAIN = "https://api.v-ka.com";
//    public static String MAIN = "https://api.vi-ni.com";// 测试接口
//    public static String MAIN = "http://192.168.1.10:8081";
    /**
     * 正式接口
     */
    public static String MAIN_OFFICIAL = "https://api.v-ka.com";// 正式接口
    /**
     * 测试接口
     */
    public static String MAIN_TEST = "https://api.vi-ni.com";// 测试接口
    /**
     * 登陆
     * /pc/v2/getOrder/login
     */
    public static String LOGIN = MAIN + "/pc/v1/getOrder/login";
    /**
     * 获取订单列表
     * /pc/v2/orders/
     */
    public static String GET_ORDER = MAIN + "/pc/v2/orders/";
    /**
     * 获取订单详情
     * /pc/v2/orders/OrdersDetails/
     */
    public static String GET_ORDER_DETAIL = MAIN + "/pc/v2/orders/OrdersDetails/";
    /**
     * 修改订单状态
     * /pc/v2/orders/updateOrder/
     */
    public static String UPDATE_ORDER_STATE = MAIN + "/pc/v2/orders/updateOrder/";
    /**
     * 获取已取消订单列表
     * /pc/v2/orders/by/
     */
    public static String GET_CANCEL_ORDER = MAIN + "/pc/v2/orders/by/";
    /**
     * 取消订单
     * /pc/v2/orders/cancel/
     */
    public static String CANCEL_ORDER = MAIN + "/pc/v1/orders/cancel/";
    /**
     * 根据订单编号获取订单
     * /pc/v1/orders
     */
    public static String GET_ORDER_BY_ID = MAIN+"/pc/v1/orders/";
}
