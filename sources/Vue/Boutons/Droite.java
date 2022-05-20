package Vue.Boutons;

import Controleur.ControleurMediateur;
import Modele.Jeu;
import Modele.Pion;
import Vue.JeuVue;

public class Droite extends Bouton {
    JeuVue frame;

    public Droite(ControleurMediateur ctrl, JeuVue frame, Jeu jeu) {
        super(ctrl, jeu);
        this.frame = frame;
    }

    @Override
    void action() {
        if (jeu.peutSelectionnerDirection(Jeu.JOUEUR_RGE)) {
            ctrl.selectionnerDirection(Jeu.JOUEUR_RGE);
            this.frame.defausserJeu(jeu.getJoueurCourant());
            System.out.println("Position " + jeu.getPlateau().getPositionPion(Pion.SOR));
        }
    }
}
