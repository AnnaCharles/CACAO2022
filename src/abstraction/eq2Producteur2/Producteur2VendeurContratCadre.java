package abstraction.eq2Producteur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Feve;

public abstract class Producteur2VendeurContratCadre extends Producteur2Acteur implements IVendeurContratCadre{
	
	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeurNonBio;
	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeurBio;
	protected HashMap<ExemplaireContratCadre,Integer> mesContratCadreExpire; //Contrat cadres expiré depuis moins de 100 next
	protected Journal classement;
	protected Journal journalCC;
	
	public Producteur2VendeurContratCadre() {
		super();
		this.mesContratEnTantQueVendeurNonBio = new LinkedList<ExemplaireContratCadre>();
		this.mesContratEnTantQueVendeurBio = new LinkedList<ExemplaireContratCadre>();
		this.classement=new Journal(this.getNom()+" classement", this);
		this.journalCC = new Journal("Cacao Doré Contrats Cadres", this);
		this.mesContratCadreExpire=new HashMap<ExemplaireContratCadre,Integer>();
	}
	
	/**
	 * @param transformateur
	 * @return Un nombre de point qui représente la quantité de fèves non Bio achetée par ce transformateur
	 */
	
	public double getPointTransformateur(IActeur transformateur) {
		double point=0.000;
		for (int i=0 ; i<this.mesContratEnTantQueVendeurNonBio.size() ; i++) {
			if(this.mesContratEnTantQueVendeurNonBio.get(i).getAcheteur().equals(transformateur)) //Selectionne les contrats cadres non bio équitable du transformateurs
				{ point+=this.mesContratEnTantQueVendeurNonBio.get(i).getQuantiteTotale(); // Point ajoute en fonction de la quantité
			}
		}	
		return point;
	}
	
	
	public List<IAcheteurContratCadre> getListeTransformateurContratCadre(){
		List<IAcheteurContratCadre> Liste= new ArrayList<IAcheteurContratCadre>();
		for (int i=0 ; i<this.mesContratEnTantQueVendeurNonBio.size() ; i++) {
			if (!Liste.contains(this.mesContratEnTantQueVendeurNonBio.get(i).getAcheteur())) {
				Liste.add(this.mesContratEnTantQueVendeurNonBio.get(i).getAcheteur());
			}
		}
	return Liste;
	}
	
	/**
	 * @param transformateur
	 * @return un classement du transformateur par rapport aux autres par rapport à l'achat de fèves non Bio
	 */
	
	public int getClassementTransformateur(IActeur transformateur) {
		int classement=1;
		if(!getListeTransformateurContratCadre().contains(transformateur)) {
			return 4;
		}
		else { for (int i=0 ; i < getListeTransformateurContratCadre().size() ; i++) {
			if(getPointTransformateur(transformateur)<getPointTransformateur(getListeTransformateurContratCadre().get(i))) {
				classement+=1;
				}
			}
		return classement;
		}
	}
	
	
	
	//Toute les fèves
	
	public boolean vend(Object produit) {
		return (produit instanceof Feve);
	}

		
	/**
	 * Methode pour le bio 
	 * @param produit
	 * @return la quantite par step vendu
	 */
	
	public double qtiteTotaleContratEnCours(Object produit) {
		double qtiteTotaleContratEnCours = 0;
		for ( int i=0; i<mesContratEnTantQueVendeurBio.size();i++) {
			if(mesContratEnTantQueVendeurBio.get(i).getProduit()==produit) {
			qtiteTotaleContratEnCours=mesContratEnTantQueVendeurBio.get(i).getQuantiteTotale()/mesContratEnTantQueVendeurBio.get(i).getEcheancier().getNbEcheances();
			}
		}	
		return qtiteTotaleContratEnCours;
	}
	
	/**
	 * Methode pour le non bio 
	 * @param produit
	 * @return la quantite par step vendu
	 */
	
	public double quantiteTotaleContratEnCours(Object produit) {
		double quantiteTotaleContratEnCours = 0;
		for ( int i=0; i<mesContratEnTantQueVendeurNonBio.size();i++) {
			if (mesContratEnTantQueVendeurNonBio.get(i).getProduit()==produit) {
			quantiteTotaleContratEnCours=mesContratEnTantQueVendeurNonBio.get(i).getQuantiteTotale()/mesContratEnTantQueVendeurNonBio.get(i).getEcheancier().getNbEcheances();
			}
		}
		return quantiteTotaleContratEnCours;
	}
	
