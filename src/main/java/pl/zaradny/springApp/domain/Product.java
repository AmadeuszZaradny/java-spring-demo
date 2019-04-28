package pl.zaradny.springApp.domain;

import com.google.common.base.Preconditions;
import pl.zaradny.springApp.exceptions.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public final class Product {

    private final String id;
    private final String name;
    private final Price price;
    private final Image image;
    private final Description description;
    private final LocalDateTime createdAt;

    private Product(String id, String name, Price price, LocalDateTime createdAt, Image image, Description description) {
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

    public Optional<Description> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<Image> getImage() {
        return Optional.ofNullable(image);
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

    public static Buildable build(){
        return new ProductBuilder();
    }

    public interface Buildable {
        Product build();
        Buildable withId(String id);
        Buildable withName(String name);
        Buildable withPrice(Price price);
        Buildable withCreatedAt(LocalDateTime createdAt);
        Buildable withImage(Image newImage);
        Buildable withDescription(Description description);
    }

    private static class ProductBuilder implements Buildable{

        private String id;
        private String name;
        private Price price;
        private LocalDateTime createdAt;
        private Image image;
        private Description description;

        public ProductBuilder() {}

        public Buildable withId(String id) {
            this.id = id;
            return this;
        }

        public Buildable withName(String name) {
            this.name = name;
            return this;
        }

        public Buildable withPrice(Price price) {
            this.price = price;
            return this;
        }

        public Buildable withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Buildable withImage(Image newImage){
            this.image = newImage;
            return this;
        }

        public Buildable withDescription(Description description){
            this.description = description;
            return this;
        }

        private boolean isNameValid() {
            try{
                String validatedName = Preconditions.checkNotNull(this.name);
                if(validatedName.isEmpty()) throw new EmptyProductNameException();
            }catch (NullPointerException e){
                throw new EmptyProductNameException();
            }
            return true;
        }

        private boolean isPriceValid(){
            try {
                Preconditions.checkNotNull(this.price);
            }catch (NullPointerException e){
                throw new ProductPriceIsNullException();
            }
            return true;
        }

        private boolean isCreatedAtValid(){
            try{
                Preconditions.checkNotNull(this.createdAt);
            }catch (NullPointerException e){
                throw new EmptyCreatedAtException();
            }
            return true;
        }

        private boolean isIdValid(){
            try {
                Preconditions.checkNotNull(this.id);
            }catch (NullPointerException e){
                throw new EmptyIdException();
            }
            return true;
        }

        public Product build(){
            if(isIdValid() && isCreatedAtValid() && isPriceValid() && isNameValid()){
                return new Product(this.id, this.name, this.price, this.createdAt, this.image, this.description);
            }else throw new BadProductFieldException();
        }
    }
}
