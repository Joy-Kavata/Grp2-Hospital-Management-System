// AddBillTest.java
import java.sql.*;
import java.util.*;

public class AddBillTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // List all patients
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, full_name, bill FROM Patients")) {

            System.out.println("📋 List of Patients:");
            while (rs.next()) {
                System.out.println(" - ID: " + rs.getInt("id") + " | " + rs.getString("full_name") + " | Bill: " + rs.getString("bill"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Choose patient and add bill
        System.out.print("\nEnter Patient ID to add a bill: ");
        int patientId = scanner.nextInt();

        System.out.print("Enter Bill Amount: ");
        double amount = scanner.nextDouble();

        BillingDAO.addBill(patientId, amount);
    }
}
