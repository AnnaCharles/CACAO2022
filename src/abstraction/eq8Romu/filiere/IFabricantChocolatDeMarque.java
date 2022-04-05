package abstraction.eq8Romu.filiere;

import java.util.List;

import abstraction.eq8Romu.produits.ChocolatDeMarque;

/**
 * Interface implementee par tous les acteurs produisant des chocolats de marque
 * @author Romuald DEBRUYNE
 *
 */
public interface IFabricantChocolatDeMarque extends IActeur {
	/**
	 * @return Retourne la liste de toutes les sortes de ChocolatDeMarque que l'acteur produit 
	 * et peut vendre.
	 */
	public List<ChocolatDeMarque> getChocolatsProduits();

}
