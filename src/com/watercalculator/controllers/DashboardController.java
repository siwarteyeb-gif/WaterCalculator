package com.watercalculator.controllers;

import com.watercalculator.db.ConsommationDAO;
import com.watercalculator.models.Consommation;
import com.watercalculator.utils.SceneManager;
import com.watercalculator.utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    @FXML private Text          welcomeText;
    @FXML private Text          totalJourText;
    @FXML private Text          statusText;
    @FXML private Text          villeText;
    @FXML private BarChart<String, Number>   barChart;
    @FXML private PieChart                   pieChart;
    @FXML private TableView<Consommation>    historyTable;
    @FXML private TableColumn<Consommation, String>  colType;
    @FXML private TableColumn<Consommation, Double>  colQuantite;
    @FXML private TableColumn<Consommation, String>  colDate;
    @FXML private TableColumn<Consommation, String>  colReco;
    @FXML private Button        calculerBtn;
    @FXML private Button        logoutBtn;
    @FXML private ProgressBar   progressBar;
    @FXML private Label         progressLabel;

    private final ConsommationDAO dao = new ConsommationDAO();
    private static final double NORME_JOURNALIERE = 150.0; 

    @FXML
    public void initialize() {
        String prenom = Session.getCurrentUser().getPrenom();
        String ville  = Session.getCurrentUser().getVille();
        welcomeText.setText("Bienvenue, " + prenom + " 👋");
        villeText.setText("📍 " + ville);

        rafraichir();
    }

    private void rafraichir() {
        int userId = Session.getCurrentUser().getId();
        double total = dao.getTotalJour(userId, LocalDate.now());

        totalJourText.setText(String.format("%.1f L", total));

        double progress = Math.min(total / NORME_JOURNALIERE, 1.0);
        progressBar.setProgress(progress);
        progressLabel.setText(String.format("%.0f%% de la norme journalière (%.0f L)", progress * 100, NORME_JOURNALIERE));

        if (total <= NORME_JOURNALIERE * 0.6) {
            statusText.setText("🌿 Excellent ! Consommation très faible.");
            statusText.setStyle("-fx-fill: #27ae60;");
        } else if (total <= NORME_JOURNALIERE) {
            statusText.setText("✅ Bonne consommation, dans les normes.");
            statusText.setStyle("-fx-fill: #f39c12;");
        } else {
            statusText.setText("⚠️ Consommation excessive ! Pensez à économiser.");
            statusText.setStyle("-fx-fill: #e74c3c;");
        }

        chargerBarChart(userId);
        chargerPieChart(userId);
        chargerHistorique(userId);
    }

    private void chargerBarChart(int userId) {
        List<double[]> semaine = dao.getConsommationSemaine(userId);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Litres/jour");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM");
        for (double[] row : semaine) {
            LocalDate d = new java.sql.Date((long) row[0]).toLocalDate();
            series.getData().add(new XYChart.Data<>(d.format(fmt), row[1]));
        }
        barChart.getData().clear();
        barChart.getData().add(series);
        barChart.setTitle("Consommation 7 derniers jours");
    }

    private void chargerPieChart(int userId) {
        double[] stats = dao.getStatsByType(userId);
        String[] labels = {"🚿 Douche", "🍽️ Vaisselle", "🌱 Arrosage", "🌾 Agriculture"};
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (int i = 0; i < labels.length; i++) {
            if (stats[i] > 0)
                pieData.add(new PieChart.Data(labels[i] + " " + String.format("%.0f L", stats[i]), stats[i]));
        }
        if (pieData.isEmpty())
            pieData.add(new PieChart.Data("Aucune donnée", 1));
        pieChart.setData(pieData);
        pieChart.setTitle("Répartition par usage");
    }

    private void chargerHistorique(int userId) {
        List<Consommation> historique = dao.getHistorique(userId);
        ObservableList<Consommation> obs = FXCollections.observableArrayList(historique);

        colType.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getType()));
        colQuantite.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().calculerConsommation()));
        colDate.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getDate().toString()));
        colReco.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleStringProperty(cd.getValue().getRecommandation()));

        historyTable.setItems(obs);
    }

    @FXML
    private void handleCalculer() {
        Stage stage = (Stage) calculerBtn.getScene().getWindow();
        SceneManager.switchScene(stage, "/resources/fxml/Calculator.fxml", "Calculateur");    }

    @FXML
    private void handleLogout() {
        Session.clear();
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        SceneManager.switchScene(stage, "/resources/fxml/Login.fxml", "Connexion");    }

    @FXML
    private void handleRefresh() {
        rafraichir();
    }
}
