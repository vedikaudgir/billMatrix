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
            printMainMenu();
            int choice = readInt("Enter your choice (1-3): ");
            switch (choice) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    userMenu();
                    break;
                case 3:
                    exitProgram();
                    return;
                default:
                    System.out.println(">> Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n========================================");
        System.out.println("      ELECTRICITY BILLING SYSTEM      ");
        System.out.println("========================================");
        System.out.println("Welcome! Please select your role:");
        System.out.println("1. Admin");
        System.out.println("2. User");
        System.out.println("3. Exit");
        System.out.println("========================================");
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n--------------- ADMIN MENU ---------------");
            System.out.println("1. Add Customer");
            System.out.println("2. Generate Bill");
            System.out.println("3. View All Customers");
            System.out.println("4. View Customer Bill History");
            System.out.println("5. Return to Main Menu");
            System.out.println("------------------------------------------");
            int choice = readInt("Admin Choice (1-5): ");
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
                    viewBillHistoryAdmin();
                    break;
                case 5:
                    return;
                default:
                    System.out.println(">> Invalid choice. Please enter between 1 and 5.");
            }
        }
    }

    private void userMenu() {
        System.out.println("\n========================================");
        System.out.println("               USER LOGIN               ");
        System.out.println("========================================");
        int id = readInt("Enter your Customer ID to login (0 to cancel): ");
        if (id == 0)
            return;

        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println(">> Error: Customer not found. Please contact admin to register.");
            return;
        }

        System.out.println(">> Login Successful! Welcome, " + customer.getName() + ".");

        while (true) {
            System.out.println("\n--------------- USER MENU ----------------");
            System.out.println("1. View My Bill History");
            System.out.println("2. View My Monthly Expenses Graph");
            System.out.println("3. View Reminders");
            System.out.println("4. View Carbon Footprint Insights");
            System.out.println("5. Logout / Return to Main Menu");
            System.out.println("------------------------------------------");
            int choice = readInt("User Choice (1-5): ");
            switch (choice) {
                case 1:
                    viewBillHistoryUser(id);
                    break;
                case 2:
                    viewMonthlyExpensesGraphUser(id);
                    break;
                case 3:
                    viewReminders();
                    break;
                case 4:
                    viewCarbonFootprintUser(id);
                    break;
                case 5:
                    System.out.println(">> Logging out...");
                    return;
                default:
                    System.out.println(">> Invalid choice. Please enter between 1 and 5.");
            }
        }
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

    private void viewBillHistoryAdmin() {
        System.out.println("\n--- View Customer Bill History ---");
        int id = readInt("Enter customer ID: ");
        viewBillHistory(id);
    }

    private void viewBillHistoryUser(int id) {
        System.out.println("\n--- My Bill History ---");
        viewBillHistory(id);
    }

    private void viewBillHistory(int id) {
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

    private void viewMonthlyExpensesGraphUser(int id) {
        System.out.println("\n--- Monthly Expenses Bar Graph ---");
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

        double maxAmount = 0;
        for (Bill bill : bills) {
            if (bill.getAmount() > maxAmount) {
                maxAmount = bill.getAmount();
            }
        }

        int maxBarLength = 50;
        System.out.printf("Expense Graph for %s (ID %d):\n", customer.getName(), customer.getCustomerId());

        double previousAmount = -1;
        for (Bill bill : bills) {
            int barLength = maxAmount > 0 ? (int) ((bill.getAmount() / maxAmount) * maxBarLength) : 0;
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("█");
            }

            String trend = "";
            if (previousAmount != -1) {
                if (bill.getAmount() > previousAmount) {
                    trend = "(↑ Increasing)";
                } else if (bill.getAmount() < previousAmount) {
                    trend = "(↓ Decreasing)";
                } else {
                    trend = "(- Stable)";
                }
            }
            previousAmount = bill.getAmount();

            System.out.printf("%-12s | %-50s %.2f %s\n", bill.getMonth(), bar.toString(), bill.getAmount(), trend);
        }
    }

    private void viewCarbonFootprintUser(int id) {
        System.out.println("\n--- Eco-Insights: Carbon Footprint ---");
        Customer customer = customerService.findCustomerById(id);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        List<Bill> bills = billingService.getBillsByCustomerId(id);
        if (bills.isEmpty()) {
            System.out.println("No data available to calculate carbon footprint.");
            return;
        }

        System.out.println("📉 Monthly Carbon Trend Graph:");
        double maxCo2 = 0;
        for (Bill bill : bills) {
            double co2 = bill.getUnitsConsumed() * 0.85; // approx 0.85 kg CO2 per kWh
            if (co2 > maxCo2) {
                maxCo2 = co2;
            }
        }

        int maxBarLength = 40;
        double previousCo2 = -1;
        for (Bill bill : bills) {
            double co2 = bill.getUnitsConsumed() * 0.85;
            int barLength = maxCo2 > 0 ? (int) ((co2 / maxCo2) * maxBarLength) : 0;
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("█");
            }

            String trend = "";
            if (previousCo2 != -1) {
                if (co2 > previousCo2) {
                    trend = "(↑ Increasing)";
                } else if (co2 < previousCo2) {
                    trend = "(↓ Decreasing)";
                } else {
                    trend = "(- Stable)";
                }
            }
            previousCo2 = co2;

            System.out.printf("%-12s | %-40s %.2f kg CO2 %s\n", bill.getMonth(), bar.toString(), co2, trend);
        }

        Bill latestBill = bills.get(bills.size() - 1);
        double latestCo2 = latestBill.getUnitsConsumed() * 0.85;
        int treeEquivalent = (int) Math.ceil(latestCo2 / 2.0); // Assuming 1 mature tree offsets ~2kg CO2/month

        System.out.println("\n--- Latest Month Highlights (" + latestBill.getMonth() + ") ---");

        // CO2 Meter Progress Bar
        int meterLength = 30;
        int filledMeter = Math.min((int) ((latestCo2 / 300.0) * meterLength), meterLength);
        StringBuilder meter = new StringBuilder("🔵 CO₂ Meter: [");
        for (int i = 0; i < meterLength; i++) {
            if (i < filledMeter)
                meter.append("■");
            else
                meter.append("-");
        }
        meter.append(String.format("] %.2f kg", latestCo2));
        System.out.println(meter.toString());

        System.out
                .println("🌳 Tree Equivalent: You need " + treeEquivalent + " trees to offset this month's emissions.");

        if (latestCo2 > 150) { // threshold for high footprint
            System.out.println("⚠️ Warning: High carbon footprint this month. Please consider reducing energy usage.");
        }
    }

    private void viewReminders() {
        System.out.println("\n--- Date Reminders ---");
        LocalDate today = LocalDate.now();
        System.out.println("Today's Date: " + today);

        if (today.getDayOfMonth() <= 15) {
            System.out.println("Reminder: Your electricity bill is due on the 15th of this month.");
            System.out.println("Status: " + (15 - today.getDayOfMonth()) + " days left to pay without late fees.");
        } else {
            System.out.println(
                    "Reminder: The due date (15th) has passed. Please clear any pending dues to avoid disconnection.");
        }

        // Reminder for next bill generation
        LocalDate nextMonth = today.plusMonths(1).withDayOfMonth(1);
        System.out.println("Reminder: Next bill cycle starts on " + nextMonth.getMonth() + " 1st.");
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
