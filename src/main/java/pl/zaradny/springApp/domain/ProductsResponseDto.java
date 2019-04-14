package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class ProductsResponseDto {

    private final List<ProductResponseDto> products;

    @JsonCreator
    public ProductsResponseDto(@JsonProperty("products") List<ProductResponseDto> products) {
        this.products = products;
    }

    public List<ProductResponseDto> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "ProductsResponseDto{" +
                "products=" + products +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductsResponseDto that = (ProductsResponseDto) o;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
