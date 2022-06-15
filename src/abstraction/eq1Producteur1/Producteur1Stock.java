package abstraction.eq1Producteur1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import abstraction.eq8Romu.filiere.Banque;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Feve;


public abstract class Producteur1Stock extends Producteur1Acteur {
	private  HashMap<Feve,List<FeveProducteur1>> Feves;
	private Variable StockBasse;
	private Variable StockMoyenne;
	private Variable StockHaut_BE;
	private Variable StockMoyenne_BE;
	private Variable StockBasse_NA; //on compte les fèves non affinées
	private Variable StockMoyenne_NA;
	private Variable StockHaut_BE_NA;
	private Variable StockMoyenne_BE_NA;
	private Variable PrixEntretienArbre;
	
	
	//Auteur : Khéo
	public Producteur1Stock() {
		this.Feves = new HashMap<Feve, List<FeveProducteur1>>() ;
		Feves.put(Feve.FEVE_BASSE, new ArrayList<FeveProducteur1>());
		Feves.put(Feve.FEVE_HAUTE, new ArrayList<FeveProducteur1>());
		Feves.put(Feve.FEVE_HAUTE_BIO_EQUITABLE, new ArrayList<FeveProducteur1>());
		Feves.put(Feve.FEVE_MOYENNE, new ArrayList<FeveProducteur1>());
		Feves.put(Feve.FEVE_MOYENNE_BIO_EQUITABLE, new ArrayList<FeveProducteur1>());
		
		this.StockBasse_NA= new Variable(this.getNom()+"StockBasse_NA", "Stock de Fèves Basse avec sans Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_BASSE, true));
		this.StockMoyenne_NA= new Variable(this.getNom()+"StockMoyenne_NA", "Stock de Fèves Moyenne avec sans Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_MOYENNE, true));
		this.StockHaut_BE_NA= new Variable(this.getNom()+"StockHautBE_NA", "Stock de Fèves Haut Bio équitable avec sans Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE, true));
		this.StockMoyenne_BE_NA= new Variable(this.getNom()+"StockMoyenne_BE_NA", "Stock de Fèves Moyenne Bio équitable avec sans Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE, true));
		
		this.StockBasse= new Variable(this.getNom()+"StockBasse", "Stock de Fèves Basse sans non Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_BASSE, false));
		this.StockMoyenne= new Variable(this.getNom()+"StockMoyenne", "Stock de Fèves Moyenne sans non Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_MOYENNE, false));
		this.StockHaut_BE= new Variable(this.getNom()+"StockHautBE", "Stock de Fèves Haut Bio équitable sans non Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE, false));
		this.StockMoyenne_BE= new Variable(this.getNom()+"StockMoyenne_BE", "Stock de Fèves Moyenne Bio équitable sans non Affinage", 
				this, 0, 1000000000, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE, false));
		
		this.PrixEntretienArbre= new Variable(this.getNom()+"Prix Entretien Arbre", "Prix Entretien des Arbres", 
				this, 0, 1000000000, 0.001);
		
		
		
	}
	
	
	




	public abstract LinkedList<Parc> getListeParc();
	
	//Auteur : Khéo
	public void initialiser() {
	this.addLot(Feve.FEVE_BASSE, 1000000, this.getListeParc().get(0));
	this.addLot(Feve.FEVE_MOYENNE, 1000000,this.getListeParc().get(0));
	this.addLot(Feve.FEVE_MOYENNE_BIO_EQUITABLE, 100000,this.getListeParc().get(0));
	this.addLot(Feve.FEVE_HAUTE_BIO_EQUITABLE, 100000,this.getListeParc().get(0));
	}
	
	//Auteur : Khéo
	public void next(){
		super.next();
		
		
		this.MAJStock();
		this.nextParasites();	

		for (Feve f : this.getFeves().keySet()) {
			for(FeveProducteur1 Lot : this.getFeves().get(f)) {
				Lot.MAJAffinage(f);
			}
		}
		
		//Mis à jour Variable
		this.getStockBasse().setValeur(this, this.getStock(Feve.FEVE_BASSE, false));
		this.getStockHaut_BE().setValeur(this, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE, false));
		this.getStockMoyenne().setValeur(this, this.getStock(Feve.FEVE_MOYENNE, false));
		this.getStockMoyenne_BE().setValeur(this, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE, false));
		
		this.getStockBasse_NA().setValeur(this, this.getStock(Feve.FEVE_BASSE, true));
		this.getStockHaut_BE_NA().setValeur(this, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE, true));
		this.getStockMoyenne_NA().setValeur(this, this.getStock(Feve.FEVE_MOYENNE, true));
		this.getStockMoyenne_BE_NA().setValeur(this, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE, true));
		
	}
	
	//Auteur : Khéo
	public double getStock(Feve f, boolean affinage){
		
		double somme = 0.0 ;

		for(FeveProducteur1 Lot : this.getFeves().get(f)) {
			if (Lot.isAffine() || affinage) {

				somme = somme + Lot.getPoids() ;
			}
		}

		return somme ;	

	}
	
	
	
	public double getStockParc(Feve f, boolean affinage, Parc provenance){
		
		double somme = 0.0 ;
		for(FeveProducteur1 Lot : this.getFeves().get(f)) {
			if (Lot.getProvenance() == provenance) {
				if (Lot.isAffine() || affinage) {
					somme = somme + Lot.getPoids() ;
				}
			}
		}
		return somme ;	
	}
	
	
	//Auteur : Khéo
	public void addLot(Feve f, double quantite, Parc provenance) {
		this.getFeves().get(f).add(new FeveProducteur1(quantite,provenance));

	}
	
	
	//Auteur : Khéo
	public void retirerQuantite(Feve f, double quantite) {
		while (quantite>0) {
			double poids = this.getFeves().get(f).get(0).getPoids() ;
			
			if (quantite >= poids) { //On regarde en fonction de la quantite qui reste dans le lot. Si il y'a plus on enlève le lot
				quantite = quantite - poids ;
				this.getFeves().get(f).remove(0);
			}
			else {
				this.getFeves().get(f).get(0).setPoids(poids-quantite); //Si non, on ajuste le poids du lot
				quantite = 0 ;
			}
		}
	}
	
	public double retirerQuantiteParc(Feve f, double quantite, Parc provenance) {
		
		
		int i = 0 ;
		while (quantite>0 && this.getStockParc(f, false, provenance)>0 && i < this.getFeves().get(f).size()) {
			double poids = this.getFeves().get(f).get(i).getPoids() ;
			
			if (this.getFeves().get(f).get(i).getProvenance() == provenance) {
			if (quantite >= poids) { //On regarde en fonction de la quantite qui reste dans le lot. Si il y'a plus on enlève le lot
				quantite = quantite - poids ;
				this.getFeves().get(f).remove(i);
			}
			else {
				this.getFeves().get(f).get(i).setPoids(poids-quantite); //Si non, on ajuste le poids du lot
				quantite = 0 ;
			}
			}
		i += 1 ;
		}
		
		return quantite; //On peut retourner la quantité à redistribuer
		
	}
	
	//Auteur : Khéo
	public void MAJStock() {
		
		for(Feve Feve : this.getFeves().keySet()) {
			for (int i=0; i< this.getFeves().get(Feve).size(); i++) {
				this.getFeves().get(Feve).get(i).MajPeremption();
				
				if (this.getFeves().get(Feve).get(i).isPerime()) { //On retire du stock si c'est périmée
					this.getFeves().get(Feve).remove(i);
				}
			}
		}
	}
	
	/**
	 * @return the feves
	 */
	public HashMap<Feve, List<FeveProducteur1>> getFeves() {
		return Feves;
	}
	
	//Auteur : Khéo
	/**
	 * @return the stockBasse
	 */
	public Variable getStockBasse() {
		return StockBasse;
	}

	/**
	 * @return the stockMoyenne
	 */
	public Variable getStockMoyenne() {
		return StockMoyenne;
	}

	/**
	 * @return the stockHaut_BE
	 */
	public Variable getStockHaut_BE() {
		return StockHaut_BE;
	}
		
	/**
	 * @return the stockMoyenne_BE
	 */
	public Variable getStockMoyenne_BE() {
		return StockMoyenne_BE;
	}
		
	/**
	 * @return the prixEntretienArbre
	 */
	public Variable getPrixEntretienArbre() {
		return PrixEntretienArbre;
	}



	/**
	 * @return the stockBasse_NA
	 */
	public Variable getStockBasse_NA() {
		return StockBasse_NA;
	}

	/**
	 * @return the stockMoyenne_NA
	 */
	public Variable getStockMoyenne_NA() {
		return StockMoyenne_NA;
	}

	/**
	 * @return the stockHaut_BE_NA
	 */
	public Variable getStockHaut_BE_NA() {
		return StockHaut_BE_NA;
	}

	/**
	 * @return the stockMoyenne_BE_NA
	 */
	public Variable getStockMoyenne_BE_NA() {
		return StockMoyenne_BE_NA;
	}
		
	/**
	 * Auteur : Laure
	 * met à jour le stock après des parasites
	 */
	public void nextParasites2() {
		// Differentiation entre gammes / bio :
		// bio : 25% par UT
		// non bio : 15% par UT
		// 3 niveaux de parasites
		// 1 : 70 %
		// 2 : 25%
		// 3 : 5%
		// 1 : Perte de 10% de la récolte
		// 2 : Perte de 50% de la récolte
		// 3 : Perte de 80% de la récolte
		double aleaParasiteGlobal = 0.0;
		for(Feve Feve : this.getFeves().keySet()) {
			
			aleaParasiteGlobal = Math.random(); //Proba d'avoir un parasite
			double tauxPerteStock = 0.0;
			
			
			if (Feve.isBioEquitable()) {
				if (aleaParasiteGlobal <=0.25) { //Force du parasite
					double aleaForceParasites = Math.random();
					if (aleaForceParasites <= 0.70) {
						// On perd 10% de notre stock
						tauxPerteStock = 0.1;
					} else if (aleaForceParasites <=0.95) {
						// On perd 50% de notre stock
						tauxPerteStock = 0.5;
					} else {
						// On perd 80% de notre stock
						tauxPerteStock = 0.8;
					}
				}
				
			} else { // Pas bio-équitable moins de chance d'avoir un parasite
				if (aleaParasiteGlobal <=0.15) {
					double aleaForceParasites = Math.random();
					if (aleaForceParasites <= 0.70) {
						// On perd 10% de notre stock
						tauxPerteStock = 0.1;
					} else if (aleaForceParasites <=0.95) {
						// On perd 50% de notre stock
						tauxPerteStock = 0.5;
					} else {
						// On perd 80% de notre stock
						tauxPerteStock = 0.8;
					}

				}
				
			}
			
			double stockAvantPar = this.getStock(Feve, true);
			double perteStock = stockAvantPar*tauxPerteStock;
			this.retirerQuantite(Feve, perteStock);
		}
	}
	
	/**
	 * @author laure
	 * Parasites en fonction du parc
	 */
	public void nextParasites() {
		// Differentiation entre gammes / bio :
		// bio : 25% par UT
		// non bio : 15% par UT
		// 3 niveaux de parasites
		// 1 : 70 %
		// 2 : 25%
		// 3 : 5%
		// 1 : Perte de 10% de la récolte
		// 2 : Perte de 50% de la récolte
		// 3 : Perte de 80% de la récolte
		double aleaParasiteGlobal = 0.0;
		for (Parc parc : this.getListeParc()) {
			
		for(Feve Feve : this.getFeves().keySet()) {
			
			aleaParasiteGlobal = Math.random(); //Proba d'avoir un parasite
			double tauxPerteStock = 0.0;
			
			
			if (Feve.isBioEquitable()) {
				if (aleaParasiteGlobal <=0.25) { //Force du parasite
					double aleaForceParasites = Math.random();
					if (aleaForceParasites <= 0.70) {
						// On perd 10% de notre stock
						tauxPerteStock = 0.1;
					} else if (aleaForceParasites <=0.95) {
						// On perd 50% de notre stock
						tauxPerteStock = 0.5;
					} else {
						// On perd 80% de notre stock
						tauxPerteStock = 0.8;
					}
				}
				
			} else { // Pas bio-équitable moins de chance d'avoir un parasite
				if (aleaParasiteGlobal <=0.15) {
					double aleaForceParasites = Math.random();
					if (aleaForceParasites <= 0.70) {
						// On perd 10% de notre stock
						tauxPerteStock = 0.1;
					} else if (aleaForceParasites <=0.95) {
						// On perd 50% de notre stock
						tauxPerteStock = 0.5;
					} else {
						// On perd 80% de notre stock
						tauxPerteStock = 0.8;
					}

				}
				
			}
			double stockAvantPar = this.getStockParc(Feve, true,parc);
			double perteStock = stockAvantPar*tauxPerteStock;
			this.retirerQuantiteParc(Feve, perteStock,parc);
		}
		}
	}
}
