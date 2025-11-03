package backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import backend.*;

public interface HospitalService extends Remote {
    // Doctors
    void addDoctor(Doctor doctor) throws RemoteException;
    List<Doctor> getAllDoctors() throws RemoteException;

    // Patients
    void addPatient(Patient patient) throws RemoteException;
    List<Patient> getAllPatients() throws RemoteException;

    // Utility
    String getServerStatus() throws RemoteException;
}
