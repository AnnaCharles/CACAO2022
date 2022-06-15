package abstraction.eq7Distributeur2.tools;

import java.util.List;

import abstraction.eq8Romu.produits.ChocolatDeMarque;

public interface IDemande {
	
	
	/**
	 * Retourne la demande restante à combler pour le chocolat choco
	 * @param choco
	 */
	double get(ChocolatDeMarque choco);
	
	/**
	 * Définie la demande restante à combler pour le chocolat choco à valeur
	 * @param choco
	 * @param valeur
	 */
	void set(ChocolatDeMarque choco,double valeur);
	
	/**
	 * Retire de la demande restante à combler pour le chocolat choco la valeur valeur
	 * @param choco
	 * @param valeur
	 */
	void remove(ChocolatDeMarque choco,double valeur);
	
	/**
	 * Initialise la demande à zero
	 */
	void initialiserZero(List<ChocolatDeMarque> listeChocolatsProduits);
				
}
