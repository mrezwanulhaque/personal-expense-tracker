
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExpenseManager {

    private final List<Expense> expenses;

    public ExpenseManager() {
        this.expenses = new ArrayList<>();
    }

    // Add a new expense
    public void addExpense(Expense expense) {

        validateExpense(expense);

        boolean duplicateId = expenses.stream()
                .anyMatch(e -> e.getId() == expense.getId());

        if (duplicateId) {
            throw new IllegalArgumentException(
                    "An expense with this ID already exists."
            );
        }

        expenses.add(expense);
    }

    // Remove an expense using its ID
    public boolean removeExpense(int id) {

        return expenses.removeIf(
                expense -> expense.getId() == id
        );
    }

    // Find an expense by ID
    public Optional<Expense> findExpenseById(int id) {

        return expenses.stream()
                .filter(expense -> expense.getId() == id)
                .findFirst();
    }

    // Return all expenses
    public List<Expense> getAllExpenses() {

        return Collections.unmodifiableList(
                new ArrayList<>(expenses)
        );
    }

    // Return expenses sorted by date
    public List<Expense> getExpensesSortedByDate() {

        List<Expense> sortedExpenses =
                new ArrayList<>(expenses);

        sortedExpenses.sort(
                Comparator.comparing(Expense::getDate)
        );

        return Collections.unmodifiableList(
                sortedExpenses
        );
    }

    // Calculate total expenses
    public double getTotalExpenses() {

        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // Calculate expenses for a specific month
    public double getMonthlyTotal(YearMonth month) {

        if (month == null) {
            throw new IllegalArgumentException(
                    "Month cannot be null."
            );
        }

        return expenses.stream()
                .filter(expense ->
                        YearMonth.from(expense.getDate())
                                .equals(month)
                )
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    // Get expenses for a specific month
    public List<Expense> getExpensesByMonth(
            YearMonth month) {

        if (month == null) {
            throw new IllegalArgumentException(
                    "Month cannot be null."
            );
        }

        List<Expense> monthlyExpenses =
                new ArrayList<>();

        for (Expense expense : expenses) {

            YearMonth expenseMonth =
                    YearMonth.from(expense.getDate());

            if (expenseMonth.equals(month)) {
                monthlyExpenses.add(expense);
            }
        }

        return Collections.unmodifiableList(
                monthlyExpenses
        );
    }

    // Generate category-wise expense summaries
    public Map<String, Double> getCategorySummaries() {

        Map<String, Double> summaries =
                new LinkedHashMap<>();

        for (Expense expense : expenses) {

            String category = expense.getCategory();

            summaries.put(
                    category,
                    summaries.getOrDefault(category, 0.0)
                            + expense.getAmount()
            );
        }

        return Collections.unmodifiableMap(summaries);
    }

    // Return the total number of expenses
    public int getExpenseCount() {

        return expenses.size();
    }

    // Remove all expenses
    public void clearExpenses() {

        expenses.clear();
    }

    // Validate expense information
    private void validateExpense(Expense expense) {

        if (expense == null) {
            throw new IllegalArgumentException(
                    "Expense cannot be null."
            );
        }

        if (expense.getId() <= 0) {
            throw new IllegalArgumentException(
                    "Expense ID must be positive."
            );
        }

        if (expense.getTitle() == null
                || expense.getTitle().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Expense title cannot be empty."
            );
        }

        if (expense.getAmount() <= 0
                || !Double.isFinite(expense.getAmount())) {

            throw new IllegalArgumentException(
                    "Expense amount must be greater than zero."
            );
        }

        if (expense.getCategory() == null
                || expense.getCategory().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Expense category cannot be empty."
            );
        }

        if (expense.getDate() == null) {
            throw new IllegalArgumentException(
                    "Expense date cannot be null."
            );
        }
    }
}
