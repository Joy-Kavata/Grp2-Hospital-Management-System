// AssignDoctorTest.java
import java.util.*;
import java.sql.*;

public class AssignDoctorTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Step 1: Show all patients
        List<Integer> patientIds = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, full_name FROM Patients")) {

            System.out.println("📋 List of Patients:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("full_name");
                System.out.println(" - " + id + ": " + name);
                patientIds.add(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (patientIds.isEmpty()) {
            System.out.println("⚠️ No patients found. Add one first!");
            return;
        }

        // Step 2: Choose a patient
        System.out.print("Enter the patient ID to assign a doctor: ");
        int patientId = scanner.nextInt();

        // Step 3: Show all doctors
        List<DoctorDAO.Doctor> doctors = DoctorDAO.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("⚠️ No doctors found. Add some first!");
            return;
        }

        System.out.println("\n📋 List of Doctors:");
        for (DoctorDAO.Doctor d : doctors) {
            System.out.println(" - " + d.getId() + ": " + d.getFullName() + " (" + d.getSpecialty() + ")");
        }

        // Step 4: Choose a doctor
        System.out.print("Enter the doctor ID to assign: ");
        int doctorId = scanner.nextInt();

        // Step 5: Assign doctor
        PatientDAO.assignDoctorToPatient(patientId, doctorId);
    }
}