	/**
	 * Methode pour le non bio 
	 */
	
	public Echeancier contrePropositionDuVendeurNonBio(ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Proposition de Contrat cadre avec l'échéancier : "+contrat.getEcheancier()+"Feve : "+contrat.getProduit());
		if (vend(contrat.getProduit())) {
			if (quantiteTotaleContratEnCours(contrat.getProduit()) + contrat.getQuantiteTotale()/contrat.getEcheancier().getNbEcheances() < this.production((Feve)(contrat.getProduit()))) { 
				this.journalCC.ajouter(Color.GREEN,Color.BLACK,"Echéancier accepté");
				return contrat.getEcheancier();
				}
			else {
				// On propose un echéancier avec les mêmes echéances et une quantité par echéances égale à 1/3 de ceux que l'on peut fournir
				Echeancier e = new Echeancier(contrat.getEcheancier().getStepDebut(),contrat.getEcheancier().getNbEcheances(),(this.production((Feve)(contrat.getProduit())) + this.getStock((Feve)(contrat.getProduit()))/contrat.getEcheancier().getNbEcheances())/3);
				this.journalCC.ajouter("Proposition d'un Nouvelle échéancier : "+e);
				return e;
			}
		}	
		 else {
			 	return null;// on ne vend pas de ce produit
		 }
	}

	/**
	 * Methode pour bio 
	 */
	
