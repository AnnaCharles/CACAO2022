package abstraction.eq3Transformateur1;

import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;

public class Transformateur1ContratCadreAcheteur extends Transformateur1ContratCadreVendeur implements IAcheteurContratCadre {
	
	protected List<ExemplaireContratCadre> mesContratEnTantQueAcheteur;
	
	public Transformateur1ContratCadreAcheteur() {
		super();
		this.mesContratEnTantQueAcheteur=new LinkedList<ExemplaireContratCadre>();
	}
	/** On décide d'initier un nouveau contrat cadre lorsque moins de 50% de la quantité de fèves désrirées provient de contrats cadre.
	 *  Alexandre*/
	public boolean achete(Object produit) {
		//calcule la quantité de fèves reçues ce tour-ci par les contrats cadres déjà signés
		if (produit instanceof Feve) {
			if (((Feve)produit) == Feve.FEVE_BASSE || ((Feve)produit) == Feve.FEVE_MOYENNE || ((Feve)produit) == Feve.FEVE_MOYENNE_BIO_EQUITABLE) {
				double quantiteFeveContrat = 0. ;
				for (ExemplaireContratCadre c : this.mesContratEnTantQueAcheteur) {
					if (produit == c.getProduit()) {
						quantiteFeveContrat = quantiteFeveContrat + c.getQuantiteALivrerAuStep();
					}
				}
				journalCCA.ajouter("On a prévu d'acheter "+ quantiteFeveContrat + " de " + produit +" au prochain tour.");
				// décide si on est ouvert ou non à des propositions
				if (quantiteFeveContrat < 0.5*this.quantiteAchatFeve.get(produit)) {
					journalCCA.ajouter("Je souhaite faire un nouveau contrat cadre acheteur de " + produit);
					return true;
				}
			}
		}
		//journalCCA.ajouter("Je ne souhaite pas faire un nouveau contrat cadre acheteur de " + produit);
		return false;
	}

	// négocie un échancier inférieur à 6 échéances; auteur Julien */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheancier().getNbEcheances()<100) {
			journalCCA.ajouter("J'accepte cet échéancier de "+ contrat.getEcheancier().getNbEcheances()+" avec l'acteur"+contrat.getVendeur().getNom());
			return contrat.getEcheancier();
		}
		journalCCA.ajouter("Je refuse cet échéancier");
		return null;
	}

	// négociation du prix; auteur Julien on accepte tout pour l'instant*/
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getPrix()<prixAchatFeve.get(contrat.getProduit()) + 1000000000000000000000000.) {
			journalCCA.ajouter("J'accepte ce prix "+ contrat.getPrix());
			return contrat.getPrix();
		} else {
			journalCCA.ajouter("Je propose un nouveau prix car ils ont proposé : "+ contrat.getPrix()+" du vendeur "+ contrat.getVendeur().getNom());
			return prixAchatFeve.get(contrat.getProduit());
			
		}
	}

	@Override
	public void receptionner(Object produit, double quantite, ExemplaireContratCadre contrat) {
		stockFeve.put(((Feve)produit), stockFeve.get(contrat.getProduit())+quantite);
		
		journalCCA.ajouter("Je receptionne "+ quantite + "kg de "+ (Feve)produit);
		journalCCA.ajouter("Le nouveau stock de "+((Feve)produit)+" est de "+ stockFeve.get((Feve)produit));
	}
	
	// Alexandre
	public void initialiser( ) {
		super.initialiser();
	}
	
	public void next() {
		super.next();
		// supprime les contrats d'achats obsolètes; auteur Julien
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueAcheteur) {
			//System.out.println(contrat + "qtRestantALivrer " + contrat.getQuantiteRestantALivrer() + "Montant "+ contrat.getMontantRestantARegler());
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				//System.out.println("ajouté");
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQueAcheteur.removeAll(contratsObsoletes);

		journalCCA.ajouter("Les contrats cadres acheteur obsoletes ont ete supprimes");

	}

}
