package abstraction.eq5Transformateur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.filiere.IMarqueChocolat;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.general.VariableReadOnly;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;

public class Transformateur3Acteur implements IActeur, IMarqueChocolat, IFabricantChocolatDeMarque{
	
	protected int cryptogramme;
	protected Journal achats;
	protected Journal ventes;
	protected Journal transformation;
	
	//Karla

	protected Double SeuilMinFeves; // en kg : En dessous de ce seuil, on achète des fèves car stock trop "bas"
	protected Double SeuilMinChocolat; // en kg : Au dessus de ce seuil, on vend du chocolat car stock trop "haut"
	protected Double achatMaxFeves; // en kg, quantité de fèves max qu'on peut acheter en 1 tour
	protected Double capaciteStockageEQ5; // en kg, quantité de fèves max qu'on peut acheter en 1 tour
	
	protected HashMap<Feve, Double> besoinFeves; //Quantité nécéssaire pour satisfaire le besoin des cc en cours pour chaque feve
	protected HashMap<Feve, Double> dispoFeves; //Quantité de fèves disponible au tour courant pour grace au cc en cours et aux stocks

	

	//Paramètres
	protected Variable seuilTransformation;
	protected Variable limiteStockage; // quantité maximale que l'on peut stocker
	protected Variable prixEntrepot; // prix d'un entrepot qui a une capacite de stockage de limiteStockage
	protected Variable dureePeremption; // nombre de tour avant qu'un produit soit périmé
	protected Variable rendement;
	protected Variable coutTransformation;
	protected Variable coutOriginal;
	protected Stock<Feve> stockFeves;
	protected Stock<Chocolat> stockChocolat;
	protected DicoPeremption<Feve> perempFeves;
	protected DicoPeremption<Chocolat> perempChocolat;
	
	
	// Stock Moyenne et haut de Gamme en variables BE et non BE
	protected Variable stockFevesVariableM;
	protected Variable stockFevesVariableH;
	protected Variable stockChocolatVariableM;
	protected Variable stockChocolatVariableH;
	
	//karla
	protected LinkedList<ExemplaireContratCadre> contratsEnCoursVente;
	protected LinkedList<ExemplaireContratCadre> contratsEnCoursAchat;

	
	
	//Karla
	public Transformateur3Acteur() {
		this.seuilTransformation = new VariableReadOnly ("seuiTransformation", "seuil de transformation par etape en kg", this,  0, 250000000, 100000000);
		this.rendement = new VariableReadOnly ("rendement", "rendement de la transformation longue", this,  0, 0.99, 0.7);
		this.coutTransformation = new VariableReadOnly ("coutTransformation", "cout de transformation en milliers de dollars par etape par kg", this,  0, 1000, 1);
		this.coutOriginal = new VariableReadOnly ("coutOriginal", "cout supplementaire pour un produire un chocolat orginal en milliers de dollars par etape par kg", this, 0, 100, 1);
		this.limiteStockage = new VariableReadOnly ("limiteStockage", "quantité maximale que l'on peut stocker", this, 0, 1000000000, 250000000);
		this.prixEntrepot = new VariableReadOnly ("prixEntrepot", "prix d'un entrepot qui a une capacite de stockage de limiteStockage", this, 0, 100000000, 9000000);
		this.dureePeremption = new VariableReadOnly ("dureePeremption", "nombre de tour avant qu'un produit soit périmé", this, 0, 50, 12);

		this.contratsEnCoursVente = new LinkedList <ExemplaireContratCadre>();
		this.contratsEnCoursAchat = new LinkedList <ExemplaireContratCadre>();

		this.capaciteStockageEQ5 = this.limiteStockage.getValeur();
		this.stockFeves = new Stock<Feve> ();
		this.stockChocolat = new Stock<Chocolat> ();
		this.perempChocolat=new DicoPeremption<Chocolat>();
		this.perempFeves=new DicoPeremption<Feve>() ;

		this.SeuilMinFeves = 100000.00;
		this.SeuilMinChocolat = 500.00;
		this.achatMaxFeves = 500.00;
		this.achats= new Journal ("Achats", this);
		this.ventes= new Journal ("Ventes", this);
		this.transformation= new Journal ("Transfo", this);
		
		this.besoinFeves = new HashMap <Feve, Double>();
		this.dispoFeves = new HashMap <Feve, Double>();


		
		Double s = 1000000.00;
		this.stockFeves.ajouter(Feve.FEVE_MOYENNE_BIO_EQUITABLE, s);
		this.stockFeves.ajouter(Feve.FEVE_HAUTE_BIO_EQUITABLE, s);
		this.stockFeves.ajouter(Feve.FEVE_MOYENNE, s);
		this.stockFeves.ajouter(Feve.FEVE_HAUTE, s);
		
		this.stockChocolat.ajouter(Chocolat.MQ_BE, s);
		this.stockChocolat.ajouter(Chocolat.MQ_BE_O, s);
		this.stockChocolat.ajouter(Chocolat.HQ_BE, s);
		this.stockChocolat.ajouter(Chocolat.HQ_BE_O, s);
		this.stockChocolat.ajouter(Chocolat.MQ, s);
		this.stockChocolat.ajouter(Chocolat.MQ_O, s);
		this.stockChocolat.ajouter(Chocolat.HQ, s);
		this.stockChocolat.ajouter(Chocolat.HQ_O, s);
		
		this.perempFeves.ajouterQtt(this.dureePeremption.getValeur(),Feve.FEVE_MOYENNE_BIO_EQUITABLE,s);
		this.perempFeves.ajouterQtt(this.dureePeremption.getValeur(),Feve.FEVE_HAUTE_BIO_EQUITABLE, s);
		this.perempFeves.ajouterQtt(this.dureePeremption.getValeur(),Feve.FEVE_MOYENNE,s);
		this.perempFeves.ajouterQtt(this.dureePeremption.getValeur(),Feve.FEVE_HAUTE, s);
		
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.MQ_BE, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.MQ_BE_O, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.HQ_BE, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.HQ_BE_O, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.MQ, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.MQ_O, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.HQ, s);
		this.perempChocolat.ajouterQtt(this.dureePeremption.getValeur(),Chocolat.HQ_O, s);
		
