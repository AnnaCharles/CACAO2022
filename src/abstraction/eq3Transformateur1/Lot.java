package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* anna*/
import abstraction.eq8Romu.produits.Chocolat;
// création de lot (quantité, date de péremtion) et des fonctions asociées, auteur : anna
public class Lot{
	
	private double quantite;
	private int date;
	
	public Lot(double quantite, int date) {
		this.quantite = quantite ;
		this.date = date;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
	
	
	
}