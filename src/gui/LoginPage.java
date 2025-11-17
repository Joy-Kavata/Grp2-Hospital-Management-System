package gui;

import backend.LoginDAO;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hospital Login");

        // 1. Create the "Card" Container
        VBox loginCard = new VBox();
        loginCard.getStyleClass().add("login-card"); // Uses CSS .login-card
        loginCard.setMaxWidth(350); // Limit width so it looks like a box

        // 2. UI Elements
        Label title = new Label("Hospital Login");
        title.getStyleClass().add("heading-label"); // Uses CSS .heading-label

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE); // Make button fill the card width
        // Note: .button class is applied automatically by JavaFX

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label"); // Uses CSS .error-label

        // 3. Button Logic
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all fields.");
                return;
            }

            boolean success = LoginDAO.login(username, password);

            if (success) {
                errorLabel.setText("");
                try {
                    new HospitalDashboard().start(new Stage());
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorLabel.setText("Error opening dashboard.");
                }
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });

        // 4. Layout Assembly
        loginCard.getChildren().addAll(title, usernameField, passwordField, loginBtn, errorLabel);

        // Use a StackPane as the root to center the card perfectly
        StackPane root = new StackPane(loginCard);
        
        Scene scene = new Scene(root, 600, 500); // Bigger window to show off the centering

        // 5. Load the Theme (Defaulting to Dark Theme for the Login)
        try {
            String css = getClass().getResource("/styles/dark-theme.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (NullPointerException ex) {
            System.err.println("Error: Could not find CSS file. Make sure it is in the 'out' folder.");
        }

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}