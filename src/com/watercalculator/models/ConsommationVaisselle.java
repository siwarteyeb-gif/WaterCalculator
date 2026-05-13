package com.watercalculator.models;

import java.time.LocalDate;

public class ConsommationVaisselle extends Consommation {

    private int nombrePersonnes;
    private boolean avecLavVaisselle;
    private static final double NORME_OMS = 15.0;

    public ConsommationVaisselle(int userId, int nombrePersonnes, boolean avecLavVaisselle, LocalDate date) {
        super(userId, 0, date, "Vaisselle");
        this.nombrePersonnes   = nombrePersonnes;
        this.avecLavVaisselle  = avecLavVaisselle;
        this.quantiteLitres    = calculerConsommation();
    }

    @Override
    public double calculerConsommation() {
        if (avecLavVaisselle) {
            return 12.0;
        } else {
            return nombrePersonnes * 15.0;
        }
    }

    @Override
    public String getRecommandation() {
        if (!avecLavVaisselle && nombrePersonnes >= 3) {
            return "Utilisez un lave-vaisselle plein — économisez jusqu'à "
                    + String.format("%.0f", calculerConsommation() - 12.0) + " L !";
        }
        return " Bonne pratique ! Pensez à utiliser l'eau de rinçage pour arroser.";
    }

    @Override
    protected double getNormeOMS() { return NORME_OMS; }

    public int getNombrePersonnes()               { return nombrePersonnes; }
    public boolean isAvecLavVaisselle()           { return avecLavVaisselle; }
}
