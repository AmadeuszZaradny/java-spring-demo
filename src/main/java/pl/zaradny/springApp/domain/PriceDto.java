package pl.zaradny.springApp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PriceDto {

    private final String amount;
    private final String currency;


    @JsonCreator
    public PriceDto(@JsonProperty("amount")  String amount, @JsonProperty("currency") String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public PriceDto(Price price){
        this.amount = price.getAmount().toString();
        this.currency = price.getCurrency().getCurrencyCode();
    }

    public String getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceDto priceDto = (PriceDto) o;
        return Objects.equals(amount, priceDto.amount) &&
                Objects.equals(currency, priceDto.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "PriceDto{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
