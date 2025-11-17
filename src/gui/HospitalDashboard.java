package gui;

import backend.Patient;
import backend.Doctor;
import backend.DatabaseConnection;
import backend.Billing;
import backend.BillingDAO;

// Charts & Lists
import javafx.scene.chart.PieChart;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.*;

public class HospitalDashboard extends Application {

    private TableView<Patient> patientTable = new TableView<>();
    private TableView<Doctor> doctorTable = new TableView<>();
    private TableView<Billing> billingTable = new TableView<>();

    private boolean isDarkMode = true;
    private Scene scene;
    private BorderPane root;
    
    // Buttons
    private Button btnDashboard, btnPatients, btnDoctors, btnBilling;
    private StackPane contentArea;

    @Override
    public void start(Stage stage) {
        stage.setTitle("🏥 Hospital Management System");

        root = new BorderPane();

        // --- Sidebar ---
        VBox sidebar = new VBox(10); 
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(240); 
        sidebar.getStyleClass().add("sidebar");

        // 1. Main Navigation
        btnDashboard = createNavButton("📊 Dashboard");
        btnPatients = createNavButton("👨‍⚕️ Patients");
        btnDoctors = createNavButton("🩺 Doctors");
        btnBilling = createNavButton("💰 Billing");

        // 2. Bottom Controls
        Button btnTheme = createNavButton("🌗 Switch Theme");
        btnTheme.setOnAction(e -> toggleTheme());

        Button btnLogout = createNavButton("🚪 Logout");
        btnLogout.getStyleClass().add("logout-btn"); 
        btnLogout.setOnAction(e -> handleLogout(stage));

        // 3. The Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(
            createSidebarHeader(), 
            new Separator(), 
            btnDashboard, btnPatients, btnDoctors, btnBilling, 
            spacer, 
            new Separator(), 
            btnTheme,
            btnLogout
        );