	public Echeancier contrePropositionDuVendeurBio(ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Proposition de Contrat cadre avec l'échéancier : "+contrat.getEcheancier()+"Feve : "+contrat.getProduit());
		if (vend(contrat.getProduit())) {
			if (qtiteTotaleContratEnCours(contrat.getProduit()) + contrat.getQuantiteTotale()/contrat.getEcheancier().getNbEcheances() < (this.production((Feve)contrat.getProduit()) + this.getStock((Feve)(contrat.getProduit()))/contrat.getEcheancier().getNbEcheances())) { 
				this.journalCC.ajouter(Color.GREEN,Color.BLACK,"Echéancier accepté");
				return contrat.getEcheancier();
				}
			else {
				// On propose un echéancier avec les mêmes echéances et une quantité par echéances égale à 1/3 de ceux que l'on peut fournir
				Echeancier e = new Echeancier(contrat.getEcheancier().getStepDebut(),contrat.getEcheancier().getNbEcheances(),(this.production((Feve)(contrat.getProduit())) + this.getStock((Feve)(contrat.getProduit()))/contrat.getEcheancier().getNbEcheances())/3);
				this.journalCC.ajouter("Proposition d'un Nouvelle échéancier : "+e);
				return e;
			}
		}	
		 else {
			 	return null;// on ne vend pas de ce produit
		 }
	}
	
	
	
	
	public double propositionPrixNonBio(ExemplaireContratCadre contrat) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		this.journalCC.ajouter("Proposition Prix : "+0.95*bourse.getCours((Feve)(contrat.getProduit())).getValeur());
		return 0.95*bourse.getCours((Feve)(contrat.getProduit())).getValeur();
	}

	
	public double contrePropositionPrixVendeurNonBio(ExemplaireContratCadre contrat) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		if (contrat.getQuantiteTotale()>12*(this.production((Feve)contrat.getProduit()))){ // Grosse commande, proposition de prix plus bas
			if (contrat.getPrix()>0.5 ) {
				return contrat.getPrix();}
			else {
				return 0.6*bourse.getCours((Feve)(contrat.getProduit())).getValeur() ; }
		}
		else { if(!(contrat.getListePrix().size()>4)) { // plus petite commande, pris plus élévée (4 negociations maximum)
			return 0.7*bourse.getCours((Feve)(contrat.getProduit())).getValeur() ;}
		}
		return -1.0;
	}

	public void notificationNouveauContratCadreNonBio(ExemplaireContratCadre contrat) {
		this.mesContratEnTantQueVendeurNonBio.add(contrat);
		
	}
	
	
	public double propositionPrixBio(ExemplaireContratCadre contrat) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double prix = 0.8*bourse.getCours((Feve)(contrat.getProduit())).getValeur();
		if (this.getClassementTransformateur( contrat.getAcheteur() )==1){
			prix = 0.5*bourse.getCours((Feve)(contrat.getProduit())).getValeur();
		}
		if (this.getClassementTransformateur(contrat.getAcheteur() )==2){
			prix = 0.6*bourse.getCours((Feve)(contrat.getProduit())).getValeur();
		}
		if (this.getClassementTransformateur( contrat.getAcheteur() )==3){
			prix = 0.7*bourse.getCours((Feve)(contrat.getProduit())).getValeur();
		}
		this.journalCC.ajouter("Proposition Prix : "+prix);
		return prix;
	}

	
	
	public double contrePropositionPrixVendeurBio(ExemplaireContratCadre contrat) {
		this.journalCC.ajouter("Proposition de l'acheteur : " +contrat.getPrix());
		double contrepropositionprix = -1.0;
		if (contrat.getPrix()>0.5*propositionPrix(contrat)) {
			if (getClassementTransformateur( contrat.getAcheteur() )==1){
				if (Math.random()<0.9) {
					contrepropositionprix = contrat.getPrix(); // on ne cherche pas a negocier dans 90% des cas
				}
			}
			if (getClassementTransformateur( contrat.getAcheteur() )==2){
				if (Math.random()<0.7) {
					contrepropositionprix = contrat.getPrix(); // on ne cherche pas a negocier dans 70% des cas
				}
			}	
			if (getClassementTransformateur( contrat.getAcheteur() )==3){
				if (Math.random()<0.5) {
					contrepropositionprix = contrat.getPrix(); // on ne cherche pas a negocier dans 50% des cas
				}
			}
			if (getClassementTransformateur( contrat.getAcheteur() )==4){
				if (Math.random()<0.3) {
					contrepropositionprix = contrat.getPrix(); // on ne cherche pas a negocier dans 30% des cas
				}
			}
		}
	
	 return contrepropositionprix;
	}


	/**
	 * Methodes gènèrales ppour tous les types de fèves, auteur : Jules
	 */
	
	public void notificationNouveauContratCadreBio(ExemplaireContratCadre contrat) {
		this.mesContratEnTantQueVendeurBio.add(contrat);

		
	}

	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		if((contrat.getProduit()==Feve.FEVE_HAUTE_BIO_EQUITABLE)||(contrat.getProduit()==Feve.FEVE_MOYENNE_BIO_EQUITABLE)) {
			return this.contrePropositionDuVendeurBio(contrat);
		}
		
		else{return this.contrePropositionDuVendeurNonBio(contrat);}
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		if((contrat.getProduit()==Feve.FEVE_HAUTE_BIO_EQUITABLE)||(contrat.getProduit()==Feve.FEVE_MOYENNE_BIO_EQUITABLE)) {
			return this.propositionPrixBio(contrat);
		}
		else{return this.propositionPrixNonBio(contrat);}
	}
	
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		if((contrat.getProduit()==Feve.FEVE_HAUTE_BIO_EQUITABLE)||(contrat.getProduit()==Feve.FEVE_MOYENNE_BIO_EQUITABLE)) {
			return this.contrePropositionPrixVendeurBio(contrat);
		}
		else{return this.contrePropositionPrixVendeurNonBio(contrat);}
	}

	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if((contrat.getProduit()==Feve.FEVE_HAUTE_BIO_EQUITABLE)||(contrat.getProduit()==Feve.FEVE_MOYENNE_BIO_EQUITABLE)) {
			 this.notificationNouveauContratCadreBio(contrat);
			 this.journalCC.ajouter(Color.GREEN,Color.BLACK,"Contrat Cadre Bio : " + " Acheteur " + contrat.getAcheteur()+ " Produit : " + contrat.getProduit() +" Quantité : "+contrat.getQuantiteTotale()+ " Prix : " + contrat.getPrix() );

		} 
		else {
			this.notificationNouveauContratCadreNonBio(contrat);
			this.journalCC.ajouter(Color.GREEN,Color.BLACK,"Contrat Cadre Non Bio : " + " Acheteur " + contrat.getAcheteur()+ " Produit : " + contrat.getProduit() + " Quantité : "+contrat.getQuantiteTotale()+" Prix : " + contrat.getPrix() );
		}
	}

	/**
	 * Methodes pour calculer proportion de vente des CC, auteur : Jules
	 */
	
	public double vente20DerniersNexts(Feve f) {
		Double vente = 0.0;
		for(ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeurBio) {
			if(contrat.getProduit().equals(f)) {
				vente+=contrat.getQuantiteTotale();
			}
		}
		for(ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeurNonBio) {
			if(contrat.getProduit().equals(f)) {
				vente+=contrat.getQuantiteTotale();
			}
		}
		for (Entry<ExemplaireContratCadre, Integer> m : mesContratCadreExpire.entrySet()) {
			if(m.getKey().getProduit().equals(f)) {
				vente+=m.getKey().getQuantiteTotale();
			}
		}
		return vente;
	}
	
	public double proportionVente(Feve f) {
		Double totale = 0.0001;
		for (Feve feve : Feve.values()) {
			totale+=this.vente20DerniersNexts(feve);
		}
	return (this.vente20DerniersNexts(f)/totale);
	}

	
	/**
	 * Methode Next 
	 */
	
	public void next() {
		super.next();
		
		// Ajout des contrats expires mais à prendre en compte, ie expire il y a moins de 20 steps
		List<ExemplaireContratCadre> contratsBio=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeurBio) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				this.mesContratCadreExpire.put(contrat,Filiere.LA_FILIERE.getEtape());
				contratsBio.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeurBio.removeAll(contratsBio);
	
		List<ExemplaireContratCadre> contratsNonBio=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeurNonBio) {
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				this.mesContratCadreExpire.put(contrat,Filiere.LA_FILIERE.getEtape());
				contratsNonBio.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeurNonBio.removeAll(contratsNonBio);
		
		List<ExemplaireContratCadre> contratsExpire=new LinkedList<ExemplaireContratCadre>();
		for (Entry<ExemplaireContratCadre, Integer> m : mesContratCadreExpire.entrySet()) {
			if(Filiere.LA_FILIERE.getEtape()- m.getValue()> 20) {
				contratsExpire.add(m.getKey());
			}
		}
		for(ExemplaireContratCadre contrat : contratsExpire) {
			this.mesContratCadreExpire.remove(contrat);
		}

		/**
		 * Affichage Journal  
		 */

		for(IActeur a : Filiere.LA_FILIERE.getActeursSolvables()) {
			if (a instanceof IFabricantChocolatDeMarque) {
				this.classement.ajouter(a.getColor(),Color.BLACK,a.getNom()+" : "+this.getClassementTransformateur(a)+", "+this.getPointTransformateur(a));
			}
		}
		
		this.journalCC.ajouter("Quantité par step de Feve HAUTE BIO EQUITABLE : "+this.qtiteTotaleContratEnCours(Feve.FEVE_HAUTE_BIO_EQUITABLE )+" Vente sur les 50 dernier next "+proportionVente(Feve.FEVE_HAUTE_BIO_EQUITABLE));
		this.journalCC.ajouter("Quantité par step de Feve MOYENNE BIO EQUITABLE : "+this.qtiteTotaleContratEnCours(Feve.FEVE_MOYENNE_BIO_EQUITABLE)+" Vente sur les 50 dernier next "+proportionVente(Feve.FEVE_MOYENNE_BIO_EQUITABLE));
		this.journalCC.ajouter("Quantité par step de Feve HAUTE Non Bio : "+this.quantiteTotaleContratEnCours(Feve.FEVE_HAUTE)+" Vente sur les 50 dernier next "+proportionVente(Feve.FEVE_HAUTE));
		this.journalCC.ajouter("Quantité par step de Feve MOYENNE Non BIO  : "+this.quantiteTotaleContratEnCours(Feve.FEVE_MOYENNE)+" Vente sur les 50 dernier next "+proportionVente(Feve.FEVE_MOYENNE));
		this.journalCC.ajouter("Quantité par step de Feve BASSE Non BIO  : "+this.quantiteTotaleContratEnCours(Feve.FEVE_BASSE)+" Vente sur les 50 dernier next "+proportionVente(Feve.FEVE_BASSE));
		this.journalCC.ajouter("=======================================================================================");

	}
	
	public double livrer(Object produit, double quantite, ExemplaireContratCadre contrat) {
		this.removeQuantite(quantite, (Feve)(produit));
		return quantite;
	}

	public void initialiser() {
		super.initialiser();
	}

	public boolean peutVendre(Object produit) {
		// TODO Auto-generated method stub
		return true;
	}





	
}
