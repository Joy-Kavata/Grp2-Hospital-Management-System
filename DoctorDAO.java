import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // Add a new doctor
    public static void addDoctor(String fullName, String specialty) {
        String sql = "INSERT INTO Doctors (full_name, specialty) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, specialty);
            pstmt.executeUpdate();
            System.out.println("✅ Doctor added successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to add doctor:");
            e.printStackTrace();
        }
    }

    // Remove a doctor
    public static void removeDoctor(int doctorId) {
        String sql = "DELETE FROM Doctors WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Doctor removed successfully.");
            else
                System.out.println("⚠️ No doctor found with that ID.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to remove doctor:");
            e.printStackTrace();
        }
    }

    // Get doctors by specialty
    public static List<String> getDoctorsBySpecialty(String specialty) {
        List<String> doctors = new ArrayList<>();
        String sql = "SELECT id, full_name FROM Doctors WHERE specialty = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, specialty);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getInt("id") + ": " + rs.getString("full_name"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching doctors by specialty:");
            e.printStackTrace();
        }
        return doctors;
    }

    // Assign patient to doctor
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

    // View patients for a specific doctor
    public static List<String> viewDoctorPatients(int doctorId) {
        List<String> patients = new ArrayList<>();
        String sql = """
            SELECT P.id, P.full_name, P.age, P.gender, P.bill, P.status 
            FROM Patients P
            JOIN DoctorPatient DP ON P.id = DP.patient_id
            WHERE DP.doctor_id = ?
        """;
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                patients.add(
                    rs.getInt("id") + ". " + rs.getString("full_name") +
                    " | Age: " + rs.getInt("age") +
                    " | Gender: " + rs.getString("gender") +
                    " | Bill: " + rs.getDouble("bill") +
                    " | Status: " + rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching doctor's patients:");
            e.printStackTrace();
        }
        return patients;
    }

    public static List<Doctor> getAllDoctorsFromDB() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT id, full_name, specialty FROM Doctors";

        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setFullName(rs.getString("full_name")); // ✅ fixed here
                doctor.setSpecialization(rs.getString("specialty"));
                doctors.add(doctor);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching doctors from DB:");
            e.printStackTrace();
        }
        return doctors;
    }


}
