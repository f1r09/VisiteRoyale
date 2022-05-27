package Vue;

import Controleur.ControleurMediateur;
import Modele.*;
import Vue.Boutons.*;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class JeuVue extends JComponent {
    public final JFrame frame;
    static final int LARGEURFENETRE = 1600;
    static final int HAUTEURFENETRE = 900;
    private static int OFFSET ;

    Jeu jeu;
    ControleurMediateur ctrl;

    List<CarteVue> deck;
    List<CarteVue> mainA;
    List<CarteVue> mainB;
    List<CarteVue> joueesA;
    List<CarteVue> joueesB;
    List<CarteVue> defausse;
    public PlateauVue terrain;
    List<Bouton> boutons;

    int heigth, width, carteH, carteW, joueeH, joueeW, xDep, yA, yB,yAdec, yBdec ,delai;
    Point jeuPosA, jeuPosB;
    boolean FinAnim;
    Dimension screenSize;
    MenuPanel menuPanel;
    TourPanel tourA;
    TourPanel tourB;
    BoutonPouvoirFou pouvoirFou ;
    BoutonPouvoirSorcier pouvoirSorcier ;
    BoutonFinirTour finTour ;
    BoutonAnnuler annuler ;
    BoutonRefaire refaire ;
    JLabel DeckNb;
    private String deckTaille;

    boolean test;


    public JeuVue(ControleurMediateur ctrl, Jeu jeu) {
        this.test = false;
        this.delai = 25;

        this.deck = new ArrayList<>();
        this.mainA = new ArrayList<>();
        this.mainB = new ArrayList<>();
        this.joueesB = new ArrayList<>();
        this.joueesA = new ArrayList<>();
        this.defausse = new ArrayList<>();
        this.boutons = new ArrayList<>();

        this.frame = new JFrame();

        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //this.frame.setMinimumSize(new Dimension(LARGEURFENETRE, HAUTEURFENETRE));
        //this.frame.setSize(this.screenSize);
        this.frame.setSize(1200,800);
        this.frame.setVisible(true);

        this.jeu = jeu;
        this.ctrl = ctrl;

        this.terrain = new PlateauVue(ctrl, this.jeu);

        this.heigth = this.frame.getHeight();
        this.width = this.frame.getWidth();
        this.carteH = this.frame.getWidth() / 10;
        this.carteW = this.frame.getHeight() / 10;
        this.joueeH = (this.carteH/4 )+ (this.carteH / 2);
        this.joueeW = (this.carteW/4 )+ (this.carteW / 2);
        this.OFFSET = this.width /80;
        this.xDep = (this.width / 2) - (this.carteW * 4);
        this.yA = this.heigth - this.carteH - 40;
        this.yAdec = this.yA - (this.carteH/8);
        this.yB = 5;
        this.yBdec = this.yB + (this.carteH/12);

        this.jeuPosA = new Point( (this.width / 2) - (this.joueeW *3), this.terrain.getY() + this.terrain.getHeight() + (this.carteH/4) );
        this.jeuPosB =new Point( (this.width / 2) - (this.joueeW*3), this.terrain.getY() - (this.carteH/4) - (this.joueeH) );

        this.setVisible(true);



        this.menuPanel = new MenuPanel(this, this.ctrl);
        this.frame.add(this.menuPanel);


        this.genererDeck();
        this.afficheTerrain();
        this.genererBoutons();
        this.genererTours();

        this.deckTaille = Integer.toString(  this.deck.size() );
        this.DeckNb = new JLabel("PIOCHE : " + this.deckTaille);

        this.DeckNb.setSize(this.carteW, this.carteH/10);
        this.DeckNb.setBackground(Color.orange);
        this.DeckNb.setOpaque(true);
        this.DeckNb.setLocation(this.OFFSET , (this.heigth / 2) - (this.carteH/2)- 40 - (this.DeckNb.getHeight() + 1) );
        this.DeckNb.setVisible(true);
        this.frame.add(this.DeckNb);


        this.GenererMains();
        this.FinAnim = false;
        this.repaint();

        this.ctrl.setJeuVue(this);
    }

    public JFrame getFrame() {
        return frame;
    }
    public int JA(int j){
        return this.jeu.getSelectionCartes(j).getTaille();
    }

    /* PaintComponent */
    @Override
    public void paintComponent(Graphics g) {
        this.afficherTours();
        this.afficheMain();
        this.afficheTerrain();
        this.afficherBoutons();
        super.paintComponent(g);
        g.setColor(Color.darkGray);
        int w = this.carteW + (this.OFFSET/2);
        int h = this.carteH + (this.OFFSET*2);
        g.fillRect(this.OFFSET - (this.OFFSET/4),(this.heigth/2) - (h/2) - 40,w,h);
        g.fillRect(( (2 * this.OFFSET ) - (this.OFFSET/4) ) + this.carteW,((this.heigth / 2) - (h / 2)) - 40,w,h);
        g.setColor(new Color(225,15, 50));
        g.drawRect((this.width/2) - (this.joueeW*3), this.terrain.getY() + this.terrain.getHeight() + (this.joueeH/8), this.joueeW*6, this.joueeH + (this.joueeH/2)   );

        g.setColor(new Color(71, 132, 78));
        g.drawRect((this.width/2) - (this.joueeW*3), this.terrain.getY() - (this.carteH/4) - (this.joueeH + this.joueeH/4) , this.joueeW*6, this.joueeH + (this.joueeH/2)   );

    }

    /* GENERATIONS & GESTION DES LISTES */

    public List<CarteVue> getMainJoueur(int joueur){
        if (joueur == jeu.JOUEUR_RGE){
            return this.mainA;
        }else{
            return  this.mainB;
        }
    }

    public Type getTypeJoueur(int joueur){
        if (joueur == jeu.JOUEUR_RGE){
            return this.joueesA.get(0).getCarte().getType();
        }else{
            return  this.joueesB.get(0).getCarte().getType();
        }
    }

    public Carte getCarteJouee(int joueur){
        if(joueur == jeu.JOUEUR_RGE){
            return this.joueesA.get(0).getCarte();
        } else {
            return this.joueesB.get(0).getCarte();
        }
    }

    public CarteVue carteFromCartevue(Carte carte, List<CarteVue> list){
        int t =list.size();
        for (int i=0; i<t; i++) {
            if ( carte.equals(list.get(i).getCarte()) ) {
                return list.get(i);
            }
        }

        return null;
    }

    public void genererDeck() {
        int taille = this.jeu.getPioche().getTaille();
        int y = ((this.heigth / 2) - (this.carteH / 2)) - 40;

        for (int i = 0; i < taille; i++) {
            CarteVue carteVue = new CarteVue(jeu, ctrl, this);
            carteVue.setCarte(this.jeu.getPioche().getCarte(i));
            carteVue.setSize(this.carteW, this.carteH);
            carteVue.setVisible(true);
            carteVue.setLocation(OFFSET, y);

            this.deck.add(carteVue);
            this.frame.add(carteVue);

        }

        this.frame.repaint();
    }

    public void donnerCarte(int i, List<CarteVue> Target, List<CarteVue> Source) {
        Target.add(Source.get(i));
        Source.remove(i);
    }

    public void piocher(int joueur, int nbCartes) {
        int max = nbCartes;

        Point dest;


        if (joueur == this.jeu.JOUEUR_RGE) {
            dest = new Point(this.xDep, this.yA);

            if (this.test  && this.deck.size() == 38 && this.defausse.size() > 0){
                this.test = false;
                int t =  this.defausse.size();
                for (int i=0; i<t; i++){
                    envoiPioche(this.defausse.get(this.defausse.size()-1), dest);
                    donnerCarte(this.defausse.size()-1, this.mainA, this.defausse);
                }
            }

            if ( (8-this.mainA.size() ) < max && (max>0) ){max = 8 - this.mainA.size();}

            for (int i=0; i< max; i++){
                envoiPioche(this.deck.get(this.deck.size()-1), dest);
                donnerCarte(this.deck.size()-1, this.mainA, this.deck);

            }
        }else {
            dest = new Point(this.xDep, this.yB);

            if (this.test && this.deck.size() == 38 && this.defausse.size() > 0){
                this.test = false;
                int t =  this.defausse.size();
                for (int i=0; i<t; i++){
                    envoiPioche(this.defausse.get(this.defausse.size()-1), dest);
                    donnerCarte(this.defausse.size()-1, this.mainB, this.defausse);
                }
            }

            if ( (8-this.mainA.size() ) < max && (max>0) ){max = 8 - this.mainB.size();}

            for (int i=0; i< max; i++){
                envoiPioche(this.deck.get(this.deck.size()-1), dest);
                donnerCarte(this.deck.size()-1, this.mainB, this.deck);
            }
        }
        this.deckTaille = Integer.toString( this.deck.size() );
        this.DeckNb.setText("PIOCHE : " +  this.deckTaille);

    }

    public void jouer(int i, boolean joueur) {
        if (joueur) {
            donnerCarte(i, this.joueesA, this.mainA);
        }else {
            donnerCarte(i, this.joueesB, this.mainB);
        }
    }

    public void jouerCarte (CarteVue c){
        if (jeu.getJoueurCourant() == this.jeu.JOUEUR_RGE){
            jouer(this.mainA.indexOf(c), true);
        }else{
            jouer(this.mainB.indexOf(c), false);
        }
    }

    public void defausserMain(int i, boolean joueur) {
        if (joueur)
            donnerCarte(i, this.defausse, this.mainA);
        else
            donnerCarte(i, this.defausse, this.mainB);
        PlacerDefausse(this.defausse.get(this.defausse.size() - 1));
    }

    public void defausserJeu(int joueur) {
        int taille;
        if (joueur == Jeu.JOUEUR_RGE) {
            taille = this.joueesA.size();
            for (int i = 0; i < taille; i++) {
                donnerCarte(0, this.defausse, this.joueesA);
                PlacerDefausse(this.defausse.get(this.defausse.size() - 1));
            }

        } else {
            taille = this.joueesB.size();
            for (int i = 0; i < taille; i++) {
                donnerCarte(0, this.defausse, this.joueesB);
                PlacerDefausse(this.defausse.get(this.defausse.size() - 1));
            }

        }
    }


    public void viderMains(){

        this.mainA.clear();
        this.mainB.clear();
    }
    public void GenererMains() {
        Paquet main = this.jeu.getMain(Jeu.JOUEUR_RGE);
        int taille = main.getTaille();
        int x = this.OFFSET;
        int y = ((this.heigth / 2) - (this.carteH / 2)) - 40;
        Point dest = new Point(this.xDep, this.yA);


        for (int i = 0; i < taille; i++) {
            CarteVue carteVue = new CarteVue(jeu, ctrl, this);

            carteVue.setCarte(main.getCarte(i));
            carteVue.setDos(false);
            carteVue.setSize(this.carteW, this.carteH);
            carteVue.setVisible(true);
            carteVue.setLocation(x, y);

            this.mainA.add(carteVue);
            this.frame.add(carteVue);
            dest.x = this.xDep + (i*this.carteW);
            creerMain(carteVue, dest);
        }

        main = this.jeu.getMain(Jeu.JOUEUR_VRT);
        taille = main.getTaille();
        dest = new Point(this.xDep, this.yB);

        for (int i = 0; i < taille; i++) {
            CarteVue carteVue = new CarteVue(jeu, ctrl, this);
            carteVue.setCarte(main.getCarte(i));
            carteVue.setDos(false);
            carteVue.setSize(this.carteW, this.carteH);
            carteVue.setVisible(true);
            carteVue.setLocation(x, y);

            this.mainB.add(carteVue);
            this.frame.add(carteVue);
            dest.x = this.xDep +(i*this.carteW);

            creerMain(carteVue, dest);
        }


    }

    /* AFFICHAGE */

    void afficheMain() {
        int x, y,  taille;


        //boolean joueur = this.jeu.getJoueurCourant()==1;
        x = this.xDep;
        taille = this.mainA.size();
        for (int i = 0; i < taille; i++) {
            if ( this.jeu.peutSelectionnerCarte(this.mainA.get(i).getCarte()) && this.jeu.getJoueurCourant() == this.jeu.JOUEUR_RGE){
                y = this.yAdec;
            }else{
                y = this.yA;
            }
            this.mainA.get(i).setLocation(x, y);
            if (this.mainA.get(i).isDos()) {
                this.mainA.get(i).setDos(false);
            }
            x += this.carteW;
        }

        x = (this.width / 2) - (this.carteW * 4);
        taille = this.mainB.size();
        for (int i = 0; i < taille; i++) {
            if ( this.jeu.peutSelectionnerCarte(this.mainB.get(i).getCarte()) && this.jeu.getJoueurCourant() == this.jeu.JOUEUR_VRT){
                y = this.yBdec;
            }else{
                y = this.yB;
            }
            this.mainB.get(i).setLocation(x, y);
            if (this.mainB.get(i).isDos()) {
                this.mainB.get(i).setDos(false);
            }
            x += this.carteW;
        }

        this.frame.repaint();
    }
    public void updateMains(){
        Collections.sort(this.mainA);
        Collections.sort(this.mainB);
        this.afficheMain();
        this.frame.repaint();

    }

    public void PlacerDefausse(CarteVue c) {


        int x = (2 * OFFSET) + this.carteW;
        int y = ((this.heigth / 2) - (this.carteH / 2)) - 40;

        Point posC = new Point(c.getX(), c.getY());
        Point posD = new Point(x,y);
        Dimension resize = new Dimension(this.carteW, this.carteH);

        envoiCarte(c,posC, posD, resize, this.delai);

        this.frame.repaint();

    }

    private int CalculJeuX(List<CarteVue> liste) {
        int centre = (this.width / 2);
        int largeur = (liste.get(0).getWidth() / 2);
        int taille = liste.size();
        return centre - (taille * largeur);
    }

    public void decaler(List<CarteVue> list) {
        int x = CalculJeuX(list);
        int y = list.get(0).getY();

        for (CarteVue carteVue : list) {
            carteVue.setLocation(x, y);
            x += carteVue.getWidth();
        }
        this.frame.repaint();
    }

    public void PlacerJeuA(CarteVue c) {
        int x = (this.width / 2) - (c.getWidth() / 2);
        int y = this.terrain.getY() + this.terrain.getHeight() + (this.carteH/4);


        Point posC = new Point(c.getX(), c.getY());
        Point posD = new Point(x,y);
        Dimension resize = new Dimension(this.joueeW, this.joueeH);
        envoiCarte(c, posC, posD, resize, this.delai);

        decaler(this.joueesA);
        this.frame.repaint();
    }
    public void PlacerJeuB(CarteVue c) {
        int x = (this.width / 2) - (c.getWidth() / 2);
        int y = this.terrain.getY() - (this.carteH/4) - (this.joueeH);


        Point posC = new Point(c.getX(), c.getY());
        Point posD = new Point(x,y);
        Dimension resize = new Dimension(this.joueeW, this.joueeH);
        envoiCarte(c, posC, posD, resize, this.delai);
        decaler(this.joueesB);
        this.frame.repaint();
    }

    public void afficheTerrain() {
        Dimension dimension = this.frame.getSize();

        int w = carteW * 10;
        int h = carteH + carteH / 2;

        if (!(w % 17 == 0))
            w -= w % 17;

        if(!(h % 3 == 0))
            h -= h%3;

        int y = ((dimension.height / 2) - (h / 3)*2);
        int x = ((dimension.width / 2) - (w / 2));

        terrain.setBounds(x, y, w, h);
        terrain.setVisible(true);
        this.frame.add(terrain);
        terrain.majPositions();

        terrain.repaint();
    }

    public void genererBoutons() {
        int BoutonLargeur = this.carteW / 2;
        int BoutonHauteur = BoutonLargeur;
        int x = this.terrain.getX() + this.terrain.getWidth() + BoutonLargeur;
        int y = this.terrain.getY() + BoutonHauteur;

        this.pouvoirFou = new BoutonPouvoirFou(ctrl, this.jeu);
        this.pouvoirSorcier = new BoutonPouvoirSorcier(ctrl, this.jeu);
        this.finTour = new BoutonFinirTour(ctrl, this.jeu, this);
        this.annuler = new BoutonAnnuler(ctrl, this.jeu);
        this.refaire = new BoutonRefaire(ctrl, this.jeu);


        pouvoirFou.setSize(BoutonLargeur, BoutonHauteur);
        pouvoirFou.setLocation(x, y);
        pouvoirFou.setVisible(true);

        pouvoirSorcier.setSize(BoutonLargeur, BoutonHauteur);
        y = this.terrain.getY() + this.terrain.getHeight() - (BoutonHauteur * 2);
        pouvoirSorcier.setLocation(x, y);
        pouvoirSorcier.setVisible(true);

        finTour.setSize((BoutonLargeur * 2) + (BoutonLargeur / 2), BoutonHauteur + (BoutonHauteur / 2));
        x = this.width - (finTour.getWidth() * 2);
        y = this.heigth - this.carteH - finTour.getHeight();
        finTour.setLocation(x, y);
        finTour.setVisible(true);

        annuler.setSize(BoutonLargeur, BoutonHauteur);
        refaire.setSize(BoutonLargeur, BoutonHauteur);
        y = this.terrain.getY() + this.terrain.getHeight() + annuler.getHeight();
        x = (this.width / 2) - (this.carteW * 4) - (annuler.getWidth() + (annuler.getWidth() / 2));
        annuler.setLocation(x, y);
        annuler.setVisible(true);
        x = (this.width / 2) + (this.carteW * 4) + (annuler.getWidth() + (annuler.getWidth() / 2));
        refaire.setLocation(x, y);
        refaire.setVisible(true);

        this.boutons.add(pouvoirFou);
        this.boutons.add(pouvoirSorcier);
        this.boutons.add(finTour);
        this.boutons.add(annuler);
        this.boutons.add(refaire);


        ajoutBoutons();
    }

    public void ajoutBoutons() {
        for (Bouton bouton : this.boutons) this.frame.add(bouton);
    }

    public void majBoutons(){
        if (this.jeu.peutUtiliserPouvoirFou()){
            this.pouvoirFou.setIcon(new ImageIcon(this.pouvoirFou.getImageCouleur()) );
            this.pouvoirFou.setBackground(Color.cyan);
        }
        if (!this.jeu.peutUtiliserPouvoirFou()){
            this.pouvoirFou.setIcon(new ImageIcon(this.pouvoirFou.getImageGris()) );
            this.pouvoirFou.setBackground(Color.darkGray);
        }
        if (!this.jeu.peutUtiliserPouvoirSorcier() ){
            this.pouvoirSorcier.setIcon(new ImageIcon(this.pouvoirSorcier.getImageGris()) );
            this.pouvoirSorcier.setBackground(Color.darkGray);
        }
        if (this.jeu.peutUtiliserPouvoirSorcier()){
            this.pouvoirSorcier.setIcon(new ImageIcon(this.pouvoirSorcier.getImageCouleur()) );
            this.pouvoirSorcier.setBackground(Color.orange);
        }
        this.pouvoirSorcier.repaint();
        this.pouvoirFou.repaint();
    }

    public void afficherBoutons() {
        for (Bouton bouton : this.boutons) {
            bouton.setVisible(true);
            bouton.setLocation(bouton.getLocation());

        }
        majBoutons();
    }

    public void carteSelecTaille(CarteVue carteVue, boolean b){
        int decalage = (this.carteH/5);
        if (b) {
            carteVue.setSize(this.carteW, this.carteH);

        }else {
            carteVue.setSize(this.carteW + decalage, this.carteH + decalage);
        }
        carteVue.repaint();
    }

    public void genererTours(){
        this.tourA = new TourPanel(this, this.ctrl);
        this.tourB = new TourPanel(this, this.ctrl);

        this.tourA.setLocation(this.tourA.getWidth(), this.yA );
        this.tourB.setLocation(this.tourB.getWidth(), this.yB + this.carteH - this.tourB.getHeight() );

        this.tourA.setVisible(true);
        this.tourB.setVisible(true);

        this.frame.add(this.tourA);
        this.frame.add(this.tourB);

    }
    public void afficherTours(){
        if (this.jeu.getJoueurCourant() == this.jeu.JOUEUR_RGE){
            this.tourA.setImage(this.tourA.getRougeBase());
            this.tourB.setImage(this.tourB.getBleuSec());
        }else{
            this.tourA.setImage(this.tourA.getRougeSec());
            this.tourB.setImage(this.tourB.getBleuBase());
        }
        this.tourA.repaint();
        this.tourB.repaint();
    }


    /* ANIMATIONS */

    public void envoiCarte(CarteVue c, Point dep, Point fin, Dimension d,int delai){
        AnimationPanel animationPanel = new AnimationPanel(c, dep, fin, d, delai);
        animationPanel.AnimStart();

    }

    public void creerMain(CarteVue carteVue, Point dest){
        Point depart = new Point(carteVue.getX(), carteVue.getY());
        AnimationPanel animationPanel = new AnimationPanel(carteVue, depart, dest,new Dimension(carteVue.getSize()), 5);
        animationPanel.AnimStart();
        while ( animationPanel.timer.isRunning()){}
    }
    public void envoiPioche(CarteVue carteVue, Point dest){
        Point depart = new Point(carteVue.getX(), carteVue.getY());
        if (carteVue.isDos()) {
            carteVue.setDos(false);
        }
        envoiCarte(carteVue, depart, dest, new Dimension(carteVue.getSize()), 5);
    }

    public boolean cartesJoueesEstVide (int joueur){
        if(joueur == this.jeu.JOUEUR_RGE && this.joueesA.isEmpty()){
            return true;
        } else if (joueur == this.jeu.JOUEUR_VRT && this.joueesB.isEmpty()){
            return true;
        } else {
            return false;
        }

    }


    public void genererSansAnimMains() {
        Paquet main = this.jeu.getMain(Jeu.JOUEUR_RGE);
        int taille = main.getTaille();
        Point dest = new Point(this.xDep, this.yA);


        for (int i = 0; i < taille; i++) {
            dest.x = this.xDep + (i*this.carteW);
            CarteVue carteVue = new CarteVue(jeu, ctrl, this);
            carteVue.setCarte(main.getCarte(i));
            carteVue.setDos(false);
            carteVue.setSize(this.carteW, this.carteH);
            carteVue.setVisible(true);
            carteVue.setLocation(dest);

            this.mainA.add(carteVue);
            this.frame.add(carteVue);
        }

        main = this.jeu.getMain(Jeu.JOUEUR_VRT);
        taille = main.getTaille();
        dest = new Point(this.xDep, this.yB);

        for (int i = 0; i < taille; i++) {
            dest.x = this.xDep +(i*this.carteW);
            CarteVue carteVue = new CarteVue(jeu, ctrl, this);
            carteVue.setCarte(main.getCarte(i));
            carteVue.setDos(false);
            carteVue.setSize(this.carteW, this.carteH);
            carteVue.setVisible(true);
            carteVue.setLocation(dest);

            this.mainB.add(carteVue);
            this.frame.add(carteVue);
        }


    }

    public boolean testMains(){
        int j = this.jeu.JOUEUR_RGE;
        int t = this.jeu.getMain(j).getTaille();
        for (int i=0; i<t; i++){
            if (this.jeu.getMain(j).getCarte(i) != this.mainA.get(i).getCarte())
                return false;
        }

        j = this.jeu.JOUEUR_VRT;
        t =  this.jeu.getMain(j).getTaille();
        for (int i=0; i<t; i++){
            if (this.jeu.getMain(j).getCarte(i) != this.mainB.get(i).getCarte())
                return false;
        }

        return true;
    }
    public void refaireDeck(){
        int t1 = this.jeu.getPioche().getTaille();

        for (int i=0; i<t1; i++){

            CarteVue carteVue = carteFromCartevue(this.jeu.getPioche().getCarte(i), this.defausse );
            carteVue.setDos(true);
            this.defausse.remove(carteVue);
            this.deck.add(carteVue);
            carteVue.setLocation(this.OFFSET, ((this.heigth / 2) - (this.carteH / 2)) - 40);

        }

    }

    public void refaireMains(){
        int t1 = this.mainA.size();
        int t2 = this.mainB.size();
        int t3 = this.jeu.getMain(this.jeu.JOUEUR_RGE).getTaille();
        int t4 = this.jeu.getMain(this.jeu.JOUEUR_VRT).getTaille();
        if (t1 != t3 || t2 != t4 || !this.testMains()) {
            for (int i=0; i<t1; i++){
                this.frame.remove(this.mainA.get(i));
            }
            for (int i=0; i<t2; i++){
                this.frame.remove(this.mainB.get(i));
            }
            this.mainA.clear();
            this.mainB.clear();
            this.genererSansAnimMains();
        }

    }
    public void refaireCartes( int joueur, int nbCartes) {
        Point dest;
        if (this.deck.isEmpty() || this.jeu.getPioche().getTaille() == 0 || nbCartes > this.deck.size() ){
            this.test = true;

            if (this.deck.size() > 0){
                nbCartes = this.deck.size();
                if (joueur == this.jeu.JOUEUR_RGE) {
                    dest = new Point(this.xDep, this.yA);
                    for (int i=0; i< nbCartes; i++){
                        envoiPioche(this.deck.get(this.deck.size()-1), dest);
                        donnerCarte(this.deck.size()-1, this.mainA, this.deck);
                    }
                }else {
                    dest = new Point(this.xDep, this.yB);
                    for (int i=0; i< nbCartes; i++){
                        envoiPioche(this.deck.get(this.deck.size()-1), dest);
                        donnerCarte(this.deck.size()-1, this.mainB, this.deck);
                    }
                }
            }

            refaireDeck();


        }
    }

}
