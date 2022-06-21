package abstraction.eq6Distributeur1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.general.VariableReadOnly;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Gamme;

public class Distributeur1Acteur implements IActeur {
	protected int cryptogramme;
	protected SuperviseurVentesContratCadre supCCadre;
	protected Stock NotreStock;
	Random ran;
	protected Map<ChocolatDeMarque,Variable> stockageQte;
	protected Journal JournalPrincipal;
	protected Journal JournalCompte;
	protected Journal JournalVente;
	protected List<Variable> prix; 
	protected Double prixTotalTour;
	protected Map<ChocolatDeMarque, Double> prixVente;
	protected Variable QteChocoHQ;
	protected Variable QteChocoMQ;
	protected Variable QteChocoBq;
	protected Integer Compteur;	
	protected Map<ChocolatDeMarque, VariableReadOnly> HistoChoco; // Léo
	protected Double ChocoTotalTour; // variable qui donne ce qui a été vendu l'année précédente pour le tour correspondant
	protected Double TauxTour; // renvoi la part de marché visée par FourAll pour le tour en cours
	protected final double partCC = 0.9;
	protected Journal stockJ;
	
	/**
	 * @return the notreStock
	 */
	
	public Stock getNotreStock() {
		return NotreStock;
	}
	
	/**
	 * @author Nolann
	 */
	public Distributeur1Acteur() {
		stockJ = new Journal("Stocks", this);
		HistoChoco = new HashMap<ChocolatDeMarque, VariableReadOnly>(); // Léo
		JournalPrincipal = new Journal("Journal Principal",this);
		JournalCompte = new Journal("Journal Compte",this);
		JournalVente = new Journal("Journal des Ventes", this);

		
		this.prixTotalTour = 100000.0;
		prix = new ArrayList<Variable>();
		prixVente = new HashMap<ChocolatDeMarque, Double>();
		ran = new Random();
		
		this.ChocoTotalTour = 0.0;
		NotreStock = new Stock(this);
		for(ChocolatDeMarque c : this.getNotreStock().getMapStock().keySet()) 
		{
			JournalPrincipal.ajouter("ajout d'une variable stock pour le chocolat" + c + "effectué" );
			prix.add(new Variable(c+"",this,0));
			JournalPrincipal.ajouter("ajout d'une variable prix pour le chocolat " + c + "effectué");
		}	
		
		JournalPrincipal.ajouter("création de la liste de variable des prix terminée");
		JournalPrincipal.ajouter("création de la liste de variable stock terminée");
	}
	
	
	public String getNom() {
		return "EQ6-FourAll";
	}

	public String getDescription() {
		return "Rendre toutes les gammes de produit accessibles à tous !";
	}

	public Color getColor() {
		return new Color(155, 89, 182);
	}


	public void initialiser() {
		supCCadre = ((SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre")));
		for (ChocolatDeMarque C : Filiere.LA_FILIERE.getChocolatsProduits()) {
			HistoChoco.put(C, new VariableReadOnly(C.toString(), this,0));

		}
		NotreStock.initialiser();
		
	}

	private int chocolatToInt(Chocolat c) {
		switch (c) {
			case BQ: return 1;
			case BQ_O: return 2;
			case MQ: return 3;
			case MQ_O: return 4;
			case MQ_BE: return 5;
			case MQ_BE_O: return 6;
			case HQ: return 7;
			case HQ_BE: return 8;
			case HQ_BE_O : return 9;
			default: return 10;
		}
	}

	private void afficherStockJournal() {
		stockJ.ajouter(Color.BLACK, Color.WHITE, "------------------ Etape " + Filiere.LA_FILIERE.getEtape() + "--------------------");
		List<ChocolatDeMarque> chocos = Filiere.LA_FILIERE.getChocolatsProduits();
		chocos.sort((c1, c2) -> {
			if (c1.getChocolat() == c2.getChocolat()) {
				return c1.getMarque().compareTo(c2.getMarque());
			}
			return chocolatToInt(c1.getChocolat()) - chocolatToInt(c2.getChocolat());
		});
		for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			stockJ.ajouter(c + ": " + NotreStock.getStock(c));
		}
		stockJ.ajouter("");
	}
	
	public void next() {
		for (ChocolatDeMarque choco : NotreStock.getMapStock().keySet()) {
			if (NotreStock.getStock(choco) > 100*partDuMarcheVoulu(choco.getChocolat()) * Filiere.LA_FILIERE.getVentes(choco, (Filiere.LA_FILIERE.getEtape()%24) - 24)) {
				NotreStock.addQte(choco, -NotreStock.getStock(choco)/2);
			}
		}
		
		//leorouppert
		for (ChocolatDeMarque C : Filiere.LA_FILIERE.getChocolatsProduits()) {
			JournalVente.ajouter("Ventes de " + C + " au tour  " +Filiere.LA_FILIERE.getEtape()+" : "+ (HistoChoco.get(C).getValeur(Filiere.LA_FILIERE.getEtape(),cryptogramme)-HistoChoco.get(C).getValeur(Filiere.LA_FILIERE.getEtape()-1,cryptogramme)));
			JournalVente.ajouter("Soit une part de marché de " + 0.01*Math.round(10000*(HistoChoco.get(C).getValeur(Filiere.LA_FILIERE.getEtape(),cryptogramme)-HistoChoco.get(C).getValeur(Filiere.LA_FILIERE.getEtape()-1,cryptogramme))/Filiere.LA_FILIERE.getVentes(C, Filiere.LA_FILIERE.getEtape())) + "%");
		}
		JournalPrincipal.ajouter("entrée dans next pour le tour n° " + Filiere.LA_FILIERE.getEtape());
		/**
		 *  
		 * Gestion des compte -> retirer argent :
		 * @author Nolann	
		 * 	
		 */
		//calcul cout sur le tour :
		
		JournalPrincipal.ajouter(getDescription());
		
		prixTotalTour = NotreStock.getCoûtStockageTotale();
		if (prixTotalTour > 0) {
			Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), prixTotalTour);
			JournalCompte.ajouter("le compte a été débité de "+prixTotalTour);
			JournalCompte.ajouter("le il reste"+this.getSolde()+"sur le compte");
		}		
		JournalPrincipal.ajouter("Tour "+ Filiere.LA_FILIERE.getEtape() +" terminé pour "+ this.getNom());
	
