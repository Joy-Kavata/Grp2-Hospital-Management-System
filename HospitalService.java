import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HospitalService extends Remote {
    void addDoctor(Doctor doctor) throws RemoteException;
    void addPatient(Patient patient) throws RemoteException;
    List<Doctor> getAllDoctors() throws RemoteException;
    List<Patient> getAllPatients() throws RemoteException;
    String getServerStatus() throws RemoteException;
}
