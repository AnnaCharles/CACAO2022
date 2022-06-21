package abstraction.eq4Transformateur2;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import abstraction.eq8Romu.appelsOffres.IVendeurAO;
import abstraction.eq8Romu.appelsOffres.PropositionAchatAO;
import abstraction.eq8Romu.appelsOffres.SuperviseurVentesAO;
import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.contratsCadres.ExemplaireContratCadre;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Chocolat;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Feve;

public abstract class Transformateur2VenteAO extends Transformateur2Bourse implements IVendeurAO {
	protected SuperviseurVentesAO superviseur;

 
// Gabriel
 public Transformateur2VenteAO() {
		super();
	}
 
// Gabriel
	public void initialiser() {
		super.initialiser();
		this.superviseur = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));

	}
	
// Gabriel
	public void next() { 
		super.next();
		this.getJournalVente().ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		this.getJournalVente().ajouter(Color.black,Color.white,"----------------------------------APPELS D'OFFRES DU STEP------------------------------------------------------");
		this.getJournalVente().ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		double onDoitLivrer = 0;
		
		// Pour chaque chocolat de marque, nous regardons la quantité écoulons par contrat-cadre
		for(ChocolatDeMarque c :this.getStockchocolatdemarque().getStock().keySet()) {
			for ( ExemplaireContratCadre i : this.mesContratEnTantQueVendeur) {
				if(i.getProduit().equals(c)) {
					onDoitLivrer = onDoitLivrer+ i.getQuantiteALivrerAuStep();
				} 
			}
		// Nous définissons ensuite la quantité que nous souhaitons vendre par chocolat :
		// C'est la quantité telle que tous les contrats sont remplis et que 20% du stock initial reste
		
			double q = 0.95*(this.getStockchocolatdemarque().getQuantite(c))-onDoitLivrer;
		
		// Vérification de la quantité minimale autorisée sur le marché
		
		if(q>250) {
			PropositionAchatAO retenue = superviseur.vendreParAO(this, cryptogramme, c, q, false); // Offre lancée
		if (retenue!=null) {
			// Actualisation du stock en cas de vente
			this.getStockchocolatdemarque().enlever(retenue.getOffre().getChocolat(), retenue.getOffre().getQuantiteKG()); 
			this.getVente_choco().ajouter(retenue.getOffre().getChocolat(),  retenue.getOffre().getQuantiteKG());
			this.getJournalVente().ajouter("Merci à "+retenue.getAcheteur().getNom()+ " ! Faites bon usage de vos "+ retenue.getOffre().getQuantiteKG()+" kg.");
			this.getJournalVente().ajouter(Color.white,Color.red,"-----------------------------------------------------------------------------------------------------------------");
		} 
		else { // S'il n'y a pas d'offre, rien ne se passe
			this.getJournalVente().ajouter("Nous ne concluerons pas d'affaire aujourd'hui ! Peut-être la prochaine fois ;)");
			this.getJournalVente().ajouter(Color.white,Color.red,"-----------------------------------------------------------------------------------------------------------------");

		}
		}
		}
		this.getJournalVente().ajouter("");
		this.getJournalVente().ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		this.getJournalVente().ajouter(Color.black,Color.white,"--------------------------------------FIN DU STEP--------------------------------------------------------------");
		this.getJournalVente().ajouter(Color.black,Color.white,"---------------------------------------------------------------------------------------------------------------");
		this.getJournalVente().ajouter("");
		this.getJournalVente().ajouter("");
		}
	
	
	
	
	
		


