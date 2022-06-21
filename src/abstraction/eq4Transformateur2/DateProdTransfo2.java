package abstraction.eq4Transformateur2;

import java.util.Objects;

public class DateProdTransfo2<Produit> { //classe contruite pour avoir une Hashmap avec la date,le produit et la quantité
	protected double date;
	protected Produit p;

//Marie
	public DateProdTransfo2(){
		date=0;
	}
	
	public DateProdTransfo2(double date,Produit p) {
		this.date=date;
		this.p=p;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(date, p);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateProdTransfo2 other = (DateProdTransfo2) obj;
		return Double.doubleToLongBits(date) == Double.doubleToLongBits(other.date) && Objects.equals(p, other.p);
	}

	public Produit getProduit() {
		return this.p;
	}
	public double getDate() {
		return this.date;
	}
	public void setProduit(Produit p) {
		this.p=p;
	}
	public void setDate(double date) {
		this.date=date;
	}
	
}
