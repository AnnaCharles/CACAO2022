package abstraction.eq3Transformateur1;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.ExempleTransformateurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

public class Transformateur1ContratCadreVendeur extends Transformateur1Bourse implements IVendeurContratCadre{
	
	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	
	public Transformateur1ContratCadreVendeur() {
		super();
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
	}
	
	/** fonction qui détermine quel type de chocolat on vend en contrat cadre; auteur Julien 
	 *  correction : maintenant on regarde le nom de marque pour ne vendre que du chocolat cote d'or ; Alexandre */
	public boolean vend(Object produit) {
		//journal.ajouter("debut CC avec distrib");

		if (produit instanceof ChocolatDeMarque && ((ChocolatDeMarque) produit).getMarque() == "cote d'or"){
			if ((((ChocolatDeMarque)produit).getChocolat()==Chocolat.MQ)
					||(((ChocolatDeMarque)produit).getChocolat()==Chocolat.MQ_BE)
					||(((ChocolatDeMarque)produit).getChocolat()==Chocolat.MQ_O)) {
				journalCCV.ajouter("On veut bien vendre " + produit);
				return true;
			}
		}
		//journalCCV.ajouter("on ne veut pas vendre (car on ne vend pas ce chocolat)");
		return false;
	}

	// négocie un échancier inférieur à X échéances; auteur Julien */
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheancier().getNbEcheances()<100) {
			journalCCV.ajouter("on aime cet échéancier");
			return contrat.getEcheancier();
		}
		//journalCCV.ajouter("on chie sur leurs échéances et on refuse car CC long terme");
		return null;
	}

	/** Prix initial = 1.4 x prixVenteMin 
	 *  Alexandre*/
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (!( contrat.getProduit() instanceof ChocolatDeMarque)) {
			journalCCV.ajouter("________________" + contrat.getAcheteur() +" "+ contrat.getProduit()); // pour debugger
			return 0.;
		}
		journalCCV.ajouter("On propose comme prix initial : " + this.prixVenteMin.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()));
		return this.prixVenteMin.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())*1.4;
		
	}

	// négocie une contreproposition du prix; auteur Julien */
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if (contrat.getPrix()>prixVenteMin.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())) {
			journalCCV.ajouter("on aime ce prix");
			return contrat.getPrix();
		} else {
			//journalCCV.ajouter("ça négocie dur les prix");
			return Math.max(contrat.getPrix()+prixVenteMin.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())/2.0,0.0);
		}
		
	}

	/** Ajout du contrat dans la liste de contrats à honorer
	 *  et maj de dernierPrixVenteChoco
	 *  Alexandre*/
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		mesContratEnTantQueVendeur.add(contrat);
		
		journalCCV.ajouter("nouveau contrat cadre vendeur signé");
		journalCCV.ajouter("Acheteur : "+ contrat.getAcheteur().getNom());
		journalCCV.ajouter("Produit : " + contrat.getProduit() + " " + ((ChocolatDeMarque)contrat.getProduit()).getChocolat());
		journalCCV.ajouter("Echeancier : " + contrat.getEcheancier());
		journalCCV.ajouter("Prix : " + contrat.getPrix());
		
		//System.out.println("notif new CC");
		//System.out.println("dsitrib : "+ dernierPrixVenteChocoReset.getDistributeurs() + ", choco : " + dernierPrixVenteChocoReset.getChocolats() );
		
		if (contrat.getAcheteur().getNom() != "EQ8") {
			if (dernierPrixVenteChocoReset.getPrix(
					contrat.getAcheteur().getNom(), 
					((ChocolatDeMarque)contrat.getProduit()).getChocolat())
					> contrat.getPrix() 
					&& dernierPrixVenteChocoReset.getPrix(
							contrat.getAcheteur().getNom(), 
							((ChocolatDeMarque)contrat.getProduit()).getChocolat()) 
					!= 0) { // on garde le prix minimum negocie avec les vendeurs
				dernierPrixVenteChocoReset.setPrix(
						contrat.getAcheteur().getNom(), 
						((ChocolatDeMarque)contrat.getProduit()).getChocolat(), 
						contrat.getPrix());
				
			}
		}
		
	}

	/** modification du stock ; auteur Julien
	 *  modification du stock en fonction de la date de péremption (commencer par le plus ancien, auteur : Anna
	 *  correction : avant lorsqu'on n'avait pas assez pour satisfaire la demande, on livrait ce qu'on pouvait mais on oubliait d'acutaliser les stockss ; Alexandre */
	
	 public double livrer(Object produit, double quantite, ExemplaireContratCadre contrat) {
		double livre = Math.min(stockChoco.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat()), quantite);
		livre = Math.max(0., livre);
		if (livre==quantite) {
			//System.out.println("________________livre = qt________________ Etape : " + Filiere.LA_FILIERE.getEtape());
			//System.out.println("Acheteur : " + contrat.getAcheteur().getNom());
			//System.out.println("Produit : " + contrat.getProduit());
			//System.out.println("Nousveau stock : " + (stockChoco.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())-quantite));
			stockChoco.put(((ChocolatDeMarque)contrat.getProduit()).getChocolat(),stockChoco.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())-quantite);
			stockChocoPeremption.venteLot(((ChocolatDeMarque)contrat.getProduit()).getChocolat(), quantite);
			return quantite;					
		}
		//System.out.println("!!!_____________livre < qt_____________!!! Etape : " + Filiere.LA_FILIERE.getEtape());
		//System.out.println("Acheteur : " + contrat.getAcheteur().getNom());
		//System.out.println("Produit : " + contrat.getProduit());
		//System.out.println("Quantité à livrer : " + contrat.getQuantiteALivrerAuStep());
		//System.out.println("Nousveau stock : " + (stockChoco.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())-livre));
		stockChoco.put(((ChocolatDeMarque)contrat.getProduit()).getChocolat(),stockChoco.get(((ChocolatDeMarque)contrat.getProduit()).getChocolat())-livre);
		stockChocoPeremption.venteLot(((ChocolatDeMarque)contrat.getProduit()).getChocolat(), livre);
		return livre;
	}
		
	
	public void next() {
		super.next();
		// Supprime les contrats obsolètes et honorés -Julien
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeur) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeur.removeAll(contratsObsoletes);
		journalCCV.ajouter("test ContratCadreVendeur / Les contrats cadres vendeur obsolete ont ete supprime");
	}
	
	/** 
	 *  Alexandre*/
	public void initialiser() {
		super.initialiser();
	}
	

	
}
