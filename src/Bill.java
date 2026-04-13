import java.util.Objects;

/**
 * Represents a generated electricity bill for a customer.
 */
public class Bill {
    private int billId;
    private int customerId;
    private int unitsConsumed;
    private double amount;
    private String month;

    public Bill(int billId, int customerId, int unitsConsumed, double amount, String month) {
        this.billId = billId;
        this.customerId = customerId;
        this.unitsConsumed = unitsConsumed;
        this.amount = amount;
        this.month = month;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(int unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String toCsvString() {
        return String.format("%d,%d,%d,%.2f,%s", billId, customerId, unitsConsumed, amount, month);
    }

    public static Bill fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid bill record: " + csvLine);
        }
        int id = Integer.parseInt(parts[0].trim());
        int customerId = Integer.parseInt(parts[1].trim());
        int units = Integer.parseInt(parts[2].trim());
        double amount = Double.parseDouble(parts[3].trim());
        String month = parts[4].trim();
        return new Bill(id, customerId, units, amount, month);
    }

    @Override
    public String toString() {
        return String.format("Bill ID: %d, Customer ID: %d, Units: %d, Amount: %.2f, Month: %s",
                billId, customerId, unitsConsumed, amount, month);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bill bill = (Bill) obj;
        return billId == bill.billId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId);
    }
}
