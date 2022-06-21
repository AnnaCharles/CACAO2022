package abstraction.eq4Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
//auteur Jad
public abstract class Transformateur2Transfo<I> extends Transformateur2Stock {
	
//	protected double rdt=Filiere.LA_FILIERE.getIndicateur("rendement").getValeur();
//	protected double prix_transfo= Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur();
//	protected double prix_ori=Filiere.LA_FILIERE.getIndicateur("coutOriginal").getValeur();
//	protected double cap=Filiere.LA_FILIERE.getIndicateur("seuilTransformation").getValeur();
	
	private Journal journalTransfo;
	private Stock<ChocolatDeMarque> commandes;
	private Stock<ChocolatDeMarque> commandes_step;
	private Stock<ChocolatDeMarque> commandes_retard;

	protected double rdt;
	protected double prix_transfo;
	protected double prix_ori;
	protected double cap;
	
	public abstract void utiliserQuantProd(Object p, double quant);
	public abstract void ajouterQuant(double date, Object op, Double quant); //a faire modif fct de peremption pour mettre obkject à la place de prod, mettre dans achat vente et tout et journal
	public abstract double getQuant(double date, Object op);
	public abstract double quantTotaleProduit(Object op);
	
	
	
	public void next() {//EN V1 on ne transforme que de façon arbitraire
		
		for(ChocolatDeMarque c : this.commandes_step.keySet()) {
			if(this.commandes_step.getQuantite(c)>=0) {
			this.commandes_retard.ajouter(c, this.commandes_step.getQuantite(c));
		}
		}
		
		
		//this.commandes_step= new Stock<ChocolatDeMarque>();
		//this.commandes=new Stock<ChocolatDeMarque>();
		super.next();
		NewCap=cap;
		
		this.commandes=this.GetCommandes(mesContratEnTantQueVendeur);
		this.commandes_step=this.GetCommandes(mesContratEnTantQueVendeur);
		
		//il faut régler les qauntités transformées pour chaque types de fèves
		
		//Les transformations non originales courtes
		
		this.transfo(this.bestCombi("CBQ")*cap, false, "courte",Feve.FEVE_BASSE);
		this.transfo(this.bestCombi("CMQ")*cap,false,"courte",Feve.FEVE_MOYENNE);
		this.transfo(this.bestCombi("CMQBE")*cap,false,"courte",Feve.FEVE_MOYENNE_BIO_EQUITABLE);
		this.transfo(this.bestCombi("CHQ")*cap,false,"courte",Feve.FEVE_HAUTE);
		this.transfo(this.bestCombi("CHHBE")*cap,false,"courte",Feve.FEVE_HAUTE_BIO_EQUITABLE);
		
		//les transformations originales courtes
		this.transfo(this.bestCombi("COBQ")*cap, true, "courte",Feve.FEVE_BASSE);
		this.transfo(this.bestCombi("COMQ")*cap,true,"courte",Feve.FEVE_MOYENNE);
		this.transfo(this.bestCombi("COMQBE")*cap,true,"courte",Feve.FEVE_MOYENNE_BIO_EQUITABLE);
		this.transfo(this.bestCombi("COHQ")*cap,true,"courte",Feve.FEVE_HAUTE);
		this.transfo(this.bestCombi("COHQBE")*cap,true,"courte",Feve.FEVE_HAUTE_BIO_EQUITABLE);
		
		//les transformations originales longues
		this.transfo(this.bestCombi("LOBQ")*cap, true, "longue",Feve.FEVE_BASSE);
		this.transfo(this.bestCombi("LOMQ")*cap,true,"longue",Feve.FEVE_MOYENNE);
		this.transfo(this.bestCombi("LOMQBE")*cap,true,"longue",Feve.FEVE_MOYENNE_BIO_EQUITABLE);
		
		//les transformations non originales longues
		this.transfo(this.bestCombi("LBQ")*cap, false, "longue",Feve.FEVE_BASSE);
		this.transfo(this.bestCombi("LMQ")*cap,false,"longue",Feve.FEVE_MOYENNE);
		this.transfo(this.bestCombi("LMQBE")*cap,false,"longue",Feve.FEVE_MOYENNE_BIO_EQUITABLE);
		
		//Stock<ChocolatDeMarque> s=this.commandes;
		
				
	}
	public void initialiser() {
		super.initialiser();
		cap=Filiere.LA_FILIERE.getIndicateur("seuiTransformation").getValeur();
		rdt=Filiere.LA_FILIERE.getIndicateur("rendement").getValeur();
		prix_transfo=Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur();
		prix_ori=Filiere.LA_FILIERE.getIndicateur("coutOriginal").getValeur();
		
	}
	
