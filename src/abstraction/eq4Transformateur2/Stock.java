package abstraction.eq4Transformateur2;

import java.util.HashMap;
import java.util.Set;

import abstraction.eq8Romu.filiere.Filiere;


public class Stock<I> {
	

	private HashMap<I,Double> stock;
	
// Nawfel
	public Stock(HashMap<I, Double> stock) {
		this.stock = stock;
	}
	
// Nawfel
	public Stock() {
		this.stock=new HashMap<I,Double>();
	}
	
	
	
// Marie
	// Pour un produit donné, renvoie la quantité contenu dans le stock si nous avons le produit, 0 autrement.
	public double getQuantite(I produit) {
		if (this.stock.keySet().contains(produit)) {
			return this.stock.get(produit);
		}
		else {
			return 0;
		}
	}
	
	
// Nawfel
// Renvoie le quantité total contenu dans le stock/
	public double getStocktotal() {
		double somme=0;
		for(Double qt :this.stock.values()) {
			somme=somme+qt;
		}
		return somme;
	}
	
// Marie
	public void ajouter (I produit, double qt) {
		if (qt>0) {	// L'ajout n'est possible que pour une quantité positive

			if (this.stock.keySet().contains(produit)) { 
				this.stock.put(produit, this.stock.get(produit)+qt);
				// Si nous avons déjà un stock du produit, la nouveau stock est la somme de qt et du stock initial
			}
			else {
				this.stock.put(produit, qt);} // Si on ne stockons pas le produit, ajout à la HashMap du stock
		}else{
			throw new IllegalArgumentException("impossible"); // Quantité négative impossible
		}
	}
// Marie	
	public void enlever (I produit, double qt) {
	if (qt>0) {	// Le retrait n'est possible que pour une quantité positive
		if (this.stock.keySet().contains(produit) ) {
			this.stock.put(produit, this.stock.get(produit)-qt);
			// Si nous avons du stock du produit, la nouveau stock est la différence du stock initial et de qt
	}}
	else{
		throw new IllegalArgumentException("impossible"); // Quantité négative impossible
}
}
		

// Marie
	public double stockRestant(I produit,double notreCapaciteStockage) {
		return (notreCapaciteStockage/2)-this.getStocktotal();
	}


	public HashMap<I, Double> getStock() {
		return stock;
	}

	public void clear() {
		this.clear();
	}


	public double get(I  prod) {
		return this.stock.get(prod);
	}
	
	public Set<I> keySet() {
		return this.stock.keySet();
	}


	
}