// Implémentation de la stratégie de choix des offres pour un appel d'offres
// Nawfel/Gabriel
	
	public PropositionAchatAO choisir(List<PropositionAchatAO> propositions) {
		if (propositions==null) {
			return null; // Pas de choix si pas de propositions
		} else {
			this.getJournalVente().ajouter("Oyé Oyé, nous vendons "+propositions.get(0).getOffre().getQuantiteKG()+" kg de "+propositions.get(0).getOffre().getChocolat()+" !");
			this.getJournalVente().ajouter("Nous avons reçu un total de " + (propositions.size()) + " proposition(s) pour notre offre.");
			this.getJournalVente().ajouter("Les prix vont de "+ propositions.get(propositions.size()-1).getPrixKg() +"€ à "+ propositions.get(0).getPrixKg()+ "€ au kg !");
			
			// Nous prenons toujours l'offre avec le prix le plus haut
			
			PropositionAchatAO retenue=propositions.get(0);
			
			// Dans ce qu'il suit, nous comparons alors la meilleur proposition avec le prix de vente que nous
			// souhaitons. Si l'offre est supérieur alors nous vendons. Autrement, nous rejetons l'offre
			
			// Pour un chocolat de basse qualité
			if(retenue.getOffre().getChocolat().equals(this.getChocolatsProduits().get(0))) {
				if (retenue.getPrixKg()>(this.prixVouluB())) {
					this.getJournalVente().ajouter(Color.white,Color.green," -->" +" Nous choisissons l'offre de  "+retenue.getAcheteur().getNom());
					return retenue;
				} else {
					
					return null;
				}
			}
			// Pour un chocolat de moyenne qualité
			if(retenue.getOffre().getChocolat().equals(this.getChocolatsProduits().get(1))) {
				if (retenue.getPrixKg()>(this.prixVouluM())) {
					this.getJournalVente().ajouter(Color.white,Color.green," -->" + " Nous choisissons l'offre de  "+retenue.getAcheteur().getNom());
					
					return retenue;
				} else {
					
					
					return null;
				}
			}
			// Pour un chocolat de moyenne qualité bio
			if(retenue.getOffre().getChocolat().equals(this.getChocolatsProduits().get(2))) {
				if (retenue.getPrixKg()>(this.prixVouluMb())) {
					this.getJournalVente().ajouter(Color.white,Color.green," -->" +" Nous choisissons l'offre de  "+retenue.getAcheteur().getNom());
					
					return retenue;
				} else {
					
					
					return null;
				}
			}
			// Pour un chocolat de haute qualité
			if(retenue.getOffre().getChocolat().equals(this.getChocolatsProduits().get(3))) {
				if (retenue.getPrixKg()>(this.prixVouluH())) {
					this.getJournalVente().ajouter(Color.white,Color.green," -->" +" Nous choisissons l'offre de  "+retenue.getAcheteur().getNom());
					
					return retenue;
				} else {
					this.getJournalVente().ajouter("  --> je ne retiens rien");
					
					return null;
				}
			}
			// Pour un chocolat de haute qualité bio
			if(retenue.getOffre().getChocolat().equals(this.getChocolatsProduits().get(4))) {
				if (retenue.getPrixKg()>(this.prixVouluHb())) {
					this.getJournalVente().ajouter(Color.white,Color.green," -->" +" Nous choisissons l'offre de  "+retenue.getAcheteur().getNom());
					
					return retenue;
				} else {
					this.getJournalVente().ajouter("  --> je ne retiens rien");
					
					return null;
				}
			}
			}
		return null;
		}


//Gabriel
	 
// Calcul du prix de vente voulu pour un chocolat respectivement qualité basse, moyenne, moyenne bio, haute et haute bio 
//qualité en fonction du prix des fèves en bourse, du prix de transformation et du cout de stockage

	public double prixVouluB() { // Basse qualité 
	 return (this.prixMinB.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeAO; 	 
}
	
public double prixVouluM() {  // Moyenne qualité
	 return (this.prixMinM.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeAO;
}

public double prixVouluMb() {  // Moyenne qualité bio
	 return (this.prixMinMb.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeAO;
}

public double prixVouluH() {   // Haute qualité
	 return (this.prixMinH.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeAO;
}

public double prixVouluHb() { // Haute qualité bio
	 return (this.prixMinHb.getValeur()+4*Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur()+ Filiere.LA_FILIERE.getIndicateur("coutTransformation").getValeur())*this.margeAO;
}


}








