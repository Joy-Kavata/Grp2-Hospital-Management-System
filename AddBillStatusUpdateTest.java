import java.util.Scanner;

public class AddBillStatusUpdateTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Patient ID to mark bill as Paid: ");
        int patientId = sc.nextInt();

        BillingDAO.markBillAsPaid(patientId);
    }
}
