import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "root";
    private static final String PASSWORD = "tusar@04";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Create 2. Read 3. Update 4. Delete 5. Exit");
                switch (scanner.nextInt()) {
                    case 1 -> createProduct(conn, scanner);
                    case 2 -> readProducts(conn);
                    case 3 -> updateProduct(conn, scanner);
                    case 4 -> deleteProduct(conn, scanner);
                    case 5 -> { return; }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void createProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Name, Price, Quantity: ");
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)");) {
            pstmt.setString(1, scanner.next());
            pstmt.setDouble(2, scanner.nextDouble());
            pstmt.setInt(3, scanner.nextInt());
            pstmt.executeUpdate();
            System.out.println("Product added.");
        }
    }

    private static void readProducts(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {
            while (rs.next()) System.out.println(rs.getInt("ProductID") + " " + rs.getString("ProductName") + " " + rs.getDouble("Price") + " " + rs.getInt("Quantity"));
        }
    }

    private static void updateProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID, new Price, new Quantity: ");
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?")) {
            pstmt.setDouble(1, scanner.nextDouble());
            pstmt.setInt(2, scanner.nextInt());
            pstmt.setInt(3, scanner.nextInt());
            System.out.println(pstmt.executeUpdate() > 0 ? "Product updated." : "Product not found.");
        }
    }

    private static void deleteProduct(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter ProductID to delete: ");
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Product WHERE ProductID = ?")) {
            pstmt.setInt(1, scanner.nextInt());
            System.out.println(pstmt.executeUpdate() > 0 ? "Product deleted." : "Product not found.");
        }
    }
}
