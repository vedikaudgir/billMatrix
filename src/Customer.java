import java.util.Objects;

/**
 * Represents a customer in the electricity billing system.
 */
public class Customer {
    private int customerId;
    private String name;
    private String meterNumber;
    private String city;

    public Customer(int customerId, String name, String meterNumber, String city) {
        this.customerId = customerId;
        this.name = name;
        this.meterNumber = meterNumber;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String toCsvString() {
        return String.format("%d,%s,%s,%s", customerId, name, meterNumber, city);
    }

    public static Customer fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 3 || parts.length > 4) {
            throw new IllegalArgumentException("Invalid customer record: " + csvLine);
        }
        int id = Integer.parseInt(parts[0].trim());
        String name = parts[1].trim();
        String meter = parts[2].trim();
        String city = parts.length == 4 ? parts[3].trim() : "Delhi"; // Default to Delhi for backward compatibility
        return new Customer(id, name, meter, city);
    }

    @Override
    public String toString() {
        return String.format("Customer ID: %d, Name: %s, Meter Number: %s, City: %s", customerId, name, meterNumber, city);
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
