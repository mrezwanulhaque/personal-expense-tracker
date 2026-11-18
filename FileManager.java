
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private final Path filePath;

    // Default constructor
    public FileManager() {
        this("expenses.csv");
    }

    // Custom file path constructor
    public FileManager(String fileName) {

        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "File name cannot be empty."
            );
        }

        this.filePath = Paths.get(fileName);
    }

    // Save all expenses to CSV file
    public void saveExpenses(List<Expense> expenses)
            throws IOException {

        if (expenses == null) {
            throw new IllegalArgumentException(
                    "Expense list cannot be null."
            );
        }

        Path parent = filePath.toAbsolutePath().getParent();

        Files.createDirectories(parent);

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8)) {

            for (Expense expense : expenses) {

                if (expense == null) {
                    throw new IllegalArgumentException(
                            "Expense list contains null data."
                    );
                }

                writer.write(expense.toCSV());
                writer.newLine();
            }
        }
    }

    // Load expenses from CSV file
    public List<Expense> loadExpenses() throws IOException {

        List<Expense> expenses = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return expenses;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath,
                StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Expense expense = Expense.fromCSV(line);
                    expenses.add(expense);

                } catch (RuntimeException exception) {

                    throw new IOException(
                            "Invalid expense data at line "
                                    + lineNumber + ": " + line,
                            exception
                    );
                }
            }
        }

        return expenses;
    }

    // Check whether the CSV file exists
    public boolean fileExists() {

        return Files.exists(filePath);
    }

    // Get the current file path
    public String getFilePath() {

        return filePath.toString();
    }

    // Delete the CSV file
    public boolean deleteFile() throws IOException {

        return Files.deleteIfExists(filePath);
    }
}
