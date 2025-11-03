package backend;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import backend.*;

public class HospitalServiceImpl extends UnicastRemoteObject implements HospitalService {

    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;

    public HospitalServiceImpl() throws RemoteException {
        super();
        doctorDAO = new DoctorDAO();
        patientDAO = new PatientDAO();
    }

    @Override
    public void addDoctor(Doctor doctor) throws RemoteException {
        // Use DAO to persist doctor; DAO expects (fullName, specialty)
        doctorDAO.addDoctor(doctor.getFullName(), doctor.getSpecialization());
    }

    @Override
    public List<Doctor> getAllDoctors() throws RemoteException {
        return doctorDAO.getAllDoctorsFromDB();
    }

    @Override
    public void addPatient(Patient patient) throws RemoteException {
        patientDAO.addPatient(patient);
    }

    @Override
    public List<Patient> getAllPatients() throws RemoteException {
        return patientDAO.getAllPatientsFromDB();
    }

    @Override
    public String getServerStatus() throws RemoteException {
        return "✅ Hospital Server is running smoothly and connected to SQL Server.";
    }
}
