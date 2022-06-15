	package abstraction.eq1Producteur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eq8Romu.bourseCacao.BourseCacao;
import abstraction.eq8Romu.bourseCacao.IVendeurBourse;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.Feve;

public class ProducteurActeur1VenteBourse extends Producteur1Producteur implements IVendeurBourse{
	
	//Auteur : Khéo
	private HashMap<Feve, Double> prixmoyenFeve ;
	private HashMap<Parc, Double> repartitionGuerre;
	/**
	 * @param feve
	 * @param stock
	 */
	public ProducteurActeur1VenteBourse() {
		super();
		this.prixmoyenFeve = new HashMap<Feve, Double>() ;
	}




	//Auteur : Khéo
	public double offre(Feve f, double cours) {
		
			//On met à jour les prix de la HashMap
			if (Filiere.LA_FILIERE.getEtape()>=1) { //Petite dijonction de cas pour le premier tour afin d'éviter d'aller chercher dans une hashmap vide
				this.getPrixmoyenFeve().put(f, this.getPrixmoyenFeve().get(f)+cours);
			
			} else {
				this.getPrixmoyenFeve().put(f, cours);
			
			}
			
			this.repartitionGuerre =  parcGuerre() ;
			
			//On vend en fonction du prix
			if(f!=Feve.FEVE_HAUTE_BIO_EQUITABLE) { //Pas de bourse pour le HAUT_BE
			if (Filiere.LA_FILIERE.getEtape()>=1) {
				// Ici, étape + 1 car la 1e etape est l'étape 0, et on y rentre le cours
				
			if (this.getStock(f, false)>0) {
				if (this.getStock(f, false)<1000000000) {
					double tauxdecroit = ((0.25-1)/1000000000)*this.getStock(f, false) + 1 ; //Taux décroissant sur 100 million jusqu'à 50 %
					return venteBourse(f, cours, tauxdecroit);
			
			}
				else {
					return venteBourse(f, cours, 0.25);
		}
			}
			}
			}
			
		return 0.0 ;
	}
			
	
	
	public double venteBourse(Feve f, double cours, double tauxdecroit) {
		double coutStockage=Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur(); 
		double coutProduction=this.getPrixEntretienArbre().getValeur();
		double coutTotalFeve= coutProduction + coutStockage*this.getFeves().get(f).get(0).getAge();
		
		if(f==Feve.FEVE_BASSE) {
			if (cours>coutTotalFeve && (this.getPrixmoyenFeve().get(f)/(Filiere.LA_FILIERE.getEtape()+1))*tauxdecroit <= cours) {
				double stock = 0 ;
				if (repartitionGuerre != null) {
				for (Parc ele : repartitionGuerre.keySet()) {
					stock += this.getStockParc(f, false, ele);
				}
				return stock*0.25;
				}
			}
		}
			
			if(f==Feve.FEVE_MOYENNE) {
			if (cours>coutTotalFeve*1.1 && (this.getPrixmoyenFeve().get(f)/(Filiere.LA_FILIERE.getEtape()+1))*tauxdecroit <= cours) {
				double stock = 0 ;
				if (repartitionGuerre != null) {
				for (Parc ele : repartitionGuerre.keySet()) {
					stock += this.getStockParc(f, false, ele);
				}
				return stock*0.25;
				}
			}
		}
			
			if(f==Feve.FEVE_MOYENNE_BIO_EQUITABLE) {
				if (cours>coutTotalFeve*1.3 && (this.getPrixmoyenFeve().get(f)/(Filiere.LA_FILIERE.getEtape()+1))*tauxdecroit <= cours) {
					double stock = 0 ;
					if (repartitionGuerre != null) {
					for (Parc ele : repartitionGuerre.keySet()) {
						stock += this.getStockParc(f, false, ele);
					}
					return stock*0.25;
					}
				}
			}
		
		return 0.0;
	}



	/**
	 * @return the prixmoyenFeve
	 */
	//Auteur : Khéo
	public HashMap<Feve, Double> getPrixmoyenFeve() {
		
		return this.prixmoyenFeve;
	}



