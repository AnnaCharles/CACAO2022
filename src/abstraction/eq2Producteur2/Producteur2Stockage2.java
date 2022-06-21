package abstraction.eq2Producteur2;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

import abstraction.eq8Romu.produits.Feve;


/**
 * 
 * @author Clément
 *
 */

public abstract class Producteur2Stockage2 extends Producteur2Couts {
	
	protected Integer cryptogramme;
	protected HashMap<Feve,LinkedList<Stock>> Stocks;

	
	public Producteur2Stockage2() {
		// auteur : Clément 
		super();
		
		this.Stocks = new HashMap<Feve,LinkedList<Stock>>();
		for (Feve f : Feve.values()) {
			this.Stocks.put(f,new LinkedList<Stock>());
		}
	}

	public void removeQuantite(double q, Feve f) {
		// auteur : Clément 
		LinkedList<Stock> L=this.Stocks.get(f);	

		for (int k=0;k<30;k++) {
			int m=0;
			for (int i=0 ; i<L.size() ; i++) {
				if ((L.get(i)).getStep_arrivee()<L.get(m).getStep_arrivee() && L.get(i).getQuantite()>0) {
					m=i;
				}			
			}
			if ((L.get(m)).getQuantite()>q) {
				(L.get(m)).removequantite(q);
				q=0;//q=000000
			}else {
				double r = (L.get(m)).getQuantite();
				(L.get(m)).removequantite(r);
				q=q-r;				
			}
		}	

	}
	
	public void addQuantite(double q, Feve f) {
		// auteur : Clément 
		this.Stocks.get(f).add(new Stock(q));
	}
	
	public double getStock(Feve feve) {
		double s = 0.0 ;
		for(Stock stock : this.getStocks().get(feve)) {
			s = s + stock.getQuantite() ;
			}
			return s ;	
		}
	
	
	public void initialiser() {
		super.initialiser();
		for (Feve feve : Feve.values()) {
			int qt = super.getNbArbreTotal(feve);
			this.addQuantite(qt, feve);
		}
		
		
	}

	public void next() {
		// auteur : Clément 
		super.next();
		for(Feve feve : Feve.values()) {
			this.addQuantite(this.production(feve), feve);
		}
		this.Peremption();
		this.Parasites();
	}


	public HashMap<Feve, LinkedList<Stock>> getStocks() {
		// auteur : Clément 
		return this.Stocks;
	}
	public void Peremption() {
		// auteur : Clément 
		for (Feve f : Feve.values()) {    						//On retire les fèves périmées 
			for (int i=0;i<this.Stocks.get(f).size();i++) {
				if (this.Stocks.get(f).get(i).isPerime()==true) {
					this.Stocks.get(f).remove(i);
				}
			}
		}
	}
	
	public void Parasites() {
		// auteur : Clément 
		
		
		// Differentiation entre gammes / bio :
		// bio : 20% par UT
		// non bio : 10% par UT
		// 3 niveaux de parasites
		// 1 : 70 %
		// 2 : 25%
		// 3 : 5%
		// 1 : Perte de 10% de la récolte
		// 2 : Perte de 50% de la récolte
		// 3 : Perte de 80% de la récolte
		
		double aleaParaBio = Math.random();
		double aleaParaNonBio = Math.random();
		double aleaPerteBio = Math.random();
		double aleaPerteNonBio = Math.random();
		double PerteBio = 0.0;
		double PerteNonBio = 0.0;
		boolean ParaBio=(aleaParaBio<0.2);
		boolean ParaNonBio=(aleaParaNonBio<0.1);
		
		if (ParaNonBio==true) {
			if (aleaPerteNonBio <= 0.05) {
				PerteNonBio=0.8;
			}
			if (aleaPerteNonBio <= 0.30 && aleaPerteNonBio>0.05) {
				PerteNonBio=0.5;
			}
			if (aleaPerteNonBio > 0.30) {
				PerteNonBio=0.1;
			}
		}
		if (ParaBio==true) {
			if (aleaPerteBio <= 0.05) {
				PerteBio=0.8;
			}
			if (aleaPerteBio <= 0.30 && aleaPerteBio>0.05) {
				PerteBio=0.5;
			}
			if (aleaPerteBio > 0.30) {
				PerteBio=0.1;
			}
		}
		for (Feve f : Feve.values()) {
			if (f.isBioEquitable()==true) {
				for (int i=0;i<this.Stocks.get(f).size();i++) {
					this.Stocks.get(f).get(i).pertetaux(PerteBio);	
				}		
			}
			else {
				for (int i=0;i<this.Stocks.get(f).size();i++) {
					this.Stocks.get(f).get(i).pertetaux(PerteNonBio);
				}		
			}
		}
	}
}

	

