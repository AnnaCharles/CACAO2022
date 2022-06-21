package abstraction.eq5Transformateur3;

import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;

public class Transformateur3 extends VenteAppel {
	

	public Transformateur3() {
		super();
	}

	public void next() {
		super.next();
		
		//mise à jour des variables indicateurs des stocks
		this.stockFevesVariableM.setValeur(this, this.stockFeves.getstock(Feve.FEVE_MOYENNE_BIO_EQUITABLE) + this.stockFeves.getstock(Feve.FEVE_MOYENNE));
		this.stockFevesVariableH.setValeur(this, this.stockFeves.getstock(Feve.FEVE_HAUTE_BIO_EQUITABLE) + this.stockFeves.getstock(Feve.FEVE_HAUTE));
		this.stockChocolatVariableM.setValeur(this, this.stockChocolat.getstock(Chocolat.MQ_BE)+this.stockChocolat.getstock(Chocolat.MQ_BE_O)+this.stockChocolat.getstock(Chocolat.MQ)+this.stockChocolat.getstock(Chocolat.MQ_O));
		this.stockChocolatVariableH.setValeur(this, this.stockChocolat.getstock(Chocolat.HQ_BE)+this.stockChocolat.getstock(Chocolat.HQ_BE_O)+this.stockChocolat.getstock(Chocolat.HQ)+this.stockChocolat.getstock(Chocolat.HQ_O));
		

	}

	
}
