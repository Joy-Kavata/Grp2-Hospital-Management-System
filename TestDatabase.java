import java.sql.*;

public class TestDatabase {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.connect()) {
            String insertQuery = "INSERT INTO Patients (full_name, age, gender) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, "Faith Adni");
            stmt.setInt(2, 20);
            stmt.setString(3, "Female");
            stmt.executeUpdate();
            System.out.println("✅ Patient inserted successfully!");
            System.out.println("Using DB path: " + new java.io.File("hospital.db").getAbsolutePath());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
