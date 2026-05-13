package com.watercalculator.db;

import com.watercalculator.models.User;
import java.sql.*;

public class UserDAO {

    private Connection conn;

    public UserDAO() {
        this.conn = DatabaseConnection.getInstance();
    }

    public boolean inscrire(User user) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, ville) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getMotDePasse());
            ps.setString(5, user.getVille());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur inscription : " + e.getMessage());
            return false;
        }
    }

    public User seConnecter(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE email=? AND mot_de_passe=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setVille(rs.getString("ville"));
                return u;
            }
        } catch (SQLException e) {
            System.err.println("Erreur connexion : " + e.getMessage());
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT id FROM utilisateurs WHERE email=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }
}
