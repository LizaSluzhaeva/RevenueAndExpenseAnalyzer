import money.CurrencyType;
import money.Money;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Analyzer {

    private final List<MonetaryOperation> operations;

    public Analyzer(Collection<MonetaryOperation> operations) {
        this.operations = operations.stream()
                .sorted(Comparator.comparing(MonetaryOperation::getDate))
                .collect(Collectors.toList());
    }

    public Map<CurrencyType, Double> sumAllMoneyByPeriod(Date beginning, Date ending) {
        Map<CurrencyType, Double> result = getMapWithZeroForEachCurrencyType();
        operations.stream()
                .dropWhile(operation -> operation.getDate().before(beginning))
                .takeWhile(operation -> operation.getDate().before(ending) || operation.getDate().equals(ending))
                .forEach(operation -> addMoneyByCurrencyType(result, operation.getCost()));
        return result;
    }

    public Map<CurrencyType, Double> sumAllMoney() {
        Date firstDate = operations.get(0).getDate();
        Date lastDare = operations.get(operations.size() - 1).getDate();
        return sumAllMoneyByPeriod(firstDate, lastDare);
    }

    public Map<CurrencyType, Double> sumAllMoneyByYearAndMouth(int month, int year) {
        Date monthBeginning = getMonthBeginning(month + 1, year);
        Date monthEnding = getMonthBeginning(month + 2, year);
        return sumAllMoneyByPeriod(monthBeginning, monthEnding);
    }

    public Map<CurrencyType, Double> sumAllMoneyByYearAndMouthAndDay(int day, int month, int year){
        Date dayBeginning = getDayBeginning(day, month + 1, year);
        Date dayEnding = getDayBeginning(day + 1, month + 1, year);
        return sumAllMoneyByPeriod(dayBeginning, dayEnding);
    }

    public long getAnalyzedTimePeriod() {
        Date firstDate = operations.get(0).getDate();
        Date lastDate = operations.get(operations.size() - 1).getDate();
        long delta = lastDate.getTime() - firstDate.getTime();
        return delta/(1000*60*60*24);
    }

    public Map<CurrencyType, Double> getAverageOperation() {
        // TODO приводить к одной валюте, иначе нет смысла
        Map<CurrencyType, Double> sum = sumAllMoney();
        long days = getAnalyzedTimePeriod();
        sum.put(CurrencyType.RUBLE, sum.get(CurrencyType.RUBLE)/days);
        sum.put(CurrencyType.DOLLAR, sum.get(CurrencyType.DOLLAR)/days);
        sum.put(CurrencyType.EURO, sum.get(CurrencyType.EURO)/days);
        return sum;
    }


//    public Map getSumPerDay(List<MonetaryOperation> operations) {
//        Map<String, Map> operationsPerDay = new HashMap<>();
//        //SortedMap operationsPerDay = new TreeMap();
//        Calendar c = new GregorianCalendar();
//        for (MonetaryOperation operation : operations) {
//            c.setTime(operation.getDate());
//            operationsPerDay.put(operation.getDateFormat().format(operation.getDate()), getSumInDay(operations, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)));
//        }
//        return operationsPerDay;
//    }

    public Map<Date, Map<CurrencyType, Double>> getSumPerDay() {
        Map<Date, Map<CurrencyType, Double>> operationsPerDay = new TreeMap<>();
        Calendar calendar = new GregorianCalendar();
        operations.stream().map(MonetaryOperation::getDate).distinct().forEach(date -> {
            calendar.setTime(date);
            operationsPerDay.put(date, sumAllMoneyByYearAndMouthAndDay(
                    calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.YEAR)));
        });
        return operationsPerDay;
    }
    public void getSumPerDayForPeriod() {
        // TODO Реализация с помощью суммы в определенный день

    }

    private static Map<CurrencyType, Double> getMapWithZeroForEachCurrencyType() {
        Map<CurrencyType, Double> sum = new HashMap<>();
        Arrays.stream(CurrencyType.values()).forEach(value -> sum.put(value, 0.0));
        return sum;
    }

    private static void addMoneyByCurrencyType(Map<CurrencyType, Double> map, Money money) {
        map.put(money.getCurrency(), map.get(money.getCurrency()) + money.getValue());
    }

    private static Date getMonthBeginning(int month, int year) {
        return new GregorianCalendar(year, month, 0).getTime();
    }

    private static Date getDayBeginning(int day, int month, int year) {
        return new GregorianCalendar(year, month, day).getTime();
    }
}
