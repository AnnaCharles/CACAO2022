package abstraction.eq6Distributeur1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import abstraction.eq8Romu.clients.ClientFinal;
import abstraction.eq8Romu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.general.VariableReadOnly;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

public class DistributeurChocolatDeMarque extends MarqueDistributeur1 implements IDistributeurChocolatDeMarque{

	private Map<ChocolatDeMarque, Double> teteGondole; // (nom du chocolat,% en tête de gondole), Emma Humeau 
	private double qteEnVenteTG; //Emma Humeau
	protected VariableReadOnly totalVente; // Nathan

	/**
	 * @author Nathan
	 */
	public DistributeurChocolatDeMarque() {
		super();
		totalVente = new VariableReadOnly("Total des ventes", this, 0);
		teteGondole = new HashMap<ChocolatDeMarque, Double>();
	}

	/**
	 * @author Nathan
	 * @return la liste des indicateurs faite dans Distributeur1Acteur et la variable totalVente
	 */
	@Override
	public List<Variable> getIndicateurs() {
		List<Variable> l = super.getIndicateurs();
		l.add(totalVente);
		return l;
	}

	/**
	 * @author Nathan
	 * @return La liste des journaux renvoyée dans Distributeur1Acteur et le journal de vente
	 */
	// @Override
	// public List<Journal> getJournaux() {
	// 	List<Journal> j = super.getJournaux();
	// 	j.add(JournalVente);
	// 	return j;
	// }

	/**
	 * @return the teteGondole
	 */
	public Map<ChocolatDeMarque, Double> getTeteGondole() { //Emma Humeau
		return teteGondole;
	}

	/**
	 * @param teteGondole the teteGondole to set
	 */
	public void setTeteGondole(Map<ChocolatDeMarque, Double> teteGondole, ChocolatDeMarque choco) { //Emma Humeau
		double qteDispoEnTg = NotreStock.qteStockageTotale()*0.1 - qteEnVenteTG ; 
		if (qteEnVenteTG <= NotreStock.qteStockageTotale()*0.1) {  //la quantité de choco en vente en TG doit etre inférieure à 10% du stock
			teteGondole.put(choco, qteDispoEnTg );}
	}

	@Override
	public double prix(ChocolatDeMarque choco) { //Emma humeau
		return facteurPrixChocolat(choco.getChocolat()) * 10;
		// Double res = prixVente.get(choco);
		// return (res == null) ? 0.0 : res;
	}

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) { //Emma Humeau, Nathan
		double qte =  NotreStock.getStock(choco);
		return qte;
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) { //Emma humeau
		//calcule la qte en vente en tete de gondole de tous les chocolats

				double sumqteTG = 0;

				for (Map.Entry<ChocolatDeMarque, Double > entry : teteGondole.entrySet()) {
		           Double ValueQte = entry.getValue();
		           sumqteTG += ValueQte;
				}

				this.qteEnVenteTG = sumqteTG;

		return qteEnVenteTG;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) { //emma Humeau
		NotreStock.addQte(choco, -quantite);
		totalVente.ajouter(this, quantite, crypto);
		HistoChoco.get(choco).ajouter(this,quantite,crypto); // Léo Historique qui mémorise les ventes
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) { //Emma Humeau, Nathan
			JournalVente.ajouter("Rayon vide pour " + choco);
	}

}