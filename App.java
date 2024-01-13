import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

    private static final String URL = "jdbc:mysql://localhost:3306/ecom";
    private static final String USER = "ad";
    private static final String PASSWORD = "ad123";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            createTables(connection); // Create tables if they do not exist

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("E-Commerce System Menu:");
                System.out.println("1. Display All Products");
                System.out.println("2. Add a New Product");
                System.out.println("3. Update a Product Name");
                System.out.println("4. Add a New Customer");
                System.out.println("5. Display All Customers");
                System.out.println("6. Delete a Customer");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        readProducts(connection);
                        break;
                    case 2:
                        System.out.print("Enter Product Name: ");
                        String productName = scanner.nextLine();
                        System.out.print("Enter Product Price: ");
                        double productPrice = scanner.nextDouble();
                        createProduct(connection, productName, productPrice);
                        break;
                    case 3:
                        System.out.print("Enter Product ID to update: ");
                        int productId = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        System.out.print("Enter New Product Name: ");
                        String newProductName = scanner.nextLine();
                        updateProduct(connection, productId, newProductName);
                        break;
                    case 4:
                        System.out.print("Enter Customer Name: ");
                        String customerName = scanner.nextLine();
                        System.out.print("Enter Customer Email: ");
                        String customerEmail = scanner.nextLine();
                        System.out.print("Enter Customer Phone: ");
                        String customerPhone = scanner.nextLine();
                        createCustomer(connection, customerName, customerEmail, customerPhone);
                        break;
                    case 5:
                        readCustomers(connection);
                        break;
                    case 6:
                        System.out.print("Enter Customer ID to delete: ");
                        int customerIdToDelete = scanner.nextInt();
                        deleteCustomer(connection, customerIdToDelete);
                        break;
                    case 7:
                        System.out.println("Exiting program. Goodbye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        createProductsTable(connection);
        createCustomersTable(connection);
    }

    private static void createProductsTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Products ("
                + "product_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(255) NOT NULL,"
                + "price DOUBLE NOT NULL)";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void createCustomersTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Customers ("
                + "customer_id INT PRIMARY KEY AUTO_INCREMENT,"
                + "name VARCHAR(255) NOT NULL,"
                + "email VARCHAR(255) NOT NULL,"
                + "phone VARCHAR(20) NOT NULL)";
        
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    private static void readProducts(Connection connection) throws SQLException {
        String query = "SELECT * FROM Products";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Product ID: " + resultSet.getInt("product_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Price: " + resultSet.getDouble("price"));
                System.out.println("---------------");
            }
        }
    }

    private static void createProduct(Connection connection, String name, double price) throws SQLException {
        String query = "INSERT INTO Products (name, price) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product created successfully!");
            } else {
                System.out.println("Failed to create product.");
            }
        }
    }

    private static void updateProduct(Connection connection, int productId, String newProductName) throws SQLException {
        String query = "UPDATE Products SET name = ? WHERE product_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newProductName);
            preparedStatement.setInt(2, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Failed to update product.");
            }
        }
    }

    private static void createCustomer(Connection connection, String name, String email, String phone) throws SQLException {
        String query = "INSERT INTO Customers (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer created successfully!");
            } else {
                System.out.println("Failed to create customer.");
            }
        }
    }

    private static void readCustomers(Connection connection) throws SQLException {
        String query = "SELECT * FROM Customers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Customer ID: " + resultSet.getInt("customer_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Phone: " + resultSet.getString("phone"));
                System.out.println("--------------------");
            }
        }
    }

    private static void deleteCustomer(Connection connection, int customerId) throws SQLException {
        String query = "DELETE FROM Customers WHERE customer_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, customerId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Failed to delete customer.");
            }
        }
    }
}
