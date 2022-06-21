package abstraction.eq2Producteur2;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq1Producteur1.FiliereTestBourseEq1;
import abstraction.eq8Romu.bourseCacao.IVendeurBourse;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.general.VariableReadOnly;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;

public abstract class Producteur2Acteur extends Producteur2Stockage2 implements IActeur{
	
	protected int cryptogramme;
	protected Journal journal;
	private Variable StockFeveBasse;
	private Variable StockFeveMoyenne;
	private Variable StockFeveMoyenne_BE;
	private Variable StockFeveHaute;
	private Variable StockFeveHaute_BE; 
	private Variable StockChocoHQ;
	private HashMap<Double, Double> Benefices;
	private LinkedList<Double> Soldes;
	private HashMap<Double, Boolean> AugmentationSalaires;
	protected Journal journalplantation ;

	private Variable prixstockage ;

	
	// Auteur : Clément
	
	public Producteur2Acteur() {
		super();
		this.prixstockage= new Variable("Prix Stockage", "Prix en euros par kilo par step", this,  0.0, 1000000000, 0.01) ;
		this.journal = new Journal(this.getNom()+" activites", this);
		this.journalplantation=new Journal("Plantation",this);
		this.StockFeveBasse= new Variable("StockFeveBasse", "Stock de Fèves Basse", this, 0.0, 1000000000, this.getStock(Feve.FEVE_BASSE));
		this.StockFeveMoyenne= new Variable("StockFeveMoyenne", "Stock de Fèves Moyenne", this, 0.0, 1000000000, this.getStock(Feve.FEVE_MOYENNE));
		this.StockFeveMoyenne_BE= new Variable("StockFeveMoyenne_BE", "Stock de Fèves Moyenne BE", this, 0.0, 1000000000, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE));
		this.StockFeveHaute= new Variable("StockFeveHaute", "Stock de Fèves Haute", this, 0.0, 1000000000, this.getStock(Feve.FEVE_HAUTE));
		this.StockFeveHaute_BE= new Variable("StockFeveHaute_BE", "Stock de Fèves Haute BE", this, 0.0, 1000000000, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE));
		this.StockChocoHQ = new Variable("StockChocoHQ","Stock de chocolat issue de fève de haute qualité", this, 0.0, 1000000000, this.getStockChoco(Chocolat.HQ_BE));
		

