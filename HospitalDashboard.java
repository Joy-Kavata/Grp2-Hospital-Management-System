import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.*;

public class HospitalDashboard extends Application {

    private TableView<Patient> patientTable = new TableView<>();
    private TableView<Doctor> doctorTable = new TableView<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏥 Hospital Management System");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1E1E1E;");

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #111;");
        sidebar.setPrefWidth(200);

        Button btnPatients = createNavButton("Patients");
        Button btnDoctors = createNavButton("Doctors");
        Button btnBilling = createNavButton("Billing");
        sidebar.getChildren().addAll(btnPatients, btnDoctors, btnBilling);

        StackPane content = new StackPane();
        content.setPadding(new Insets(20));

        root.setLeft(sidebar);
        root.setCenter(content);

        Pane patientsPane = createPatientsPane();
        Pane doctorsPane = createDoctorsPane();

        btnPatients.setOnAction(e -> {
            content.getChildren().setAll(patientsPane);
            loadPatients();
        });

        btnDoctors.setOnAction(e -> {
            content.getChildren().setAll(doctorsPane);
            loadDoctors();
        });

        btnBilling.setOnAction(e -> showInfo("Billing section coming soon."));

        content.getChildren().setAll(patientsPane);

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.show();

        loadPatients();
        loadDoctors();
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setStyle("-fx-background-color: #222; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #0F4C75; -fx-text-fill: white;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color: #222; -fx-text-fill: white;"));
        return b;
    }

    private Pane createPatientsPane() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #1E1E1E;");

        Label title = createTitle("👨‍⚕️ Patients");

        // --- Input Fields ---
        TextField tfName = new TextField();
        tfName.setPromptText("Full Name");
        tfName.setPrefWidth(200);

        TextField tfAge = new TextField();
        tfAge.setPromptText("Age");
        tfAge.setPrefWidth(80);

        ComboBox<String> cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Male", "Female");
        cbGender.setPromptText("Gender");
        cbGender.setPrefWidth(120);

        // --- Buttons ---
        Button btnAdd = new Button("Add Patient");
        btnAdd.setStyle("-fx-background-color: #0F4C75; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdd.setOnAction(e -> {
            String name = tfName.getText();
            String ageText = tfAge.getText();
            String gender = cbGender.getValue();

            if (name.isEmpty() || ageText.isEmpty() || gender == null) {
                alert("Please fill all fields.");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                addPatient(name, age, gender);
                loadPatients();
                tfName.clear();
                tfAge.clear();
                cbGender.setValue(null);
            } catch (NumberFormatException ex) {
                alert("Age must be a valid number.");
            }
        });

        Button btnDelete = new Button("Delete Selected");
        btnDelete.setStyle("-fx-background-color: #990000; -fx-text-fill: white; -fx-font-weight: bold;");
        btnDelete.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Please select a patient to delete.");
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setHeaderText(null);
            confirm.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");
            confirm.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK) {
                    deletePatient(selected.getId());
                    loadPatients();
                }
            });
        });

        Button btnAssign = new Button("Assign Doctor");
        btnAssign.setStyle("-fx-background-color: #00695C; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAssign.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Please select a patient first.");
                return;
            }
            showAssignDoctorDialog(selected);
        });

        // --- HBox to align all elements in one row ---
        HBox form = new HBox(10);
        form.getChildren().addAll(tfName, tfAge, cbGender, btnAdd, btnDelete, btnAssign);
        form.setPadding(new Insets(10, 0, 10, 0));

        // --- Patient Table ---
        TableColumn<Patient, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Patient, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Patient, Integer> colAge = new TableColumn<>("Age");
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Patient, String> colGender = new TableColumn<>("Gender");
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Patient, String> colDoctor = new TableColumn<>("Doctor");
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));

        TableColumn<Patient, String> colBill = new TableColumn<>("Bill");
        colBill.setCellValueFactory(new PropertyValueFactory<>("bill"));

        patientTable.getColumns().setAll(colId, colName, colAge, colGender, colDoctor, colBill);
        patientTable.setPrefHeight(500);

        box.getChildren().addAll(title, form, patientTable);
        return box;
    }

    private void showAssignDoctorDialog(Patient patient) {
        Dialog<Doctor> dialog = new Dialog<>();
        dialog.setTitle("Assign Doctor to " + patient.getFullName());
        dialog.setHeaderText(null);

        ComboBox<Doctor> doctorCombo = new ComboBox<>();
        doctorCombo.setItems(loadDoctorList());
        doctorCombo.setPromptText("Select a Doctor");
        doctorCombo.setPrefWidth(300);

        dialog.getDialogPane().setContent(doctorCombo);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> button == ButtonType.OK ? doctorCombo.getValue() : null);
        dialog.showAndWait().ifPresent(selectedDoctor -> {
            assignDoctorToPatient(patient.getId(), selectedDoctor.getId(), selectedDoctor.getFullName());
        });
    }

    private ObservableList<Doctor> loadDoctorList() {
        ObservableList<Doctor> doctors = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, full_name, specialty FROM Doctors")) {
            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialty")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    private void assignDoctorToPatient(int patientId, int doctorId, String doctorName) {
        String sql = "UPDATE Patients SET doctor_id = ?, doctor = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setString(2, doctorName);
            ps.setInt(3, patientId);
            ps.executeUpdate();
            showInfo("Doctor assigned successfully!");
            loadPatients();
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Failed to assign doctor.");
        }
    }

    private Pane createDoctorsPane() {
        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #1E1E1E;");

        Label title = createTitle("🩺 Doctors");

        TextField tfName = new TextField();
        tfName.setPromptText("Full Name");

        TextField tfSpec = new TextField();
        tfSpec.setPromptText("Specialty");

        Button btnAdd = new Button("Add Doctor");
        btnAdd.setStyle("-fx-background-color: #0F4C75; -fx-text-fill: white;");
        btnAdd.setOnAction(e -> {
            if (tfName.getText().isEmpty() || tfSpec.getText().isEmpty()) {
                alert("Please fill all fields.");
                return;
            }
            addDoctor(tfName.getText(), tfSpec.getText());
            loadDoctors();
            tfName.clear();
            tfSpec.clear();
        });

        Button btnDelete = new Button("Delete Selected");
        btnDelete.setStyle("-fx-background-color: #990000; -fx-text-fill: white;");
        btnDelete.setOnAction(e -> {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Please select a doctor to delete.");
                return;
            }
            deleteDoctor(selected.getId());
            loadDoctors();
        });

        HBox form = new HBox(10);
        form.getChildren().addAll(tfName, tfSpec, btnAdd, btnDelete);
        form.setPadding(new Insets(10, 0, 10, 0));

        TableColumn<Doctor, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Doctor, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Doctor, String> colSpec = new TableColumn<>("Specialty");
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specialty"));

        doctorTable.getColumns().setAll(colId, colName, colSpec);
        doctorTable.setPrefHeight(500);

        box.getChildren().addAll(title, form, doctorTable);
        return box;
    }

    private void addPatient(String name, int age, String gender) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Patients(full_name, age, gender) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.executeUpdate();
            showInfo("Patient added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Failed to add patient.");
        }
    }

    private void addDoctor(String name, String specialty) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Doctors(full_name, specialty) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, specialty);
            ps.executeUpdate();
            showInfo("Doctor added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Failed to add doctor.");
        }
    }

    private void loadPatients() {
        ObservableList<Patient> data = FXCollections.observableArrayList();
        String sql = "SELECT id, full_name, age, gender, bill, doctor, doctor_id, status FROM Patients";
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                data.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("bill"),
                        rs.getString("doctor"),
                        rs.getInt("doctor_id"),
                        rs.getString("status")
                ));
            }
            patientTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error loading patients.");
        }
    }

    private void loadDoctors() {
        ObservableList<Doctor> data = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, full_name, specialty FROM Doctors")) {
            while (rs.next()) {
                data.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialty")
                ));
            }
            doctorTable.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error loading doctors.");
        }
    }

    private void deletePatient(int id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Patients WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error deleting patient.");
        }
    }

    private void deleteDoctor(int id) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Doctors WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error deleting doctor.");
        }
    }

    private Label createTitle(String text) {
        Label lbl = new Label(text);
        lbl.setTextFill(Color.WHITE);
        lbl.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        return lbl;
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