        // --- Content Area ---
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(30));
        contentArea.getStyleClass().add("content-pane");

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        // --- Navigation Logic ---
        btnDashboard.setOnAction(e -> loadDashboardView());
        btnPatients.setOnAction(e -> loadPatientsView());
        btnDoctors.setOnAction(e -> loadDoctorsView());
        btnBilling.setOnAction(e -> loadBillingView());

        // Default View
        loadDashboardView();

        // --- Scene Setup ---
        scene = new Scene(root, 1280, 800);
        stage.setScene(scene);
        applyTheme();
        stage.show();
    }

    // =================================================================================
    // 🚪 LOGOUT LOGIC
    // =================================================================================

    private void handleLogout(Stage currentStage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            currentStage.close();
            try {
                new LoginPage().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // =================================================================================
    // 🧭 NAVIGATION HANDLERS
    // =================================================================================

    private void loadDashboardView() {
        contentArea.getChildren().setAll(createDashboardPane());
    }

    private void loadPatientsView() {
        contentArea.getChildren().setAll(createPatientsPane());
        loadPatients();
    }

    private void loadDoctorsView() {
        contentArea.getChildren().setAll(createDoctorsPane());
        loadDoctors();
    }

    private void loadBillingView() {
        contentArea.getChildren().setAll(createBillingPane());
        loadBilling();
    }

    // =================================================================================
    // 📊 DASHBOARD PANE
    // =================================================================================

    private Pane createDashboardPane() {
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.TOP_LEFT);
        
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Banner
        Label welcomeTitle = new Label("Hospital Overview");
        welcomeTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: -text;");
        Label welcomeSub = new Label("Welcome back, Admin. Here is what's happening today.");
        welcomeSub.setStyle("-fx-font-size: 14px; -fx-text-fill: -muted;");
        VBox header = new VBox(5, welcomeTitle, welcomeSub);

        // Stats
        HBox stats = new HBox(20);
        stats.getChildren().addAll(
            createStatCard("TOTAL PATIENTS", getDatabaseCount("Patients"), "👨‍⚕️", "#0F4C75"),
            createStatCard("DOCTORS ON DUTY", getDatabaseCount("Doctors"), "🩺", "#00695C"),
            createStatCard("PENDING BILLS", getDatabaseCount("Billing"), "🧾", "#D32F2F"),
            createStatCard("SYSTEM STATUS", "Online", "📶", "#F57C00")
        );

        // Quick Actions
        Label actionTitle = new Label("Quick Actions");
        actionTitle.getStyleClass().add("title-label");
        
        HBox quickActions = new HBox(15);
        Button qa1 = createQuickActionButton("New Patient", "➕", e -> loadPatientsView());
        Button qa2 = createQuickActionButton("Add Doctor", "➕", e -> loadDoctorsView());
        Button qa3 = createQuickActionButton("Create Bill", "💲", e -> loadBillingView());
        quickActions.getChildren().addAll(qa1, qa2, qa3);

        // Widgets
        HBox widgets = createDashboardWidgets();

        layout.getChildren().addAll(header, stats, new Separator(), actionTitle, quickActions, new Separator(), widgets);
        
        scroll.setContent(layout);
        return new BorderPane(scroll); 
    }

    private Button createQuickActionButton(String text, String icon, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button btn = new Button(icon + "  " + text);
        btn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 15 25;");
        btn.setOnAction(action);
        addHoverAnimation(btn);
        return btn;
    }

    // =================================================================================
    // 📈 DASHBOARD WIDGETS
    // =================================================================================

    private HBox createDashboardWidgets() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        
        VBox recentPatients = createWidgetCard("🕒 Recently Added Patients");
        ListView<String> list = new ListView<>();
        list.setItems(getRecentPatients());
        list.setPrefHeight(200);
        list.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        recentPatients.getChildren().add(list);
        HBox.setHgrow(recentPatients, Priority.ALWAYS); 

        VBox analytics = createWidgetCard("📊 Financial Health");
        PieChart chart = getBillingChart();
        analytics.getChildren().add(chart);
        HBox.setHgrow(analytics, Priority.ALWAYS);

        container.getChildren().addAll(recentPatients, analytics);
        return container;
    }

    private VBox createWidgetCard(String title) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: -card; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: -text;");
        card.getChildren().add(lblTitle);
        return card;
    }

    private ObservableList<String> getRecentPatients() {
        ObservableList<String> data = FXCollections.observableArrayList();
        String sql = "SELECT TOP 5 full_name, age, gender FROM Patients ORDER BY id DESC"; 
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String entry = "👤 " + rs.getString("full_name") + " (" + rs.getString("gender") + ", " + rs.getInt("age") + " yrs)";
                data.add(entry);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return data;
    }

    private PieChart getBillingChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        String sql = "SELECT status, COUNT(*) as count FROM Billing GROUP BY status";
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                pieData.add(new PieChart.Data(rs.getString("status"), rs.getInt("count")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        PieChart chart = new PieChart(pieData);
        chart.setPrefHeight(200);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false);
        return chart;
    }

    // =================================================================================
    // 🎨 MANAGEMENT PANES
    // =================================================================================

    private Pane createPatientsPane() {
        VBox box = new VBox(15);
        Label title = createTitle("👨‍⚕️ Patient Management");

        // 1. Inputs
        TextField tfName = new TextField(); tfName.setPromptText("Full Name"); tfName.setPrefWidth(200);
        TextField tfAge = new TextField(); tfAge.setPromptText("Age"); tfAge.setPrefWidth(80);
        ComboBox<String> cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Male", "Female");
        cbGender.setPromptText("Gender"); cbGender.setPrefWidth(120);

        // 2. Create ALL Buttons FIRST
        Button btnAdd = new Button("Add Patient");
        addHoverAnimation(btnAdd);
        btnAdd.setOnAction(e -> {
            try {
                if (tfName.getText().isEmpty()) return;
                addPatient(tfName.getText(), Integer.parseInt(tfAge.getText()), cbGender.getValue());
                loadPatients();
                tfName.clear(); tfAge.clear();
            } catch(Exception ex) { alert("Invalid Input"); }
        });

        Button btnEdit = new Button("Edit Selected");
        btnEdit.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(btnEdit);
        btnEdit.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Please select a patient to edit.");
                return;
            }
            showEditPatientDialog(selected);
        });

        Button btnDelete = new Button("Delete Selected");
        btnDelete.getStyleClass().add("btn-danger");
        addHoverAnimation(btnDelete);
        btnDelete.setOnAction(e -> {
             Patient p = patientTable.getSelectionModel().getSelectedItem();
             if(p != null) { deletePatient(p.getId()); loadPatients(); }
        });

        Button btnAssign = new Button("Assign Doctor");
        btnAssign.getStyleClass().add("btn-success");
        addHoverAnimation(btnAssign);
        btnAssign.setOnAction(e -> {
            Patient p = patientTable.getSelectionModel().getSelectedItem();
            if(p != null) showAssignDoctorDialog(p);
        });

        // 3. NOW add them to the HBox (Order matters!)
        HBox form = new HBox(10, tfName, tfAge, cbGender, btnAdd, btnEdit, btnDelete, btnAssign);
        form.setPadding(new Insets(10,0,10,0));

        setupPatientColumns();
        box.getChildren().addAll(title, form, patientTable);
        return box;
    }

    private Pane createDoctorsPane() {
        VBox box = new VBox(15);
        Label title = createTitle("🩺 Doctor Management");

        // 1. Inputs
        TextField tfName = new TextField(); tfName.setPromptText("Full Name");
        TextField tfSpec = new TextField(); tfSpec.setPromptText("Specialty");

        // 2. Create ALL Buttons FIRST
        Button btnAdd = new Button("Add Doctor");
        addHoverAnimation(btnAdd);
        btnAdd.setOnAction(e -> {
            if(!tfName.getText().isEmpty()) {
                addDoctor(tfName.getText(), tfSpec.getText());
                loadDoctors();
                tfName.clear(); tfSpec.clear();
            }
        });

        Button btnEdit = new Button("Edit Selected");
        btnEdit.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold;");
        addHoverAnimation(btnEdit);
        btnEdit.setOnAction(e -> {
            Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                alert("Please select a doctor to edit.");
                return;
            }
            showEditDoctorDialog(selected);
        });

        Button btnDelete = new Button("Delete Selected");
        btnDelete.getStyleClass().add("btn-danger");
        addHoverAnimation(btnDelete);
        btnDelete.setOnAction(e -> {
            Doctor d = doctorTable.getSelectionModel().getSelectedItem();
            if(d != null) { deleteDoctor(d.getId()); loadDoctors(); }
        });

        // 3. NOW add them to the HBox
        HBox form = new HBox(10, tfName, tfSpec, btnAdd, btnEdit, btnDelete);
        form.setPadding(new Insets(10,0,10,0));

        TableColumn<Doctor, Integer> colId = new TableColumn<>("ID"); colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Doctor, String> colName = new TableColumn<>("Full Name"); colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Doctor, String> colSpec = new TableColumn<>("Specialty"); colSpec.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        doctorTable.getColumns().setAll(colId, colName, colSpec);
        doctorTable.setPrefHeight(500);
        doctorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        box.getChildren().addAll(title, form, doctorTable);
        return box;
    }

    private Pane createBillingPane() {
        VBox box = new VBox(15);
        Label title = createTitle("💰 Billing Management");

        TableColumn<Billing, Integer> colId = new TableColumn<>("Bill ID"); colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Billing, String> colPatient = new TableColumn<>("Patient"); colPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        TableColumn<Billing, String> colAmount = new TableColumn<>("Amount"); colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        TableColumn<Billing, String> colStatus = new TableColumn<>("Status"); colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        billingTable.getColumns().setAll(colId, colPatient, colAmount, colStatus);
        billingTable.setPrefHeight(500);
        billingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnAdd = new Button("Create New Bill");
        addHoverAnimation(btnAdd);
        btnAdd.setOnAction(e -> showAddBillDialog());

        Button btnPay = new Button("Mark as Paid");
        btnPay.getStyleClass().add("btn-success");
        addHoverAnimation(btnPay);
        btnPay.setOnAction(e -> {
            Billing b = billingTable.getSelectionModel().getSelectedItem();
            if(b != null) {
                BillingDAO.markBillAsPaid(b.getPatientId());
                loadBilling();
            }
        });

        HBox buttons = new HBox(10, btnAdd, btnPay);
        box.getChildren().addAll(title, buttons, billingTable);
        return box;
    }

    // =================================================================================
    // ✨ UI HELPERS
    // =================================================================================

    private VBox createSidebarHeader() {
        VBox headerBox = new VBox(5); 
        headerBox.setPadding(new Insets(0, 0, 20, 10)); 

        Label appTitle = new Label("🏥 HMS");
        appTitle.setStyle("-fx-text-fill: -text; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label subTitle = new Label("Admin Portal");
        subTitle.setStyle("-fx-text-fill: -muted; -fx-font-size: 12px; -fx-font-weight: normal;");

        headerBox.getChildren().addAll(appTitle, subTitle);
        return headerBox;
    }

    private VBox createStatCard(String title, String value, String icon, String colorHex) {
        VBox card = new VBox(10);
        card.setStyle(
            "-fx-background-color: " + colorHex + ";" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);"
        );
        card.setPrefWidth(220);
        card.setPrefHeight(130);

        Label lblIcon = new Label(icon);
        lblIcon.setStyle("-fx-font-size: 30px; -fx-text-fill: rgba(255,255,255,0.8);");
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: rgba(255,255,255,0.7); -fx-font-weight: bold;");

        card.getChildren().addAll(lblIcon, lblValue, lblTitle);
        addHoverAnimation(card);
        return card;
    }

    private void addHoverAnimation(javafx.scene.Node node) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), node);
        scaleUp.setToX(1.03); scaleUp.setToY(1.03);
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), node);
        scaleDown.setToX(1.0); scaleDown.setToY(1.0);

        node.setOnMouseEntered(e -> scaleUp.playFromStart());
        node.setOnMouseExited(e -> scaleDown.playFromStart());
    }

    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setAlignment(Pos.CENTER_LEFT);
        b.getStyleClass().add("sidebar-btn");
        addHoverAnimation(b);
        return b;
    }

    private Label createTitle(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("title-label");
        return lbl;
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    private void applyTheme() {
        scene.getStylesheets().clear();
        String themeFile = isDarkMode ? "/styles/dark-theme.css" : "/styles/light-theme.css";
        var url = getClass().getResource(themeFile);
        if (url != null) scene.getStylesheets().add(url.toExternalForm());
        else System.err.println("Theme not found: " + themeFile);
    }

    // =================================================================================
    // 📊 SQL & DATA LOGIC
    // =================================================================================

    private String getDatabaseCount(String tableName) {
        String count = "0";
        try (Connection conn = DatabaseConnection.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            if (rs.next()) count = String.valueOf(rs.getInt(1));
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    private void setupPatientColumns() {
        TableColumn<Patient, Integer> colId = new TableColumn<>("ID"); colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Patient, String> colName = new TableColumn<>("Full Name"); colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<Patient, Integer> colAge = new TableColumn<>("Age"); colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Patient, String> colGender = new TableColumn<>("Gender"); colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Patient, String> colDoctor = new TableColumn<>("Doctor"); colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        TableColumn<Patient, String> colBill = new TableColumn<>("Bill"); colBill.setCellValueFactory(new PropertyValueFactory<>("bill"));

        patientTable.getColumns().setAll(colId, colName, colAge, colGender, colDoctor, colBill);
        patientTable.setPrefHeight(500);
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // =================================================================================
    // 📦 DIALOGS
    // =================================================================================

    private void showAddBillDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("New Bill");
        dialog.setHeaderText("Create Invoice");

        ComboBox<Patient> cbPatients = new ComboBox<>();
        cbPatients.setItems(loadPatientList());
        cbPatients.setPromptText("Select Patient");
        TextField tfAmount = new TextField();
        tfAmount.setPromptText("Amount");

        VBox vbox = new VBox(10, cbPatients, tfAmount);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && cbPatients.getValue() != null) {
                try {
                    BillingDAO.addBill(cbPatients.getValue().getId(), Double.parseDouble(tfAmount.getText()));
                    showInfo("Bill Created!");
                    loadBilling();
                } catch (Exception e) { alert("Invalid Amount"); }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void showEditPatientDialog(Patient p) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Patient");
        dialog.setHeaderText("Editing details for: " + p.getFullName());

        TextField tfName = new TextField(p.getFullName()); 
        TextField tfAge = new TextField(String.valueOf(p.getAge())); 
        
        ComboBox<String> cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Male", "Female");
        cbGender.setValue(p.getGender()); 

        VBox content = new VBox(10, new Label("Name:"), tfName, new Label("Age:"), tfAge, new Label("Gender:"), cbGender);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    updatePatient(p.getId(), tfName.getText(), Integer.parseInt(tfAge.getText()), cbGender.getValue());
                    loadPatients(); 
                } catch (Exception e) {
                    alert("Invalid input data.");
                }
            }
        });
    }

    private void showEditDoctorDialog(Doctor d) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Doctor");
        dialog.setHeaderText("Editing details for: " + d.getFullName());

        TextField tfName = new TextField(d.getFullName()); 
        TextField tfSpec = new TextField(d.getSpecialization()); 

        VBox content = new VBox(10, new Label("Name:"), tfName, new Label("Specialty:"), tfSpec);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (!tfName.getText().isEmpty() && !tfSpec.getText().isEmpty()) {
                    updateDoctor(d.getId(), tfName.getText(), tfSpec.getText());
                    loadDoctors(); 
                } else {
                    alert("Fields cannot be empty.");
                }
            }
        });
    }

    private void showAssignDoctorDialog(Patient patient) {
        Dialog<Doctor> dialog = new Dialog<>();
        dialog.setTitle("Assign Doctor");
        ComboBox<Doctor> doctorCombo = new ComboBox<>();
        doctorCombo.setItems(loadDoctorList());
        doctorCombo.setPromptText("Select Doctor");

        dialog.getDialogPane().setContent(doctorCombo);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK && doctorCombo.getValue() != null) {
                assignDoctorToPatient(patient.getId(), doctorCombo.getValue().getId(), doctorCombo.getValue().getFullName());
            }
            return null;
        });
        dialog.showAndWait();
    }

    // =================================================================================
    // 💾 CRUD OPERATIONS
    // =================================================================================

    private void assignDoctorToPatient(int pId, int dId, String dName) {
        try (Connection c = DatabaseConnection.connect(); PreparedStatement p = c.prepareStatement("UPDATE Patients SET doctor_id=?, doctor=? WHERE id=?")) {
            p.setInt(1, dId); p.setString(2, dName); p.setInt(3, pId); p.executeUpdate();
            showInfo("Doctor Assigned"); loadPatients();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addPatient(String name, int age, String gender) {
        try (Connection c = DatabaseConnection.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO Patients(full_name, age, gender) VALUES (?, ?, ?)")) {
            p.setString(1, name); p.setInt(2, age); p.setString(3, gender); p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addDoctor(String name, String spec) {
        try (Connection c = DatabaseConnection.connect(); PreparedStatement p = c.prepareStatement("INSERT INTO Doctors(full_name, specialty) VALUES (?, ?)")) {
            p.setString(1, name); p.setString(2, spec); p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- UPDATE LOGIC ---
    private void updatePatient(int id, String name, int age, String gender) {
        String sql = "UPDATE Patients SET full_name = ?, age = ?, gender = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setInt(4, id);
            ps.executeUpdate();
            showInfo("Patient updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Failed to update patient.");
        }
    }

    private void updateDoctor(int id, String name, String specialty) {
        String sql = "UPDATE Doctors SET full_name = ?, specialty = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, specialty);
            ps.setInt(3, id);
            ps.executeUpdate();
            showInfo("Doctor updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Failed to update doctor.");
        }
    }

    private void deletePatient(int id) {
        try (Connection c = DatabaseConnection.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM Patients WHERE id=?")) {
            p.setInt(1, id); p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void deleteDoctor(int id) {
        try (Connection c = DatabaseConnection.connect(); PreparedStatement p = c.prepareStatement("DELETE FROM Doctors WHERE id=?")) {
            p.setInt(1, id); p.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private ObservableList<Patient> loadPatientList() {
        ObservableList<Patient> list = FXCollections.observableArrayList();
        try (Connection c = DatabaseConnection.connect(); ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Patients")) {
            while (rs.next()) list.add(new Patient(rs.getInt("id"), rs.getString("full_name"), rs.getInt("age"), rs.getString("gender"), rs.getString("doctor"), rs.getString("bill"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private ObservableList<Doctor> loadDoctorList() {
        ObservableList<Doctor> list = FXCollections.observableArrayList();
        try (Connection c = DatabaseConnection.connect(); ResultSet rs = c.createStatement().executeQuery("SELECT * FROM Doctors")) {
            while (rs.next()) list.add(new Doctor(rs.getInt("id"), rs.getString("full_name"), rs.getString("specialty")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private void loadPatients() {
        patientTable.setItems(loadPatientList());
    }

    private void loadDoctors() {
        doctorTable.setItems(loadDoctorList());
    }

    private void loadBilling() {
        ObservableList<Billing> list = FXCollections.observableArrayList();
        try (Connection c = DatabaseConnection.connect(); ResultSet rs = c.createStatement().executeQuery("SELECT b.id, p.full_name, b.patient_id, b.amount, b.status FROM Billing b JOIN Patients p ON b.patient_id = p.id")) {
            while (rs.next()) list.add(new Billing(rs.getInt("id"), rs.getInt("patient_id"), rs.getString("full_name"), rs.getString("amount"), rs.getString("status")));
        } catch (SQLException e) { e.printStackTrace(); }
        billingTable.setItems(list);
    }

    private void alert(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
    private void showInfo(String msg) { new Alert(Alert.AlertType.INFORMATION, msg).showAndWait(); }

    public static void main(String[] args) { launch(args); }
}