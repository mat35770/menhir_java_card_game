package fr.utt.isi.lo02.menhir.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.ButtonModel;
import javax.swing.JRadioButton;

import fr.utt.isi.lo02.menhir.modele.carte.CarteAllie;
import fr.utt.isi.lo02.menhir.modele.carte.CarteIngredient;
import fr.utt.isi.lo02.menhir.modele.carte.Paquet;
import fr.utt.isi.lo02.menhir.modele.enumeration.Action;
import fr.utt.isi.lo02.menhir.modele.enumeration.Saison;
import fr.utt.isi.lo02.menhir.modele.enumeration.TypePartie;
import fr.utt.isi.lo02.menhir.modele.joueur.Humain;
import fr.utt.isi.lo02.menhir.modele.joueur.IA;
import fr.utt.isi.lo02.menhir.modele.joueur.Joueur;
import fr.utt.isi.lo02.menhir.modele.partie.Partie;
import fr.utt.isi.lo02.menhir.vue.VueChoixPartieAvancee;
import fr.utt.isi.lo02.menhir.vue.VuePartie;

/**
 * Classe qui permet de controler la vue graphique et le modele
 * @author Mathieu DELALANDE, Nicolas GRANET
 *
 */
public class ControleurVue {	
	private Paquet paquet;	
	private Partie p;
	private VuePartie vp;
	
	int valeurJuste = 0, indiceChoix =0, choixAction=1, choixCarte =0, value = 0, valCarte[], valueCarteAllie = 0;
	String nomJoueurGagnant, typePartie;
	char reponseBonusAvancee, choixTypePartie;
	Action[] tabChoixAction = Action.values();
	Saison[] tabSaison = Saison.values();
	
	/**
	 * Constructeur, initialise la partie, les cartes et l'interface graphique du jeu 
	 */
	public ControleurVue(){
		paquet = new Paquet();
		p = new Partie();
		vp = new VuePartie(p, this);
	}
	
	/**
	 * Initialise le type de partie (rapid/avanc�e) et le nombre de joueurs humains
	 * @param nbJoueursHumain Nombre de joueurs humains
	 * @param rdbtRapide Boolean qui prend pour valeur vrai si l'utilisateur a choisi partie rapide et faux si l'utilisateur a choisi partie avanc�e
	 */
	public void paramPartie(int nbJoueursHumain, boolean rdbtRapide){
		if(rdbtRapide) p.setTypePartie(TypePartie.rapide);
		else p.setTypePartie(TypePartie.avanc�e);
		p.setNbHumains(nbJoueursHumain);
	}

	/**
	 * Ajoute un joueur humain par l'interm�diaire de la m�thode ajouterHumain2 de la classe Partie et trie les joueurs quand tous
	 * les joueurs sont ajout�s � la partie
	 * @param nom Le nom du joueur � ajouter
	 * @param age L'age du joueur � ajouter
	 * @param genre Le genre du joueur � ajouter
	 */
	public void ajouterHumain(String nom, String age, String genre){
		int ageInt;
		char genreF;
		ageInt =Integer.parseInt(age);
		if(genre == "masculin") genreF='n';
		else genreF='o';		
		p.ajouterHumain2(nom, ageInt, genreF);
		if (p.listeHumains.size()== p.getNbHumains())
			p.triOrdreJeu();		
	}
	
	/**
	 * Ajoute un joueur IA par l'interm�diaire de la m�thode ajouterIA2 de la classe Partie 
	 * @param nom Le nom du joueur IA � ajouter
	 */
	public void ajouterIA(String nom){
		p.ajouterIA2(nom);	
	}
	
	/**
	 * Initialise le nombre de manches, le choix des graines ou de la carte Alli�s en partie avanc�e et lance la partie.
	 */
	public void lancerPartie(){		

		if(p.getTypePartie().equals(TypePartie.rapide)){
			p.initGrainesPartieRapide();
			p.setNbManche(1);
				
			for (int numManche = 1; numManche <= p.getNbManche(); numManche++){
				p.setNumManche(numManche);
				paquet.genererPaquetIngredient();
				paquet.genererPaquetAllie();
				paquet.distribuerCartesIngredientsJoueur(p.ordreJeu);			
				     					 						
				vp.vueManche(p.ordreJeu.get(0), p, false);
			}			
		}	
		
		else{
			lancerPartieAvanc�e();
		}
		
	}
	
	/**
	 * Initialise une partie avanc�e
	 */
	public void lancerPartieAvanc�e(){
		int compteur = 1;
		p.setNbManche(p.ordreJeu.size());
		paquet.genererPaquetAllie();
		VueChoixPartieAvancee vCPA = new VueChoixPartieAvancee(p.ordreJeu.get(0), this, p.ordreJeu.size(), compteur, p.ordreJeu);
	}
	
