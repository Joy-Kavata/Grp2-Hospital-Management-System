import java.io.Serializable;

public class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;  
    private int id;
    private String fullName;
    private String specialty;

    public Doctor(int id, String fullName, String specialty) {
        this.id = id;
        this.fullName = fullName;
        this.specialty = specialty;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getSpecialty() { return specialty; }

    @Override
    public String toString() {
        return fullName + " (" + specialty + ")";
    }
}
