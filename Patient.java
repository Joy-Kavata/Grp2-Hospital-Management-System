public class Patient implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String fullName;
    private int age;
    private String gender;
    private String doctor;
    private double bill;
    private String status; // ✅ New field

    // No-argument constructor
    public Patient() {}

    // Parameterized constructor
    public Patient(int id, String fullName, int age, String gender, String doctor, double bill, String status) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.doctor = doctor;
        this.bill = bill;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDoctor() {
        return doctor;
    }
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public double getBill() {
        return bill;
    }
    public void setBill(double bill) {
        this.bill = bill;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return fullName + " (" + gender + ", " + age + ", " + status + ")";
    }
}
