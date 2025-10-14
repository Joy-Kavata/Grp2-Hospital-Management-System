import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientDAO {

    public static boolean addPatient(Patient patient) {
        String sql = "INSERT INTO Patients (full_name, age, gender) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("❌ Cannot insert patient because connection is null.");
                return false;
            }

            stmt.setString(1, patient.getFullName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());

            int rows = stmt.executeUpdate();
            System.out.println("✅ Patient inserted successfully (" + rows + " row(s) affected).");
            return true;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error while adding patient:");
            e.printStackTrace();
            return false;
        }
    }

    // Assign a doctor to a patient
    public static void assignDoctorToPatient(int patientId, int doctorId) {
        String getDoctorNameSQL = "SELECT full_name FROM Doctors WHERE id = ?";
        String updatePatientSQL = "UPDATE Patients SET doctor_id = ?, doctor = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {

            // Step 1: Fetch the doctor's name
            String doctorName = null;
            try (PreparedStatement getStmt = conn.prepareStatement(getDoctorNameSQL)) {
                getStmt.setInt(1, doctorId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        doctorName = rs.getString("full_name");
                    } else {
                        System.out.println("⚠️ No doctor found with ID " + doctorId);
                        return;
                    }
                }
            }

            // Step 2: Update the patient record
            try (PreparedStatement updateStmt = conn.prepareStatement(updatePatientSQL)) {
                updateStmt.setInt(1, doctorId);
                updateStmt.setString(2, doctorName);
                updateStmt.setInt(3, patientId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Assigned " + doctorName + " to patient (ID " + patientId + ").");
                } else {
                    System.out.println("⚠️ No patient found with ID " + patientId + ".");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to assign doctor to patient:");
            e.printStackTrace();
        }
    }

    public static void deletePatient(int patientId) {
    String sql = "DELETE FROM Patients WHERE id = ?";
    try (Connection conn = DatabaseConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, patientId);
        int rows = pstmt.executeUpdate();

        if (rows > 0)
            System.out.println("✅ Patient deleted successfully.");
        else
            System.out.println("⚠️ No patient found with that ID.");

    } catch (SQLException e) {
        System.err.println("❌ Error deleting patient:");
        e.printStackTrace();
    }
}


}
