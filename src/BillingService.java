import java.util.ArrayList;
import java.util.List;

/**
 * Billing service for calculating and generating bills.
 */
public class BillingService {
    private final List<Bill> bills;
    private int nextBillId;

    public BillingService(List<Bill> bills) {
        this.bills = new ArrayList<>(bills);
        this.nextBillId = calculateNextBillId();
    }

    private int calculateNextBillId() {
        int maxId = 0;
        for (Bill bill : bills) {
            if (bill.getBillId() > maxId) {
                maxId = bill.getBillId();
            }
        }
        return maxId + 1;
    }

    public double calculateBill(int units, String city) {
        double ratePerUnit;
        switch (city) {
            case "Mumbai / Maharashtra":
                ratePerUnit = 9.0; // Average of 4.4 to 14+
                break;
            case "Delhi":
                ratePerUnit = 5.5; // Average of 3 to 8
                break;
            case "Bangalore (Karnataka)":
                ratePerUnit = 6.25; // Average of 5 to 7.5
                break;
            case "Chennai (Tamil Nadu)":
                ratePerUnit = 7.0; // Average of ~5 to 9
                break;
            case "Kolkata (West Bengal)":
                ratePerUnit = 7.0; // Average of 6 to 8
                break;
            default:
                ratePerUnit = 5.5; // Default to Delhi rate
        }
        return units * ratePerUnit;
    }

    public Bill generateBill(Customer customer, int units, String month) {
        double amount = calculateBill(units, customer.getCity());
        Bill bill = new Bill(nextBillId++, customer.getCustomerId(), units, amount, month);
        bills.add(bill);
        return bill;
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    public List<Bill> getBillsByCustomerId(int customerId) {
        List<Bill> result = new ArrayList<>();
        for (Bill bill : bills) {
            if (bill.getCustomerId() == customerId) {
                result.add(bill);
            }
        }
        return result;
    }

    public List<Bill> getMutableBillList() {
        return bills;
    }
}
