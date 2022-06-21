package abstraction.eq4Transformateur2;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.Romu;
import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.bourseCacao.ExempleAcheteurBourseCacao;
import abstraction.eq8Romu.bourseCacao.ExempleVendeurBourseCacao;
import abstraction.eq8Romu.clients.ClientFinal;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IMarqueChocolat;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.Feve;

//Nawfel




public class Transformateur2 extends Transformateur2VenteAO{

	private Journal journalcours;
	
// Nawfel
	public void next() {
		super.next();
		journalcours.ajouter(Color.white,Color.red,"Cours Feve Basse : "+Filiere.LA_FILIERE.getIndicateur("BourseCacao cours B").getValeur() +" €/kg");
		journalcours.ajouter(Color.white,Color.red,"Cours Feve Moyenne : "+Filiere.LA_FILIERE.getIndicateur("BourseCacao cours M").getValeur() +" €/kg");
		journalcours.ajouter(Color.white,Color.red,"Cours Feve Moyenne BIO : "+Filiere.LA_FILIERE.getIndicateur("BourseCacao cours MBE").getValeur() +" €/kg");
		journalcours.ajouter(Color.white,Color.red,"Cours Feve Haute : "+Filiere.LA_FILIERE.getIndicateur("BourseCacao cours H").getValeur() +" €/kg");
		journalcours.ajouter(Color.white,Color.red,"Cours Feve Haute BIO : "+Filiere.LA_FILIERE.getIndicateur("BourseCacao cours HBE").getValeur() +" €/kg");
		journalcours.ajouter(Color.LIGHT_GRAY,Color.magenta,"-----------------------------------------------------------------------------------------------------------------------");
	}
	
// Nawfel 
	public void initialiser() {
		super.initialiser();
	}
	
// Nawfel
	public Transformateur2 () {
		super();
		this.journalcours=new Journal("O'ptiCours", this);
		
		
	}
	
// Marie
	public LinkedList<String> getMarquesChocolat() {
		LinkedList<String> res = new LinkedList<String>();
		res.add("O'ptella");// Bas de gamme
		res.add("O'max");// Moyen de gamme
		res.add("O'lait");// Moyen dde gamme bio
		res.add("O'Chock");// Haut de gamme 
		res.add("O'vert");// Haut de gamme bio
		return res;
	}
// Nawfel	
	public Journal getJournalcours() {
		return journalcours;
	}

	@Override
	public void utiliserQuantProd(Object p, double quant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ajouterQuant(double date, Object op, Double quant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Journal getJournalPeremption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getQuant(double date, Object op) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double quantTotaleProduit(Object op) {
		// TODO Auto-generated method stub
		return 0;
	}


}
	

