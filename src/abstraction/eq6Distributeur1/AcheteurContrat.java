package abstraction.eq6Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

public class AcheteurContrat extends DistributeurChocolatDeMarque implements IAcheteurContratCadre{//leorouppert
	protected Journal journalNegociationCC;
	protected Journal journalSuiviCC;
	protected List<ExemplaireContratCadre> mesContrats;
	private final double SEUIL_AJOUT_ECHEANCE = 0.3;
	private final int STEP_INTERSECTION_MIN = 6;
	private final double PRIX_LIMITE = 1.2;
	private final int MIN_NEGO = 5;
	
	public AcheteurContrat() {
		super();
		mesContrats = new ArrayList<ExemplaireContratCadre>();
		journalNegociationCC = new Journal("Negociations CC", this);
		journalSuiviCC = new Journal("Suivi des livraisons des CC", this);
	}


	/**
	 * @author Nathan,
	 * @return La liste des journaux renvoyé par distributeurChocolatDeMarque en ajoutant le journal pour les contrats cadre
	 */
	@Override
	public List<Journal> getJournaux() {
		List<Journal> l = super.getJournaux();
		l.add(journalNegociationCC);
		l.add(journalSuiviCC);
		return l;
	}
	/**
	 * @author Leo, Emma, Nathan
	 */
	@Override
	public boolean achete(Object produit) {
		if (NotreStock.seuilSecuFaillite() == true || ! (produit instanceof ChocolatDeMarque)) {
			return false;
		}
		if (partDuMarcheVoulu( ((ChocolatDeMarque)produit).getChocolat()) > 0.0) {
			return true;
		}
		return false;
	}

	private void negoReussie(ExemplaireContratCadre ecc) {
		journalNegociationCC.ajouter(Color.GREEN, Color.BLACK, "Négociation réussie ! Contrat #" + ecc.getNumero());
		mesContrats.add(ecc);
		setPrixVente((ChocolatDeMarque)ecc.getProduit(), ecc.getPrix());
	}
	
	private void nouvelleNego(IVendeurContratCadre vendeur, ChocolatDeMarque choco, Echeancier e, boolean entrante) {
		String msg = "Nouvelle demande " + ((entrante) ? "entrante" : "sortante") + " de CC pour " + choco + " avec " + vendeur.getNom() + " pour l'écheancier " + e;
		journalNegociationCC.ajouter(Color.CYAN, Color.BLACK, msg);
	}

	private void nouvelleNego(ExemplaireContratCadre ecc, boolean entrante) {
		nouvelleNego(ecc.getVendeur(), (ChocolatDeMarque) ecc.getProduit(), ecc.getEcheancier(), entrante);
	}

	public int[] interesctionEtapes(Echeancier e1, Echeancier e2) {
		int[] res = {0, 0};
		res[0] = (e1.getStepDebut() < e2.getStepDebut()) ? e2.getStepDebut() : e1.getStepDebut();
		res[1] = (e1.getStepFin() < e2.getStepFin()) ? e1.getStepFin() : e2.getStepFin();
		if (res[0] > res[1]) {
			return null;
		}
		return res;
	}

	public double echenacierDelta(Echeancier e1, Echeancier e2) {
		int[] intersection = interesctionEtapes(e1, e2);
		if (intersection == null || intersection[1] - intersection[0] < STEP_INTERSECTION_MIN) {
			return 1.0;
		}
		double delta = 0.0;
		for (int i = intersection[0]; i <= intersection[1]; i++) {
			if (e1.getQuantite(i) > e2.getQuantite(i)) {
				return -1.0;
			}
			delta += e1.getQuantite(i) - e2.getQuantite(i);
		}
		return delta;
	}
	