	//Auteur : Khéo
	public void notificationVente(Feve f, double quantiteEnKg, double coursEnEuroParKg) {
		// TODO Auto-generated method stub
		
		venteGuerre(this.repartitionGuerre, quantiteEnKg, f);
	}
	

	/**
	 * offre2 (pour conserver la V1 avant d'être sûr)
	 * @author laure
	 * @param f la fève qu'on veut vendre
	 * @param cours le cours actuel
	 * @return quantité de fèves à vendre
	 */
	public double offre2(Feve f, double cours) {
		double coutStockage=Filiere.LA_FILIERE.getParametre("Prix Stockage").getValeur(); 
		double coutProduction=this.getPrixEntretienArbre().getValeur();
		double coutTotalFeve= coutProduction + coutStockage*this.getFeves().size();
		double tauxdecroit = (((0.50-1)/1000000)*this.getStock(f, false) + 1)/this.getStock(f, false) ; //Taux décroissant sur 1 million jusqu'à 75 % 
		
		if(f!=Feve.FEVE_HAUTE_BIO_EQUITABLE) { //Pas de bourse pour le HAUT_BE 
			if (Filiere.LA_FILIERE.getEtape()>=1) { 
				if (this.getStock(f, false)<1000000) {
					if(f==Feve.FEVE_BASSE) {
						if (cours>coutTotalFeve &&((this.getPrixmoyenFeve().get(f)/(Filiere.LA_FILIERE.getEtape()+1))*tauxdecroit <= cours)) {
							return this.getStock(f, false)*0.4;
						}
					} else 	if (f==Feve.FEVE_MOYENNE) {
						if (cours>coutTotalFeve*1.2 &&((this.getPrixmoyenFeve().get(f)/(Filiere.LA_FILIERE.getEtape()+1))*tauxdecroit <= cours)) {
							return this.getStock(f, false)*0.4;
						}
					}
				} else {
					return this.getStock(f, false);
				}
			}
		}
		return 0.0 ; 
	}
	
	
	//Auteur : Khéo
	public HashMap<Parc, Double> parcGuerre(){ //On obtient la répartition qu'il faut en prenant compte les répartition initiales et la mémoire
		
		HashMap<Parc, Double> res = new HashMap<Parc, Double>();
		LinkedList<Parc> guerre = new LinkedList<Parc>() ;
		
		for (Parc parc : this.getListeParc()) {
				
			if (parc.getNom() == "Ghana") {
				res.put(parc, 0.62);
			} else if (parc.getNom() == "Côte d'Ivoire"){
				res.put(parc, 0.23);
			} else if (parc.getNom() == "Nigéria"){
				res.put(parc, 0.08);
			} else if (parc.getNom() == "Cameroun"){
				res.put(parc, 0.07);
			} 
			if (parc.getGuerre()) { 
			guerre.add(parc);
		}
			
		}
		
		
		int repart = this.getListeParc().size() - guerre.size() ;
		
		if (repart == 0) {
			return null ; //Tout les parcs sont en guerre 
		} else {
			for (Parc enguerre : guerre) {
				double ajout = res.get(enguerre)/repart ;
				res.remove(enguerre);
				
				for (Parc support : res.keySet()) {
					if (!guerre.contains(support)) {
						res.replace(support, res.get(support)+ajout);
					}
				}
				
			}
		}
		
				
		return res ;
	}
	
	//auteur : Khéo
	public void venteGuerre(HashMap<Parc, Double> repart, double quantite, Feve f) {
				
		for (Parc parc : repart.keySet()) {
			double reste = this.retirerQuantiteParc(f, quantite*repart.get(parc), parc); //On retire la quantité multiplié par le pourcentabe calculé avec la guerre
			if (reste >0) {
				for (Parc parcreste : repart.keySet()) {
					reste = this.retirerQuantiteParc(f, quantite*repart.get(parcreste), parcreste);
				}
			}
			
		}
	}


	/**
	 * @return the repartitionGuerre
	 */
	public HashMap<Parc, Double> getRepartitionGuerre() {
		return repartitionGuerre;
	}
	
	

}
