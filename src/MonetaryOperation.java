import money.Money;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonetaryOperation {
    private String name;
    private Date date;
    private Money cost;
    private DateFormat format = new SimpleDateFormat("dd.MM.yy");

    public MonetaryOperation(String name, Money cost, Date date) {
        this.name = name;
        this.cost = cost;
        this.date = date;
    }

    public DateFormat getDateFormat() {
        return this.format;
    }

    public void changeDateFormat(DateFormat newFormat) {
        this.format = newFormat;
    }

    public Money getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return name + ": " + cost + " (" + format.format(date) + ')';
    }
}
