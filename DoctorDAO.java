import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public static void addDoctor(String name, String specialty) {
        String sql = "INSERT INTO Doctors (name, specialty) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, specialty);
            pstmt.executeUpdate();
            System.out.println("✅ Doctor added successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to add doctor:");
            e.printStackTrace();
        }
    }

    public static void removeDoctor(int doctorId) {
        String sql = "DELETE FROM Doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.executeUpdate();
            System.out.println("✅ Doctor removed successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to remove doctor:");
            e.printStackTrace();
        }
    }

    public static List<String> getDoctorsBySpecialty(String specialty) {
        List<String> doctors = new ArrayList<>();
        String sql = "SELECT id, name FROM Doctors WHERE specialty = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, specialty);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getInt("id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching doctors by specialty:");
            e.printStackTrace();
        }
        return doctors;
    }

    public static void assignPatient(int doctorId, int patientId) {
        String sql = "INSERT INTO DoctorPatient (doctor_id, patient_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setInt(2, patientId);
            pstmt.executeUpdate();
            System.out.println("✅ Patient assigned to doctor successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to assign patient to doctor:");
            e.printStackTrace();
        }
    }

    public static List<String> viewDoctorPatients(int doctorId) {
        List<String> patients = new ArrayList<>();
        String sql = """
            SELECT P.full_name, P.bill, P.status 
            FROM Patients P
            JOIN DoctorPatient DP ON P.id = DP.patient_id
            WHERE DP.doctor_id = ?
        """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                patients.add(rs.getString("full_name") + 
                             " | Bill: " + rs.getDouble("bill") +
                             " | Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching doctor's patients:");
            e.printStackTrace();
        }
        return patients;
    }
}
