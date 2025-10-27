import java.rmi.Naming;

public class HospitalClient {
    public static void main(String[] args) {
        try {
            HospitalService service = (HospitalService) Naming.lookup("rmi://localhost/HospitalService");

            // Add sample data
            service.addDoctor(new Doctor(1, "Dr. Mercy Wangari", "Pediatrics"));
            service.addDoctor(new Doctor(2, "Dr. David Ochieng", "Orthopedics"));

            service.addPatient(new Patient(1, "Alice Mwikali", 24, "Female", "Ksh 3,500", "Dr. Mercy Wangari", 1, "Admitted"));
            service.addPatient(new Patient(2, "John Mutua", 41, "Male", "Ksh 5,000", "Dr. David Ochieng", 2, "Discharged"));

            // Display results
            System.out.println("\n--- Doctors ---");
            for (Doctor doc : service.getAllDoctors()) {
                System.out.println(doc);
            }

            System.out.println("\n--- Patients ---");
            for (Patient pat : service.getAllPatients()) {
                System.out.println(pat.getFullName() + " (" + pat.getDoctor() + ")");
            }

            System.out.println("\nServer Message: " + service.getServerStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
