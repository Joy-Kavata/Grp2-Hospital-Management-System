import java.lang.classfile.Label;

import javax.swing.table.TableColumn;
import javax.swing.text.TableView;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HospitalManagementSystem extends Application {

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    private Label statusLabel;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏥 Hospital Management System");

        Label title = new Label("🏥 Hospital Management System");
        title.setFont(new Font("Arial", 26));
        title.setTextFill(Color.DARKBLUE);

        Button addBtn = new Button("➕ Add Patient");
        Button viewBtn = new Button("📋 View Patients");
        Button assignBtn = new Button("👨‍⚕️ Assign Doctor");
        Button billBtn = new Button("💰 Billing");
        Button exitBtn = new Button("❌ Exit");

        for (Button btn : new Button[]{addBtn, viewBtn, assignBtn, billBtn, exitBtn}) {
            btn.setPrefWidth(220);
            btn.setFont(new Font("Arial", 14));
            btn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 8;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #005A9E; -fx-text-fill: white; -fx-background-radius: 8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-background-radius: 8;"));
        }

        statusLabel = new Label("Status: Waiting for action...");
        statusLabel.setFont(new Font("Arial", 14));
        statusLabel.setTextFill(Color.DARKSLATEGRAY);


        addBtn.setOnAction(e -> showAddPatientForm());
        viewBtn.setOnAction(e -> showPatientTable());
        assignBtn.setOnAction(e -> showDoctorAssignment());
        billBtn.setOnAction(e -> showBillingMenu());
        exitBtn.setOnAction(e -> stage.close());

        VBox buttons = new VBox(12, addBtn, viewBtn, assignBtn, billBtn, exitBtn);
        buttons.setPadding(new Insets(25));
        buttons.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(15), Insets.EMPTY)));

        VBox layout = new VBox(20, title, buttons, statusLabel);
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(layout, 480, 520);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddPatientForm() {
        Stage formStage = new Stage();
        formStage.setTitle("➕ Add Patient");

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();

        Label genderLabel = new Label("Gender:");
        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female", "Other");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> {
            String name = nameField.getText();
            String age = ageField.getText();
            String gender = genderBox.getValue();

            if (name.isEmpty() || age.isEmpty() || gender == null) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            patients.add(new Patient(name, age, gender));
            statusLabel.setText("Status: New patient added.");
            formStage.close();
        });

        VBox form = new VBox(10, nameLabel, nameField, ageLabel, ageField, genderLabel, genderBox, saveBtn);
        form.setPadding(new Insets(20));
        form.setBackground(new Background(new BackgroundFill(Color.BEIGE, new CornerRadii(10), Insets.EMPTY)));

        formStage.setScene(new Scene(form, 300, 300));
        formStage.show();
    }

    private void showPatientTable() {
        Stage tableStage = new Stage();
        tableStage.setTitle("📋 Patient List");

        TableView<Patient> table = new TableView<>(patients);

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patient, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        TableColumn<Patient, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));

        TableColumn<Patient, String> billCol = new TableColumn<>("Bill");
        billCol.setCellValueFactory(new PropertyValueFactory<>("bill"));

        table.getColumns().addAll(nameCol, ageCol, genderCol, doctorCol, billCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox vbox = new VBox(table);
        vbox.setPadding(new Insets(20));

        tableStage.setScene(new Scene(vbox, 600, 350));
        tableStage.show();
    }

    private void showDoctorAssignment() {
        if (patients.isEmpty()) {
            showAlert("Info", "No patients available.");
            return;
        }

        Stage assignStage = new Stage();
        assignStage.setTitle("👨‍⚕️ Assign Doctor");

        ComboBox<Patient> patientBox = new ComboBox<>(patients);
        patientBox.setPromptText("Select Patient");
        patientBox.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Patient p) { return (p != null) ? p.getName() : ""; }
            @Override public Patient fromString(String s) { return null; }
        });

        ComboBox<String> doctorBox = new ComboBox<>();
        doctorBox.getItems().addAll("Dr. Kimani", "Dr. Achieng", "Dr. Mwangi", "Dr. Patel", "Dr. Hassan");

        Button assignBtn = new Button("Assign Doctor");
        assignBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
        assignBtn.setOnAction(e -> {
            Patient selected = patientBox.getValue();
            String doctor = doctorBox.getValue();

            if (selected == null || doctor == null) {
                showAlert("Error", "Please select both patient and doctor.");
                return;
            }

            selected.setDoctor(doctor);
            showAlert("Success", "Doctor " + doctor + " assigned to " + selected.getName());
            statusLabel.setText("Status: Doctor assigned.");
            assignStage.close();
        });

        VBox layout = new VBox(12, new Label("Select Patient:"), patientBox,
                new Label("Select Doctor:"), doctorBox, assignBtn);
        layout.setPadding(new Insets(20));

        assignStage.setScene(new Scene(layout, 350, 250));
        assignStage.show();
    }

    private void showBillingMenu() {
        if (patients.isEmpty()) {
            showAlert("Info", "No patients available.");
            return;
        }

        Stage billStage = new Stage();
        billStage.setTitle("💰 Assign Billing");

        ComboBox<Patient> patientBox = new ComboBox<>(patients);
        patientBox.setPromptText("Select Patient");
        patientBox.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Patient p) { return (p != null) ? p.getName() : ""; }
            @Override public Patient fromString(String s) { return null; }
        });

        TextField billField = new TextField();
        billField.setPromptText("Enter Amount (Ksh)");

        Button assignBtn = new Button("Assign Bill");
        assignBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        assignBtn.setOnAction(e -> {
            Patient selected = patientBox.getValue();
            String bill = billField.getText();

            if (selected == null || bill.isEmpty()) {
                showAlert("Error", "Please select a patient and enter a bill amount.");
                return;
            }

            selected.setBill("Ksh " + bill);
            showAlert("Success", "Bill of Ksh " + bill + " assigned to " + selected.getName());
            statusLabel.setText("Status: Bill assigned.");
            billStage.close();
        });

        VBox layout = new VBox(12, new Label("Select Patient:"), patientBox,
                new Label("Enter Bill Amount:"), billField, assignBtn);
        layout.setPadding(new Insets(20));

        billStage.setScene(new Scene(layout, 350, 250));
        billStage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ✅ Patient Class
    public static class Patient {
        private final String name;
        private final String age;
        private final String gender;
        private String doctor = "Not Assigned";
        private String bill = "Pending";

        public Patient(String name, String age, String gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public String getName() { return name; }
        public String getAge() { return age; }
        public String getGender() { return gender; }
        public String getDoctor() { return doctor; }
        public String getBill() { return bill; }

        public void setDoctor(String doctor) { this.doctor = doctor; }
        public void setBill(String bill) { this.bill = bill; }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
