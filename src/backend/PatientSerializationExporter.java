package backend;

import backend.models.PatientModel; // Import the new model
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientSerializationExporter {

    public static void serializePatientsToFile(String fileName) {
        
        // This list will hold all our patient objects
        List<PatientModel> patientList = new ArrayList<>();
        String query = "SELECT id, full_name, age, gender, doctor, status, bill FROM Patients";

        // Step A: Fetch all patients from the database and put them in the list
        try (
            Connection conn = DatabaseConnection.connect();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()
        ) {
            
            while (rs.next()) {
                // Create a new PatientModel object for each row
                PatientModel patient = new PatientModel(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getInt("age"),
                    rs.getString("gender"),
                    rs.getString("doctor"),
                    rs.getString("status"),
                    rs.getDouble("bill")
                );
                // Add the object to our list
                patientList.add(patient);
            }
            System.out.println("✅ Data fetched from database. Found " + patientList.size() + " patients.");

        } catch (SQLException e) {
            System.err.println("❌ Error fetching patients from database:");
            e.printStackTrace();
        }

        // Step B: Serialize the entire list to a file
        if (!patientList.isEmpty()) {
            try (
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))
            ) {
                // Write the whole list as a single object
                oos.writeObject(patientList);
                System.out.println("✅ Patient list successfully serialized to: " + fileName);
                
            } catch (IOException e) {
                System.err.println("❌ Error serializing patient list:");
                e.printStackTrace();
            }
        }
    }

    // Main method to make this file runnable for testing
    public static void main(String[] args) {
        serializePatientsToFile("patients.ser");
    }
}