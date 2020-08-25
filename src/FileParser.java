import exceptions.IncorrectFormatException;
import money.Money;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileParser implements AutoCloseable {
    private final Scanner mainScanner;
    private final Pattern datePattern = Pattern.compile("^   \\d\\d?.\\d\\d.\\d\\d");
    private final Pattern purchasePattern = Pattern.compile(".+\\s-\\s+[0-9]+(.[0-9]+)?[рР€$₽]?");
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");

    public FileParser(String fileName) throws IOException {
        //todo log <- opening
        this.mainScanner = new Scanner(Paths.get(fileName));
    }

    public List<MonetaryOperation> readAllOperations() throws IncorrectFormatException {
        //todo log <- reading all purchases
        List<MonetaryOperation> result = new LinkedList<>();
        Date currentDate = null;
        Date newDate;
        IncorrectFormatException incorrectOperationException = null;
        int currentLine = 0;
        String nextLine = null;
        while (mainScanner.hasNextLine()) {
            if (currentDate != null) {
                try {
                    newDate = parseDate(nextLine);
                } catch (IncorrectFormatException exception) {
                    if (incorrectOperationException == null) {
                        throw new IncorrectFormatException(exception.getMessage()
                                + ". Line: " + currentLine, exception);
                    }
                    throw incorrectOperationException;
                }
                if (before(newDate, currentDate)) {
                    throw new IncorrectFormatException("The date is earlier than the previous one. Line: "
                            + currentLine);
                }
                currentDate = newDate;
            } else { // если currentDate = null
                try {
                    ++currentLine; // пытаемся искать currentDate на следующей строке
                    currentDate = parseDate(mainScanner.nextLine());
                } catch (IncorrectFormatException exception) {
                    throw new IncorrectFormatException(exception.getMessage()
                            + ". Line: " + currentLine, exception);
                }
            }
            try {
                while (mainScanner.hasNextLine()) {
                    ++currentLine;
                    nextLine = mainScanner.nextLine();
                    result.add(parseMonetaryOperation(nextLine, currentDate));
                }
            }
            catch (IncorrectFormatException exception) {
                incorrectOperationException = new IncorrectFormatException(exception.getMessage()
                        + ". Line: " + currentLine, exception);
            }
        }
        //todo log <- done
        return result;
    }

    private boolean before(Date date1, Date date2) {
        return date1.getTime() < date2.getTime();
    }

    private Date parseDate(String input) throws IncorrectFormatException {
        boolean isCorrectFormat = datePattern.matcher(input).matches();
        Date result;
        try {
            result = dateFormat.parse(input);
        }
        catch (ParseException exception) {
            throw new IncorrectFormatException("Incorrect date format", exception);
        }
        if (!isCorrectFormat) {
            System.out.println("Incorrect spaces in line: " + input);
            //todo log <- incorrect spaces
        }
        return result;
    }

    private MonetaryOperation parseMonetaryOperation(String input, Date operationDate) throws IncorrectFormatException {
        if (!purchasePattern.matcher(input).matches()) {
            throw new IncorrectFormatException("Incorrect monetary operation format");
        }
        String[] split = input.split(" - ");
        try {
            return new MonetaryOperation(split[0].trim(), Money.fromString(split[1].trim()), operationDate);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new IncorrectFormatException("Incorrect monetary operation format", exception);
        }
    }

    @Override
    public void close() {
        //todo log <- closing
        this.mainScanner.close();
    }
}
