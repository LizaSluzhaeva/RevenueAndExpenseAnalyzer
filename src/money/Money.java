package money;

import exceptions.IncorrectFormatException;

public class Money {
    private Double value;
    private CurrencyType currency = CurrencyType.RUBLE;

    public Money(Double value, CurrencyType currency) {
        this.value = value;
        this.currency = currency;
    }

    public Money(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public static Money fromString(String s) throws IncorrectFormatException {
        try {
            CurrencyType currencyFromString = CurrencyType.fromString(String.valueOf(s.charAt(s.length() - 1)));
            if (currencyFromString == null) {
                return new Money(Double.parseDouble(s));
            } else {
                return new Money(Double.parseDouble(s.substring(0, s.length() - 1)), currencyFromString);
            }
        }
        catch (NumberFormatException exception) {
            throw new IncorrectFormatException("Invalid money format", exception);
        }
    }

    @Override
    public String toString() {
        return value.toString() + currency.getCurrency();
    }
}
