package pl.zaradny.springApp.testUtils;

import pl.zaradny.springApp.domain.*;

import java.util.List;

public final class ProductRequestDtoBuilder {
    private final String name;
    private final PriceDto price;
    private ImageDto image;
    private DescriptionDto description;
    private List<TagDto> tags;

    public ProductRequestDtoBuilder(String name, PriceDto price) {
        this.name = name;
        this.price = price;
    }

    public ProductRequestDtoBuilder withDescription(DescriptionDto description){
        this.description = description;
        return this;
    }

    public ProductRequestDtoBuilder withImage(ImageDto image){
        this.image = image;
        return this;
    }

    public ProductRequestDtoBuilder withTags(List<TagDto> tags){
        this.tags = tags;
        return this;
    }

    public ProductRequestDto build(){
        return new ProductRequestDto(this.name, this.price, this.image, this.description, this.tags);
    }
}
