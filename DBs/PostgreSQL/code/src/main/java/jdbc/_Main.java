package jdbc;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class _Main {

    private static final Logger LOGGER =
            Logger.getLogger(_Main.class.getName());
    private static final Dao<Customer, Integer> CUSTOMER_DAO = new PostgreSqlDao();

    public static void main(final String[] args) {
        // Test whether an exception is thrown when
        // the database is queried for a non-existent customer.
        // But, if the customer does exist, the details will be printed
        // on the console
        try {
            final Customer customer = getCustomer(1);
        } catch (final NonExistentEntityException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }

        // Test whether a customer can be added to the database
        final Customer firstCustomer =
                new Customer("Manuel", "Kelley", "ManuelMKelley@jourrapide.com");
        final Customer secondCustomer =
                new Customer("Joshua", "Daulton", "JoshuaMDaulton@teleworm.us");
        final Customer thirdCustomer =
                new Customer("April", "Ellis", "AprilMellis@jourrapide.com");
        addCustomer(firstCustomer).ifPresent(firstCustomer::setId);
        addCustomer(secondCustomer).ifPresent(secondCustomer::setId);
        addCustomer(thirdCustomer).ifPresent(thirdCustomer::setId);

        // Test whether the new customer's details can be edited
        firstCustomer.setFirstName("Franklin");
        firstCustomer.setLastName("Hudson");
        firstCustomer.setEmail("FranklinLHudson@jourrapide.com");
        updateCustomer(firstCustomer);

        // Test whether all customers can be read from database
        getAllCustomers().forEach(System.out::println);

        // Test whether a customer can be deleted
        deleteCustomer(secondCustomer);
    }

    // Static helper methods referenced above
    public static Customer getCustomer(final int id) throws NonExistentEntityException {
        final Optional<Customer> customer = CUSTOMER_DAO.get(id);
        return customer.orElseThrow(NonExistentCustomerException::new);
    }

    public static Collection<Customer> getAllCustomers() {
        return CUSTOMER_DAO.getAll();
    }

    public static void updateCustomer(final Customer customer) {
        CUSTOMER_DAO.update(customer);
    }

    public static Optional<Integer> addCustomer(final Customer customer) {
        return CUSTOMER_DAO.save(customer);
    }

    public static void deleteCustomer(final Customer customer) {
        CUSTOMER_DAO.delete(customer);
    }

    public static class NonExistentEntityException extends Throwable {

        private static final long serialVersionUID = -3760558819369784286L;

        public NonExistentEntityException(final String message) {
            super(message);
        }
    }

    public static class NonExistentCustomerException extends NonExistentEntityException {

        private static final long serialVersionUID = 8633588908169766368L;

        public NonExistentCustomerException() {
            super("Customer does not exist");
        }
    }
}