		this.stockFevesVariableM = new Variable(this.getNom()+"stockFevesMoyennes", "stock de feves moyenne qualite BE + non BE", this,  0, 100000000, this.stockFeves.getstock(Feve.FEVE_MOYENNE_BIO_EQUITABLE) + this.stockFeves.getstock(Feve.FEVE_MOYENNE) );
		this.stockFevesVariableH = new Variable(this.getNom()+"stockFevesHautes", "stock de feves haute qualite BE + non BE",this,  0, 100000000, this.stockFeves.getstock(Feve.FEVE_HAUTE_BIO_EQUITABLE) + this.stockFeves.getstock(Feve.FEVE_HAUTE) );
		this.stockChocolatVariableM = new Variable(this.getNom()+"stockChocolatMoyen", "stock de chocolat moyenne qualite",this,  0, 100000000, this.stockChocolat.getstock(Chocolat.MQ_BE)+this.stockChocolat.getstock(Chocolat.MQ_BE_O)+this.stockChocolat.getstock(Chocolat.MQ)+this.stockChocolat.getstock(Chocolat.MQ_O)  );
		this.stockChocolatVariableH = new Variable(this.getNom()+"stockChocolatHaut", "stock de chocolat huate qualite ",this,  0, 100000000, this.stockChocolat.getstock(Chocolat.HQ_BE)+this.stockChocolat.getstock(Chocolat.HQ_BE_O)+this.stockChocolat.getstock(Chocolat.HQ)+this.stockChocolat.getstock(Chocolat.HQ_O)  );

	}

	//julien
	public String getNom() {
		return "EQ5";
	}

	//julien
	public String getDescription() {
		return "Nous sommes BIO'riginal. Venez goûter notre bon chocolat";
	}

	public Color getColor() {
		return new Color(231, 76, 60);
	}


	public void initialiser() {
		//Initialiser le dictionnaire du besoin en feve
		for (Feve f : this.stockFeves.getProduitsEnStock()) {
			this.besoinFeves.put(f, 0.00);
			this.dispoFeves.put(f, this.stockFeves.getstock(f));
			
		}
		//this.capaciteStockageEQ5=this.capaciteStockageEQ5*2;
		//Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ5"), this.cryptogramme, Filiere.LA_FILIERE.getActeur("EQ8"), this.prixEntrepot.getValeur()*1);
		
	}

	

	
	// Renvoie la liste des filières proposées par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("TEST_EQ5_CC");
		filieres.add("TEST_EQ5_sans_nuls");
		return(filieres);
	}

	// Renvoie une instance d'une filière d'après son nom
	public Filiere getFiliere(String nom) {
		
		switch (nom) { 
		case "TEST_EQ5_CC" : return new FiliereTestContratCadre_5();
		case "TEST_EQ5_sans_nuls" : return new FiliereParDefaut_5();
		
	    default : return null;
		}
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.add(stockFevesVariableM);
		res.add(stockFevesVariableH);
		res.add(stockChocolatVariableM);
		res.add(stockChocolatVariableH);
		res.add(rendement);
		res.add(seuilTransformation);
		res.add(coutOriginal);
		res.add(coutTransformation);
		res.add(dureePeremption);
		return res;
	}

	// Renvoie les paramètres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(limiteStockage);
		res.add(prixEntrepot);
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(achats);
		res.add(ventes);
		res.add(transformation);
		return res;
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
		
	}

	public void notificationFaillite(IActeur acteur) {
	}

	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}
	
	// julien 10/05
	public double prixStockage() {
		double qttTotale = 0;    // on gère le chocolat et les fèves de la même manière pour les prix de stockage -> poids
		qttTotale+= this.stockChocolat.getstocktotal()+this.stockFeves.getstocktotal() ;
		return qttTotale*4*(Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()) ; 
	}
	
	//Karla
	public void next() {
		this.achats.ajouter("Etape = "+Filiere.LA_FILIERE.getEtape());
		this.ventes.ajouter("Etape = "+Filiere.LA_FILIERE.getEtape());
		this.transformation.ajouter("Etape = "+Filiere.LA_FILIERE.getEtape());
		this.achats.ajouter("taille perempChoco"+this.perempChocolat.getDateProd().size());
		this.achats.ajouter("taille perempFeve"+this.perempFeves.getDateProd().size());
		this.achats.ajouter("nb CCA"+this.contratsEnCoursAchat.size());
		this.achats.ajouter("nb CCV"+this.contratsEnCoursVente.size());
		double montant= prixStockage();
		if (montant >0) {
		 Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ5"), this.cryptogramme, Filiere.LA_FILIERE.getActeur("EQ8"), montant);
		}
		
		/* Maj des besoins pour le tour courant */
		for (ExemplaireContratCadre contrat : this.contratsEnCoursVente) {
			Gamme gamme = ((ChocolatDeMarque)contrat.getProduit()).getGamme();
			boolean isBioequitable = ((ChocolatDeMarque)contrat.getProduit()).isBioEquitable();
			for (Feve f : this.besoinFeves.keySet()) {
				if (f.getGamme() == gamme && f.isBioEquitable() == isBioequitable) {
					this.besoinFeves.put(f, contrat.getQuantiteALivrerAuStep()+this.besoinFeves.get(f));	
				}
			}
		}
		
		/* Maj des disponibilités pour le tour courant */
		for (ExemplaireContratCadre contrat : this.contratsEnCoursAchat) {
			Gamme gamme = ((Feve)contrat.getProduit()).getGamme();
			boolean isBioequitable = ((Feve)contrat.getProduit()).isBioEquitable();
			for (Feve f : this.dispoFeves.keySet()) {
				this.dispoFeves.put(f, this.stockFeves.getstock(f));
				if (f.getGamme() == gamme && f.isBioEquitable() == isBioequitable) {
					this.dispoFeves.put(f, this.dispoFeves.get(f)+contrat.getQuantiteALivrerAuStep());	
				}
			}
		}
		
		this.perempChocolat.perime(Filiere.LA_FILIERE.getEtape());
		this.perempFeves.perime(Filiere.LA_FILIERE.getEtape());
		
		//System.out.println(this.contratsEnCoursAchat.size());
		//System.out.println(this.contratsEnCoursVente.size());

	}

	//juju & Yves
	public List<String> getMarquesChocolat() {
			List<String> marques=new ArrayList<String>();
			marques.add("BIO'riginal");
			marques.add("CHOCO'riginal");
			return marques;
		}

	//juju & Yves
	public List<ChocolatDeMarque> getChocolatsProduits() {
		List<ChocolatDeMarque> cm=new ArrayList<ChocolatDeMarque>();
		cm.add(new ChocolatDeMarque(Chocolat.HQ_BE_O,"BIO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.HQ_BE,"BIO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.MQ_BE_O,"BIO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.MQ_BE,"BIO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.HQ_O,"CHOCO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.HQ,"CHOCO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.MQ_O,"CHOCO'riginal"));
		cm.add(new ChocolatDeMarque(Chocolat.MQ,"CHOCO'riginal"));
		return cm;
	}
	
	//julien fction générale qui met à jour les stocks et les peremptions
	// on a besoin d'une date qui correspond au step auquel le produit sera détruit car périmé
	public void ajouter(double date,Object p,double qtt){
		if (p instanceof Feve) {
			this.perempFeves.ajouterQtt(date,(Feve) p,qtt);
			this.stockFeves.ajouter((Feve) p, qtt);
		}
		if (p instanceof Chocolat) {
			this.perempChocolat.ajouterQtt(date,(Chocolat) p,qtt);
			this.stockChocolat.ajouter((Chocolat) p, qtt);	
		}
	}
	
	//julien fction générale qui met à jour les stocks et les peremptions
	public void utiliser(Object p,double qtt){
		if (p instanceof Feve) {
			this.perempFeves.utiliserQtt((Feve) p,qtt);
			this.stockFeves.utiliser((Feve) p, qtt);
		}
		if (p instanceof Chocolat) {
			this.perempChocolat.utiliserQtt((Chocolat) p,qtt);
			this.stockChocolat.utiliser((Chocolat) p, qtt);
			}
	}
}

