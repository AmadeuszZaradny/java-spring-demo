package pl.zaradny.springApp.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Product {

    private final String id;
    private final String name;
    private final Price price;
    private final Image image;
    private final Description description;
    private final LocalDateTime createdAt;


    public Product(String id, String name, Price price, Image image, Description description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Description getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(image, product.image) &&
                Objects.equals(description, product.description) &&
                Objects.equals(createdAt, product.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, image, description, createdAt);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", description=" + description +
                ", createdAt=" + createdAt +
                '}';
    }

    public static final class ProductBuilder{

        //required
        private final String id;
        private final String name;
        private final Price price;
        private final LocalDateTime createdAt;

        //optional
        private Image image;
        private Description description;

        public ProductBuilder(String id, String name, Price price, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.createdAt = createdAt;
        }

        public ProductBuilder setImage(Image newImage){
            this.image = newImage;
            return this;
        }

        public ProductBuilder setDescription(Description description){
            this.description = description;
            return this;
        }

        public Product build(){
            return new Product(this.id, this.name, this.price, this.image, this.description, this.createdAt);
        }
    }
}