	/**
	 * Lance une nouvelle manche de partie avanc�e
	 */
	public void NouvelleManchePartieAvanc�e(){			 						
			p.setNumManche(1);
			paquet.genererPaquetIngredient();
			paquet.distribuerCartesIngredientsJoueur(p.ordreJeu);
			for (Saison saison : Saison.values()){
	    		p.setSaison(saison);    			
	    			
	    		//on fait jouer les joueurs les uns apr�s les autres
	    		for(int numOrdreJoueur = 0; numOrdreJoueur < p.ordreJeu.size(); numOrdreJoueur++){
	    			Joueur actif = p.getJoueurActif(numOrdreJoueur);    				
	    			//choix de la carte et de l'action pour un humain
	    			if (actif instanceof Humain && actif.getCarteAllieJoueur() != null && actif.getCarteAllieJoueur().getNom() != "")    					 						
	    				vp.vueManche(actif, p, true);
	    			else
	    				vp.vueManche(actif, p, false);
	    		}							
			}							
	}
	
	/**
	 * Initialise le choix des IA en partie avanc�e
	 * @param compteur La position du joueur dans la liste
	 */
	public void initialiserIA(int compteur){
		boolean continuer = true;
		do{
			if (p.ordreJeu.get(compteur) instanceof IA){
				int ran = (int)(Math.random()*2);
				if(ran == 0)
					initCarteAllies(p.ordreJeu.get(compteur), false);
				else{
					initCarteAllies(p.ordreJeu.get(compteur), true);
				}
				compteur++;
				if(p.ordreJeu.size() - compteur == 0)
					continuer = false;
			}
			else
				continuer = false;
		}while(continuer == true);
	}
	/**
	 * Ajoute 2 graines ou distribue une carte Alli�s au joueur pass� en param�tre 
	 * @param j Le joueur qui re�oit les graines ou une carte Alli�s
	 * @param rdGraines Boolean qui prend pour valeur vrai le joueur choisit les graines et faux si le joueur choisit la carte Alli�s 
	 */
	public void initCarteAllies(Joueur j, boolean rdGraines){
		if (rdGraines){
			j.setNbGraines(2);
			j.setCarteAllieJoueur(new CarteAllie("",null));									
		}
		else{
			paquet.distribuerCarteAllieJoueur(j);
		}
	}
	
	
	public void validationJoueur (Joueur j, boolean engrais, boolean farfadets, String nomJoueurAttaque, int numCarte){
		//on r�cup�re la valeur de l'action
		valCarte = j.getCarteIngredientJoueur().get(numCarte-1).getValue();
		if(engrais)
			choixAction=2;
		else if(farfadets)
			choixAction=3;
		else
			choixAction=1;
		
		indiceChoix = valCarte.length/tabChoixAction.length * (choixAction-1) + tabSaison.length - j.getCarteIngredientJoueur().size();
		value = valCarte[indiceChoix];
		
		if(engrais)
			p.effectuerActionEngrais(value, j);
		else if (farfadets)
			p.effectuerActionFarfadets(value, j,stringToJoueur(nomJoueurAttaque) );
		else
			p.effectuerActionGeant(value, j);
		
		//on supprime la carte quand le joueur a fini de jouer
		j.getCarteIngredientJoueur().remove(numCarte-1);
		
		int numOrdreJoueur = p.ordreJeu.indexOf(j);	
		if(numOrdreJoueur+1 < p.ordreJeu.size()){
			vp.vueManche(p.ordreJeu.get(numOrdreJoueur+1), p, false);
		}
		
	}
	
	public void finTour(){
		if(p.getSaison() != Saison.hiver){
			int i = 0;
			boolean continuer = true;
			 Saison tabSaison[] = Saison.values();
			 do{
				 if (tabSaison[i] == p.getSaison()){
					 continuer = false;
				 }
				 i++;
			 }while(continuer == true);
			 p.setSaison(tabSaison[i]);
			 vp.vueManche(p.ordreJeu.get(0), p, false);
		}
		else
			finPartie();
		}
	
	public void finPartie(){
		vp.vueFinPartie(p);
	}
		 

	/**
	 * Retourne un joueur � partir de son nom
	 * @param nom Le nom du joueur
	 */
	public Joueur stringToJoueur(String nom){
		Joueur j = null;
		for(int i=0;i<p.ordreJeu.size();i++){
			if (nom == p.ordreJeu.get(i).getNom())
				j = p.ordreJeu.get(i);
		}
		return j;
	}
	
}
