package abstraction.eq4Transformateur2; 

import abstraction.eq8Romu.appelsOffres.FiliereTestAO;
import abstraction.eq8Romu.appelsOffres.IVendeurAO;
import abstraction.eq8Romu.appelsOffres.PropositionAchatAO;
import abstraction.eq8Romu.appelsOffres.SuperviseurVentesAO;
import abstraction.eq8Romu.bourseCacao.FiliereTestBourse;
import abstraction.eq8Romu.clients.FiliereTestClientFinal;
import abstraction.eq8Romu.contratsCadres.FiliereTestContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.filiere.IMarqueChocolat;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Transformateur2Acteur implements IActeur,IMarqueChocolat, IFabricantChocolatDeMarque {
	
	
	
//	protected Variable prixSeuilMQ; // au dela duquel nous n'achetons pas
//	protected Variable prixSeuilHQ;
//	protected Variable prixSeuilBQ;
//	protected Variable prixSeuilMQBE;
//	protected Variable prixSeuilHQBE;
	//private Variable capaciteStockageFixe;// stock que l'on souhaite en permanence
	protected Variable prixMinB;
	protected Variable prixMinM;
	protected Variable prixMinMb;
	protected Variable prixMinH;
	protected Variable prixMinHb;
	private Stock<Feve> stockReferenceFeve; //Le stock referent de feve, celui vers lequel on essaye de retourner à chaque etape
	private Stock<ChocolatDeMarque> stockReferenceChocolat;//Idem pour choco
	protected double margeAO;
	protected double margeCC;
	
	protected SuperviseurVentesAO superviseur;
	protected int cryptogramme;
	protected double NewCap;//à réinitialiser=cpacité de production au début de chaque tour
	private int comptFaillite;
	



	

	
	
	//Nawfel
	public Transformateur2Acteur() {
	
//		this.prixSeuilBQ = new Variable("prix seuil basse qualité", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 10);
//		this.prixSeuilMQ = new Variable("prix seuil moyenne qualité", "<html>Prix Seuil Moyenne Qualité</html>",this, 0.0, 10000000, 10);
//		this.prixSeuilMQBE = new Variable("prix seuil moyenne qualité bio", "<html>Prix Seuil Moyenne Qualité BIO</html>",this, 0.0, 10000000, 10);
//		this.prixSeuilHQ = new Variable("prix seuil haute qualité", "<html>Prix Seuil Haute Qualité</html>",this, 0.0, 10000000, 10);
//		this.prixSeuilHQBE = new Variable("prix seuil haute qualité bio", "<html>Prix Seuil Haute Qualité BIO</html>",this, 0.0, 10000000, 10);
		this.prixMinB = new Variable("prix seuil basse qualité", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 4);
		this.prixMinM = new Variable("prix seuil moyenne qualité", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 5);
		this.prixMinMb = new Variable("prix seuil moyenne qualité bio", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 4);
		this.prixMinH = new Variable("prix seuil haute qualité", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 5);
		this.prixMinHb = new Variable("prix seuil haute qualité bio", "<html>Prix Seuil Basse Qualité</html>",this, 0.0, 10000000, 5);
		//this.capaciteStockageFixe=new Variable("stock theorique desire", "<html>Stock Theorique désiré en permanence</html>",this, 0.0, 1000000.0, 8000);
		this.margeAO = 1.5;
		this.margeCC=1.30;
		this.comptFaillite=0;
		//On crée notre stock referent, qui servira juste de guide pour savoir combien acheter/transformer à chaque tour.
		this.stockReferenceFeve=new Stock();
		this.stockReferenceFeve.ajouter(Feve.FEVE_BASSE, 20000000);
		this.stockReferenceFeve.ajouter(Feve.FEVE_MOYENNE, 20000000);
		this.stockReferenceFeve.ajouter(Feve.FEVE_MOYENNE_BIO_EQUITABLE, 2500000);
		this.stockReferenceFeve.ajouter(Feve.FEVE_HAUTE, 5000000);
		this.stockReferenceFeve.ajouter(Feve.FEVE_HAUTE_BIO_EQUITABLE, 2500000);
		ChocolatDeMarque c1=new ChocolatDeMarque(Chocolat.MQ,this.getMarquesChocolat().get(1));
		ChocolatDeMarque c0=new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		ChocolatDeMarque c2=new ChocolatDeMarque(Chocolat.MQ_BE,this.getMarquesChocolat().get(2));
		ChocolatDeMarque c3=new ChocolatDeMarque(Chocolat.HQ,this.getMarquesChocolat().get(3));
		ChocolatDeMarque c4=new ChocolatDeMarque(Chocolat.HQ_BE,this.getMarquesChocolat().get(4));
		//ajouter des marques de chocolats
		
		
		this.stockReferenceChocolat=new Stock();
		this.stockReferenceChocolat.ajouter(c1, 20000000);
		this.stockReferenceChocolat.ajouter(c0, 20000000);
		this.stockReferenceChocolat.ajouter(c2, 2500000);
		this.stockReferenceChocolat.ajouter(c3, 5000000);
		this.stockReferenceChocolat.ajouter(c4, 5000000);
		
		
		
		



	}
	
// Jad
// Renvoie le prix seuil de chaque feves
//	public Variable getPrixSeuil(Feve f) {
//		if(f.equals(Feve.FEVE_BASSE)) {
//			return this.getPrixSeuilBQ();
//		}
//		else if(f.equals(Feve.FEVE_HAUTE)) {
//			return this.getPrixSeuilHQ();
//		}
//		else if(f.equals(Feve.FEVE_HAUTE_BIO_EQUITABLE)) {
//			return this.getPrixSeuilHQBE();
//		}
//		else if (f.equals(Feve.FEVE_MOYENNE)) {
//			return this.getPrixSeuilMQ();
//		}
//		else if (f.equals(Feve.FEVE_MOYENNE_BIO_EQUITABLE)) {
//			return this.getPrixSeuilMQBE();
//					
//		}
//		return null;
//	}

//	public Variable getPrixSeuilMQ() {
//		return prixSeuilMQ;
//	}
//
//
//	public void setPrixSeuilMQ(Variable prixSeuilMQ) {
//		this.prixSeuilMQ = prixSeuilMQ;
//	}
//
//
//	public Variable getPrixSeuilHQ() {
//		return prixSeuilHQ;
//	}
//
//
//	public void setPrixSeuilHQ(Variable prixSeuilHQ) {
//		this.prixSeuilHQ = prixSeuilHQ;
//	}
//
//
//	public Variable getPrixSeuilBQ() {
//		return prixSeuilBQ;
//	}
//
//
//	public void setPrixSeuilBQ(Variable prixSeuilBQ) {
//		this.prixSeuilBQ = prixSeuilBQ;
//	}
//
//
//	public Variable getPrixSeuilMQBE() {
//		return prixSeuilMQBE;
//	}
//
//
//	public void setPrixSeuilMQBE(Variable prixSeuilMQBE) {
//		this.prixSeuilMQBE = prixSeuilMQBE;
//	}
//
//
//	public Variable getPrixSeuilHQBE() {
//		return prixSeuilHQBE;
//	}
//
//
//	public void setPrixSeuilHQBE(Variable prixSeuilHQBE) {
//		this.prixSeuilHQBE = prixSeuilHQBE;
//	}


	public void initialiser() {
		
		
	}
	
	public String getNom() {
		return "Opti'Cacao";
	}

	public String getDescription() {
		return "Aux petits soins pour vous";
	}

	public Color getColor() {
		return new Color(230, 126, 34);
	}
	

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	
	public void next() {
		List<String> res = new ArrayList<String>();
		for (IActeur test : Filiere.LA_FILIERE.getActeursSolvables()) {
			res.add(test.getNom());
		}
		
		if (res.contains("BioFour")) {
			
		}
		else {
			this.stockReferenceFeve.enlever(Feve.FEVE_HAUTE, this.stockReferenceFeve.getQuantite(Feve.FEVE_HAUTE)*0.5);
		}
		if (res.contains("EQ3") && res.contains("EQ5")) {
			
		}
		else {
			if(this.comptFaillite<1) {
				this.comptFaillite = 1;
				List<Feve> fevesCibles = new ArrayList<Feve>();
				fevesCibles.add(Feve.FEVE_HAUTE_BIO_EQUITABLE);
				fevesCibles.add(Feve.FEVE_MOYENNE_BIO_EQUITABLE);
				for(Feve f : fevesCibles) {
					System.out.println(this.stockReferenceFeve.getQuantite(f));
					this.stockReferenceFeve.ajouter(f, this.stockReferenceFeve.getQuantite(f)*0.2);
					System.out.println(this.stockReferenceFeve.getQuantite(f));
				}
			}
		}
	}
	
	
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filiere = new ArrayList<String>();
		filiere.add("TESTAOOPTI'CACAO");  
		filiere.add("TESTCCOPTI'CACAO"); 
		return filiere;
	}

	public Filiere getFiliere(String nom) {
		switch (nom) { 
		case "TESTAOOPTI'CACAO" : return new CopieFiliereTestAO();
		case "TESTCCOPTI'CACAO" : return new CopieFiliereTestContratCadre();
	    default : return null;
		}
	}
	
	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
		
	}
	
	public List<Variable> getParametres() { 
		List<Variable> p= new ArrayList<Variable>();
//		p.add(this.expirationChoco);
		return p;
	} 
	

	public abstract Journal getJournalcours();
	public abstract Journal getJournalVente();
	public abstract Journal getJournalStock();
	public abstract Journal getJournalTransfo();
	public abstract Journal getJournalAchat();
	
	public List<Journal> getJournaux() {
		List<Journal> j= new ArrayList<Journal>();
		j.add(this.getJournalcours());
		j.add(this.getJournalVente());
		j.add(this.getJournalStock());
		j.add(this.getJournalTransfo());
		j.add(this.getJournalAchat());
		return j;
	}
	

	public void notificationFaillite(IActeur acteur) {
		if (this==acteur) {
		System.out.println("I'll be back... or not... "+this.getNom());
		} else {
			System.out.println("Allez hop, rentrez chez vous "+acteur.getNom()+". "+this.getNom());
		}
	}
	
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
	}


	public double getMarge() {
		return this.margeAO;
	}


	public int getCryptogramme() {
		return cryptogramme;
	}












	@Override
	public LinkedList<String> getMarquesChocolat() {
//		LinkedList<String> res = new LinkedList<String>();
//		res.add("O'ptella");
////		res.add("O'ptibon");
//		res.add("O'max");
		return this.getMarquesChocolat();
	}


	@Override
	public LinkedList<ChocolatDeMarque> getChocolatsProduits() {
		LinkedList<ChocolatDeMarque> res= new LinkedList<ChocolatDeMarque>();
		ChocolatDeMarque c0=new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		ChocolatDeMarque c1=new ChocolatDeMarque(Chocolat.MQ,this.getMarquesChocolat().get(1));
		ChocolatDeMarque c2=new ChocolatDeMarque(Chocolat.MQ_BE,this.getMarquesChocolat().get(2));
		ChocolatDeMarque c3=new ChocolatDeMarque(Chocolat.HQ,this.getMarquesChocolat().get(3));
		ChocolatDeMarque c4=new ChocolatDeMarque(Chocolat.HQ_BE,this.getMarquesChocolat().get(4));
		res.add(c0);
		res.add(c1);
		res.add(c2);
		res.add(c3);
		res.add(c4);
		return res;
	}


	public Stock<Feve> getStockReferenceFeve() {
		return stockReferenceFeve;
	}


	public Stock<ChocolatDeMarque> getStockReferenceChocolat() {
		return stockReferenceChocolat;
	}

	public double getMargeCC() {
		return margeCC;
	}

}