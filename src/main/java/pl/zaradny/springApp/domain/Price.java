package pl.zaradny.springApp.domain;

import com.google.common.base.Preconditions;
import pl.zaradny.springApp.exceptions.BadPriceException;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public final class Price {

    private final BigDecimal amount;
    private final Currency currency;

    private Price(BigDecimal amount, Currency currency) {
        this.currency = currency;
        this.amount = amount;
    }

    public static Price build(BigDecimal amount, Currency currency) {
        try {
            Preconditions.checkNotNull(currency);
            Preconditions.checkNotNull(amount);
        }catch (NullPointerException e){
            throw new BadPriceException();
        }
        return new Price(amount, currency);
    }

    public static Price build(String amount, String currency) {
        try {
            BigDecimal priceAmount = new BigDecimal(amount);
            Currency priceCurrency = Currency.getInstance(currency);
            return new Price(priceAmount, priceCurrency);
        }catch (IllegalArgumentException | NullPointerException e){
            throw new BadPriceException();
        }
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(amount, price.amount) &&
                Objects.equals(currency, price.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }
}
