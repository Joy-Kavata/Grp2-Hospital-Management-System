import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class HospitalClient {
    public static void main(String[] args) {
        try {
            // Connect to RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HospitalService service = (HospitalService) registry.lookup("HospitalService");

            System.out.println("\n=== 🏥 Doctors in the System ===");
            List<Doctor> doctors = service.getAllDoctors();
            if (doctors == null || doctors.isEmpty()) {
                System.out.println("⚠️ No doctors found in the database.");
            } else {
                for (Doctor d : doctors) {
                    System.out.println("- ID: " + d.getId() +
                                       " | Name: " + d.getName() +
                                       " | Specialty: " + d.getSpecialization());
                }
            }

            System.out.println("\n=== 👩‍⚕️ Patients in the System ===");
            List<Patient> patients = service.getAllPatients();
            if (patients == null || patients.isEmpty()) {
                System.out.println("⚠️ No patients found in the database.");
            } else {
                for (Patient p : patients) {
                    System.out.println("- ID: " + p.getId() +
                                       " | Name: " + p.getFullName() +
                                       " | Gender: " + p.getGender() +
                                       " | Age: " + p.getAge());
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error in HospitalClient:");
            e.printStackTrace();
        }
    }
}
