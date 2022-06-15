package abstraction.eq2Producteur2;

import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;

/**
 * 
 * @author Clément
 *
 */

public class Producteur2StockChoco extends Producteur2VendeurContratCadre{
	
	protected Integer cryptogramme;
	protected HashMap<Chocolat,LinkedList<Stock>> StocksChoco;
	
	
	
	public Producteur2StockChoco() {
		super();
		this.StocksChoco = new HashMap<Chocolat,LinkedList<Stock>>();
		for (Chocolat C : Chocolat.values()) {
			this.StocksChoco.put(C,new LinkedList<Stock>());
		}
	}
	
	
	public void removeQuantite(double q, Chocolat C) {
		LinkedList<Stock> L=this.StocksChoco.get(C);	

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
	
	public void addQuantite(double q, Chocolat C) {
		this.StocksChoco.get(C).add(new Stock(q));
	}
	
	public double getStockChoco(Chocolat choco) {
		double s = 0.0 ;
//		for(Stock stock : this.getStocksChoco().get(choco)) {
//			s = s + stock.getQuantite() ;
//			}
			return s ;	
		}
	public HashMap<Chocolat, LinkedList<Stock>> getStocksChoco() {
		return this.StocksChoco;
	}
	
	
	public void initialiser() {
		super.initialiser();
		this.addQuantite(3000, Chocolat.HQ_BE);
		}
		
		

	public void next() {
		super.next();
		this.addQuantite(this.production(Feve.FEVE_HAUTE_BIO_EQUITABLE)*0.05, Chocolat.HQ_BE);
		this.removeQuantite(this.production(Feve.FEVE_HAUTE_BIO_EQUITABLE)*0.05, Chocolat.HQ_BE);
		
		}
		

}