//		this.StockChocoHQ = new Variable("StockChocoHQ","Stock de chocolat issue de fève de haute qualité", this, 0.0, 1000000000, this.getStockChoco(Chocolat.HQ_BE);
		this.Benefices = new HashMap<Double, Double>();
		this.Soldes = new LinkedList<Double>();
		this.AugmentationSalaires = new HashMap<Double, Boolean>();
		this.MainOeuvremecontente = false;
	}

	public void initialiser() {
		super.initialiser();
	}
	
	public String getNom() {
		return "Cacao Doré";
	}

	public String getDescription() {
		return "Cacao Doré";
	}

	public Color getColor() {
		return new Color(46, 204, 113);
	}
	public abstract double getStockChoco(Chocolat choco);
	

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	
	public HashMap<Double, Double> MAJBenefices() {
		// auteur : Fiona 
		/* 
		 * Je calcule les bénéfices à chaque étape et je les "sauvegarde" dans la HashMAp Benefices
		 * 
		 * Ensuite, si sur les 3 derniers Step on est en bénéfice, j'augmente les salaires
		 * (ie ajout de "True" dans la HashMap AugmentationSalaires pour le step courant) de 1% de 
		 * notre bénéfice (certes cela peut sembler dérisoire mais les autres producteurs n'augmentent 
		 * pas les salaires...)
		 * 
		 * Finalement, si on bout de 5 steps de bénéfices les salariés n'ont pas été augmenté, 
		 * ils sont "mécontents" ce qui diminue le rendement (dans Producteur2Plantation).  
		 * 
		 */
		
		if (Filiere.LA_FILIERE.getEtape() == 0) {
			this.Benefices.put((double) Filiere.LA_FILIERE.getEtape(), 0.0);
			this.Soldes.add(this.getSolde());
			this.AugmentationSalaires.put((double) Filiere.LA_FILIERE.getEtape(), false);
			
		}
		
		else {
			double solde_prec = this.Soldes.getLast();
			double benefice = this.getSolde() - solde_prec;
			this.Benefices.put((double) Filiere.LA_FILIERE.getEtape(), benefice);
		}
		
		if (Filiere.LA_FILIERE.getEtape() > 4 && this.Benefices.get((double)Filiere.LA_FILIERE.getEtape()-1)>0 && this.Benefices.get((double)Filiere.LA_FILIERE.getEtape()-2)>0  && this.Benefices.get((double)Filiere.LA_FILIERE.getEtape()-3)>0) {
			double proba = Math.random();
			if (proba < 0.33) {
				this.AugmentationSalaires.put((double) Filiere.LA_FILIERE.getEtape(), true);
				Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.Benefices.get((double)Filiere.LA_FILIERE.getEtape()-1)*0.01);
			}
			else {
				this.AugmentationSalaires.put((double) Filiere.LA_FILIERE.getEtape(), false);
			}
			
		}		
		else {
			this.AugmentationSalaires.put((double) Filiere.LA_FILIERE.getEtape(), false);
		}
		
		
		if (Filiere.LA_FILIERE.getEtape() > 6 && !this.AugmentationSalaires.get((double)Filiere.LA_FILIERE.getEtape()-1) && !this.AugmentationSalaires.get((double)Filiere.LA_FILIERE.getEtape()-2) && !this.AugmentationSalaires.get((double)Filiere.LA_FILIERE.getEtape()-3) && !this.AugmentationSalaires.get((double)Filiere.LA_FILIERE.getEtape()-4) && !this.AugmentationSalaires.get((double)Filiere.LA_FILIERE.getEtape()-5) ) {
			this.MainOeuvremecontente = true;
		}
		else {
			this.MainOeuvremecontente = false;
		}
		
		return this.Benefices;
		
		

	}

	public void next() {
		super.next();	
		super.next();
		
		this.MAJBenefices();
		
		for(Feve f : Feve.values()) {
			this.journalplantation.ajouter(f.toString()+" : "+this.difference(f)+", Arbre : " + this.proportionArbre(f)+ ", Vente : " + this.proportionVente(f));
			this.journalplantation.ajouter(f.toString()+" : "+this.getNbArbreTotal(f));
		
		}
		this.journalplantation.ajouter("=========================================================================");
		// Cout de production, Jules DORE
		this.setCoutParKg();
		double coutProduction = 0.0;
		for(Feve f : this.coutParKg.keySet()) {
			coutProduction = coutProduction + this.coutParKg.get(f)*this.production(f);
		}
		Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutProduction);
		
		// Cout de stockage, Jules DORE
		
		double coutStockage = 0.0 ;
		for (Feve f : this.getStocks().keySet()) {
			coutStockage = coutStockage + (this.getStock(f)*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur());
		}
		Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutStockage);
		
		// journal, Jules DORE

		journal.ajouter("Stock Feve Moyenne : "+this.getStock(Feve.FEVE_MOYENNE)+", Production Feve Moyenne : "+this.production(Feve.FEVE_MOYENNE)+", Nombre d'arbre Moyenne : "+this.getNbArbreTotal(Feve.FEVE_MOYENNE)+"");
		journal.ajouter("Stock Feve Haute : "+this.getStock(Feve.FEVE_HAUTE)+", Production Feve Haute : "+this.production(Feve.FEVE_HAUTE)+", Nombre d'arbre Haute : "+this.getNbArbreTotal(Feve.FEVE_HAUTE)+"");
		journal.ajouter("Stock Feve Basse : "+this.getStock(Feve.FEVE_BASSE)+", Production Feve Basse : "+this.production(Feve.FEVE_BASSE)+", Nombre d'arbre Basse : "+this.getNbArbreTotal(Feve.FEVE_BASSE)+"");
		journal.ajouter("Stock Feve Haute BE : "+this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE)+", Production Feve Haute BE : "+this.production(Feve.FEVE_HAUTE_BIO_EQUITABLE)+", Nombre d'arbre Haute BE : "+this.getNbArbreTotal(Feve.FEVE_HAUTE_BIO_EQUITABLE)+"");
		journal.ajouter("Stock Feve Moyenne BE : "+this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE)+", Production Feve Moyenne BE : "+this.production(Feve.FEVE_MOYENNE_BIO_EQUITABLE)+", Nombre d'arbre Moyenne BE : "+this.getNbArbreTotal(Feve.FEVE_MOYENNE_BIO_EQUITABLE)+"");
		journal.ajouter("Stock Choco Haute Qualité : "+this.getStockChoco(Chocolat.HQ_BE)+",Transformation Chocolat Haute Qualité : "+this.production(Feve.FEVE_HAUTE)*0.05);
		journal.ajouter("=====================================================");
		this.GetStockHaute().setValeur(this, this.getStock(Feve.FEVE_HAUTE));
		this.GetStockMoyenne().setValeur(this, this.getStock(Feve.FEVE_MOYENNE));
		this.GetStockMoyenne_BE().setValeur(this, this.getStock(Feve.FEVE_MOYENNE_BIO_EQUITABLE));
		this.GetStockBasse().setValeur(this, this.getStock(Feve.FEVE_HAUTE));
		this.GetStockHausse_BE().setValeur(this, this.getStock(Feve.FEVE_HAUTE_BIO_EQUITABLE));	
		this.GetStockChocoHQ().setValeur(this, this.getStockChoco(Chocolat.HQ_BE));
	}
	
	public List<String> getNomsFilieresProposees() {
		this.getSolde();
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("Producteur2TestBourse"); 
		return filieres;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "Producteur2TestBourse" : return new Producteur2TestBourse();
	    default : return null;
		}
	}
	
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(StockFeveBasse);
		res.add(StockFeveMoyenne);
		res.add(StockFeveMoyenne_BE);
		res.add(StockFeveHaute);
		res.add(StockFeveHaute_BE);
		res.add(StockChocoHQ);
		return res;
	}
	
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(this.prixstockage);
		return res; 
	}

	
	
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(journal);
		return res;
	}

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
		System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Poor "+acteur.getNom()+"... We will not miss you. "+this.getNom());
		}
	}
	
	public void notificationOperationBancaire(double montant) {
	}
	
	/**
	 *@return Le solde
	 */
	
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
	}
	public Variable GetStockBasse() {
		return StockFeveBasse;
	}
	public Variable GetStockMoyenne() {
		return StockFeveMoyenne;
	}
	public Variable GetStockMoyenne_BE() {
		return StockFeveMoyenne_BE;
	}
	public Variable GetStockHaute() {
		return StockFeveHaute;
	}
	
	public Variable GetStockHausse_BE() {
		return StockFeveHaute_BE;
	}
	public Variable GetStockChocoHQ() {
		return StockChocoHQ;
	}




}
