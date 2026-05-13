package com.watercalculator.models;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private String ville;

    public User() {}

    public User(String nom, String prenom, String email, String motDePasse, String ville) {
        this.nom       = nom;
        this.prenom    = prenom;
        this.email     = email;
        this.motDePasse = motDePasse;
        this.ville     = ville;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }

    public String getNom()               { return nom; }
    public void setNom(String nom)       { this.nom = nom; }

    public String getPrenom()            { return prenom; }
    public void setPrenom(String p)      { this.prenom = p; }

    public String getEmail()             { return email; }
    public void setEmail(String e)       { this.email = e; }

    public String getMotDePasse()        { return motDePasse; }
    public void setMotDePasse(String m)  { this.motDePasse = m; }

    public String getVille()             { return ville; }
    public void setVille(String v)       { this.ville = v; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + email + ")";
    }
}
