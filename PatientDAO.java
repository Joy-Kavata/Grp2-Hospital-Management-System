import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // Add a new patient
    public static boolean addPatient(Patient patient) {
        String sql = "INSERT INTO Patients (full_name, age, gender, status, bill) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFullName());
            stmt.setInt(2, patient.getAge());
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getStatus());
            stmt.setDouble(5, patient.getBill());

            int rows = stmt.executeUpdate();
            System.out.println("✅ Patient added successfully (" + rows + " row(s)).");
            return true;

        } catch (SQLException e) {
            System.err.println("❌ SQL Error while adding patient:");
            e.printStackTrace();
            return false;
        }
    }

    // Assign doctor to a patient
    public static void assignDoctorToPatient(int patientId, int doctorId) {
        String getDoctorSQL = "SELECT full_name FROM Doctors WHERE id = ?";
        String updatePatientSQL = "UPDATE Patients SET doctor_id = ?, doctor = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect()) {
            if (conn == null) {
                System.err.println("❌ Connection is null.");
                return;
            }

            // Get doctor name
            String doctorName = null;
            try (PreparedStatement stmt = conn.prepareStatement(getDoctorSQL)) {
                stmt.setInt(1, doctorId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        doctorName = rs.getString("full_name");
                    } else {
                        System.out.println("⚠️ Doctor not found.");
                        return;
                    }
                }
            }

            // Update patient record
            try (PreparedStatement stmt = conn.prepareStatement(updatePatientSQL)) {
                stmt.setInt(1, doctorId);
                stmt.setString(2, doctorName);
                stmt.setInt(3, patientId);
                int rows = stmt.executeUpdate();
                if (rows > 0)
                    System.out.println("✅ Doctor assigned successfully.");
                else
                    System.out.println("⚠️ No patient found with that ID.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Failed to assign doctor to patient:");
            e.printStackTrace();
        }
    }

    // Delete a patient
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

    // Fetch all patients
    public static List<String> getAllPatients() {
        List<String> patients = new ArrayList<>();
        String sql = "SELECT id, full_name, age, gender, status, bill FROM Patients";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(
                    rs.getInt("id") + ". " + rs.getString("full_name") +
                    " | Age: " + rs.getInt("age") +
                    " | Gender: " + rs.getString("gender") +
                    " | Status: " + rs.getString("status") +
                    " | Bill: " + rs.getDouble("bill")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching patients:");
            e.printStackTrace();
        }
        return patients;
    }

    public static List<Patient> getAllPatientsFromDB() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT id, full_name, age, gender FROM Patients";

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setFullName(rs.getString("full_name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patients.add(patient);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching patients from DB:");
            e.printStackTrace();
        }
        return patients;
    }

}
