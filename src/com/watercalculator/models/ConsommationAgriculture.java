package com.watercalculator.models;

import java.time.LocalDate;

public class ConsommationAgriculture extends Consommation {

    private double superficieHectares;
    private String typeCulture;
    private static final double NORME_OMS = 5000.0;

    public ConsommationAgriculture(int userId, double superficieHectares, String typeCulture, LocalDate date) {
        super(userId, 0, date, "Agriculture");
        this.superficieHectares = superficieHectares;
        this.typeCulture        = typeCulture;
        this.quantiteLitres     = calculerConsommation();
    }

    @Override
    public double calculerConsommation() {
        double litresParHectare;
        switch (typeCulture.toLowerCase()) {
            case "blé":      litresParHectare = 4500; break;
            case "maïs":     litresParHectare = 7000; break;
            case "tomate":   litresParHectare = 6000; break;
            case "olive":    litresParHectare = 3000; break;
            default:         litresParHectare = 5000; break;
        }
        return superficieHectares * litresParHectare;
    }

    @Override
    public String getRecommandation() {
        return " Adoptez l'irrigation goutte-à-goutte — économisez 40% d'eau. "
             + "Pour " + typeCulture + " sur " + superficieHectares + " ha, "
             + "cela représente "
             + String.format("%.0f", calculerConsommation() * 0.4) + " L économisés/jour.";
    }

    @Override
    protected double getNormeOMS() { return NORME_OMS; }

    public double getSuperficieHectares()        { return superficieHectares; }
    public String getTypeCulture()               { return typeCulture; }
}
