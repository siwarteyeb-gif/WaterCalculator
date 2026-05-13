package com.watercalculator.controllers;

import com.watercalculator.db.UserDAO;
import com.watercalculator.models.User;
import com.watercalculator.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField     nomField;
    @FXML private TextField     prenomField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmField;
    @FXML private ComboBox<String> villeCombo;
    @FXML private Label         errorLabel;
    @FXML private Label         successLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        villeCombo.getItems().addAll(
            "Tunis", "Sfax", "Sousse", "Kairouan", "Bizerte",
            "Gabès", "Ariana", "Gafsa", "Monastir", "Médenine"
        );
        villeCombo.setValue("Tunis");
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        String nom      = nomField.getText().trim();
        String prenom   = prenomField.getText().trim();
        String email    = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm  = confirmField.getText().trim();
        String ville    = villeCombo.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Tous les champs sont obligatoires.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Les mots de passe ne correspondent pas.");
            return;
        }
        if (!email.contains("@")) {
            showError("Email invalide.");
            return;
        }
        if (userDAO.emailExiste(email)) {
            showError("Cet email est déjà utilisé.");
            return;
        }

        User user = new User(nom, prenom, email, password, ville);
        if (userDAO.inscrire(user)) {
            successLabel.setText("✅ Compte créé ! Vous pouvez vous connecter.");
            successLabel.setVisible(true);
            errorLabel.setVisible(false);
            clearFields();
        } else {
            showError("Erreur lors de l'inscription. Réessayez.");
        }
    }

    @FXML
    private void handleGoLogin() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        SceneManager.switchScene(stage, "/resources/fxml/Login.fxml", "Connexion");    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void clearFields() {
        nomField.clear(); prenomField.clear();
        emailField.clear(); passwordField.clear(); confirmField.clear();
    }
}
