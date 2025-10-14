import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingDAO {

    public static void addBill(int patientId, double amount) {
        String insertBillingSQL = "INSERT INTO Billing (patient_id, amount, status) VALUES (?, ?, 'Pending')";
        String updatePatientSQL = "UPDATE Patients SET bill = ?, status = 'Pending' WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {

            // Step 1: Insert into Billing table
            try (PreparedStatement pstmt = conn.prepareStatement(insertBillingSQL)) {
                pstmt.setInt(1, patientId);
                pstmt.setDouble(2, amount);
                pstmt.executeUpdate();
                System.out.println("✅ Bill record added successfully.");
            }

            // Step 2: Update Patients table
            try (PreparedStatement pstmt = conn.prepareStatement(updatePatientSQL)) {
                pstmt.setDouble(1, amount);
                pstmt.setInt(2, patientId);
                pstmt.executeUpdate();
                System.out.println("✅ Patient bill and status updated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to add bill:");
            e.printStackTrace();
        }
    }
    public static void markBillAsPaid(int patientId) {
        String updateBillingSQL = "UPDATE Billing SET status = 'Paid' WHERE patient_id = ?";
        String updatePatientSQL = "UPDATE Patients SET status = 'Paid' WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {

            // Step 1: Update Billing table
            try (PreparedStatement pstmt = conn.prepareStatement(updateBillingSQL)) {
                pstmt.setInt(1, patientId);
                pstmt.executeUpdate();
            }

            // Step 2: Update Patients table
            try (PreparedStatement pstmt = conn.prepareStatement(updatePatientSQL)) {
                pstmt.setInt(1, patientId);
                pstmt.executeUpdate();
            }

            System.out.println("✅ Billing status updated to 'Paid' successfully for patient ID " + patientId);

        } catch (SQLException e) {
            System.err.println("❌ Failed to update billing status:");
            e.printStackTrace();
        }
    }

}
