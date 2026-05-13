package com.watercalculator.models;

import java.time.LocalDate;


public abstract class Consommation {

    protected int id;
    protected int userId;
    protected double quantiteLitres;
    protected LocalDate date;
    protected String type;

    public Consommation(int userId, double quantiteLitres, LocalDate date, String type) {
        this.userId         = userId;
        this.quantiteLitres = quantiteLitres;
        this.date           = date;
        this.type           = type;
    }

    public abstract double calculerConsommation();

   
    public abstract String getRecommandation();

    public String comparerNormeOMS() {
        double norme = getNormeOMS();
        double consommation = calculerConsommation();
        double diff = consommation - norme;
        if (diff <= 0) {
            return String.format(" Vous consommez %.1f L. Vous êtes dans la norme OMS (%.1f L).", consommation, norme);
        } else {
            return String.format(" Vous dépassez la norme OMS de %.1f L (norme: %.1f L, vous: %.1f L).", diff, norme, consommation);
        }
    }

    protected abstract double getNormeOMS();

    public int getId()  
    { return id; }
    public void setId(int id)         
    { this.id = id; }

    public int getUserId()           
    { return userId; }
    public void setUserId(int userId)    
    { this.userId = userId; }

    public double getQuantiteLitres()     
    { return quantiteLitres; }
    public void setQuantiteLitres(double q) 
    { this.quantiteLitres = q; }

    public LocalDate getDate()          
    { return date; }
    public void setDate(LocalDate date)   
    { this.date = date; }

    public String getType()             
    { return type; }
    public void setType(String type) 
    { this.type = type; }
}
