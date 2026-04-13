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

    public double calculateBill(int units) {
        double amount = 0.0;
        if (units <= 100) {
            amount = units * 1.5;
        } else if (units <= 300) {
            amount = 100 * 1.5 + (units - 100) * 2.5;
        } else {
            amount = 100 * 1.5 + 200 * 2.5 + (units - 300) * 4.0;
        }
        return amount;
    }

    public Bill generateBill(Customer customer, int units, String month) {
        double amount = calculateBill(units);
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
