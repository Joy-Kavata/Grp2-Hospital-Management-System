import java.io.Serializable;

public class Patient implements Serializable {
    private int id;
    private String fullName;
    private int age;
    private String gender;
    private String bill;
    private String doctor;
    private int doctorId;
    private String status;

    public Patient() {}

    public Patient(int id, String fullName, int age, String gender, String bill, String doctor, int doctorId, String status) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.bill = bill;
        this.doctor = doctor;
        this.doctorId = doctorId;
        this.status = status;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getBill() { return bill; }
    public String getDoctor() { return doctor; }
    public int getDoctorId() { return doctorId; }
    public String getStatus() { return status; }

    public void setDoctor(String doctor) { this.doctor = doctor; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setStatus(String status) { this.status = status; }
}
