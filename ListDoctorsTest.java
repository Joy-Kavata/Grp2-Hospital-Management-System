// ListDoctorsTest.java
import java.util.List;

public class ListDoctorsTest {
    public static void main(String[] args) {
        List<DoctorDAO.Doctor> doctors = DoctorDAO.getAllDoctors();

        if (doctors.isEmpty()) {
            System.out.println("⚠️ No doctors found in the database!");
        } else {
            System.out.println("✅ Doctors found:");
            for (DoctorDAO.Doctor doc : doctors) {
                System.out.println(" - ID: " + doc.getId() + " | " + doc.getFullName() + " | Specialty: " + doc.getSpecialty());
            }
        }
    }
}
