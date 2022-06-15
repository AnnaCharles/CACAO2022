package abstraction.eq7Distributeur2.tools;


import java.util.List;

import abstraction.eq8Romu.produits.ChocolatDeMarque;

public interface ITracker {

	/**
	 * Méthode à appeller à chaque vente pour enregistrer une vente d'un produit @choco en kg @quantite
	 */
	public void trackVentesQuantite(ChocolatDeMarque choco, Double quantite);
	
	/**
	 * Méthode à appeller à chaque vente pour enregistrer le prix auquel on a vendu
	 */
	public void trackVentePrix(ChocolatDeMarque choco, Double prix);
	
	/**
	 * Méthode à appeller à la fin d'une étape qui va sommer toutes les ventes effectuées pendant celle-ci et les stocker
	 */
	public void endStepSumUp();
	
	
	
	//PRIVATE GETTERS
	
	/**
	 * Retourne la liste des ventes effectuée pour l'étape actuelle
	 */
	public List<Double> getVentesQuantiteEtapeActuelle(ChocolatDeMarque choco);
	
	/**
	 * Retourne la liste des prix vendus pour l'étape actuelle
	 */
	public List<Double> getVentesPrixEtapeActuelle(ChocolatDeMarque choco);
	
	/**
	 * Reset les listes d'étapes
	 */
	public void resetEtapeHashMaps();
	
	//GETTERS
	
	/**
	 * Retourne la quantitée totale des ventes de la dernière étape pour le @choco
	 */
	public Double getPreviousVenteQuantite(ChocolatDeMarque choco);
	
	/**
	 * Retourne le prix moyen des ventes du @choco de la dernière étape
	 */
	public Double getPreviousVentePrix(ChocolatDeMarque choco);
	
	/**
	 * Retourne la liste de toutes les quantites vendues pour chaque étape pour le chocolat @choco
	 */
	public List<Double> getVentesQuantite(ChocolatDeMarque choco);
	
	/**
	 * Retourne la liste de tous les prix de vente moyen du chocolat @choco pour chaque étape
	 */
	public List<Double> getVentesPrix(ChocolatDeMarque choco);
	
	/**
	 * Retourne la quantite vendue à l'étape @i du chocolat @choco
	 */
	public Double getVentesQuantiteEtape(ChocolatDeMarque choco,int i);
	
	/**
	 * Retourne le prix moyen à l'étape @i du chocolat @choco
	 */
	public Double getVentesPrixEtape(ChocolatDeMarque choco,int i);
}
