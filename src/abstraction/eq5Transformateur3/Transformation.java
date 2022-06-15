package abstraction.eq5Transformateur3;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;

//Karla
public class Transformation extends AcheteurContrat {

	// Payer à la banque
	public void payer(double montant) {
		if (montant > 0.0) {
			Filiere.LA_FILIERE.getBanque().virer(Filiere.LA_FILIERE.getActeur("EQ5"), this.cryptogramme, Filiere.LA_FILIERE.getActeur("EQ8"), montant);
		}
	}
	
	// Transformation Classique
	public void transformationClassique (Feve f, double quantité, boolean B) {
		if (stockFeves.getProduitsEnStock().contains(f)) {
			if (stockFeves.getstock(f)-quantité >=0) {
				
				ajouter(this.perempFeves.trouverPlusUrgent(f),Chocolat.get(f.getGamme(),f.isBioEquitable(), B), quantité);
				utiliser(f, quantité);
				this.transformation.ajouter("Transformation classique de "+ quantité +"kg  de " + f);

				double montant = quantité * this.coutTransformation.getValeur() ;
				if (B = true) {
					montant += quantité * this.coutOriginal.getValeur();
				}
			payer(montant);
			}
			else {
				double newquantite =  stockFeves.getstock(f);
				
				ajouter(this.perempFeves.trouverPlusUrgent(f),Chocolat.get(f.getGamme(),f.isBioEquitable(), B), newquantite);
				utiliser(f, newquantite);
				this.transformation.ajouter("Transformation classique de "+ newquantite +"kg  de " + f);
				double montant = newquantite * this.coutTransformation.getValeur() ;
				if (B = true) {
					montant += newquantite * this.coutOriginal.getValeur();
				}
			payer(montant);
			}
		}
	}
	
	// Transformation qui augmente la qualité
	public void transformationUpgrade (Feve f, double quantité, boolean B) {
		double r = this.rendement.getValeur();
		if (stockFeves.getProduitsEnStock().contains(f)) {
			if (stockFeves.getstock(f)-quantité >=0) {
				
				// julien -> on associe au chocolat la date de peremption de la feve la plus "vieille"
				ajouter(this.perempFeves.trouverPlusUrgent(f),Chocolat.get(f.getGamme(),f.isBioEquitable(), B), r*quantité);
				utiliser(f, quantité);
				double montant = quantité * this.coutTransformation.getValeur() ;
				if (B = true) {
					montant += quantité * this.coutOriginal.getValeur();
				}
			payer(montant);
			}
			else {
				double newquantite =  stockFeves.getstock(f);
				
				
				if (f.getGamme() == Gamme.BASSE) {
					ajouter(this.perempFeves.trouverPlusUrgent(f),Chocolat.get(Gamme.MOYENNE,f.isBioEquitable(), B), r*newquantite);
				}
				else {
					ajouter(this.perempFeves.trouverPlusUrgent(f),Chocolat.get(Gamme.HAUTE,f.isBioEquitable(), B), r*newquantite);
				}
				utiliser(f, newquantite);
				double montant = newquantite * this.coutTransformation.getValeur() ;
				if (B = true) {
					montant += newquantite * this.coutOriginal.getValeur();
				}
			payer(montant);
			}
		}
	}
	
	// Yves
	public void next() {
		super.next();
		double somme = 0;
		for (Feve f : this.stockFeves.getProduitsEnStock()) {
			somme = somme + this.stockFeves.getstock(f);
		}
		if (somme <= this.seuilTransformation.getValeur()) {
			this.transformation.ajouter("Transformation BIO : ");
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_MOYENNE_BIO_EQUITABLE)*0.6, true);
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_MOYENNE_BIO_EQUITABLE)*0.4, false);
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_HAUTE_BIO_EQUITABLE)*0.4, false);
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_HAUTE_BIO_EQUITABLE)*0.6, true);
			this.transformation.ajouter("Transformation non BIO : ");
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_MOYENNE)*0.6, true);
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_MOYENNE)*0.4, false);
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_HAUTE)*0.4, false);
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, this.stockFeves.getstock(Feve.FEVE_HAUTE)*0.6, true);
		}
		else {
			this.transformation.ajouter("Transformation BIO : ");
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, 0.35*this.seuilTransformation.getValeur()*0.6, true); 
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, 0.3*this.seuilTransformation.getValeur()*0.6, true);
			transformationClassique(Feve.FEVE_MOYENNE_BIO_EQUITABLE, 0.35*this.seuilTransformation.getValeur()*0.4, false); 
			transformationClassique(Feve.FEVE_HAUTE_BIO_EQUITABLE, 0.3*this.seuilTransformation.getValeur()*0.4, false);
			this.transformation.ajouter("Transformation non BIO : ");
			transformationClassique(Feve.FEVE_MOYENNE, 0.20*this.seuilTransformation.getValeur()*0.6, true);
			transformationClassique(Feve.FEVE_HAUTE, 0.15*this.seuilTransformation.getValeur()*0.6, true);
			transformationClassique(Feve.FEVE_MOYENNE, 0.20*this.seuilTransformation.getValeur()*0.4, false);
			transformationClassique(Feve.FEVE_HAUTE, 0.15*this.seuilTransformation.getValeur()*0.4, false);
		}
	}
	
}
