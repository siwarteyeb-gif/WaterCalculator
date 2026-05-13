package com.watercalculator.controllers;

import com.watercalculator.db.UserDAO;
import com.watercalculator.models.User;
import com.watercalculator.utils.SceneManager;
import com.watercalculator.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField      emailField;
    @FXML private PasswordField  passwordField;
    @FXML private Label          errorLabel;
    @FXML private Button         loginBtn;
    @FXML private Button         registerBtn;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    private void handleLogin() {
        String email= emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        User user = userDAO.seConnecter(email, password);
        if (user != null) {
            Session.setCurrentUser(user);
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            SceneManager.switchScene(stage, "/resources/fxml/Dashboard.fxml", "Dashboard");
            } else {
            showError("Email ou mot de passe incorrect.");
        }
    }

    @FXML
    private void handleGoRegister() {
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        SceneManager.switchScene(stage, "/resources/fxml/Register.fxml", "Inscription");  }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
