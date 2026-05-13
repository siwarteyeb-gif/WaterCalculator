package com.watercalculator.db;

import com.watercalculator.models.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsommationDAO {

    private Connection conn;

    public ConsommationDAO() {
        this.conn = DatabaseConnection.getInstance();
    }

    public boolean sauvegarder(Consommation c) {
        String sql = "INSERT INTO consommations (user_id, type, quantite_litres, date_consommation) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getType());
            ps.setDouble(3, c.calculerConsommation());
            ps.setDate(4, Date.valueOf(c.getDate()));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
            return false;
        }
    }

    public List<Consommation> getHistorique(int userId) {
        List<Consommation> liste = new ArrayList<>();
        String sql = "SELECT * FROM consommations WHERE user_id=? ORDER BY date_consommation DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                double qte  = rs.getDouble("quantite_litres");
                LocalDate d = rs.getDate("date_consommation").toLocalDate();
                Consommation c = creerDepuisType(userId, type, qte, d);
                if (c != null) {
                    c.setId(rs.getInt("id"));
                    liste.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur historique : " + e.getMessage());
        }
        return liste;
    }

    public double getTotalJour(int userId, LocalDate date) {
        String sql = "SELECT SUM(quantite_litres) FROM consommations WHERE user_id=? AND date_consommation=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Erreur total jour : " + e.getMessage());
        }
        return 0;
    }

    public List<double[]> getConsommationSemaine(int userId) {
        List<double[]> data = new ArrayList<>();
        String sql = "SELECT date_consommation, SUM(quantite_litres) as total "
                   + "FROM consommations WHERE user_id=? "
                   + "GROUP BY date_consommation ORDER BY date_consommation DESC LIMIT 7";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new double[]{
                    rs.getDate("date_consommation").getTime(),
                    rs.getDouble("total")
                });
            }
        } catch (SQLException e) {
            System.err.println("Erreur semaine : " + e.getMessage());
        }
        return data;
    }

    public double[] getStatsByType(int userId) {
        double[] stats = new double[4];
        String[] types = {"Douche", "Vaisselle", "Arrosage", "Agriculture"};
        String sql = "SELECT SUM(quantite_litres) FROM consommations WHERE user_id=? AND type=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < types.length; i++) {
                ps.setInt(1, userId);
                ps.setString(2, types[i]);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) stats[i] = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur stats : " + e.getMessage());
        }
        return stats;
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM consommations WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private Consommation creerDepuisType(int userId, String type, double qte, LocalDate d) {
        switch (type) {
            case "Douche":
                return new ConsommationDouche(userId, (int)(qte / 10.0), d);
            case "Vaisselle":
                return new ConsommationVaisselle(userId, 1, false, d) {
                    { this.quantiteLitres = qte; }
                    @Override public double calculerConsommation() { return qte; }
                };
            case "Arrosage":
                return new ConsommationArrosage(userId, qte / 10.0, 0, d);
            case "Agriculture":
                return new ConsommationAgriculture(userId, qte / 5000.0, "autre", d);
            default:
                return null;
        }
    }
}
