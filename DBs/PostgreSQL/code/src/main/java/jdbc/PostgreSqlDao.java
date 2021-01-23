package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlDao implements Dao<Customer, Integer> {

    private static final Logger LOGGER =
            Logger.getLogger(PostgreSqlDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Customer> get(final int id) {
        return this.connection.flatMap(conn -> {
            Optional<Customer> customer = Optional.empty();
            final String sql = "SELECT * FROM customer WHERE customer_id = " + id;

            try (final Statement statement = conn.createStatement();
                 final ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    final String firstName = resultSet.getString("first_name");
                    final String lastName = resultSet.getString("last_name");
                    final String email = resultSet.getString("email");

                    customer = Optional.of(
                            new Customer(id, firstName, lastName, email));

                    LOGGER.log(Level.INFO, "Found {0} in database", customer.get());
                }
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return customer;
        });
    }

    @Override
    public Collection<Customer> getAll() {
        final Collection<Customer> customers = new ArrayList<>();
        final String sql = "SELECT * FROM customer";

        this.connection.ifPresent(conn -> {
            try (final Statement statement = conn.createStatement();
                 final ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    final int id = resultSet.getInt("customer_id");
                    final String firstName = resultSet.getString("first_name");
                    final String lastName = resultSet.getString("last_name");
                    final String email = resultSet.getString("email");

                    final Customer customer = new Customer(id, firstName, lastName, email);

                    customers.add(customer);

                    LOGGER.log(Level.INFO, "Found {0} in database", customer);
                }

            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return customers;
    }

    @Override
    public Optional<Integer> save(final Customer customer) {
        final Customer nonNullCustomer = Objects.requireNonNull(customer, "The customer to be added should not be null");
        final String sql = "" +
                "INSERT INTO "
                + "customer(first_name, last_name, email) "
                + "VALUES(?, ?, ?)";

        return this.connection.flatMap(conn -> {
            Optional<Integer> generatedId = Optional.empty();

            try (final PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, nonNullCustomer.getFirstName());
                statement.setString(2, nonNullCustomer.getLastName());
                statement.setString(3, nonNullCustomer.getEmail());

                final int numberOfInsertedRows = statement.executeUpdate();

                // Retrieve the auto-generated id
                if (numberOfInsertedRows > 0) {
                    try (final ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            generatedId = Optional.of(resultSet.getInt(1));
                        }
                    }
                }

                LOGGER.log(Level.INFO, "{0} created successfully? {1}", new Object[]{nonNullCustomer, (numberOfInsertedRows > 0)});
            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return generatedId;
        });
    }

    @Override
    public void update(final Customer customer) {
        final String message = "The customer to be updated should not be null";
        final Customer nonNullCustomer = Objects.requireNonNull(customer, message);
        final String sql = "UPDATE customer "
                + "SET "
                + "first_name = ?, "
                + "last_name = ?, "
                + "email = ? "
                + "WHERE "
                + "customer_id = ?";

        this.connection.ifPresent(conn -> {
            try (final PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setString(1, nonNullCustomer.getFirstName());
                statement.setString(2, nonNullCustomer.getLastName());
                statement.setString(3, nonNullCustomer.getEmail());
                statement.setInt(4, nonNullCustomer.getId());

                final int numberOfUpdatedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer updated successfully? {0}",
                        numberOfUpdatedRows > 0);

            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void delete(final Customer customer) {
        final String message = "The customer to be deleted should not be null";
        final Customer nonNullCustomer = Objects.requireNonNull(customer, message);
        final String sql = "DELETE FROM customer WHERE customer_id = ?";

        this.connection.ifPresent(conn -> {
            try (final PreparedStatement statement = conn.prepareStatement(sql)) {

                statement.setInt(1, nonNullCustomer.getId());

                final int numberOfDeletedRows = statement.executeUpdate();

                LOGGER.log(Level.INFO, "Was the customer deleted successfully? {0}",
                        numberOfDeletedRows > 0);

            } catch (final SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
    }

}
