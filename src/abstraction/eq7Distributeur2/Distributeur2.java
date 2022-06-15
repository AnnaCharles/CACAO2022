package abstraction.eq7Distributeur2;

import abstraction.eq8Romu.filiere.Filiere;

public class Distributeur2 extends Distributeur2ChocolatDeMarque{
	
	public static double COUT_STOCK_PAR_KILO = 0.16;
	
	public Distributeur2() {
		super();
	}

	public void next() {
		super.next();
		
		//Les lois imposées seront listés ici
		this.coutVariableDeStockage();
	}
	
	private void coutVariableDeStockage() {
		double quantiteTotale = this.stock.getQuantiteTotale();
		double coutStockage = quantiteTotale*COUT_STOCK_PAR_KILO;
		if (coutStockage != 0.) {
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), coutStockage);
		}
	}
	
}
