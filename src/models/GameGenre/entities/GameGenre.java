package models.GameGenre.entities;

public class GameGenre {
    private String genreId;
    private String productId;

    public GameGenre(String genreId, String productId) {
        this.genreId = genreId;
        this.productId = productId;
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "GameGenre{" +
                "genreId='" + genreId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
