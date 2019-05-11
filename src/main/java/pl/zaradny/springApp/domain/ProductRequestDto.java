package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {

    private final String name;
    private final PriceDto price;
    private final ImageDto image;
    private final DescriptionDto description;
    private final List<TagDto> tags;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name,
                             @JsonProperty("price") PriceDto price,
                             @JsonProperty("image") ImageDto image,
                             @JsonProperty("description") DescriptionDto description,
                             @JsonProperty("tags") List<TagDto> tags) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.tags = tags;
    }

    public List<TagDto> getTags() {
        return tags;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRequestDto that = (ProductRequestDto) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(image, that.image) &&
                Objects.equals(description, that.description) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, image, description, tags);
    }
}
