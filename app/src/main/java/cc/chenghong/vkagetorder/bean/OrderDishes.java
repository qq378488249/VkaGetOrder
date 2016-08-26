package cc.chenghong.vkagetorder.bean;

/**
 * 订单菜品
 * Created by hcl on 2016/7/5.
 */
public class OrderDishes {
    private String name;
    private String number;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public OrderDishes(String name, String number, String price) {
        this.name = name;
        this.number = number;
        this.price = price;
    }

    public OrderDishes() {
    }
}
