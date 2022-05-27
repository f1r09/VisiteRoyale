package Vue;

import Controleur.ControleurMediateur;
import Modele.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
    JeuVue jeuVue;
    ControleurMediateur ctrl;

    AdaptateurSouris(JeuVue jeuVue, ControleurMediateur ctrl) {
        this.jeuVue = jeuVue;
        this.ctrl = ctrl;
    }

    public int positionPion(Type type){
        switch (type.toString()) {
            case "R":
                return this.jeuVue.jeu.getPlateau().getPositionPion(Pion.ROI);
            case "S":
                return this.jeuVue.jeu.getPlateau().getPositionPion(Pion.SOR);
            case "F":
                return this.jeuVue.jeu.getPlateau().getPositionPion(Pion.FOU);
            default:
                throw new RuntimeException("Vue.AdaptateurSouris.positionPion() : Pion invalide");
        }
    }

    public int positionGarde(String name){
        switch (name){
            case "GV":
                return this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_VRT);
            case "GR":
                return this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_RGE);
            default:
                throw new RuntimeException("Vue.AdaptaeurSouris.positionGarde() : Garde invalide.");
        }

    }

    public int choixGarde(int x) {
        if ((x >= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_VRT) * this.jeuVue.terrain.getWidth() / 17) && (x <= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_VRT) * this.jeuVue.terrain.getWidth() / 17 + this.jeuVue.terrain.getWidth() / 17)) {
            return 0;
        }
        if ((x >= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_RGE) * this.jeuVue.terrain.getWidth() / 17) && (x <= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.GAR_RGE) * this.jeuVue.terrain.getWidth() / 17 + this.jeuVue.terrain.getWidth() / 17)){
            return 1;
        }
        return 2;


    }

    public int choixPion(int x){
        if((x >= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.ROI) * this.jeuVue.terrain.getWidth()/17) && (x <= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.ROI) * this.jeuVue.terrain.getWidth()/17 +this.jeuVue.terrain.getWidth()/17)){
            return 0;
        }
        if((x >= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.FOU) * this.jeuVue.terrain.getWidth()/17) && (x <= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.FOU) * this.jeuVue.terrain.getWidth()/17 +this.jeuVue.terrain.getWidth()/17)){
            return 1;
        }
        if((x >= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.SOR) * this.jeuVue.terrain.getWidth()/17) && (x <= this.jeuVue.terrain.getX() + this.jeuVue.jeu.getPlateau().getPositionPion(Pion.SOR) * this.jeuVue.terrain.getWidth()/17 +this.jeuVue.terrain.getWidth()/17)){
            return 2;
        }
        return 3;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int pos, posB;
        Carte c;
        if((e.getX() >= this.jeuVue.terrain.getX()) && (e.getX() <= this.jeuVue.terrain.getWidth() + this.jeuVue.terrain.getX()) && (e.getY()>= this.jeuVue.terrain.getY()) && (e.getY()<= this.jeuVue.terrain.getHeight()+this.jeuVue.terrain.getY())){
            if(this.jeuVue.jeu.getActivationPouvoirSor()){
                if((choixPion(e.getX()) == 0) && this.jeuVue.jeu.peutSelectionnerPion(Pion.ROI)){
                    this.ctrl.selectionnerPion(Pion.ROI);
                    this.ctrl.selectionnerDirection(Plateau.DIRECTION_IND);
                }
                if((choixGarde(e.getX()) == 0) && this.jeuVue.jeu.peutSelectionnerPion(Pion.GAR_VRT)){
                    this.ctrl.selectionnerPion(Pion.GAR_VRT);
                    this.ctrl.selectionnerDirection(Plateau.DIRECTION_IND);
                }
                if((choixGarde(e.getX()) == 1)  && this.jeuVue.jeu.peutSelectionnerPion(Pion.GAR_RGE)){
                    this.ctrl.selectionnerPion(Pion.GAR_RGE);
                    this.ctrl.selectionnerDirection(Plateau.DIRECTION_IND);
                }

            }


            if((this.jeuVue.getTypeJoueur(this.jeuVue.jeu.getJoueurCourant())).toString()=="G" && this.jeuVue.jeu.getSelectionPions(0)==null){

                if (this.choixGarde(e.getX())==0) {
                    this.ctrl.selectionnerPion(Pion.GAR_VRT);
                    System.out.println(this.jeuVue.jeu.getSelectionPions(0));
                }
                if (this.choixGarde(e.getX())==1) {
                    this.ctrl.selectionnerPion(Pion.GAR_RGE);
                    System.out.println(this.jeuVue.jeu.getSelectionPions(0));
                }
                if (this.choixGarde(e.getX())==2) {
                    System.out.println("Aucun");
                }
            }



            if((this.jeuVue.getTypeJoueur(this.jeuVue.jeu.getJoueurCourant())).toString()=="G" && this.jeuVue.jeu.getSelectionPions(0)!=null){
                c = this.jeuVue.getCarteJouee(this.jeuVue.jeu.getJoueurCourant());


                pos = positionGarde(this.jeuVue.jeu.getSelectionPions(0).toString());



                if(c.estDeplacementGar1Plus1() && this.jeuVue.jeu.getSelectionPions(1)!=null){
                    posB = positionGarde(this.jeuVue.jeu.getSelectionPions(1).toString());
                    if(this.jeuVue.jeu.getSelectionDirections(0) == Plateau.DIRECTION_IND){
                        if(e.getX()>pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth()/17){
                            this.jeuVue.jeu.putSelectionDirections(0, Plateau.DIRECTION_RGE);
                        }
                        if(e.getX()<pos * (this.jeuVue.terrain.getWidth()/17)+this.jeuVue.terrain.getX()){
                            this.jeuVue.jeu.putSelectionDirections(0, Plateau.DIRECTION_VRT);
                            System.out.println(this.jeuVue.jeu.getSelectionDirections(0));
                        }
                    } else if ((this.jeuVue.jeu.getSelectionDirections(1) == Plateau.DIRECTION_IND) && (this.jeuVue.jeu.getSelectionDirections(0) != Plateau.DIRECTION_IND)){
                        if(e.getX()>posB * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth()/17){
                            ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE));
                        }
                        if(e.getX()<pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX()){
                            ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT));
                        }
                    }
                } else if(c.estDeplacementGar1Plus1() && this.jeuVue.jeu.getSelectionPions(1) == null){
                    if((this.jeuVue.jeu.getSelectionPions(0).toString()==Pion.GAR_VRT.toString())){
                        if(this.choixGarde(e.getX())==1){
                            this.ctrl.selectionnerPion(Pion.GAR_RGE);
                        } else {
                            if((this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE)))&&(e.getX()>pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth()/17)){
                                this.ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE));
                            }
                            if((this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT)))&&(e.getX()<pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() )){
                                this.ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT));
                            }
                        }

                    }
                    if((this.jeuVue.jeu.getSelectionPions(0).toString()==Pion.GAR_RGE.toString())){
                        if (this.choixGarde(e.getX())==0) {
                            this.ctrl.selectionnerPion(Pion.GAR_VRT);
                        } else {
                            if((this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE)))&&(e.getX()>pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth()/17)){
                                this.ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE));
                            }
                            if((this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT)))&&(e.getX()<pos * (this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() )){
                                this.ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT));
                            }
                        }
                    }

                } else {

                    if (this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE)) && !this.jeuVue.cartesJoueesEstVide(this.jeuVue.jeu.getJoueurCourant())) {
                        if (e.getX() > pos * (this.jeuVue.terrain.getWidth() / 17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth() / 17) {
                            ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE));
                        }
                    }

                    if (this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT)) && !this.jeuVue.cartesJoueesEstVide(this.jeuVue.jeu.getJoueurCourant())) {

                        if (e.getX() < pos * (this.jeuVue.terrain.getWidth() / 17) + this.jeuVue.terrain.getX()) {
                            ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT));
                        }
                    }
                }


            }


            if((this.jeuVue.getTypeJoueur(this.jeuVue.jeu.getJoueurCourant())).toString()!="G") {
                pos = positionPion(this.jeuVue.getTypeJoueur(this.jeuVue.jeu.getJoueurCourant()));

                if (this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE)) && !this.jeuVue.cartesJoueesEstVide(this.jeuVue.jeu.getJoueurCourant())) {
                    if (e.getX() > pos*(this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX() + this.jeuVue.terrain.getWidth()/17) {
                        ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_RGE));
                    }
                }
                if (this.jeuVue.jeu.peutSelectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT)) && !this.jeuVue.cartesJoueesEstVide(this.jeuVue.jeu.getJoueurCourant())) {

                    if (e.getX() < pos*(this.jeuVue.terrain.getWidth()/17) + this.jeuVue.terrain.getX()) {
                        ctrl.selectionnerDirection(Jeu.getDirectionJoueur(Jeu.JOUEUR_VRT));
                    }
                }
            }
        }
        ctrl.clicSouris(e.getX(), e.getY());
    }
}
