package abstraction.eq5Transformateur3;

import java.util.HashMap;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
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
		double somme = this.stockFeves.getstocktotal();
		
		
		int step = Filiere.LA_FILIERE.getEtape();
		double quantite = this.seuilTransformation.getValeur();
		boolean change=true;
		HashMap<Feve, Double> copieStock = stockFeves.getCopie();
		while (change && step<Filiere.LA_FILIERE.getEtape()+20) { //quantité échéancier inférieur à ce qui reste à transformer
			change=false;
			for (int i=0; i<this.contratsEnCoursVente.size() ;i++){
				ChocolatDeMarque c = (ChocolatDeMarque)(this.contratsEnCoursVente.get(i)).getProduit();
				Gamme gamme = c.getGamme();
				boolean bioequitable = c.isBioEquitable();
				Feve feveproduit = null;
				for (Feve f : Feve.values()){
					if (f.isBioEquitable() == bioequitable && f.getGamme() == gamme){
						feveproduit = f;
					}	
				}
				boolean original = c.isOriginal();
				double qtStepCC = this.contratsEnCoursVente.get(i).getEcheancier().getQuantite(step);
				if (copieStock.get(feveproduit)>=qtStepCC) {
					copieStock.put(feveproduit, copieStock.get(feveproduit)-qtStepCC);
					change=true;
				} else {
					if (copieStock.get(feveproduit)>0) {
						qtStepCC=qtStepCC-copieStock.get(feveproduit);
						copieStock.put(feveproduit,0.0);
						change=true;
					}
					if (qtStepCC>0 && quantite>=qtStepCC) {
						this.transformationClassique(feveproduit, qtStepCC, original);
						quantite = quantite -qtStepCC;
						change=true;
					}
				}
			}
			step +=1;
		}
		
		
		/* Si on a rien tranformé et que le stock de chocolat est vide, 
		 * on transforme pour avoir du stock
		 */
		if (quantite == this.seuilTransformation.getValeur() &&  this.stockChocolat.getstocktotal() == 0) {
			for (Feve f : this.stockFeves.getProduitsEnStock()) {
				this.transformationClassique(f, quantite/4*0.6, true);
				this.transformationClassique(f, quantite/4*0.4, false);
			}
		}
	}
	
}
	
	
	