	@Override
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) {
		if (contrat.getEcheanciers().size() == 1) {
			nouvelleNego(contrat, true);
		}
		journalNegociationCC.ajouter("--> Contre propisiton du vendeur: voici l'echeancier proposée et celui proposée par le vendeur");
		Echeancier voulu = nouveauxEcheanciersVoulus().get(contrat.getProduit());
		journalNegociationCC.ajouter("--> " + voulu);
		journalNegociationCC.ajouter("--> " + contrat.getEcheancier());
		if (voulu == null) return null;
		double delta = echenacierDelta(contrat.getEcheancier(), voulu);
		journalNegociationCC.ajouter("--> Delta de ce qu'on voulait de: " + delta);
		if (delta <= 0){ // TODO: Pour l'instant on s'assure juste qu'il n'y a pas de surplus
			return contrat.getEcheancier();
		}
		else {
			if (contrat.getQuantiteTotale() > voulu.getQuantiteTotale()* 1.3) {
				return voulu;
			}
			return contrat.getEcheancier();
		}
	}

	@Override
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) {
		double espere = 4 * facteurPrixChocolat(((ChocolatDeMarque) contrat.getProduit()).getChocolat());
		int nbNego = contrat.getListePrix().size();
		if (contrat.getPrix() > PRIX_LIMITE * 7.5* facteurPrixChocolat(((ChocolatDeMarque) contrat.getProduit()).getChocolat())) {
			double res = 7.5 * facteurPrixChocolat(((ChocolatDeMarque) contrat.getProduit()).getChocolat());
			journalNegociationCC.ajouter("--> " + contrat.getPrix() + " le kilo est trop élevé nous proposons " + res);
			return res;
		}
		else {
			if (nbNego < MIN_NEGO+3) {
				double prop = 0.0;
				prop = (contrat.getPrix() + espere)/2;
				journalNegociationCC.ajouter("--> Nous negocions un prix de " + prop);
				return prop;
			}
			journalNegociationCC.ajouter("--> Nous acceptons le prix proposé qui est " + contrat.getPrix());
			return contrat.getPrix();
		}
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		negoReussie(contrat);
	}

	@Override
	public void receptionner(Object produit, double quantite, ExemplaireContratCadre contrat) {
		double qteAttendu = contrat.getQuantiteALivrerAuStep();
		if (quantite != qteAttendu) {
			journalSuiviCC.ajouter(Color.RED, Color.BLACK, "Il manque " + (qteAttendu - quantite) + "Kg de ce que nous etions censé recevoir de " + contrat.getVendeur().getNom() + " pour le contrat #" + contrat.getNumero());
			
		}
		else {
			journalSuiviCC.ajouter("La quantité attendu (" + quantite + ") a été recu de " + contrat.getVendeur().getNom() + " pour le contrat #" + contrat.getNumero());
		}
		this.getNotreStock().addQte((ChocolatDeMarque) produit, quantite);
		this.setPrixVente((ChocolatDeMarque) produit, contrat.getPrix());
	}

	public void suppAnciensContrats() { // Léo
		List<ExemplaireContratCadre> aSupprimer = new ArrayList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : mesContrats) {
			if (contrat.getQuantiteRestantALivrer() == 0.0 && contrat.getMontantRestantARegler() == 0.0) {
				aSupprimer.add(contrat);
			}
		}
		mesContrats.removeAll(aSupprimer);		
	}

	/**
	 * @author Nathan
	 * Permet d'avoir un echenacier additionnant tous les echeancier d'un chocolat 
	 * @return un echeancier de ce qu'il manque à fournir dans nos prévision
	 */
	public Map<ChocolatDeMarque, Echeancier> getEcheanceParChoco() {
		Map<ChocolatDeMarque, Echeancier> res = new HashMap<ChocolatDeMarque, Echeancier>();
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			res.put(choco, new Echeancier());
		}
		for (ExemplaireContratCadre ecd : mesContrats) {
			ChocolatDeMarque cm = (ChocolatDeMarque) ecd.getProduit();
			Echeancier e = ecd.getEcheancier();
			int start = (e.getStepDebut() < Filiere.LA_FILIERE.getEtape()+1) ? Filiere.LA_FILIERE.getEtape()+1 : e.getStepDebut();
			for (int i = start; i < e.getStepFin(); i++) {
				res.get(cm).set(i, res.get(cm).getQuantite(i) + e.getQuantite(i));
			}
		}
		return res;
	}

	public double getSomme(int stepDebut, int stepFin, Echeancier e) {
		double res = 0;
		for (int i = stepDebut; i <= stepFin; i++) {
			res += e.getQuantite(i);
		}
		return res;
	}

	public double attenduNProchainesEtapes(int n, ChocolatDeMarque choco) {
		double res = 0;
		int ajd = Filiere.LA_FILIERE.getEtape();
		for (int i = ajd+1; i <= n+ajd; i++) {
			res += Filiere.LA_FILIERE.getVentes(choco, (i%24)-24);
		}
		return res;
	}

	private Echeancier createEcheancier(Echeancier aCombler, int stepDebut, ChocolatDeMarque c) {
		Echeancier e = new Echeancier(stepDebut);
		double stock = NotreStock.getStock(c);
		for (int i = stepDebut; i < stepDebut + 24; i++) {
			double aComblerI = (aCombler == null) ? 0 : aCombler.getQuantite(i);
			stock += aComblerI;
			double doitAvoir = partCC*Filiere.LA_FILIERE.getVentes(c, (i%24)-24) * partDuMarcheVoulu(c.getChocolat());
			if (stock < doitAvoir) {
				e.set(i, doitAvoir-stock);
				stock = 0;
			}
		}
		return e;
	}

	

	public Map<ChocolatDeMarque, Echeancier> nouveauxEcheanciersVoulus() {
		Map<ChocolatDeMarque, Echeancier> res = new HashMap<ChocolatDeMarque, Echeancier>();
		Map<ChocolatDeMarque, Echeancier> echeancierTotal = getEcheanceParChoco();
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			//if (! achat.get(choco)) {
			// continue;
			//}
			Echeancier eChoco = echeancierTotal.get(choco);
			Echeancier e = createEcheancier(eChoco, Filiere.LA_FILIERE.getEtape()+1, choco);
			if (e.getQuantiteTotale() >= SuperviseurVentesContratCadre.QUANTITE_MIN_ECHEANCIER && e.getQuantiteTotale() > SEUIL_AJOUT_ECHEANCE*attenduNProchainesEtapes(24, choco)*partDuMarcheVoulu(choco.getChocolat())*partCC) {
				// Sinon cela ne sert à rien de faire un nouveau contrat cadre
				res.put(choco, e);
			}
		}
		return res;
	}


	/**
	 * @author Nathan
	 */
	@Override
	public void next() {
		super.next();
		this.suppAnciensContrats();
		Map<ChocolatDeMarque, Echeancier> aAjouter = nouveauxEcheanciersVoulus();
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			for (IVendeurContratCadre vendeur : supCCadre.getVendeurs(choco)) {
				Echeancier aAjouterChoco = aAjouter.get(choco);
				if (aAjouterChoco != null) {
					nouvelleNego(vendeur, choco, aAjouterChoco, false);
					ExemplaireContratCadre ecc = supCCadre.demandeAcheteur((IAcheteurContratCadre)this, vendeur, choco, aAjouterChoco, this.cryptogramme, false);
					if (ecc != null) {
						negoReussie(ecc);
						aAjouter = nouveauxEcheanciersVoulus();
					}
					else {
						journalNegociationCC.ajouter(Color.RED, Color.BLACK, "La negociation a echouée.");
					}
				}
			}
		}
	}
}

