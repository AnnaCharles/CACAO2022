package abstraction.eq8Romu.bourseCacao;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.Feve;

public class ExempleAbsAcheteurBourseCacao implements IActeur {
	private static int NB_INSTANCES = 0; // Afin d'attribuer un nom different a toutes les instances
	protected int numero;
	protected Integer cryptogramme;
	protected Journal journal;
	protected Variable stockFeve;
	protected Feve feve;

	public ExempleAbsAcheteurBourseCacao(Feve feve, double stock) {	
		if (feve==null ||stock<0) {
			throw new IllegalArgumentException("creation d'une instance de ExempleAbsAcheteurBourseCacao avec des arguments non valides");
		}		
		NB_INSTANCES++;
		this.numero=NB_INSTANCES;
		this.stockFeve=new Variable(this.getNom()+"Stock"+feve, this, 0.0, 1000000000.0,stock);
		this.feve = feve;
		this.journal = new Journal(this.getNom()+" activites", this);
	}

	protected Feve getFeve() {
		return this.feve;
	}
	public String toString() {
		return this.getNom();
	}
	public String getNom() {
		return "A.Bourse"+this.numero;
	}

	public String getDescription() {
		return "Acheteur de feve a la bourse du cacao "+this.numero;
	}

	public Color getColor() {
		return new Color(64+((numero)*(157/NB_INSTANCES)), 128+((numero)*(64/NB_INSTANCES)), 0);
	}

	public void initialiser() {
	}

	public void next() {
		journal.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
			journal.ajouter("le cours de "+feve+" est : "+Journal.doubleSur(bourse.getCours(feve).getValeur(), 2));
		}
	}

	public List<String> getNomsFilieresProposees() {
		return new ArrayList<String>();
	}

	public Filiere getFiliere(String nom) {
		return null;
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res=new ArrayList<Variable>();
		res.add(stockFeve);
		return res;
	}

	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	public List<Journal> getJournaux() {
		List<Journal> j= new ArrayList<Journal>();
		j.add(this.journal);
		return j;
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public void notificationFaillite(IActeur acteur) {
	}

	public void notificationOperationBancaire(double montant) {
	}
	
	protected Variable getStock() {
		return this.stockFeve;
	}
}
