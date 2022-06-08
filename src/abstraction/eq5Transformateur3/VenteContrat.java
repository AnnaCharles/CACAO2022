package abstraction.eq5Transformateur3;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;


public class VenteContrat extends Transformation implements IVendeurContratCadre {
	
	protected int nb_prop;
	
	//chgmt 
	public void lanceruncontratVendeur(ChocolatDeMarque c) {
		List<IAcheteurContratCadre> L =  ((SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"))).getAcheteurs(c);
		Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, 100); //100 kg de chocolat sur 10 steps
		List<IAcheteurContratCadre>L2 = new LinkedList<IAcheteurContratCadre>();
		this.nb_prop = 0;
		for (IAcheteurContratCadre a : L) {
			if (a instanceof IDistributeurChocolatDeMarque) {
				L2.add(a);//d.remove(Filiere.LA_FILIERE.getActeur("eq8"));
			}
		}
		if (L2.size()!=0) {
			if (L2.size()== 1) {
				((SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"))).demandeVendeur(L2.get(0), (IVendeurContratCadre)Filiere.LA_FILIERE.getActeur("EQ5"), (Object)c, e, this.cryptogramme, true);
				}
			else {
				// On choisit aléatoirement un des distributeurs
				Random randomizer = new Random();
				IAcheteurContratCadre random = L2.get(randomizer.nextInt(L2.size()));
				((SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"))).demandeVendeur(random, (IVendeurContratCadre)Filiere.LA_FILIERE.getActeur("EQ5"), (Object)c, e, this.cryptogramme, true);

			}
		}
	}

	
	//Yves, Karla
	public boolean vend(Object produit) {
		if (! (produit instanceof ChocolatDeMarque)) { return false;} // ju
		Chocolat c = ((ChocolatDeMarque) produit).getChocolat();
		if (stockChocolat.getProduitsEnStock().contains(c)) {
			return true;
		}
		return false;
	}

	//Yves
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		List<Echeancier> listeEcheanciers=contrat.getEcheanciers();
		int l = listeEcheanciers.size();
		this.ventes.ajouter("nous acceptons l' échéancier");
		return listeEcheanciers.get(l-1); //////////
	}

	//Yves
	public double propositionPrix(ExemplaireContratCadre contrat) {
		this.nb_prop += 1;
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		//Double seuilMax = bourse.getCours((Feve)contrat.getProduit()).getMin();
		Double seuilMax = 0.0;
		if (contrat.getProduit() instanceof ChocolatDeMarque) {
			if (((ChocolatDeMarque)(contrat.getProduit())).isOriginal()) {
				return 2*(seuilMax+this.coutTransformation.getValeur()+this.coutOriginal.getValeur());
			}
			else {
				return 2*(seuilMax+this.coutTransformation.getValeur());
			}
		}
		else {
			return 0.0;
		}
	}

	@Override
	//Yves
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		//Double seuilMax = bourse.getCours((Feve)contrat.getProduit()).getMin();
		Double seuilMax = 0.0;
		if (((ChocolatDeMarque)(contrat.getProduit())).isOriginal()) {
			if (contrat.getPrix()>2*(seuilMax+this.coutTransformation.getValeur()+this.coutOriginal.getValeur())){
				this.ventes.ajouter("nous acceptons "+contrat.getPrix().toString());
				return contrat.getPrix();
			}
		} 
		else {
			/* on baisse nos prix progressivement tout en étant sur d'être rentable (vendre au dessus de 1 fois nos couts totaux) */
			double Nprix = (2.0-0.07*this.nb_prop)*(seuilMax+this.coutTransformation.getValeur()+this.coutOriginal.getValeur());
			this.ventes.ajouter("nous contreproposons " + Nprix);
			return Nprix;
		}
		
		return 0.0;
	}


	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		this.ventes.ajouter("Nouveau Contrat Cadre avec"+ contrat.getAcheteur().getNom() +"sur une periode de " + contrat.getEcheancier().getNbEcheances() + " pour "+ contrat.getProduit().toString());
		this.contratsEnCoursVente.add(contrat);
	}

	//Karla
	public double livrer(Object produit, double quantite, ExemplaireContratCadre contrat) {
		ChocolatDeMarque c = (ChocolatDeMarque)produit;
		double peutlivrer = Math.min(this.stockChocolat.getstock(c.getChocolat()), quantite);
		if (peutlivrer>0.0) {
			this.utiliser(c.getChocolat(), peutlivrer);
		}
		this.ventes.ajouter("nous livrons " + peutlivrer + " kg de" + c.toString()+ " à " + contrat.getAcheteur().getNom());
		return peutlivrer;
	}
	
	//Karla & Yves
	/* on regarde l etat de nos stocks et on lance la procédure */
	public void next() {
		super.next();
		
		/* Mise à jour de la liste des CC en cours */
		this.contratsEnCoursVente = majCC(this.contratsEnCoursVente);
		
		/* Lancer des CC */
		for (Chocolat c : this.stockChocolat.getProduitsEnStock()) {
			if (this.stockChocolat.getstock(c) > this.SeuilMinChocolat && c.isBioEquitable() == true) {
				ChocolatDeMarque choco = new ChocolatDeMarque(c,"BIO'riginal");
				lanceruncontratVendeur(choco);
			}
			if (this.stockChocolat.getstock(c) > this.SeuilMinChocolat && c.isBioEquitable() == false) {
				ChocolatDeMarque choco = new ChocolatDeMarque(c,"CHOCO'riginal");
				lanceruncontratVendeur(choco);
			}
		}
	}
}