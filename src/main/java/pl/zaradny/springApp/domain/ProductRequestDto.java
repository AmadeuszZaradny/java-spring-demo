package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequestDto {

    private final String name;
    private final PriceDto price;

    @JsonCreator
    public ProductRequestDto(@JsonProperty("name") String name,
                             @JsonProperty("price") PriceDto price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public PriceDto getPrice() {
        return price;
    }

    public boolean isValidToCreate(){
        return name != null && !name.equals("") && price != null && price.getAmount() != null
                && !price.getAmount().equals("") && price.getCurrency() != null && !price.getCurrency().equals("");
    }

    public boolean isValidToUpdate(){
        boolean isOnlyName = name != null && !name.equals("") && price == null;
        boolean isOnlyPrice = name == null && price != null && price.getAmount() != null && !price.getAmount().equals("")
                && price.getCurrency() != null && !price.getCurrency().equals("");
        boolean isPriceWithName =  name != null && !name.equals("") && price != null && price.getAmount() != null
                && !price.getAmount().equals("") && price.getCurrency() != null && !price.getCurrency().equals("");

        return isOnlyName || isOnlyPrice || isPriceWithName;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
