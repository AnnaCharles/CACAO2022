package abstraction.eq4Transformateur2;

import java.util.HashMap;
import java.util.Set;

import abstraction.eq5Transformateur3.DateProd;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;

////Marie
public abstract class DictionnairePeremptionTransfo2<Produit> extends Transformateur2ContratCadreVendeur{
	
	protected HashMap<DateProdTransfo2<Produit> ,Double> peremption ;
	protected Journal journalperemption;
	
	public DictionnairePeremptionTransfo2(){
		this.peremption= new HashMap<DateProdTransfo2<Produit>,Double>();
		this.journalperemption=new Journal("Peremption",this);
	}
	
	
	public Set<DateProdTransfo2<Produit>> getDates() {
		return this.peremption.keySet(); //renvoie (date,produit)
	}
	public double getQuant(double date, Produit produit) {
		DateProdTransfo2<Produit>d=new DateProdTransfo2<Produit>(date,produit);
		return this.peremption.get(d); //renvoie la quantité associé à (date,produit)
	}

	
	public void next() {
		super.next();
		this.modifDateProd();
		journalperemption.ajouter("on a :"+ this.quantTotaleProduit(Feve.FEVE_BASSE));
		
	}
	public Journal getJournalPeremption() {
		return this.journalperemption;
	}
	
	public void initialiser() {
		super.initialiser();
//		this.peremption=new HashMap<DateProdTransfo2<Produit> ,Double>();
		
	}


//// ajoute un nouveau produit à la Hashmap (lors d'un achat ou d'une trasnformation)
	public void ajouterQuant(double date, Object op, Double quant) {
		Produit p = (Produit)op;
		DateProdTransfo2<Produit>d=new DateProdTransfo2<Produit>(date,p);
		if(quant>0) {
			if(this.getDates().contains(d)) {
				this.peremption.put(d, this.getQuant(date, p)+quant);	// si produit à la même date existe déjà, on ajoute seulement la quantité du produit à la quantité déjà présente	
			}else {
				this.peremption.put(d, quant); // s'il n'existe pas de produit à la même date, on rajoute un élément à la Hashmap
			}
		}
	}

////Marie
////modifie la date des produits à chaque step
	public void modifDateProd() {
		for(DateProdTransfo2<Produit>d:this.getDates()) {
			d.setDate(d.getDate()+1);
		}
}
	
////Marie	
////retire le produit quand il a dépassé la date de péremption
	public void perime() {
		for(DateProdTransfo2<Produit>d:this.getDates()) {
			if(d.getDate()>12) {
				this.peremption.remove(d);
		}
	}
}

////Marie	
////modifications de la quantité lors d'une transformation (pour les fèves) ou d'une vente (chocolats)
//public void utiliserQuantProd(Produit p, double quant) {
//	double quantiteRestante=quant;
//	while(quantiteRestante!=0){
//		for(DateProdTransfo2<Produit>d:this.getDates()) { //on parcourt la Hashmap jusqu'à ce qu'on ait le produit (dans ordre décroissant)
//			if(d.getProduit()==p) {
//				double quantprod=this.peremption.get(d);
//				if(quantprod>quantiteRestante) { //quand la quant est suffisante, on retire seulement la quantité voulue
//					this.peremption.put(d,quantprod-quantiteRestante); 
//				}else {
//					this.peremption.remove(d); 
//					quantiteRestante=quantiteRestante-quantprod;
//				}
//			}
//		}
//	}
//}
//
public void utiliserQuantProd(Object op, double quant) {
	Produit p = (Produit)op;
	double quantiteRestante=quant;
	while(quantiteRestante!=0){
		for(DateProdTransfo2<Produit>d:this.getDates()) { //on parcourt la Hashmap jusqu'à ce qu'on ait le produit (dans ordre décroissant)
			if(d.getProduit()==p) {
				double quantprod=this.peremption.get(d);
				if(quantprod>quantiteRestante) { //quand la quant est suffisante, on retire seulement la quantité voulue
					this.peremption.put(d,quantprod-quantiteRestante); 
				}else {
					this.peremption.remove(d); 
					quantiteRestante=quantiteRestante-quantprod;
				}
			}
		}
	}
	}
	
////Marie
	public double quantTotaleProduit(Object op) { //calcule la quantité totale d'un produit
		Produit p = (Produit)op;
		double qtt=0;
		for(DateProdTransfo2<Produit>d:this.getDates()) {
			if(d.getProduit()==p) {
				qtt+=this.peremption.get(d);
			}
		}
		return qtt;
}
	
////Marie
	public double quantTotFeve() { //calcul la quantité totale de fèves
		double qtt=0;
		for(DateProdTransfo2<Produit>d:this.getDates()) {
			if(d.getProduit()==Feve.FEVE_BASSE || d.getProduit()==Feve.FEVE_HAUTE || d.getProduit()==Feve.FEVE_HAUTE_BIO_EQUITABLE ||d.getProduit()==Feve.FEVE_MOYENNE || d.getProduit()==Feve.FEVE_MOYENNE_BIO_EQUITABLE) {
				qtt+=quantTotaleProduit(d.getProduit());
			}
		return qtt;
	}
		return qtt;
}
		
////Marie
	public double quantTotChoco() { //calcul la quantité totale de chocolats 
		double qtt=0;
		for(DateProdTransfo2<Produit>d:this.getDates()) {
			if(d.getProduit()==Chocolat.BQ || d.getProduit()==Chocolat.BQ_O || d.getProduit()==Chocolat.HQ ||d.getProduit()==Chocolat.HQ_BE ||d.getProduit()==Chocolat.MQ ||d.getProduit()==Chocolat.MQ_BE  ) {
				qtt+=quantTotaleProduit(d.getProduit());
			}
		return qtt;
	}
		return qtt;
}
	
	
	public Stock<Chocolat> getStockchocolat() {
		// TODO Auto-generated method stub
		return null;
	}

	public Journal getJournalcours() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Journal getJournalStock() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Journal getJournalTransfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Journal getJournalAchat() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Stock<ChocolatDeMarque> getStockchocolatdemarque() {
		// TODO Auto-generated method stub
		return null;
	}
}
