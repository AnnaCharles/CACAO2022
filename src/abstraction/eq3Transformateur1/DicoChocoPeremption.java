package abstraction.eq3Transformateur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.produits.Chocolat;
/*création d'une hashmap avec type de choclat, liste de lots (lot = quantité, date de péremption), auteur : anna*/
public class DicoChocoPeremption extends HashMap<Chocolat, ArrayList<Lot> >{

	
	public DicoChocoPeremption() {
		super();
		for (Chocolat c : Chocolat.values()) {
			this.put(c, new ArrayList<Lot>());
		}
	}
	
	// ajouter un lot juste après la transfo ; Julien
	public void ajoutLot(Chocolat c, Lot lot) {
		this.get(c).add(lot);
	}
	
	// supprimer le chocolat des ventes ; Julien
	public void venteLot(Chocolat c, double nbre) {
		while (nbre>0.) {
			if (this.get(c).size() == 0) { // c'est pour eviter les résidus : il y a parfois une legere diffrenece entre stockChoco et la somme des lots
				nbre = 0.;
			} else {
				if (this.get(c).get(0).getQuantite()>nbre) {
					double res=this.get(c).get(0).getQuantite()-nbre;
					this.get(c).get(0).setQuantite(res);
					nbre = 0.;
				} else { 
					nbre = nbre-this.get(c).get(0).getQuantite();
					this.get(c).remove(0);
				}
			}
		}		
	}
	// si la date de préremption est dépassée , supprime le lot du stock , auteur : anna
	public void supprimeLot(int etape, DicoChoco stockchoco) {
		for (Chocolat c : Chocolat.values()) {
			ArrayList<Lot> liste= new ArrayList<Lot>() ; // liste contenant les lots de de chocolat c
			liste = this.get(c); 
			for (int i = 0; i < liste.size(); i++) {
				//System.out.println("Lot de choco " + c + ", créé l'etape : " + liste.get(i).getDate() + ", avec une quantite : " + liste.get(i).getQuantite());
				if (Filiere.LA_FILIERE.getIndicateur("dureePeremption").getValeur()<=  Filiere.LA_FILIERE.getEtape()-liste.get(i).getDate()) {
					stockchoco.put(c, stockchoco.get(c)-liste.get(i).getQuantite());
					liste.remove(0);
			}
		}
			this.put(c,liste);
	}
	}
	public double getTotal(Chocolat c) {
		double sommequantite=0.;
		ArrayList<Lot> liste= new ArrayList<Lot>() ;
		liste = this.get(c); 
		for (int i = 0; i < liste.size(); i++) {
			sommequantite+=liste.get(i).getQuantite();
		}
		return sommequantite;
		
	}
}

