package models.EletronicItem.entities;

import models.Product.entities.Product;

import java.math.BigDecimal;

public class EletronicItem extends Product {
    private String producer;
    private String type;

    public EletronicItem(String id, String name, BigDecimal price, String description, int stock,
                         String producer, String type) {
        super(id, name, price, description, stock);
        this.producer = producer;
        this.type = type;
    }

    public EletronicItem(String name, BigDecimal price, String description, int stock,
                         String producer, String type) {
        this(null, name, price, description, stock, producer, type);
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EletronicItem{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                ", stock=" + getStock() +
                ", producer='" + producer + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
