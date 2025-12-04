package models.OrderProduct.entities;

import java.math.BigDecimal;

public class OrderProduct {
    private String id;
    private int amount;
    private BigDecimal itemsTotal;
    private String orderId;
    private String productId;

    public OrderProduct(String id, int amount, BigDecimal itemsTotal, String orderId, String productId) {
        this.id = id;
        this.amount = amount;
        this.itemsTotal = itemsTotal;
        this.orderId = orderId;
        this.productId = productId;
    }

    public OrderProduct(int amount, BigDecimal itemsTotal, String orderId, String productId) {
        this(null, amount, itemsTotal, orderId, productId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(BigDecimal itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", itemsTotal=" + itemsTotal +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
