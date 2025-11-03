package backend;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import backend.*;

public class PatientHistoryDAO {

    public static void addHistory(int patientId, String diagnosis, String treatment, String notes) {
        String sql = "INSERT INTO PatientHistory (patient_id, diagnosis, treatment, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setString(2, diagnosis);
            pstmt.setString(3, treatment);
            pstmt.setString(4, notes);
            pstmt.executeUpdate();
            System.out.println("✅ Medical history added successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to add medical history:");
            e.printStackTrace();
        }
    }

    public static List<String> viewHistory(int patientId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT visit_date, diagnosis, treatment, notes FROM PatientHistory WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                history.add("📅 " + rs.getString("visit_date") +
                            " | Diagnosis: " + rs.getString("diagnosis") +
                            " | Treatment: " + rs.getString("treatment") +
                            " | Notes: " + rs.getString("notes"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching patient history:");
            e.printStackTrace();
        }
        return history;
    }
}
