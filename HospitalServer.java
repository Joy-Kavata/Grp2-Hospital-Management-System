import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HospitalServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            HospitalService service = new HospitalServiceImpl();
            Naming.rebind("rmi://localhost/HospitalService", service);
            System.out.println("✅ Hospital RMI Server started successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
