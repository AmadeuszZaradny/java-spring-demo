package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {

    private final String name;
    private final PriceDto price;
    private final ImageDto image;
    private final DescriptionDto description;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name,
                             @JsonProperty("price") PriceDto price,
                             @JsonProperty("image") ImageDto image,
                             @JsonProperty("description") DescriptionDto description) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public PriceDto getPrice() {
        return price;
    }

    public DescriptionDto getDescription() {
        return description;
    }

    public ImageDto getImage() {
        return image;
    }

    public boolean isNameValid(){
        return name != null && !name.equals("");
    }

    public boolean isPriceValid(){
        return price != null && price.getAmount() != null && !price.getAmount().equals("")
                && price.getCurrency() != null && !price.getCurrency().equals("");
    }

    public boolean isImageValid(){
        return image != null && !image.getUrl().equals("") && image.getUrl() != null;
    }

    public boolean isDescriptionValid(){
        return description != null && !description.getText().equals("") && description.getText().length() <= 400;
    }

    public boolean isValidToCreate(){
        return isNameValid() && isPriceValid();
    }

    public boolean isValidToUpdate(){
        return isNameValid() || isPriceValid() || isImageValid() || isDescriptionValid();
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", description=" + description +
                '}';
    }

    public static final class ProductRequestDtoBuilder{
        //requited
        private final String name;
        private final PriceDto price;
        //optional
        private ImageDto image;
        private  DescriptionDto description;

        public ProductRequestDtoBuilder(String name, PriceDto price) {
            this.name = name;
            this.price = price;
        }

        public ProductRequestDtoBuilder setDescription(DescriptionDto description){
            this.description = description;
            return this;
        }

        public ProductRequestDtoBuilder setImage(ImageDto image){
            this.image = image;
            return this;
        }

        public ProductRequestDto build(){
            return new ProductRequestDto(this.name, this.price, this.image, this.description);
        }
    }
}
