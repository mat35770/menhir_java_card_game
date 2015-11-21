package fr.utt.isi.lo02.menhir.modele.partie;

import java.util.ArrayList;
import java.util.Scanner;

import fr.utt.isi.lo02.menhir.modele.enumeration.Saison;
import fr.utt.isi.lo02.menhir.modele.enumeration.TypePartie;
import fr.utt.isi.lo02.menhir.modele.joueur.*;

public class Partie implements java.lang.Comparable{
	private TypePartie typePartie;
	private Saison saison;
	private ArrayList<Joueur> ordreJeu;
	private int nbJoueur, manche, tour;
	
	public Partie() {
		this.saison=Saison.printemps;
		this.ordreJeu = new ArrayList<Joueur>();
	}
	
	public void TypePartie(){
		System.out.println("Choisir le type de Partie : ");
		System.out.println("- rapide");
		System.out.println("- avanc�e");
		Scanner in = new Scanner(System.in);
		if (TypePartie.values)
		this.typePartie=in.nextTypePartie();
		
	}
	/*
	 * M�thode qui initialise les joueurs humains
	 */
	public void ajouterJoueur(){
		System.out.println("Saisir le nombre de joueur r�els : ");
		Scanner in = new Scanner(System.in);
		int nbHumain= in.nextInt();
		
		for (int i=0; i<nbHumain; i++) {			
			System.out.println("Veuillez sasir le nom du joueur "+(i+1)+" (humain) : ");
			Scanner in2 = new Scanner(System.in);
			String name = in2.nextLine();
			
			System.out.println("Veuillez sasir l'age du joueur "+(i+1)+" (humain) : ");
			Scanner in3 = new Scanner(System.in);
			int age = in3.nextInt();
			
			System.out.println("Le joueur est-il du genre f�minin "+(i+1)+" (true/false) : ");
			Scanner in4 = new Scanner(System.in);
			boolean genreF = in4.nextBoolean();
			
			Humain humain = new Humain (name, age, genreF);
			this.ordreJeu.add(humain);			
		}
	}
	
	 public int compareTo(Object other) { 
	      boolean genreF = ((Humain) other).getGenreF(); 
	      int age = ((Humain) other).getAge();
	      int genreF2 = this.getGenreF(); 
	      int age = ((Humain) other).getAge();
	      if (nombre1 > nombre2)  return -1; 
	      else if(nombre1 == nombre2) return 0; 
	      else return 1; 
	   } 
	 
	public void ordreJeu(){	  
		this.ordreJeu.add(joueur);
	}
	
	public void definirOrdreJeu(){
		
	}
	
	
}


