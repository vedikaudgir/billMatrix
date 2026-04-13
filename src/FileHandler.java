import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes customer and bill data to text files.
 */
public class FileHandler {
    private static final String CUSTOMER_FILE = "customers.txt";
    private static final String BILL_FILE = "bills.txt";

    public List<Customer> loadCustomers() {
        Path path = Paths.get(CUSTOMER_FILE);
        List<Customer> customers = new ArrayList<>();
        if (!Files.exists(path)) {
            return customers;
        }

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    customers.add(Customer.fromCsvString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customers file: " + e.getMessage());
        }
        return customers;
    }

    public void saveCustomers(List<Customer> customers) {
        Path path = Paths.get(CUSTOMER_FILE);
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            lines.add(customer.toCsvString());
        }
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error writing customers file: " + e.getMessage());
        }
    }

    public List<Bill> loadBills() {
        Path path = Paths.get(BILL_FILE);
        List<Bill> bills = new ArrayList<>();
        if (!Files.exists(path)) {
            return bills;
        }

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    bills.add(Bill.fromCsvString(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading bills file: " + e.getMessage());
        }
        return bills;
    }

    public void saveBills(List<Bill> bills) {
        Path path = Paths.get(BILL_FILE);
        List<String> lines = new ArrayList<>();
        for (Bill bill : bills) {
            lines.add(bill.toCsvString());
        }
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error writing bills file: " + e.getMessage());
        }
    }
}
