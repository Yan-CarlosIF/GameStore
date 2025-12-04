package models.Game.entities;

import models.Product.entities.Product;

import java.math.BigDecimal;
import java.util.Date;

public class Game extends Product {
    private String developer;
    private String platform;
    private Date releaseDate;

    public Game(String id, String name, BigDecimal price, String description, int stock,
                String developer, String platform, Date releaseDate) {
        super(id, name, price, description, stock);
        this.developer = developer;
        this.platform = platform;
        this.releaseDate = releaseDate;
    }

    public Game(String name, BigDecimal price, String description, int stock,
                String developer, String platform, Date releaseDate) {
        this(null, name, price, description, stock, developer, platform, releaseDate);
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", price=" + getPrice() +
                ", description='" + getDescription() + '\'' +
                ", stock=" + getStock() +
                ", developer='" + developer + '\'' +
                ", platform='" + platform + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
