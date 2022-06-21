package abstraction.eq4Transformateur2;

import java.util.List;


import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.bourseCacao.ExempleAbsAcheteurBourseCacao;
import abstraction.eq8Romu.bourseCacao.IAcheteurBourse;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.filiere.IFabricantChocolatDeMarque;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.general.Variable;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Gamme;



public abstract class Transformateur2Bourse extends Transformateur2Transfo implements IAcheteurBourse{
	
	private Journal journalAchat;
	
	
// Marie et Jad
	public Transformateur2Bourse() {
		super();
		this.journalAchat=new Journal("O'ptits Achats",this);
		
	}
// Marie et Jad 
	public void initialiser() {
		super.initialiser();
	}

// Gabriel
	public void next() {
		super.next();
	// A partir de l'étape deux, nous basons nos prix de vente sur les cours de la bourse. Pour chacune des
	// fèves, nous faisons alors une moyenne sur les trois derniers step qui définit le prix minimal unitaire
	// brut par fèves.
		
	if(Filiere.LA_FILIERE.getEtape()>2) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double somme1 = 0;
		for (int i=0; i<3;i++ ) {
			somme1 = somme1 +  bourse.getCours(Feve.FEVE_BASSE).getValeur(Filiere.LA_FILIERE.getEtape()-3+i);
		}
		this.prixMinB.setValeur(superviseur, somme1/3); 
		double somme2 = 0;
		for (int i=0; i<3;i++ ) {
			somme2 = somme2 +  bourse.getCours(Feve.FEVE_MOYENNE).getValeur(Filiere.LA_FILIERE.getEtape()-3+i);
		}
		this.prixMinM.setValeur(superviseur, somme2/3);
		double somme3 = 0;
		for (int i=0; i<3;i++ ) {
			somme3 = somme3 +  bourse.getCours(Feve.FEVE_MOYENNE_BIO_EQUITABLE).getValeur(Filiere.LA_FILIERE.getEtape()-3+i);
		}
		this.prixMinMb.setValeur(superviseur, somme3/3);
		double somme4 = 0;
		for (int i=0; i<3;i++ ) {
			somme4 = somme4 +  bourse.getCours(Feve.FEVE_HAUTE).getValeur(Filiere.LA_FILIERE.getEtape()-3+i);
		}
		this.prixMinH.setValeur(superviseur, somme4/3);
		double somme5 = 0;
		for (int i=0; i<3;i++ ) {
			somme5 = somme5 +  bourse.getCours(Feve.FEVE_HAUTE_BIO_EQUITABLE).getValeur(Filiere.LA_FILIERE.getEtape()-3+i);
		}
		this.prixMinHb.setValeur(superviseur, somme5/3);
	}
	this.getJournalcours().ajouter("La moyenne des cours pour la fève basse sur les trois derniers mois est de "+ this.prixMinB.getValeur() + " €.");
	this.getJournalcours().ajouter("La moyenne des cours pour la fève moyenne sur les trois derniers mois est de "+ this.prixMinM.getValeur()+ " €.");
	this.getJournalcours().ajouter("La moyenne des cours pour la fève moyenne bioéquitable sur les trois derniers mois est de "+ this.prixMinMb.getValeur()+ " €.");
	this.getJournalcours().ajouter("La moyenne des cours pour la fève haute sur les trois derniers mois est de "+ this.prixMinH.getValeur()+ " €.");
	this.getJournalcours().ajouter("La moyenne des cours pour la fève haute bioéquitable sur les trois derniers mois est de "+ this.prixMinHb.getValeur()+ " €.");
	this.journalAchat.ajouter("---------------------------------------------------------------------------------------------------------------");
	}

// Gabriel
	public double demande(Feve f, double cours) {
		// Définition du premier critère : le besoin
		// Nous souhaitons faire une demande en bourse lorsque le stock actuel est plus faible que le stock
					// de référence que nous nous sommes fixé
		double besoin=Math.max(0.001,this.getStockReferenceFeve().getQuantite(f)-this.getStockfeve().getQuantite(f));
		
		if(besoin>0) {
			if(Filiere.LA_FILIERE.getBanque().verifierCapacitePaiement(this, this.cryptogramme, cours*besoin)) { 
			// Vérification de la capacité de paiement pour ne pas être blacklisté
				
				if(f.getGamme()== Gamme.BASSE) {
					if(cours<this.prixMinB.getValeur()) {
							return besoin;
					}
				}
				if(f.getGamme()== Gamme.MOYENNE){
					if(f.isBioEquitable()) {
						if(cours<this.prixMinMb.getValeur()) {
							return besoin;
						}
					}
					else {
						if(cours<this.prixMinM.getValeur()){
							return besoin;
						}
					}
				}
				if(f.getGamme()== Gamme.HAUTE){
					if(f.isBioEquitable()) {
						if(cours<this.prixMinHb.getValeur()) {
							return besoin;
						}
					}
					else {
						if(cours<this.prixMinH.getValeur()){
							return besoin;
						}
					}
				}
			
			}
		}
		return 0.0; // Si l'un des critères n'est pas rempli, pas de demande
	}
	
// Marie et Jad
		public void notificationAchat(Feve f, double quantiteEnKg, double coursEnEuroParKg) {
		this.getStockfeve().ajouter(f,quantiteEnKg); // Actualisation du stock en cas d'achat
		this.getAchat_feve().ajouter(f,quantiteEnKg);
		Filiere.LA_FILIERE.getBanque().virer(this, this.cryptogramme, Filiere.LA_FILIERE.getBanque(), coursEnEuroParKg*quantiteEnKg);
		this.getJournalAchat().ajouter("Achat de "+quantiteEnKg+" kg de fèves "+f+" pour "+coursEnEuroParKg +" €/kg");
	}

// Marie et Jad	
	public void notificationBlackList(int dureeEnStep) {
		this.journalAchat.ajouter("Aie... je suis blackliste... Nous sommes pauvres... :(");
		
	}
// Nawfel 
	public Journal getJournalAchat() {
		return journalAchat;
	}
	
}