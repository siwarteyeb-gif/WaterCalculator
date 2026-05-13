package com.watercalculator.controllers;

import com.watercalculator.db.ConsommationDAO;
import com.watercalculator.models.*;
import com.watercalculator.utils.SceneManager;
import com.watercalculator.utils.Session;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CalculatorController {

    @FXML private Button dashboardBtn;

    @FXML private TabPane tabPane;

    @FXML private Slider  doucheSlider;
    @FXML private Label   doucheMinLabel;
    @FXML private Text    doucheResultText;
    @FXML private Text    doucheOMSText;
    @FXML private Text    doucheRecoText;
    @FXML private Button  doucheSaveBtn;

    @FXML private Spinner<Integer> vaissellePersonnesSpinner;
    @FXML private CheckBox         vaisselleCheckBox;
    @FXML private Text             vaisselleResultText;
    @FXML private Text             vaisselleOMSText;
    @FXML private Text             vaisselleRecoText;
    @FXML private Button           vaisselleSaveBtn;

    @FXML private TextField arrosageSuperficieField;
    @FXML private Spinner<Integer> arrosageDureeSpinner;
    @FXML private Text      arrosageResultText;
    @FXML private Text      arrosageOMSText;
    @FXML private Text      arrosageRecoText;
    @FXML private Button    arrosageSaveBtn;

    @FXML private TextField       agricultureHectaresField;
    @FXML private ComboBox<String> agricultureCultureCombo;
    @FXML private Text            agricultureResultText;
    @FXML private Text            agricultureOMSText;
    @FXML private Text            agricultureRecoText;
    @FXML private Button          agricultureSaveBtn;

    @FXML private Label statusLabel;

    private final ConsommationDAO dao = new ConsommationDAO();

    @FXML
    public void initialize() {
        doucheSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            doucheMinLabel.setText(newVal.intValue() + " min");
        });

        vaissellePersonnesSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 4));

        arrosageDureeSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 30));

        agricultureCultureCombo.getItems().addAll("blé", "maïs", "tomate", "olive", "autre");
        agricultureCultureCombo.setValue("blé");

        statusLabel.setVisible(false);
    }

    @FXML
    private void handleCalculerDouche() {
        int duree = (int) doucheSlider.getValue();
        if (duree == 0) { showStatus("⚠️ Définissez une durée.", false); return; }
        ConsommationDouche c = new ConsommationDouche(Session.getCurrentUser().getId(), duree, LocalDate.now());
        doucheResultText.setText(String.format("💧 Consommation : %.1f L", c.calculerConsommation()));
        doucheOMSText.setText(c.comparerNormeOMS());
        doucheRecoText.setText(c.getRecommandation());
        doucheSaveBtn.setDisable(false);
    }

    @FXML
    private void handleSauvegarderDouche() {
        int duree = (int) doucheSlider.getValue();
        ConsommationDouche c = new ConsommationDouche(Session.getCurrentUser().getId(), duree, LocalDate.now());
        if (dao.sauvegarder(c)) showStatus("✅ Consommation douche sauvegardée !", true);
        else showStatus("❌ Erreur lors de la sauvegarde.", false);
    }

    @FXML
    private void handleCalculerVaisselle() {
        int personnes = vaissellePersonnesSpinner.getValue();
        boolean avecMachine = vaisselleCheckBox.isSelected();
        ConsommationVaisselle c = new ConsommationVaisselle(
            Session.getCurrentUser().getId(), personnes, avecMachine, LocalDate.now());
        vaisselleResultText.setText(String.format("💧 Consommation : %.1f L", c.calculerConsommation()));
        vaisselleOMSText.setText(c.comparerNormeOMS());
        vaisselleRecoText.setText(c.getRecommandation());
        vaisselleSaveBtn.setDisable(false);
    }

    @FXML
    private void handleSauvegarderVaisselle() {
        ConsommationVaisselle c = new ConsommationVaisselle(
            Session.getCurrentUser().getId(),
            vaissellePersonnesSpinner.getValue(),
            vaisselleCheckBox.isSelected(),
            LocalDate.now());
        if (dao.sauvegarder(c)) showStatus("✅ Consommation vaisselle sauvegardée !", true);
        else showStatus("❌ Erreur lors de la sauvegarde.", false);
    }

    @FXML
    private void handleCalculerArrosage() {
        String supStr = arrosageSuperficieField.getText().trim();
        if (supStr.isEmpty()) { showStatus("⚠️ Entrez la superficie.", false); return; }
        try {
            double superficie = Double.parseDouble(supStr);
            int duree = arrosageDureeSpinner.getValue();
            ConsommationArrosage c = new ConsommationArrosage(
                Session.getCurrentUser().getId(), superficie, duree, LocalDate.now());
            arrosageResultText.setText(String.format("💧 Consommation : %.1f L", c.calculerConsommation()));
            arrosageOMSText.setText(c.comparerNormeOMS());
            arrosageRecoText.setText(c.getRecommandation());
            arrosageSaveBtn.setDisable(false);
        } catch (NumberFormatException e) {
            showStatus("⚠️ Superficie invalide.", false);
        }
    }

    @FXML
    private void handleSauvegarderArrosage() {
        try {
            double superficie = Double.parseDouble(arrosageSuperficieField.getText().trim());
            ConsommationArrosage c = new ConsommationArrosage(
                Session.getCurrentUser().getId(), superficie,
                arrosageDureeSpinner.getValue(), LocalDate.now());
            if (dao.sauvegarder(c)) showStatus("✅ Consommation arrosage sauvegardée !", true);
            else showStatus("❌ Erreur lors de la sauvegarde.", false);
        } catch (NumberFormatException e) {
            showStatus("⚠️ Superficie invalide.", false);
        }
    }

    @FXML
    private void handleCalculerAgriculture() {
        String haStr = agricultureHectaresField.getText().trim();
        String culture = agricultureCultureCombo.getValue();
        if (haStr.isEmpty()) { showStatus("⚠️ Entrez les hectares.", false); return; }
        try {
            double hectares = Double.parseDouble(haStr);
            ConsommationAgriculture c = new ConsommationAgriculture(
                Session.getCurrentUser().getId(), hectares, culture, LocalDate.now());
            agricultureResultText.setText(String.format("💧 Consommation : %.1f L", c.calculerConsommation()));
            agricultureOMSText.setText(c.comparerNormeOMS());
            agricultureRecoText.setText(c.getRecommandation());
            agricultureSaveBtn.setDisable(false);
        } catch (NumberFormatException e) {
            showStatus("⚠️ Valeur invalide.", false);
        }
    }

    @FXML
    private void handleSauvegarderAgriculture() {
        try {
            double hectares = Double.parseDouble(agricultureHectaresField.getText().trim());
            ConsommationAgriculture c = new ConsommationAgriculture(
                Session.getCurrentUser().getId(), hectares,
                agricultureCultureCombo.getValue(), LocalDate.now());
            if (dao.sauvegarder(c)) showStatus("✅ Consommation agriculture sauvegardée !", true);
            else showStatus("❌ Erreur lors de la sauvegarde.", false);
        } catch (NumberFormatException e) {
            showStatus("⚠️ Valeur invalide.", false);
        }
    }

    @FXML
    private void handleDashboard() {
        Stage stage = (Stage) dashboardBtn.getScene().getWindow();
        SceneManager.switchScene(stage, "/resources/fxml/Dashboard.fxml", "Tableau de Bord");    }

    private void showStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setStyle(success
            ? "-fx-text-fill: #27ae60; -fx-font-weight: bold;"
            : "-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        statusLabel.setVisible(true);
    }
}
