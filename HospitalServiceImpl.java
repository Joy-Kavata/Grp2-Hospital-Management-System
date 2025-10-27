import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class HospitalServiceImpl extends UnicastRemoteObject implements HospitalService {

    private final List<Doctor> doctors;
    private final List<Patient> patients;

    public HospitalServiceImpl() throws RemoteException {
        doctors = new ArrayList<>();
        patients = new ArrayList<>();
    }

    @Override
    public void addDoctor(Doctor doctor) throws RemoteException {
        doctors.add(doctor);
        System.out.println("Doctor added: " + doctor.getFullName());
    }

    @Override
    public List<Doctor> getAllDoctors() throws RemoteException {
        return doctors;
    }

    @Override
    public void addPatient(Patient patient) throws RemoteException {
        patients.add(patient);
        System.out.println("Patient added: " + patient.getFullName());
    }

    @Override
    public List<Patient> getAllPatients() throws RemoteException {
        return patients;
    }

    @Override
    public String getServerStatus() throws RemoteException {
        return "Hospital RMI Server is up and running...";
    }
}