		afficherStockJournal();
	}
	
	
	
	
	/**
	 * @author Nathan
	 * @return La liste des filières proposées par l'acteur
	 */
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		filieres.add("FD1TEST");
		return(filieres);
	}

	/**
	 * @author Nathan
	 * @return Renvoie une instance d'une filière d'après son nom
	 */
	public Filiere getFiliere(String nom) {
		switch (nom) {
			case "FD1TEST":
				return new FiliereTestDistributeur1();
			default:
				return null;
		}
	}

	// Renvoie les indicateurs
	/**
	 * @author Nolann
	 * changement : on ne renvoie que la quantité de chocolat de type HQ, MQ, BQ
	 */
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.addAll((Collection<Variable>)NotreStock.stockVar.values());
		return res;
	}
	

	// Renvoie les paramètres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	@Override
	public List<Journal> getJournaux() {
		List<Journal> journaux = new ArrayList<Journal>();
		journaux.add(JournalPrincipal);
		journaux.add(JournalCompte);
		journaux.add(stockJ);
		journaux.add(JournalVente);
		return journaux;
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
		
	}
	
	
	//EmmaHumeau
	public void notificationFaillite(IActeur acteur) {
		if (NotreStock.seuilSecuFaillite() == true) {
			JournalPrincipal.ajouter("On risque de faire faillite au prochain tour");
		}
		}

	public void notificationOperationBancaire(double montant) {
		JournalCompte.ajouter("Une opération vient d'avoir lieu d'un montant de " + montant);
	}

	// Renvoie le solde actuel de l'acteur
	//Nolann
	public double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(this, this.cryptogramme);
	}
	

	/**
	 * @author Nathan
	 * @param prixAchat
	 * @param quantiteAchete
	 */
	public void setPrixVente(ChocolatDeMarque c, double prixAchatKilo) {
		// prixVente.put(c, 1.4*prixAchatKilo);
	}
	
	/**
	 * 
	 * @author Nolann
	 * @return prixVente (V1 prix vente = 2*prix achat)
	 *  
	 */
	public void setAllprixVente( Map<ChocolatDeMarque,Double> prixAchat,  Map<ChocolatDeMarque,Double> quantiteAchete){
		
		prixAchat.forEach((key,value)->{
		//if (ChocoTotalTour < 100) {                  //Emma Humeau, modif prix
			//prixVente.put(key, (prixAchat.get(key)));
		//}
		//else {
			prixVente.put(key, (prixAchat.get(key))*2);		
		//}
	});
	}

	public double partDuMarcheVoulu(Chocolat c) {
		switch(c) {
			case BQ: return 0.7;
			case BQ_O: return 0.7;
			case MQ: return 0.5;
			case MQ_O: return 0.5;
			case MQ_BE: return 0.5;
			case MQ_BE_O: return 0.5;
			case HQ: return 0.3;
			case HQ_O: return 0.3;
			case HQ_BE: return 0.3;
			case HQ_BE_O: return 0.3;
			default: return 0.0;
		}
	}


	public int getNbChocolatProduit(Chocolat c) {
		int count = 0;
		for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (choco.getChocolat() == c) {
				count++;
			}
		}
		return count;
	}
	
	/* public double getPartMarque(ChocolatDeMarque choco) {
		double qualitePercuTotal = 0.0;
		for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			if (c.getChocolat() == choco.getChocolat()) {
				qualitePercuTotal += Filiere.LA_FILIERE.qualitePercueMarque(c.getMarque());
			}
		}
		return Filiere.LA_FILIERE.qualitePercueMarque(choco.getMarque())/qualitePercuTotal;
	} */

	protected double facteurPrixChocolat(Chocolat c) {
		double res = 1.0;
		if (c.getGamme() == Gamme.MOYENNE) {
			res *= 1.2;
		}
		else if (c.getGamme() == Gamme.HAUTE) {
			res *= 1.4;
		}
		if (c.isBioEquitable()) {
			res *= 1.2;
		}
		if (c.isOriginal()) {
			res *= 1.2;
		}
		return res;
	}

}
