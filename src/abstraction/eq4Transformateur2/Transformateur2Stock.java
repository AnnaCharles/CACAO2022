package abstraction.eq4Transformateur2;

import java.util.HashMap;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;

//auteur Marie

public class Transformateur2Stock extends Transformateur2ContratCadre {
	
	private Stock<Feve> stockfeve;
	private Stock<Chocolat>  stockchocolat;
	private Stock<ChocolatDeMarque> stockchocolatdemarque;
	
	private double prixstockage;
	
	
	
	public void next() {
		super.next();
		//ON implemente le journal avec des infos sur nos stocks à chaque tour
				if (this.stockfeve.getStock().keySet().size()>0) {
					for (Feve f : this.stockfeve.getStock().keySet()) {
						this.journal.ajouter("stock de feve "+f+" : "+this.stockfeve.getStock().get(f));
					}
				}
				if (this.stockchocolatdemarque.getStock().keySet().size()>0) {
					for (ChocolatDeMarque c : this.stockchocolatdemarque.getStock().keySet()) {
						this.journal.ajouter("stock de chocolat de marque "+c+" : "+this.stockchocolatdemarque.getStock().get(c));
					}
				}
				
				
				
		//On paye le cout de stockage
				Filiere.LA_FILIERE.getBanque().virer(this, super.cryptogramme, Filiere.LA_FILIERE.getBanque(), this.coutStockage());
				journal.ajouter("Le stock nous coûte "+this.coutStockage());
	}
	
	public void initialiser() {
		//double prixstockage=Filiere.LA_FILIERE.getIndicateur("prixstockage").getValeur();
		
		super.initialiser();
	}

	
// Marie	
	public Transformateur2Stock() {
		//LES STOCKS INITIAUX----VALEURS A CHOISIR
		this.stockfeve=new Stock();
		this.stockfeve.ajouter(Feve.FEVE_BASSE, 150000);
		this.stockfeve.ajouter(Feve.FEVE_MOYENNE, 150000);
//		this.stockfevebasseVAR=new Variable("Opti'Cacao STOCKFEVE_BASSE", this, 0.0, 1000000000.0,this.stockfeve.getStock().get(Feve.FEVE_BASSE));
//		this.stockfevemoyVAR=new Variable("Opti'Cacao STOCKFEVE_MOY", this, 0.0, 1000000000.0,this.stockfeve.getStock().get(Feve.FEVE_MOYENNE));
		
		//On se fixe une marque pour un type de chocolat
		ChocolatDeMarque c1=new ChocolatDeMarque(Chocolat.MQ,this.getMarquesChocolat().get(1));
		ChocolatDeMarque c0=new ChocolatDeMarque(Chocolat.BQ,this.getMarquesChocolat().get(0));
		this.stockchocolatdemarque=new Stock();
		this.stockchocolatdemarque.ajouter(c1, 20000);
		this.stockchocolatdemarque.ajouter(c0, 30000);
//		this.stockchocolatdemarqueomaxVAR=new Variable("Opti'Cacao STOCK chocomax", this, 0.0, 1000000000.0,this.stockchocolatdemarque.getStock().get(chocomax));
//		this.stockchocolatdemarqueoptellaVAR=new Variable("Opti'Cacao STOCK optella", this, 0.0, 1000000000.0,this.stockchocolatdemarque.getStock().get(chocoptella));
//		this.stockchocolatdemarqueoriginalVAR=new Variable("Opti'Cacao STOCK chocoriginal", this, 0.0, 1000000000.0,this.stockchocolatdemarque.getStock().get(chocoriginal));
		this.stockchocolat=new Stock();
		this.stockchocolat.ajouter(Chocolat.MQ,30000);
		this.stockchocolat.ajouter(Chocolat.BQ, 20000);
		
		
	}
	
public Stock<Feve> getStockfeve(){
	return this.stockfeve;
}
public Stock<Chocolat> getStockchocolat(){
	return this.stockchocolat;
}
public Stock<ChocolatDeMarque> getStockchocolatdemarque(){
	return this.stockchocolatdemarque;
}

// Gabriel 
//public void setStock(double newst) {
//	this.stocktotalchoco = this.stocktotalchoco - newst;
//}

//Marie
public double coutStockage() {
	return 4*0.01*(this.getStockchocolatdemarque().getStocktotal()+this.getStockfeve().getStocktotal());
}
// Marie 

//public double quantiteStockTotaleFeve(Feve produit) {
//	for(Feve f : stockfeve.getStock().keySet()) {
//		this.stocktotalfeve=this.stocktotalfeve+stockfeve.quantite_stock.get(f);
//	}
//	return this.stocktotalfeve;	
//}
//// Marie
//
//public double quantiteStockTotaleChocolat(Chocolat produit) {
//	for(Chocolat c: stockchocolat.quantite_stock.keySet()) {
//		this.stocktotalchoco=this.stocktotalchoco+stockchocolat.quantite_stock.get(c);
//	}
//	return this.stocktotalchoco;
	
//}	




}

