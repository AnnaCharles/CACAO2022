package abstraction.eq4Transformateur2;

import abstraction.eq8Romu.filiere.Filiere;

public class Transformateur2Achat extends Transformateur2Transfo{
	

	public Transformateur2Achat(double stocktotalfeve, double stocktotalchoco) {
		super(stocktotalfeve, stocktotalchoco);
		// TODO Auto-generated constructor stub
	}


	


	
	public boolean comparer_prix(double prix_vente) {



		


		return prix_vente<super.getPrixSeuil().getValeur(); //On achete pas au dessus du prix seuil

		


	}
//	public double quantite() { 
//		return super.getCapaciteStockageFixe().getValeur()-super.getStockfeve().quantiteStockTotale();
//	}
	
	public boolean deficit(double quantite, double prix_vente) { //False si l'achat nous mettrait en deficit
		return (super.getSolde()-quantite*prix_vente)>0;
	}
	
	public boolean Achat(double prix_vente,double quantite) {//on achete ou pas
		return comparer_prix(prix_vente)&&deficit(quantite,prix_vente);
		
	}

	
}