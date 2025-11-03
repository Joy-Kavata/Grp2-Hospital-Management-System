package backend;

public class Billing {
    private int id;
    private int patientId;
    private String patientName;
    private String amount;
    private String status;

    // Constructor
    public Billing(int id, int patientId, String patientName, String amount, String status) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.amount = amount;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
