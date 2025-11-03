import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HospitalServer {
    public static void main(String[] args) {
        try {
            // Start the RMI registry on port 1099
            LocateRegistry.createRegistry(1099);

            // Create service instance (which talks to your SQL Server through DAOs)
            HospitalService service = new HospitalServiceImpl();

            // Bind it for clients to use
            Naming.rebind("rmi://localhost/HospitalService", service);

            System.out.println("✅ Hospital RMI Server started successfully and connected to SQL Server.");

        } catch (Exception e) {
            System.err.println("❌ Failed to start RMI server:");
            e.printStackTrace();
        }
    }
}
