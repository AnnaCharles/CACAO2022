package abstraction.eq2Producteur2;

import abstraction.eq8Romu.produits.Feve;
import java.util.HashMap;

/**
 * 
 * @author Jules DORE
 *
 */

public abstract class Producteur2Couts extends Producteur2Plantation {

	public static double coutHQ_BE=0.001; //coût par arbre à revoir
	public static double coutMQ_BE=0.001;
	public static double coutHQ=0.001;
	public static double coutMQ=0.001;
	public static double coutBQ=0.001;
	
	public HashMap<Feve,Double> coutParKg;
	
	public Producteur2Couts() {
		super();
		this.coutParKg=new HashMap<Feve,Double>();;
	}

	public double getCout(Feve feve) {
		return this.coutParKg.get(feve);
	}

	public void setCoutParKg() {
		this.coutParKg.put(Feve.FEVE_HAUTE_BIO_EQUITABLE, (this.coutHQ_BE*this.getNbArbreTotal(Feve.FEVE_HAUTE_BIO_EQUITABLE))/this.production(Feve.FEVE_HAUTE_BIO_EQUITABLE));
		this.coutParKg.put(Feve.FEVE_HAUTE, (this.coutHQ*this.getNbArbreTotal(Feve.FEVE_HAUTE))/this.production(Feve.FEVE_HAUTE));
		this.coutParKg.put(Feve.FEVE_MOYENNE_BIO_EQUITABLE, (this.coutMQ_BE*this.getNbArbreTotal(Feve.FEVE_MOYENNE_BIO_EQUITABLE))/this.production(Feve.FEVE_MOYENNE_BIO_EQUITABLE));
		this.coutParKg.put(Feve.FEVE_MOYENNE, (this.coutMQ*this.getNbArbreTotal(Feve.FEVE_MOYENNE))/this.production(Feve.FEVE_MOYENNE));
		this.coutParKg.put(Feve.FEVE_BASSE, (this.coutBQ*this.getNbArbreTotal(Feve.FEVE_BASSE))/this.production(Feve.FEVE_BASSE));
	}
	
	
	public void next() {
		super.next();
		this.setCoutParKg();
		}
	
	public void initialiser() {
		super.initialiser();
	}
}