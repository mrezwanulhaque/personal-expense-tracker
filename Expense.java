
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {

    private int id;
    private String title;
    private double amount;
    private String category;
    private LocalDate date;

    public Expense(int id, String title, double amount,
                   String category, LocalDate date) {

        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        if (amount > 0) {
            this.amount = amount;
        }
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String toCSV() {
        return id + "," + title + "," + amount + ","
                + category + "," + date;
    }

    public static Expense fromCSV(String csvLine) {
        String[] data = csvLine.split(",");

        if (data.length != 5) {
            throw new IllegalArgumentException(
                "Invalid expense data."
            );
        }

        return new Expense(
            Integer.parseInt(data[0]),
            data[1],
            Double.parseDouble(data[2]),
            data[3],
            LocalDate.parse(data[4])
        );
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return String.format(
            "ID: %d | Title: %s | Amount: %.2f | Category: %s | Date: %s",
            id, title, amount, category, date.format(formatter)
        );
    }
}
