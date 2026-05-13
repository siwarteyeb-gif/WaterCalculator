package com.watercalculator.models;

import java.time.LocalDate;

public class ConsommationDouche extends Consommation {

    private int dureeMinutes;
    private static final double DEBIT_LITRES_MIN = 10.0;
    private static final double NORME_OMS = 50.0;

    public ConsommationDouche(int userId, int dureeMinutes, LocalDate date) {
        super(userId, 0, date, "Douche");
        this.dureeMinutes = dureeMinutes;
        this.quantiteLitres = calculerConsommation();
    }

    @Override
    public double calculerConsommation() {
        return dureeMinutes * DEBIT_LITRES_MIN;
    }

    @Override
    public String getRecommandation() {
        if (dureeMinutes > 5) {
            return " Réduisez votre douche à 5 min max — économisez "
                    + String.format("%.0f", (dureeMinutes - 5) * DEBIT_LITRES_MIN)
                    + " L par douche !";
        }
        return " Excellent ! Votre durée de douche est optimale.";
    }

    @Override
    protected double getNormeOMS() { return NORME_OMS; }

    public int getDureeMinutes()          { return dureeMinutes; }
    public void setDureeMinutes(int d)    { this.dureeMinutes = d; }
}
