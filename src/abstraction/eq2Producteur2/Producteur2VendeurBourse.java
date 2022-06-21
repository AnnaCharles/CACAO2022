package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq8Romu.bourseCacao.IVendeurBourse;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Feve;

public class Producteur2VendeurBourse extends Producteur2Transfo implements IVendeurBourse {

	
	private Journal journalBourse;
	

	public Producteur2VendeurBourse() {
		super();
		this.journalBourse= new Journal(this.getNom()+" Bourse", this);
	}

	public double offre(Feve f, double cours) {
		double quantiteAVendre = 0;
		if (cours> 1.3*this.getCout(f)) {
			quantiteAVendre= 0.6*this.getStock(f); // on vend 60% du stock;
		}
		if (cours> 1.2*this.getCout(f)) {
			quantiteAVendre  = 0.4*this.getStock(f); // on vend 40% du stock;
		}
		if (cours> 1.1*this.getCout(f)) {
			quantiteAVendre = 0.2*this.getStock(f); // on vend 20% du stock ;
		}
		return  quantiteAVendre;
	}


	public void notificationVente(Feve f, double quantiteEnKg, double coursEnEuroParKg) {
		this.journalBourse.ajouter("Vente de "+quantiteEnKg+" de "+f+" au prix de "+coursEnEuroParKg);
		this.removeQuantite(quantiteEnKg, f);
		
	}
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.classement);
		res.add(this.journal);
		res.add(journalCC);
		res.add(journalBourse);
		res.add(this.journalplantation);
		return res;
	}
}
