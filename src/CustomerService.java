import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service layer for customer-related operations.
 */
public class CustomerService {
    private final List<Customer> customers;

    public CustomerService(List<Customer> customers) {
        this.customers = new ArrayList<>(customers);
    }

    public boolean addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) != null) {
            return false;
        }
        customers.add(customer);
        return true;
    }

    public Customer findCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == id) {
                return customer;
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public List<Customer> getMutableCustomerList() {
        return customers;
    }
}
