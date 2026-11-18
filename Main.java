
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    private static final ExpenseManager manager =
            new ExpenseManager();

    private static final FileManager fileManager =
            new FileManager();

    private static int nextId = 1;

    public static void main(String[] args) {

        loadExpenses();

        System.out.println("\n==================================");
        System.out.println("       PERSONAL EXPENSE TRACKER");
        System.out.println("==================================");

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addExpense();
                    break;

                case 2:
                    viewExpenses();
                    break;

                case 3:
                    searchExpense();
                    break;

                case 4:
                    deleteExpense();
                    break;

                case 5:
                    showMonthlyReport();
                    break;

                case 6:
                    showCategorySummary();
                    break;

                case 7:
                    showTotalExpenses();
                    break;

                case 0:
                    saveExpenses();
                    running = false;
                    System.out.println(
                            "\nThank you for using Expense Tracker!"
                    );
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please try again."
                    );
            }
        }

        scanner.close();
    }

    private static void displayMenu() {

        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println("1. Add New Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Search Expense");
        System.out.println("4. Delete Expense");
        System.out.println("5. Monthly Expense Report");
        System.out.println("6. Category Summary");
        System.out.println("7. Total Expenses");
        System.out.println("0. Exit");
        System.out.println("---------------------------------");
    }

    private static void addExpense() {

        System.out.println("\n--- ADD NEW EXPENSE ---");

        String title = readText("Expense title: ");
        String category = readText("Category: ");

        double amount = readDouble("Amount: ");

        LocalDate date = readDate();

        try {
            Expense expense = new Expense(
                    nextId,
                    title,
                    amount,
                    category,
                    date
            );

            manager.addExpense(expense);
            nextId++;

            saveExpenses();

            System.out.println(
                    "Expense added successfully!"
            );

        } catch (IllegalArgumentException exception) {

            System.out.println(
                    "Error: " + exception.getMessage()
            );
        }
    }

    private static void viewExpenses() {

        System.out.println("\n--- ALL EXPENSES ---");

        if (manager.getExpenseCount() == 0) {
            System.out.println("No expenses found.");
            return;
        }

        for (Expense expense :
                manager.getExpensesSortedByDate()) {

            System.out.println(expense);
        }

        System.out.printf(
                "\nTotal: %.2f%n",
                manager.getTotalExpenses()
        );
    }

    private static void searchExpense() {

        System.out.println("\n--- SEARCH EXPENSE ---");

        int id = readInt("Enter expense ID: ");

        Optional<Expense> result =
                manager.findExpenseById(id);

        if (result.isPresent()) {
            System.out.println(result.get());
        } else {
            System.out.println("Expense not found.");
        }
    }

    private static void deleteExpense() {

        System.out.println("\n--- DELETE EXPENSE ---");

        int id = readInt("Enter expense ID: ");

        boolean removed = manager.removeExpense(id);

        if (removed) {

            saveExpenses();

            System.out.println(
                    "Expense deleted successfully!"
            );

        } else {
            System.out.println("Expense not found.");
        }
    }

    private static void showMonthlyReport() {

        System.out.println("\n--- MONTHLY REPORT ---");

        int year = readInt("Enter year: ");
        int month = readInt("Enter month (1-12): ");

        try {
            YearMonth selectedMonth =
                    YearMonth.of(year, month);

            double total =
                    manager.getMonthlyTotal(selectedMonth);

            System.out.println(
                    "Month: " + selectedMonth
            );

            System.out.printf(
                    "Total expenses: %.2f%n",
                    total
            );

        } catch (DateTimeParseException
                | java.time.DateTimeException exception) {

            System.out.println("Invalid month or year.");
        }
    }

    private static void showCategorySummary() {

        System.out.println("\n--- CATEGORY SUMMARY ---");

        Map<String, Double> summaries =
                manager.getCategorySummaries();

        if (summaries.isEmpty()) {
            System.out.println("No expense data available.");
            return;
        }

        for (Map.Entry<String, Double> entry :
                summaries.entrySet()) {

            System.out.printf(
                    "%-20s : %.2f%n",
                    entry.getKey(),
                    entry.getValue()
            );
        }
    }

    private static void showTotalExpenses() {

        System.out.println("\n--- TOTAL EXPENSES ---");

        System.out.printf(
                "Total expenses: %.2f%n",
                manager.getTotalExpenses()
        );

        System.out.println(
                "Number of transactions: "
                        + manager.getExpenseCount()
        );
    }

    private static void loadExpenses() {

        try {
            for (Expense expense :
                    fileManager.loadExpenses()) {

                manager.addExpense(expense);

                nextId = Math.max(
                        nextId,
                        expense.getId() + 1
                );
            }

            System.out.println(
                    "Expense data loaded successfully."
            );

        } catch (IOException
                | IllegalArgumentException exception) {

            System.out.println(
                    "Unable to load expense data: "
                            + exception.getMessage()
            );
        }
    }

    private static void saveExpenses() {

        try {
            fileManager.saveExpenses(
                    manager.getAllExpenses()
            );

        } catch (IOException exception) {

            System.out.println(
                    "Unable to save data: "
                            + exception.getMessage()
            );
        }
    }

    private static String readText(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            if (!input.isEmpty() && !input.contains(",")) {
                return input;
            }

            System.out.println(
                    "Please enter valid text without commas."
            );
        }
    }

    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            try {
                return Integer.parseInt(
                        scanner.nextLine().trim()
                );

            } catch (NumberFormatException exception) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    private static double readDouble(String message) {

        while (true) {

            System.out.print(message);

            try {
                double amount = Double.parseDouble(
                        scanner.nextLine().trim()
                );

                if (Double.isFinite(amount) && amount > 0) {
                    return amount;
                }

            } catch (NumberFormatException exception) {
                // Handle invalid input below.
            }

            System.out.println(
                    "Please enter a valid positive amount."
            );
        }
    }

    private static LocalDate readDate() {

        while (true) {

            System.out.print(
                    "Date (YYYY-MM-DD, blank for today): "
            );

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(input);

            } catch (DateTimeParseException exception) {

                System.out.println(
                        "Invalid date. Example: 2026-11-19"
                );
            }
        }
    }
}