	public Transformateur2Transfo() {
		super();
		this.journalTransfo=new Journal("O'ptites Transformations",this);
		this.commandes = new Stock<ChocolatDeMarque>();
		this.commandes_step = new Stock<ChocolatDeMarque>();
		this.commandes_retard= new Stock<ChocolatDeMarque>();
		
	}

	//renvoie une HashMap des chocolats de marques et de la quantité à livrer de ces derniers 
	public Stock<ChocolatDeMarque> GetCommandes(List<ExemplaireContratCadre> CC) {///POUR LA V2
		Stock<ChocolatDeMarque> comm=new Stock<ChocolatDeMarque>();
		for(ExemplaireContratCadre c:CC) {
			if( c.getQuantiteALivrerAuStep()>0) {
			
			comm.ajouter(((ChocolatDeMarque)(c.getProduit())), c.getQuantiteALivrerAuStep());
			
		}
		}
		
		return comm;
		
		
		
	}
	
	
	//trouve la meilleur combinaison (qui minimise les coûts et si possible a une stratégie)
	//de transformation (types de fèves et de tranfos)
	//pour honorer les commandes
	//Commence par remplir less commandes les plus anciennes
	//honnore les commande par date croissante jusqu'à que ce ne soit plus possible (stock ou capacité de production)

	public double bestCombi(String fev) {//POUR LA V2
	//initialisation des variables:
		double cap_stock_retard=(this.commandes_retard.getStocktotal()+this.commandes_retard.getStocktotal());
		double cap_stock=(this.commandes.getStocktotal()+this.commandes.getStocktotal());
		double cap_retard=this.commandes_retard.getStocktotal()/(cap_stock);
		double cap_step=1-cap_retard;
		//Proportions des différents chocolats en retard
		double RBG=this.commandes_retard.getQuantite(this.getChocolatsProduits().get(0))/(cap_stock_retard);
		double RMG=this.commandes_retard.getQuantite(this.getChocolatsProduits().get(1))/(cap_stock_retard);
		double RMGB=this.commandes_retard.getQuantite(this.getChocolatsProduits().get(2))/(cap_stock_retard);
		double RHG=this.commandes_retard.getQuantite(this.getChocolatsProduits().get(3))/(cap_stock_retard);
		double RHGB=this.commandes_retard.getQuantite(this.getChocolatsProduits().get(4))/(cap_stock_retard);
		
		//proportions des différents chocolats du step
		double SBG= this.commandes.getQuantite(this.getChocolatsProduits().get(0))/cap_stock;
		double SMG=this.commandes.getQuantite(this.getChocolatsProduits().get(1))/cap_stock;
		double SMGB=this.commandes.getQuantite(this.getChocolatsProduits().get(2))/cap_stock;
		double SHG=this.commandes.getQuantite(this.getChocolatsProduits().get(3))/cap_stock;
		double SHGB=this.commandes.getQuantite(this.getChocolatsProduits().get(4))/cap_stock;
		
		
		//Variables de quantité (%) :
		//transformations courtes originales
		double COBQ=0.00;
		double COMQ=0.00;
		double COMQBE=0.00;
		double COHQ=0.00;
		double COHQBE=0.00;
		//transformations courtes non originales

		double CBQ=0.00;
		double CMQ=0.00;
		double CMQBE=0.00;
		double CHQ=0.00;
		double CHQBE=0.00;

		//transformations longues originales
		double LOBQ=0.00;
		double LOMQ=0.00;
		double LOMQBE=0.00;
		//transformations longues non originales
		double LBQ=0.00;
		double LMQ=0.00;
		double LMQBE=0.00;
		
		//Calcul des quantité (%):
		CBQ=RBG*cap_retard+SBG*cap_step;
		CMQ=0.8*(RMG*cap_retard+SMG*cap_step);
		LBQ=0.2*(RMG*cap_retard+SMG*cap_step);
		CMQBE=(RMGB*cap_retard+SMGB*cap_step);
		CHQ=0.8*(RHG*cap_retard+SHG*cap_step);
		LMQ=0.2*(RHG*cap_retard+SHG*cap_step);
		CHQBE=0.8*(RHGB*cap_retard+SHGB*cap_step);
		LMQBE=0.2*(RHGB*cap_retard+SHGB*cap_step);
		
		
		///
		
		
		//mise à jour des commandes en retard
		
		//Dans la fonction transfo 
		
		////
		
		//retour des valeurs
		if(fev.equals("COBQ")) {
			if(COBQ>0) {
			return COBQ;}
		}
		if(fev.equals("COMQ")) {
			if(COMQ>0) {
			return COMQ;}
		}if(fev.equals("COMQBE")) {
			if(COMQBE>0) {
			return COMQBE;
			}
		}if(fev.equals("COHQ")) {
			if(COHQ>0) {
			return COHQ;}
			
		}if(fev.equals("COHQBE")) {
			if(COHQBE>0) {
			return COHQBE;}
			
		}if(fev.equals("CBQ")) {
			if(CBQ>0) {
			return CBQ;}
		}if(fev.equals("CMQ")) {
			if(CMQ>0) {
			return CMQ;}
		}if(fev.equals("CMQBE")) {
			if(CMQBE>0) {
			return CMQBE;}
		}if(fev.equals("CHQ")) {
			if(CHQ>0) {
			return CHQ;}
		}if(fev.equals("CHQBE")) {
			if(CHQBE>0) {
			return CHQBE;}
		}if(fev.equals("LOMQ")) {
			if(LOMQ>0) {
			return LOMQ;}
		}if(fev.equals("LOMQBE")) {
			if(LOMQBE>0) {
			return LOMQBE;}
		}if(fev.equals("LBQ")) {
			if(LBQ>0) {
			return LBQ;}
		}if(fev.equals("LMQ")) {
			if(LMQ>0) {
			return LMQ;}
		}if(fev.equals("LMQBE")) {
			if(LMQBE>0) {
			return LMQBE;}
		}
		
			return 0.00000000000000000000000001;
				
	}
	
