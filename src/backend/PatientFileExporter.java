package backend;

import java.io.*;
import java.sql.*;

public class PatientFileExporter {

    public static void exportPatientsToFile(String fileName) {
        String query = "SELECT id, full_name, age, gender, doctor, status, bill FROM Patients";

        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))
        ) {
            writer.write("=== Hospital Patients Report ===");
            writer.newLine();
            writer.write("ID\tName\tAge\tGender\tDoctor\tStatus\tBill");
            writer.newLine();
            writer.write("------------------------------------------");
            writer.newLine();

            while (rs.next()) {
                String line = rs.getInt("id") + "\t" +
                              rs.getString("full_name") + "\t" +
                              rs.getInt("age") + "\t" +
                              rs.getString("gender") + "\t" +
                              rs.getString("doctor") + "\t" +
                              rs.getString("status") + "\t" +
                              rs.getDouble("bill");
                writer.write(line);
                writer.newLine();
            }

            System.out.println("✅ Patient data successfully exported to: " + fileName);

        } catch (SQLException | IOException e) {
            System.err.println("❌ Error exporting patients to file:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        exportPatientsToFile("patients_report.txt");
    }
}
