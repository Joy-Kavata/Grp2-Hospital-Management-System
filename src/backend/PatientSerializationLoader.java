package backend;

import backend.models.PatientModel; // Import the model
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PatientSerializationLoader {

    public static List<PatientModel> deserializePatientsFromFile(String fileName) {
        
        // We'll put the loaded patients into this list
        List<PatientModel> patientList = new ArrayList<>();

        try (
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))
        ) {
            
            // Read the one single object (the list) from the file
            // We have to "cast" it because Java doesn't know what type of object is in the file
            @SuppressWarnings("unchecked") // This suppresses the compiler warning for the cast
            List<PatientModel> loadedList = (List<PatientModel>) ois.readObject();
            
            patientList = loadedList; // Assign the loaded list to our list

            System.out.println("✅ Patient list successfully deserialized from: " + fileName);

        } catch (FileNotFoundException e) {
            System.err.println("❌ Error: File not found. Make sure 'patients.ser' exists.");
        } catch (IOException e) {
            System.err.println("❌ Error reading file:");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: The class 'PatientModel' was not found. (Did you forget to compile?)");
            e.printStackTrace();
        }

        return patientList;
    }

    // Main method to run this file directly for testing
    public static void main(String[] args) {
        // Load the patients
        List<PatientModel> loadedPatients = deserializePatientsFromFile("patients.ser");

        if (!loadedPatients.isEmpty()) {
            System.out.println("\n--- Loaded Patient Data ---");
            
            // Loop through the list and print each patient
            for (PatientModel patient : loadedPatients) {
                System.out.println(patient.toString());
            }
        } else {
            System.out.println("No patient data was loaded.");
        }
    }
}