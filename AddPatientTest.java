import java.util.Scanner;

public class AddPatientTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter patient full name: ");
        String name = scanner.nextLine();

        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter gender (M/F): ");
        String gender = scanner.nextLine();

        Patient patient = new Patient(name, age, gender);
        boolean success = PatientDAO.addPatient(patient);

        if (success) {
            System.out.println("🎉 Patient added successfully!");
        } else {
            System.out.println("❌ Failed to add patient.");
        }

        scanner.close();
    }
}
