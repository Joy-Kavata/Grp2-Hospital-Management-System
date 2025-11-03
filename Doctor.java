public class Doctor implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String specialization;

    // No-argument constructor
    public Doctor() {}

    // Parameterized constructor
    public Doctor(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setFullName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public String getFullName() {
        return name;
    }


    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
