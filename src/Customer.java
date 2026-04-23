import java.util.Objects;

/**
 * Represents a customer in the electricity billing system.
 */
public class Customer {
    private int customerId;
    private String name;
    private String meterNumber;

    public Customer(int customerId, String name, String meterNumber) {
        this.customerId = customerId;
        this.name = name;
        this.meterNumber = meterNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String toCsvString() {
        return String.format("%d,%s,%s", customerId, name, meterNumber);
    }

    public static Customer fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid customer record: " + csvLine);
        }
        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String meter = parts[2].trim();
        return new Customer(id, name, meter);
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %d, Name: %s, Meter Number: %s", customerId, name, meterNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Customer customer = (Customer) obj;
        return customerId == customer.customerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
