package com.watercalculator.models;

import java.time.LocalDate;

public class ConsommationArrosage extends Consommation {

    private double superficieM2;
    private int dureeMinutes;
    private static final double LITRES_PAR_M2 = 10.0;
    private static final double NORME_OMS     = 100.0;

    public ConsommationArrosage(int userId, double superficieM2, int dureeMinutes, LocalDate date) {
        super(userId, 0, date, "Arrosage");
        this.superficieM2  = superficieM2;
        this.dureeMinutes  = dureeMinutes;
        this.quantiteLitres = calculerConsommation();
    }

    @Override
    public double calculerConsommation() {
        return superficieM2 * LITRES_PAR_M2;
    }

    @Override
    public String getRecommandation() {
        return "Arrosez tôt le matin (6h-8h) pour réduire l'évaporation de 30%. "
             + "Récupérez l'eau de pluie — économisez jusqu'à "
             + String.format("%.0f", calculerConsommation() * 0.3) + " L par arrosage !";
    }

    @Override
    protected double getNormeOMS() { return NORME_OMS; }

    public double getSuperficieM2()      { return superficieM2; }
    public int getDureeMinutes()         { return dureeMinutes; }
}
