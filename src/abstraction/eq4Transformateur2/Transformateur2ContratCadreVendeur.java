package abstraction.eq4Transformateur2;


import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.contratsCadres.IAcheteurContratCadre;
import abstraction.eq8Romu.contratsCadres.IVendeurContratCadre;
import abstraction.eq8Romu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.filiere.IActeur;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;

//Marie

public abstract class Transformateur2ContratCadreVendeur extends Transformateur2Acteur implements IVendeurContratCadre{
	
	public abstract Stock<ChocolatDeMarque> getStockchocolatdemarque();
	
	protected SuperviseurVentesContratCadre supCCadre;
	protected List<ExemplaireContratCadre> mesContratEnTantQueVendeur;
	private Journal journalVente;
	
	
	


	public void next() {
		super.next();
		journalVente.ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		journalVente.ajouter(Color.black,Color.white,"----------------------------------CONTRATS CADRES DU STEP------------------------------------------------------");
		journalVente.ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		List<ExemplaireContratCadre> contratsObsoletes=new LinkedList<ExemplaireContratCadre>();
		for (ExemplaireContratCadre contrat : this.mesContratEnTantQueVendeur) {
			// A chaque tour, un contrat devient obsolète si la quantité restante à livrer est nulle
			if (contrat.getQuantiteRestantALivrer()==0.0 && contrat.getMontantRestantARegler()==0.0) {
				contratsObsoletes.add(contrat);
			}
		}
		this.mesContratEnTantQueVendeur.removeAll(contratsObsoletes);
		for (ChocolatDeMarque c : this.getChocolatsProduits()) {
			for (IActeur acteur : Filiere.LA_FILIERE.getActeurs()) {
				if (acteur!=this && acteur instanceof IAcheteurContratCadre && ((IAcheteurContratCadre)acteur).achete(c)) {
					journalVente.ajouter("Négociations avec "+acteur.getNom() +" pour "+c);


					if ((this.getStockchocolatdemarque().getQuantite(c)*0.1)>1000){
						ExemplaireContratCadre cc = supCCadre.demandeVendeur((IAcheteurContratCadre)acteur, (IVendeurContratCadre)this, (Object)c, new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 10, (this.getStockchocolatdemarque().getQuantite(c)*0.1)/10), cryptogramme,false);
					
						journalVente.ajouter("-->aboutit au contrat "+cc);
						if (cc!=null) 
						{
							this.mesContratEnTantQueVendeur.add(cc);
						}
					}
					journalVente.ajouter(Color.white,Color.red,"----------------------------------------------------------------------------------------------");
				}
			}
		}


	}
	
	
	public void initialiser() {
		super.initialiser();
	
		this.supCCadre = (SuperviseurVentesContratCadre) (Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	
	
	public Transformateur2ContratCadreVendeur() {
		super();
		this.mesContratEnTantQueVendeur=new LinkedList<ExemplaireContratCadre>();
		this.journalVente=new Journal("O'ptites Ventes", this);
	}
	
	

public boolean vend(Object produit) {
	// TODO Auto-generated method stub
	if (!(produit instanceof ChocolatDeMarque)) {
		return false;
	} else {
		return this.getChocolatsProduits().contains(produit);
	}
}

@Override
public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
	
	if (this.getChocolatsProduits().contains(contrat.getProduit())) {
		if (contrat.getEcheancier().getQuantiteTotale()<this.getStockchocolatdemarque().getStocktotal()) {
			journalVente.ajouter("Echeancier accepté");
			return contrat.getEcheancier();
		} else {
			return null; // on est frileux : on ne s'engage dans un contrat cadre que si on a toute la quantite en stock (on pourrait accepter meme si nous n'avons pas tout car nous pouvons produire/acheter pour tenir les engagements) 
		}
	} else {
		return null;// Nous ne vendons pas de ce produit
	}
}

public double prixVoulu(ExemplaireContratCadre contrat) {
	if (contrat.getProduit().equals(this.getChocolatsProduits().get(0))) {
		return this.prixVouluB();
	} else if(contrat.getProduit().equals(this.getChocolatsProduits().get(1))){
		return this.prixVouluM();
		
	} else if(contrat.getProduit().equals(this.getChocolatsProduits().get(2))){
		return this.prixVouluMb();
	} else if(contrat.getProduit().equals(this.getChocolatsProduits().get(3))){
		return this.prixVouluH();
	} else {
		return this.prixVouluHb();
	}
}

@Override
public double propositionPrix(ExemplaireContratCadre contrat) {
	return this.prixVoulu(contrat);
	
}

@Override
public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
	// TODO Auto-generated method stub
	if (contrat.getPrix()<0.3*this.prixVoulu(contrat)) {
		journalVente.ajouter("Prix trop bas : Rupture du contrat");
		return 0.0; //On arrete les négociations si son prix au kg
	} else if(contrat.getPrix()>propositionPrix(contrat)) {
		return contrat.getPrix();
	} else {
		return (contrat.getPrix()+propositionPrix(contrat))/2; //On coupe la poire en 2
	}
	
}

@Override
public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
	// TODO Auto-generated method stub
	this.mesContratEnTantQueVendeur.add(contrat);
	journalVente.ajouter("Contrat signé avec " +contrat.getAcheteur().getNom()+ ". Livraison prévue de "+contrat.getQuantiteTotale()+" de"+contrat.getProduit());
	
}

@Override
public double livrer(Object produit, double quantite, ExemplaireContratCadre contrat) {
	// TODO Auto-generated method stub
	return quantite;
}


public List<ExemplaireContratCadre> getMesContratEnTantQueVendeur() {
	return mesContratEnTantQueVendeur;
}


public Journal getJournalVente() {
	return journalVente;
}
public double prixVouluB() { // Basse qualité 
	 return (this.prixMinB.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeCC; 	 
}
	
public double prixVouluM() {  // Moyenne qualité
	 return (this.prixMinM.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeCC;
}

public double prixVouluMb() {  // Moyenne qualité bio
	 return (this.prixMinMb.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeCC;
}

public double prixVouluH() {   // Haute qualité
	 return (this.prixMinH.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeCC;
}

public double prixVouluHb() { // Haute qualité bio
	 return (this.prixMinHb.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeCC;
}



}