	//Dans cette seconde version la transformation ne fonctionne plus en mode tout/rien mais fait le maximum possible
	public void transfo(double qt,boolean ori, String trans,Feve f){//qt est la quantité de CHOCOLAT voulue
		//Vérifie quel type de transformation
		//Vérifie la capacité bancaire
		//Vérifie le stock de fèves
		//vérifie capacité de production
		//Baisse le stock de fèves de qt
		//Augmente le stock de chocolat de rdt*qt (et modifie la qualité si longue)
		//Ajoute ou non l'originalité
		//Débite le compte bancaire
		Gamme g=f.getGamme();
		double s=0;
		if(ori) {//indicateur d'originalité
			s=1;
		}
		
		if (trans.equals("longue")){//dans le cas d'une transformation longue
			double max_feves=Math.min(this.getStockfeve().getQuantite(f), (1/rdt)*qt);//Récupération du max de fèves que l'on peut utilisé
			//double max_stock=Math.min(this.getNotreCapaciteStockage()/2-this.getStockchocolatdemarque().getStocktotal(), max_feves);
			double max_prod=Math.min(max_feves, NewCap);//Choix du maximum qui peut être transformé (soit la capacité de production restante soit le nombre de fève dispo)

			
			if(max_prod>0) {
				if(Filiere.LA_FILIERE.getBanque().verifierCapacitePaiement(this, this.cryptogramme, max_prod*(prix_transfo+prix_ori*s))) {//(1/rdt)*qt est la quantité de fève nécessaire pour obtenir qt de chocolat
							
							NewCap-=(1/rdt)*qt;//mise à jour de la capacité de production
							
							this.getStockfeve().enlever(f,max_prod);//baisse le stock de feves	
							this.getStockchocolatdemarque().ajouter(this.fevechocoplus(f), max_prod);//augmente le stock de chocolat de marque
							this.getTransfo_feve().ajouter(f,max_prod);//ajoute le nombre de fèves utilisées (pour péremption)
							this.getTransfo_choco().ajouter(this.fevechocoplus(f),max_prod);//ajoute le nombre de chocolat produits (pour péremption)
							this.ajouterQuant(0, this.fevechocoplus(f), max_prod);
							Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), max_prod*(prix_transfo+s*prix_ori*s));//paye
							journalTransfo.ajouter("Transformation longue de " +max_prod+" kg de "+f+" en "+qt+"kg de"+this.fevechoco(f).toString()+ " pour "+max_prod*(prix_transfo+prix_ori*s)+"€");
							//mise à jour des commandes en retard
							if(this.commandes_step.keySet().contains(fevechocoplus(f))) {
								this.commandes_step.enlever(this.fevechocoplus(f), max_prod);
								}
								if(this.commandes_retard.keySet().contains(fevechocoplus(f))) {
								if(this.commandes_retard.get(this.fevechocoplus(f))>0) {
									this.commandes_retard.enlever(fevechocoplus(f), max_prod);
								}
								}
							
							
					}	
				else if(this.getMaxPayable(trans, ori)>0 && max_prod>0) {//On récupère le maximum qu'on puisse payer
					double max_paybale=this.getMaxPayable(trans, ori);
					NewCap-=max_paybale;//mise à jour de la capacité de production
					this.getStockfeve().enlever(f,max_paybale);//baisse le stock de feves
					
					this.getStockchocolatdemarque().ajouter(this.fevechocoplus(f), max_paybale);//augmente le stock de chocolat
					this.getTransfo_feve().ajouter(f,max_paybale);//ajoute le nombre de fèves utilisées (pour péremption)
					this.getTransfo_choco().ajouter(this.fevechocoplus(f),max_paybale);//ajoute le nombre de chocolat produits (pour péremption)
					journalTransfo.ajouter("Transformation Courte de " +max_paybale+" kg de "+f+" en "+this.fevechoco(f).toString()+ " pour "+max_prod*(prix_transfo+prix_ori*s)+"€");
					Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), max_paybale*(prix_transfo+s*prix_ori*s));//paye
					
