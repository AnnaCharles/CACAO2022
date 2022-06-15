package abstraction.eq6Distributeur1;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.ExempleTransformateurContratCadreVendeurAcheteur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.filiere.IMarqueChocolat;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

public class ExempleTransformateurVendeurContratCadre extends ExempleTransformateurContratCadreVendeurAcheteur
		implements IMarqueChocolat, IFabricantChocolatDeMarque {

	private Chocolat choco;
	private String marque;

	public ExempleTransformateurVendeurContratCadre(ChocolatDeMarque produit) {
		super(produit);
		choco = produit.getChocolat();
		marque = produit.getMarque();
	}

	@Override
	public List<String> getMarquesChocolat() {
		List<String> marques = new ArrayList<String>();
		marques.add(marque);
		return marques;
	}

	@Override
	public List<ChocolatDeMarque> getChocolatsProduits() {
		List<ChocolatDeMarque> chocos = new ArrayList<ChocolatDeMarque>();
		chocos.add(new ChocolatDeMarque(choco, marque));
		return chocos;
	}

}
