package cc.chenghong.vkagetorder.bean;

import java.math.BigDecimal;
import java.util.Date;

//import com.usercard.common.PayType;
//import com.usercard.utils.DataMergeUtil;
//import com.usercard.utils.TransType;
//import org.codehaus.jackson.map.annotate.JsonSerialize;
//
//import com.usercard.utils.CustomDateTimeSerializer;

public class Transaction {
	
    private Long id;

    /**
     * 会员卡Id
     * @author guoyu
     */
    private Long cardId;

    /**
     * @author guoyu
     */
    private Long userId;

    /**
     * 操作者Id
     * @author guoyu
     */
    private Long operatorId;

    /**
     * 操作者名字
     * @author guoyu
     */
    private String operatorName;

    /**
     * 店铺Id
     * @author guoyu
     */
    private Long storeId;

    /**
     * 店铺名称
     * @author guoyu
     */
    private String storeName;

    /**
     * 会员支付金额
     * @author guoyu
     */
    private BigDecimal amount;

    /**
     * 赠送金额
     * @author guoyu
     */
    private BigDecimal give;

    /**
     * 总金额
     * @author guoyu
     */
    private BigDecimal total;

    /**
     * 余额
     * @author guoyu
     */
    private BigDecimal balance;

    /**
     *
     * @author guoyu
     */
    private Integer type;

    /**
     * 备注
     * @author guoyu
     */
    private String remark;

    /**
     * 消费积分
     * @author guoyu
     */
    private Integer point;

    /**
     * 积分余额
     * @author guoyu
     */
    private Integer pointBalance;

    /**
     * 优惠劵Id集合
     * @author guoyu
     */
    private String tickets;

    /**
     * 优惠劵名字集合
     * @author guoyu
     */
    private String ticketNames;


	private Integer payType;
	
	private Date created;

    /**
     * 现金支付金额
     * @author guoyu
     */
    private BigDecimal cashAmount;

    /**
     * 银联卡支付金额
     * @author guoyu
     */
    private BigDecimal creditAmount;

    /**
     *  1:已成功 2:已退单
     *
     * 订单状态
     * @author guoyu
     */
    private Integer status;


    private BigDecimal discount;  //被折扣掉的金额(优惠券)
    
   
	private BigDecimal pointDiscount;	//积分抵扣金额
	
	private BigDecimal weixinAmount;//微信支付
    
	private BigDecimal levelDiscount;//会员等级折扣
	
	private BigDecimal itemSubtotal; //订单原始金额
	
	private Integer source;//交易类型来源

	public Transaction() {
	}

	/**

    public Transaction() {
    }

    /**

	 * 阿里支付
	 * @author guoyu
	 */
	private BigDecimal alipayAmount;

    /**
     * QQ支付
     * @author guoyu
     */
    private BigDecimal qqpayAmount;

    public BigDecimal getQqpayAmount() {
        return qqpayAmount;
    }

    public void setQqpayAmount(BigDecimal qqpayAmount) {
        this.qqpayAmount = qqpayAmount;
    }

    /**
	 * 获得计算出来的应付金额,请所有的值都set完之后,再调后此方法
	 * @author guoyu
	 */
	public BigDecimal getComputeItemSubtotal(){

//		if (this.getType() != TransType.PAYMENT.getValue()){
//			return new BigDecimal(0);
//		}
//
//		return DataMergeUtil.addValue(this.getAmount(), this.getCashAmount(), this.getWeixinAmount(), this.getAlipayAmount(),
//				this.getDiscount(), this.getLevelDiscount(), this.getPointDiscount(), this.getCreditAmount(),  this.getQqpayAmount());
		return new BigDecimal(0);
	}

	public BigDecimal getAlipayAmount() {
		return alipayAmount;
	}

	public void setAlipayAmount(BigDecimal alipayAmount) {
		this.alipayAmount = alipayAmount;
	}

	/**
	 * @return the pointDiscount
	 */
	public BigDecimal getPointDiscount() {
		return pointDiscount;
	}

	/**
	 * @param pointDiscount the pointDiscount to set
	 */
	public void setPointDiscount(BigDecimal pointDiscount) {
		this.pointDiscount = pointDiscount;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName == null ? null : operatorName.trim();
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName == null ? null : storeName.trim();
	}

	public BigDecimal getAmount() {
		if(amount == null) amount = new BigDecimal(0);
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getGive() {
		if(give == null) give = new BigDecimal(0);
		return give;
	}

	public void setGive(BigDecimal give) {
		this.give = give;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getPoint() {
		if(point == null) point = 0;
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getPointBalance() {
		return pointBalance;
	}

	public void setPointBalance(Integer pointBalance) {
		this.pointBalance = pointBalance;
	}

	public String getTickets() {
		return tickets;
	}

	public void setTickets(String tickets) {
		this.tickets = tickets;
	}

	public String getTicketNames() {
		return ticketNames;
	}

	public void setTicketNames(String ticketNames) {
		this.ticketNames = ticketNames;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public BigDecimal getCashAmount() {
		if(cashAmount == null) cashAmount = new BigDecimal(0);
		return cashAmount;
	}

	public void setCashAmount(BigDecimal cashAmount) {
		this.cashAmount = cashAmount;
	}

	public BigDecimal getCreditAmount() {
		if(creditAmount == null) creditAmount = new BigDecimal(0);
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getWeixinAmount() {
		return weixinAmount;
	}

	public void setWeixinAmount(BigDecimal weixinAmount) {
		this.weixinAmount = weixinAmount;
	}

	public BigDecimal getLevelDiscount() {
		return levelDiscount;
	}

	public void setLevelDiscount(BigDecimal levelDiscount) {
		this.levelDiscount = levelDiscount;
	}

	public BigDecimal getItemSubtotal() {
		return itemSubtotal;
	}

	public void setItemSubtotal(BigDecimal itemSubtotal) {
		this.itemSubtotal = itemSubtotal;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}