package money;

public enum CurrencyType {
    RUBLE("₽"),
    DOLLAR("$"),
    EURO("€");

    private String type;

    CurrencyType(String type) {
        this.type = type;
    }

    public static CurrencyType fromString(String s) {
        switch (s) {
            case "р":
            case "Р":
            case "₽":
                return RUBLE;
            case "$":
                return DOLLAR;
            case "€":
                return EURO;
        }
        return null;
    }

    public String getCurrency() {
        return type;
    }
}