					//mise à jour du journal de retard
					if(this.commandes_step.keySet().contains(fevechocoplus(f))) {
						this.commandes_step.enlever(this.fevechocoplus(f), max_prod);
						}
						if(this.commandes_retard.keySet().contains(fevechocoplus(f))) {
						if(this.commandes_retard.get(this.fevechocoplus(f))>0) {
							this.commandes_retard.enlever(fevechocoplus(f), max_prod);
						}
						}

				}
				}
				
		}
		if (trans.equals("courte")) {

			double max_feves=Math.min(this.getStockfeve().getQuantite(f), qt);//Récupération du max de fèves que l'on peut utilisé
			//double max_stock=Math.min(this.getNotreCapaciteStockage()/2-this.getStockchocolatdemarque().getStocktotal(), max_feves);
			double max_prod=Math.min(max_feves, NewCap);//Choix du maximum qui peut être transformé (soit la capacité de production restante soit le nombre de fève dispo)
				
			if(max_prod>0 ) {
					
				if(Filiere.LA_FILIERE.getBanque().verifierCapacitePaiement(this, this.cryptogramme, max_prod*(prix_transfo+prix_ori*s))) {//max prod est le maximum capable d'être produit pour lequel on peut payer
						NewCap-=max_prod;//mise à jour de la capacité de production
						this.getStockfeve().enlever(f,max_prod);//baisse le stock de feves
						this.getStockchocolatdemarque().ajouter(this.fevechoco(f), max_prod);//augmente le stock de chocolat
						this.getTransfo_feve().ajouter(f,max_prod);//ajoute le nombre de fèves utilisées (pour péremption)
						this.getTransfo_choco().ajouter(this.fevechoco(f),max_prod);//ajoute le nombre de chocolat produits (pour péremption)
						journalTransfo.ajouter("Transformation Courte de " +max_prod+" kg de "+f+" en "+this.fevechoco(f).toString()+ " pour "+max_prod*(prix_transfo+prix_ori*s)+"€");
						Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), max_prod*(prix_transfo+s*prix_ori*s));//paye
						
						//mise à jour du journal de retard
						if(this.commandes_step.keySet().contains(fevechoco(f))) {
						this.commandes_step.enlever(this.fevechoco(f), max_prod);
						}
						if(this.commandes_retard.keySet().contains(fevechoco(f))) {
						if(this.commandes_retard.get(this.fevechoco(f))>0) {
							this.commandes_retard.enlever(fevechoco(f), max_prod);
						}
						}

					}
				else if(this.getMaxPayable(trans, ori)>0) {//On récupère le maximum qu'on puisse payer
					double max_paybale=this.getMaxPayable(trans, ori);
					NewCap-=max_paybale;//mise à jour de la capacité de production
					this.getStockfeve().enlever(f,max_paybale);//baisse le stock de feves
					this.getStockchocolatdemarque().ajouter(this.fevechoco(f), max_paybale);//augmente le stock de chocolat
					this.getTransfo_feve().ajouter(f,max_paybale);//ajoute le nombre de fèves utilisées (pour péremption)
					this.getTransfo_choco().ajouter(this.fevechoco(f),max_paybale);//ajoute le nombre de chocolat produits (pour péremption)
					journalTransfo.ajouter("Transformation Courte de " +max_paybale+" kg de "+f+" en "+this.fevechoco(f).toString()+ " pour "+max_prod*(prix_transfo+prix_ori*s)+"€");
					Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), max_paybale*(prix_transfo+s*prix_ori*s));//paye
				
					//mise à jour du journal de retard
					if(this.commandes_step.keySet().contains(fevechoco(f))) {
						this.commandes_step.enlever(this.fevechoco(f), max_prod);
						}
						if(this.commandes_retard.keySet().contains(fevechoco(f))) {
						if(this.commandes_retard.get(this.fevechoco(f))>0) {
							this.commandes_retard.enlever(fevechoco(f), max_prod);
						}
						}
					
				}
			}
					
					
	}
		}
		
	
	//est utile pour les transfor courte : renvoie le chocolat de marque correspondant à la qualité des fèves
	public ChocolatDeMarque fevechoco(Feve f) {
		if(f.getGamme().equals(Gamme.BASSE)) {
			return new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		} else if (f.getGamme().equals(Gamme.MOYENNE) && !f.isBioEquitable()){
			return new ChocolatDeMarque(Chocolat.MQ,getMarquesChocolat().get(1));
		}
		else if(f.getGamme().equals(Gamme.MOYENNE) && f.isBioEquitable()) {
			return new ChocolatDeMarque(Chocolat.MQ_BE,getMarquesChocolat().get(2));
		}
		else if(f.getGamme().equals(Gamme.HAUTE) && !f.isBioEquitable()) {
			return new ChocolatDeMarque(Chocolat.HQ,getMarquesChocolat().get(3));
		}
		else if(f.getGamme().equals(Gamme.HAUTE) && f.isBioEquitable()) {
			return new ChocolatDeMarque(Chocolat.HQ_BE,getMarquesChocolat().get(4));
		}
		return null;
	}
	
	
	//est utile pour la transfo longue : renvoie le chocolat de marque de qualité supérieur à la fève
	public ChocolatDeMarque fevechocoplus(Feve f) {
		if(f.getGamme().equals(Gamme.BASSE)) {
			return new ChocolatDeMarque(Chocolat.MQ,getMarquesChocolat().get(1));
		} else if (f.getGamme().equals(Gamme.MOYENNE) && !f.isBioEquitable()){
			return new ChocolatDeMarque(Chocolat.HQ,getMarquesChocolat().get(3));			
		}
		else if (f.getGamme().equals(Gamme.MOYENNE) && f.isBioEquitable()) {
			return new ChocolatDeMarque(Chocolat.HQ_BE,getMarquesChocolat().get(4));
		}
		return null;
	}

	public Journal getJournalTransfo() {
		return journalTransfo;
	}

	
	
	
	public double getMaxPayable(String trans,boolean b) {
		double s=0;
		if(b) {
			s=1;
		}
		if(trans.equals("court")) {
			return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme)/(prix_transfo+s*prix_ori);
		}
		else if (trans.equals("longue")) {
			return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme)/((prix_transfo+s*prix_ori)*rdt);
		}
		return 0.0;

	}
		
		
}
	


	
	


