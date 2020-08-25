import exceptions.IncorrectFormatException;
import money.CurrencyType;
import money.Money;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String... args) throws IOException, IncorrectFormatException {
        List<MonetaryOperation> operations = new FileParser("input.txt").readAllOperations();
        showStatsTime(operations);
    }

    private static void showOperations(List<MonetaryOperation> operations) {
        for (MonetaryOperation operation: operations) {
            System.out.println(operation);
        }
    }

    private static void showSum(List<MonetaryOperation> operations) {
        Map<CurrencyType, Double> sum = new HashMap<>();
        sum.put(CurrencyType.RUBLE, 0.0);
        sum.put(CurrencyType.DOLLAR, 0.0);
        sum.put(CurrencyType.EURO, 0.0);
        Money money;
        for (MonetaryOperation operation: operations) {
            money = operation.getCost();
            sum.put(money.getCurrency(), sum.get(money.getCurrency()) + money.getValue());
        }
        System.out.println(CurrencyType.RUBLE + ": " + sum.get(CurrencyType.RUBLE));
        System.out.println(CurrencyType.DOLLAR + ": " + sum.get(CurrencyType.DOLLAR));
        System.out.println(CurrencyType.EURO + ": " + sum.get(CurrencyType.EURO));
    }

    private static void showStatsTime(List<MonetaryOperation> operations) {
        Date firstDate = operations.get(0).getDate();
        Date lastDate = operations.get(operations.size() - 1).getDate();
        double delta = lastDate.getTime() - firstDate.getTime();
        System.out.println(delta/(1000*60*60*24) + " days");
    }
}
