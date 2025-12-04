package models.Order.entitites;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String id;
    private BigDecimal totalValue;
    private String status; // delivered, shipping, pending, cancelled
    private Date date;
    private String clientCpf;

    public Order(String id, BigDecimal totalValue, String status, Date date, String clientCpf) {
        this.id = id;
        this.totalValue = totalValue;
        this.status = status;
        this.date = date;
        this.clientCpf = clientCpf;
    }

    public Order(BigDecimal totalValue, String status, Date date, String clientCpf) {
        this(null, totalValue, status, date, clientCpf);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getClientCpf() { return clientCpf; }
    public void setClientCpf(String clientCpf) { this.clientCpf = clientCpf; }

    @Override
    public String toString() {
        return "models.Order{" +
                "id='" + id + '\'' +
                ", totalValue=" + totalValue +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", clientCpf='" + clientCpf + '\'' +
                '}';
    }
}
