package pl.zaradny.springApp.domain;

import com.google.common.base.Preconditions;
import pl.zaradny.springApp.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class Product {

    private final String id;
    private final String name;
    private final Price price;
    private final Image image;
    private final Description description;
    private final LocalDateTime createdAt;
    private final Set<Tag> tags;

    private Product(String id, String name, Price price, LocalDateTime createdAt, Image image, Description description,
                    Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.createdAt = createdAt;
        this.tags = tags;
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

    public Optional<Set<Tag>> getTags() {
        return Optional.ofNullable(tags);
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
                Objects.equals(createdAt, product.createdAt) &&
                Objects.equals(tags, product.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, image, description, createdAt, tags);
    }

    public static ProductBuilder build(){
        return new ProductBuilder();
    }

    public static class ProductBuilder{

        private String id;
        private String name;
        private Price price;
        private LocalDateTime createdAt;
        private Image image;
        private Description description;
        private Set<Tag> tags;

        private ProductBuilder() {}

        public ProductBuilder withTags(Set<Tag> tags){
            this.tags = tags;
            return this;
        }

        public ProductBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public ProductBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder withPrice(Price price) {
            this.price = price;
            return this;
        }

        public ProductBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProductBuilder withImage(Image newImage){
            this.image = newImage;
            return this;
        }

        public ProductBuilder withDescription(Description description){
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
                return new Product(this.id, this.name, this.price, this.createdAt, this.image, this.description,
                        this.tags);
            }else throw new BadProductFieldException();
        }
    }
}
