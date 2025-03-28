import java.sql.*;
import java.util.Scanner;

record Student(int studentID, String name, String department, double marks) {}

class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "root";
    private static final String PASSWORD = "tusar@04";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) pstmt.setObject(i + 1, params[i]);
            System.out.println(pstmt.executeUpdate() > 0 ? "Operation successful." : "No changes made.");
        }
    }

    public static void readStudents() throws SQLException {
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Student")) {
            while (rs.next()) System.out.println(rs.getInt("StudentID") + " " + rs.getString("Name") + " " + rs.getString("Department") + " " + rs.getDouble("Marks"));
        }
    }
}

public class StudentManagement {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Create 2. Read 3. Update 4. Delete 5. Exit");
                switch (scanner.nextInt()) {
                    case 1 -> {
                        System.out.print("Enter ID, Name, Department, Marks: ");
                        StudentController.executeUpdate("INSERT INTO Student VALUES (?, ?, ?, ?)", scanner.nextInt(), scanner.next(), scanner.next(), scanner.nextDouble());
                    }
                    case 2 -> StudentController.readStudents();
                    case 3 -> {
                        System.out.print("Enter StudentID and new Marks: ");
                        StudentController.executeUpdate("UPDATE Student SET Marks = ? WHERE StudentID = ?", scanner.nextDouble(), scanner.nextInt());
                    }
                    case 4 -> {
                        System.out.print("Enter StudentID to delete: ");
                        StudentController.executeUpdate("DELETE FROM Student WHERE StudentID = ?", scanner.nextInt());
                    }
                    case 5 -> { return; }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}
