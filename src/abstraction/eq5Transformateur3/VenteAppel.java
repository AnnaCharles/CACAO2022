package abstraction.eq5Transformateur3;

import java.util.List;

import abstraction.eq8Romu.appelsOffres.IVendeurAO;
import abstraction.eq8Romu.appelsOffres.PropositionAchatAO;
import abstraction.eq8Romu.appelsOffres.SuperviseurVentesAO;
import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;

public class VenteAppel extends VenteContrat implements IVendeurAO {

	//Karla
	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		/* on pourrait comparer les acheteurs en fonction de la fidélité par ex 
		 * on initie original à 0 de façon à pouvoir calculer le prix qui permet de ne pas vendre à perte
		 * les propositions sont triées par ordre décroissant de prix -> on ne considère que la première pour l'instant */
		
		int original = 0;
		
		PropositionAchatAO propretenue = null;
		PropositionAchatAO lameilleure = propositions.get(0);
		
		if (lameilleure.getOffre().getChocolat().isOriginal()) { 
			original = 1;
		}
		
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		ChocolatDeMarque choco = lameilleure.getOffre().getChocolat();
		Gamme gamme = choco.getGamme();
		Boolean isBioEquitable = choco.isBioEquitable();
		for (Feve f : this.stockFeves.getProduitsEnStock()) {
			if (f.getGamme() == gamme && f.isBioEquitable() == isBioEquitable) {
				Double seuilMax = bourse.getCours(f).getValeur();
				Double prixrentable = seuilMax + this.coutOriginal.getValeur()*original + this.coutTransformation.getValeur();
				if (lameilleure.getPrixKg()>= prixrentable) {
					propretenue = lameilleure ;	
					this.ventes.ajouter(propretenue.getAcheteur()+ "prix kg" + propretenue.getPrixKg());
				}
			}
		}
		return propretenue; // si le prix est trop faible, on prefère garder notre chocolat
	}
	
	//Karla julien
	public void next() {
		super.next();
		SuperviseurVentesAO superviseur = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		for (Chocolat c: this.stockChocolat.getProduitsEnStock()) {
			/* Si on a un stock suffisant, on essaie de liquider la moitie */
			if (this.stockChocolat.getstock(c)>this.SeuilMinChocolat) {
				PropositionAchatAO retenueenTG = 
					superviseur.vendreParAO(this, this.cryptogramme, new ChocolatDeMarque(c,"BIO'riginal"), this.stockChocolat.getstock(c)/2, true);
					if (retenueenTG!=null) {
						this.utiliser(c, retenueenTG.getOffre().getQuantiteKG()); 
						this.ventes.ajouter("vente par AO de "+retenueenTG.getOffre().getQuantiteKG()+"  kg de " + c.name() +"  a "
						+retenueenTG.getAcheteur().getNom()+" en TG" + "a un prix de " + retenueenTG.getPrixKg());
					} else {
						// on essaye sans mettre en TG
						PropositionAchatAO retenuepasenTG = 
								superviseur.vendreParAO(this, this.cryptogramme, new ChocolatDeMarque(c,"BIO'riginal"), this.stockChocolat.getstock(c)/2, false);
						if (retenuepasenTG!=null) {
							this.utiliser(c, retenuepasenTG.getOffre().getQuantiteKG()); 
							this.ventes.ajouter("vente par AO de "+retenuepasenTG.getOffre().getQuantiteKG()+"kg  de " + c.name() +" a "
							+retenuepasenTG.getAcheteur().getNom() + "a un prix de " + retenueenTG.getPrixKg());
						} else {
							this.ventes.ajouter("pas d'offre retenue");
						}
					}
				}
			}
		}
			

}