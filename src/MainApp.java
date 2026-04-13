import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class MainApp {
    private final CustomerService customerService;
    private final BillingService billingService;
    private final FileHandler fileHandler;
    private final Scanner scanner;

    public MainApp() {
        fileHandler = new FileHandler();
        customerService = new CustomerService(fileHandler.loadCustomers());
        billingService = new BillingService(fileHandler.loadBills());
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.run();
    }

    private void run() {
        while (true) {
            printMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    generateBill();
                    break;
                case 3:
                    viewCustomers();
                    break;
                case 4:
                    viewBillHistory();
                    break;
                case 5:
                    exitProgram();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n====== Electricity Billing System ======");
        System.out.println("1. Add Customer");
        System.out.println("2. Generate Bill");
        System.out.println("3. View Customers");
        System.out.println("4. View Bill History");
        System.out.println("5. Exit");
    }

    private void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        int id = readInt("Enter customer ID: ");
        if (customerService.findCustomerById(id) != null) {
            System.out.println("A customer with that ID already exists.");
            return;
        }
        System.out.print("Enter name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter meter number: ");
        String meterNumber = scanner.nextLine().trim();

        Customer customer = new Customer(id, name, meterNumber);
        boolean added = customerService.addCustomer(customer);
        if (added) {
            fileHandler.saveCustomers(customerService.getMutableCustomerList());
            System.out.println("Customer added successfully.");
        } else {
            System.out.println("Failed to add customer.");
        }
    }

    private void generateBill() {
        System.out.println("\n--- Generate Electricity Bill ---");
        int id = readInt("Enter customer ID: ");
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer not found. Please add the customer first.");
            return;
        }

        int units = readInt("Enter units consumed: ");
        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        System.out.print("Enter billing month (leave blank for current month): ");
        String monthInput = scanner.nextLine().trim();
        String month = monthInput.isEmpty() ? currentMonth : monthInput;

        Bill bill = billingService.generateBill(customer, units, month);
        fileHandler.saveBills(billingService.getMutableBillList());
        printBillReceipt(customer, bill);
    }

    private void printBillReceipt(Customer customer, Bill bill) {
        System.out.println("\n====== Electricity Bill Receipt ======");
        System.out.printf("Bill ID       : %d\n", bill.getBillId());
        System.out.printf("Customer ID   : %d\n", customer.getCustomerId());
        System.out.printf("Name          : %s\n", customer.getName());
        System.out.printf("Meter Number  : %s\n", customer.getMeterNumber());
        System.out.printf("Month         : %s\n", bill.getMonth());
        System.out.printf("Units Consumed: %d\n", bill.getUnitsConsumed());
        System.out.printf("Amount Due    : %.2f\n", bill.getAmount());
        System.out.println("======================================");
    }

    private void viewCustomers() {
        System.out.println("\n--- Customer List ---");
        List<Customer> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private void viewBillHistory() {
        System.out.println("\n--- View Bill History ---");
        int id = readInt("Enter customer ID: ");
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        List<Bill> bills = billingService.getBillsByCustomerId(id);
        if (bills.isEmpty()) {
            System.out.println("No bills found for this customer.");
            return;
        }
        System.out.printf("Bill history for %s (ID %d):\n", customer.getName(), customer.getCustomerId());
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    private void exitProgram() {
        System.out.println("Exiting program. Goodbye!");
        scanner.close();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
    }
}
