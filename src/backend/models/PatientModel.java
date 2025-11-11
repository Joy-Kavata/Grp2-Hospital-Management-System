package backend.models;

import java.io.Serializable;

// Make the class serializable so it can be saved to a file
public class PatientModel implements Serializable {
    
    // This ID is important for serialization
    private static final long serialVersionUID = 1L;

    private int id;
    private String fullName;
    private int age;
    private String gender;
    private String doctor;
    private String status;
    private double bill;

    // Constructor to create a patient from database data
    public PatientModel(int id, String fullName, int age, String gender, String doctor, String status, double bill) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.doctor = doctor;
        this.status = status;
        this.bill = bill;
    }

    @Override
    public String toString() {
        return "Patient [ID=" + id + ", Name=" + fullName + ", Age=" + age + 
               ", Doctor=" + doctor + ", Status=" + status + ", Bill=" + bill + "]";
    }
}