package abstraction.eq4Transformateur2;

import abstraction.eq8Romu.filiere.Filiere;
import java.util.*;

//Jad


public class Utilitaire<I> {
	

	private HashMap<I,LinkedList<Double> > uti;
	
	
	public Utilitaire(HashMap<I, LinkedList<Double>> uti) {
		this.uti = uti;
	}
	
	public Utilitaire() {
		this.uti=new HashMap<I,LinkedList<Double>>();
	}
	
	
	
	//quantité d'un certain step
		public double getQuantiteAuStep(I produit, int n) {
			if (this.uti.keySet().contains(produit)) {
				return this.uti.get(produit).get(n);
			}else {
				return 0;
			}
		}
	
	
	
	//Faire la somme  de toutes les values depuis une certaine étape à l'étape +13
	public double getQuantUtiliseeDepuis(I produit,int n) {
		double somme=0;
		if (this.uti.keySet().contains(produit)) {
			for(int i=0; i<Filiere.LA_FILIERE.getIndicateur("dureePeremption").getValeur()-1;i++) {
				somme+=this.uti.get(produit).get(n+i);
		}
		}
		return somme;
	}
	

	
	//ajoute par défaut à l'étape en cours
	public void ajouter (I produit, double qt) {
		if (qt>0) {	
			
			if (this.uti.keySet().contains(produit)) {
				if (this.uti.get(produit).size()<Filiere.LA_FILIERE.getEtape())
				this.uti.get(produit).add(qt);; //
	}
				else if(this.uti.get(produit).size()==Filiere.LA_FILIERE.getEtape()) {
					this.uti.get(produit).set(this.uti.get(produit).size()-1, this.uti.get(produit).get(this.uti.get(produit).size()-1)+qt);
				}
			else{
		throw new IllegalArgumentException("impossible");
	}
	}
	}
	
	
	public void intro(I produit,double qt) {
		LinkedList<Double> l=new LinkedList<Double>();
		l.add(qt);
		this.uti.put(produit, l);
	}

		

	

	




	public void clear() {
		this.clear();
	}


	public LinkedList<Double> get(I  prod) {
		return this.uti.get(prod);
	}
	
	public Set<I> keySet() {
		return this.uti.keySet();
	}
	/*public double coutStockage( Filiere.LA_FILIERE.getIndicateur("prix_stockage")) { // demander comment ajouter variables
		return (this.quantiteStockTotale(produit)*(Filiere.LA_FILIERE.getIndicateur("prix_stockage").getValeur())); // demander a Alexandre comment calculer prix 
	}*/



//	public void setQuantite_stock(HashMap<I,Double> quantite_stock) {
//		this.quantite_stock = quantite_stock;
//	}

	
}

