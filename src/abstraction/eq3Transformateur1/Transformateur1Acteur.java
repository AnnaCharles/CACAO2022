package abstraction.eq3Transformateur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eq1Producteur1.FiliereTestBourseEq1;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;

public class Transformateur1Acteur implements IActeur {
	protected Journal journal;
	protected Journal journalCCA;
	protected Journal journalCCV;
	protected Journal journalAO;
	protected Journal journalB;
	protected Journal journalSF;
	protected Journal journalSC;
	
	protected int cryptogramme;
	protected DicoFeve stockFeve;               /** Integer --> Double*/
	protected DicoChoco stockChoco;           /** Integer --> Double*/
	
	// Alexandre
	public Transformateur1Acteur() {
		cryptogramme = 0;
		stockFeve = new DicoFeve();
		stockChoco = new DicoChoco();
		this.journal = new Journal("Divers",this);
		this.journalCCA = new Journal("Contrats cadres Acheteurs",this);
		this.journalCCV = new Journal("CC Vendeurs", this);
		this.journalAO = new Journal("Appel d Offres", this);
		this.journalB = new Journal("Bourse", this);
		this.journalSF = new Journal("Stock fèves", this);
		this.journalSC = new Journal("Stock choco", this);
	}

	public void initialiser() {
		//initialiser les stocks
	}
	
	public String getNom() {
		return "EQ3";
	}

	public String getDescription() {
		return "Notre entreprise, nommée “Côtes du monde”, a pour ligne de conduite : le meilleur rapport qualité/prix. Nous avons pour but de produire des tablettes de chocolats accessibles à tous, tout en assurant une bonne qualité pour le consommateur.";
				
	}

	public Color getColor() {
		return new Color(241, 196, 15);
	}
	

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
		
	/** 
	 *  Alexandre*/
	public void next() {
		
	}
	
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filiere = new ArrayList<String>();
		filiere.add("FiliereTest1");
		return filiere;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "FiliereTest1" : return new FiliereTest1();
	    default : return null;
		}
	}
	
	/** Indicateurs : stockChoco, stockFeve, solde
	 *  Alexandre*/
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();

		res.add(
				new Variable(
						"StockFeve",
						this,
						this.stockFeve.get(Feve.FEVE_BASSE)
						+ this.stockFeve.get(Feve.FEVE_MOYENNE)
						+ this.stockFeve.get(Feve.FEVE_MOYENNE_BIO_EQUITABLE)));
		res.add(
				new Variable(
						"StockChoco",
						this,
						this.stockChoco.get(Chocolat.MQ)
						+ this.stockChoco.get(Chocolat.MQ_BE)
						+ this.stockChoco.get(Chocolat.MQ_O)));
		return res;
	}
	
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res; 
	}

	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(journal);
		res.add(journalCCA);
		res.add(journalCCV);
		res.add(journalAO);
		res.add(journalB);
		res.add(journalSC);
		res.add(journalSF);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
		System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will miss you. "+this.getNom());
		}
	}
	
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
	}
}