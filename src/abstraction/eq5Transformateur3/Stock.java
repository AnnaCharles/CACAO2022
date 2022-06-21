package abstraction.eq5Transformateur3;
import java.util.HashMap;
import java.util.Set;

//karladedeler

public class Stock<Produit> {
	private HashMap <Produit, Double> stock;

	public Stock() {
		this.stock = new HashMap <Produit, Double>();
	}

	public HashMap<Produit, Double> getStockDico() {
		return stock;
	}
	public HashMap<Produit, Double> getCopie() {
		HashMap<Produit, Double>copie = new HashMap <Produit, Double>();
		for (Produit p : this.stock.keySet()) {
			copie.put(p,  this.stock.get(p));
		}
		return copie;
	}

	//Mise à jour les stocks
	public void ajouter(Produit p, double qtt) {
	if (qtt > 0) {
		if (this.stock.keySet().contains(p)) {
			this.stock.put(p, this.stock.get(p)+qtt);	
			}
		else {
			this.stock.put(p, qtt);
			}
		}
	}
	//julien 10/05
	public void utiliser(Produit p, double qtt) {
	// il faudrait renvoyer une erreur si le produit n'est pas en stock
	if (this.stock.keySet().contains(p)) {
		if (qtt > 0 && this.stock.get(p)-qtt >=0 ) {
			this.stock.put(p, this.stock.get(p)-qtt);	
			}
		}
	}
	

	// Récupérer la liste des produits en stock
	public Set<Produit> getProduitsEnStock() {
		return this.stock.keySet();
	}
	//Récupérer les stocks pour un type de produit
	public Double getstock(Produit p) {
		if (this.stock.keySet().contains(p)) {
			return this.stock.get(p);
		}
		return 0.0;
	}

	//julien
	public Double getstocktotal() {
		Double total = 0.0;
		for (Double q : this.stock.values()) {
			total += q;
		}
		return total;
	}
	



}