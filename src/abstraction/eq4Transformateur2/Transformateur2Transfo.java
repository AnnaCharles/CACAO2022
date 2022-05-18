package abstraction.eq4Transformateur2;

import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;
//auteur Jad
public abstract class Transformateur2Transfo extends Transformateur2Stock {
	
//	protected double rdt=Filiere.LA_FILIERE.getIndicateur("rendement").getValeur();
//	protected double prix_transfo= Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur();
//	protected double prix_ori=Filiere.LA_FILIERE.getIndicateur("coutOriginal").getValeur();
//	protected double cap=Filiere.LA_FILIERE.getIndicateur("seuilTransformation").getValeur();
	
	
	protected double rdt;
	protected double prix_transfo;
	protected double prix_ori;
	protected double cap;
	
	
	
	public void next() {//EN V1 on ne transforme que de façon arbitraire
		super.next();
		NewCap=cap;
		this.transfo(0.6*cap, false, "courte",Feve.FEVE_BASSE);
		this.transfo(0.4*cap,false,"courte",Feve.FEVE_MOYENNE);
				
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
		
		
	}

	
	public void GetCommandes() {///POUR LA V2
		int current =Filiere.LA_FILIERE.getEtape();
		int previous =Filiere.LA_FILIERE.getEtape()-1;
		int comming =Filiere.LA_FILIERE.getEtape()+1;
		//List commandes=new List<()>;
		//List HistoCommandes="".getCommandes();
		//for (javatuples c in commandes){
		//if(c.getValue3()){
		//commandes.add(c);
		//}
		//}
		//return(commandes)
		
		
		
	}
	
	public void bestCombi() {//POUR LA V2
	//trouve la meilleur combinaison (qui minimise les coûts et si possible a une stratégie)
	//de transformation (types de fèves et de tranfos)
	//pour honorer les commandes
	//Commence par remplir less commandes les plus anciennes
	//honnore les commande par date croissante jusqu'à que ce ne soit plus possible (stock ou capacité de production)
		
		
	}
	
	
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
				if(Filiere.LA_FILIERE.getBanque().verifierCapacitePaiement(this, this.cryptogramme, (1/rdt)*qt*(prix_transfo+prix_ori))) {//(1/rdt)*qt est la quantité de fève nécessaire pour obtenir qt de chocolat
					if(super.getStockfeve().getStocktotal()>(1/rdt)*qt) {//s'il y a assez de fèves
						if(NewCap>cap) {//assez de capacité de stockage
							NewCap-=(1/rdt)*qt;//mise à jour de la capacité de production
							this.getStockfeve().enlever(f,(1/rdt)*qt);//baisse le stock de feves
							if(g.equals(Gamme.BASSE)) {
							this.getStockchocolat().ajouter(Chocolat.get(Gamme.MOYENNE, f.isBioEquitable(),  ori), qt);//augmente le stock de chocolat
							}
							else if (g.equals(Gamme.MOYENNE)) {
								this.getStockchocolat().ajouter(Chocolat.get(Gamme.HAUTE, f.isBioEquitable(),  ori), qt);//augmente le stock de chocolat
							Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), (1/rdt)*qt*(prix_transfo+s*prix_ori));//paye
							}
						}
					}
				}
		}
		if (trans.equals("courte")) {
			if(Filiere.LA_FILIERE.getBanque().verifierCapacitePaiement(this, this.cryptogramme, qt*(prix_transfo+prix_ori))) {//qt est la quantité de fève nécessaire pour obtenir qt de chocolat
				if(this.getStockfeve().getStocktotal()>qt) {//s'il y a assez de fèves
					if(NewCap>=qt) {//assez de capacité de production
						NewCap-=qt;//mise à jour de la capacité de production
						this.getStockfeve().enlever(f,qt);//baisse le stock de feves
						this.getStockchocolatdemarque().ajouter(this.fevechoco(f), qt);//augmente le stock de chocolat
						this.journal.ajouter("Transformation Courte de " +qt+" kg de "+f+"en "+this.fevechoco(f).toString());
						Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), qt*(prix_transfo+s*prix_ori));//paye
					}
				}
			}
	}
		}
		
	public ChocolatDeMarque fevechoco(Feve f) {
		if(f.getGamme().equals(Gamme.BASSE)) {
			return new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		} else {

			return new ChocolatDeMarque(Chocolat.MQ,super.getMarquesChocolat().get(1));
		}
	}
	
	
		
		
}
	


	
	


