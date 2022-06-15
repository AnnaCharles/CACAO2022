package abstraction.eq1Producteur1;

import java.util.ArrayList;

import abstraction.eq8Romu.filiere.Filiere;

// Une instance de "MilleArbre" équivaut à 1000 arbres réels
public class MilleArbre {
	private int ut_plantation;
	private int ut_esperance_vie;
	private int stade_maladie;
	private int ut_debut_maladie;
	private int qualite ; //1 = basse; 2 = moyenne; 3 = haute
	private boolean cooperative;
	private boolean bioequitable;
	private boolean transition_bio;
	private int date_transition;
	private double productivite_max;
	private int mecontentement;
	
	public MilleArbre() { //Écrit par Maxime
		this.ut_plantation=0;
		this.ut_esperance_vie= this.Esperance_vie();
		this.stade_maladie=0;
		this.ut_debut_maladie = 0;
		this.qualite=1;
		this.cooperative=false;
		this.bioequitable=false;
		this.transition_bio=false;
		this.date_transition=0;
		this.productivite_max=this.Production_max();
		this.mecontentement=0;
	}
	
	public MilleArbre(int qualite,boolean cooperative, boolean BE,int ut_plantation) { //Écrit par Antoine
		this.ut_plantation = ut_plantation;
		this.ut_esperance_vie = Esperance_vie();
		this.stade_maladie = 0;
		this.ut_debut_maladie = 0;
		this.cooperative= cooperative;
		if ((qualite==1) || (qualite == 2) || (qualite == 3)) { 
			//1: basse 2: moyenne 3: haute
			this.qualite = qualite;
		}
		else {
			this.qualite = 1; 
			System.out.print("Erreur de qualité, qualité donnée = "+qualite);
		}
		this.bioequitable = BE;
		this.transition_bio = false;
		this.date_transition = 0;
		this.productivite_max = this.Production_max();
		this.mecontentement=0;
	}
	
	public void setUt_plantation(int ut_plantation) { //Écrit par Maxime
		this.ut_plantation = ut_plantation;
	}
	public void setUt_esperance_vie(int ut_esperance_vie) { //Écrit par Maxime
		this.ut_esperance_vie=ut_esperance_vie;
	}
	public void setMaladie(int maladie) { //Écrit par Maxime
		this.stade_maladie=maladie;
	}
	public void setUt_debut_maladie(int ut) { //Écrit par Maxime
		this.ut_debut_maladie=ut;
	}
	public void setQualite(int qualite) { //Écrit par Maxime
		this.qualite=qualite;
	}
	public void setCooperative(boolean cooperative) {
		this.cooperative=cooperative;
	}
	public void setBioequitable(boolean bioequitable) { //Écrit par Maxime
		this.bioequitable= bioequitable;
	}
	public void setTransition_bio(boolean transition_bio) { //Écrit par Maxime
		this.transition_bio=transition_bio;
	}
	public void setDate_transition(int date_transition) {
		this.date_transition = date_transition;
	}
	public void setProductivite_max(double productivite_max) { //Écrit par Maxime
		this.productivite_max = productivite_max;
	}
	public void setMecontentement(int mecontentement) {
		this.mecontentement=0;
		if (mecontentement>=0) {
			this.mecontentement = mecontentement;
		}
	}
	public int getUt_plantation() { //Écrit par Antoine
		return this.ut_plantation;
	}
	public int getUt_esperance_vie() { //Écrit par Antoine
		return this.ut_esperance_vie;
	}
	public int getStade_maladie() { //Écrit par Antoine
		return this.stade_maladie;
	}
	public int getUt_debut_maladie( ) { //Écrit par Antoine
		return this.ut_debut_maladie;
	}
	public int getQualite() { //Écrit par Antoine
		return this.qualite;
	}
	public boolean getCooperative() { //Écrit par Antoine
		return this.cooperative;
	}
	public boolean getBioequitable() { //Écrit par Antoine
		return this.bioequitable;
	}
	public boolean getTransition_bio() { //Écrit par Antoine
		return this.transition_bio;
	}
	public int getDate_transition() { //Écrit par Antoine
		return this.date_transition;
	}
	public double getProductivite_max() { //Écrit par Antoine
		return this.productivite_max;
	}
	public int getMecontentement() {
		return this.mecontentement;
	}
	public void MAJMecontentement(int mecontentement_basse,int mecontentement_moyenne, int mecontentement_haute) {
		ArrayList<Integer> mecontentement= new ArrayList<Integer>();
		mecontentement.add(mecontentement_basse);
		mecontentement.add(mecontentement_moyenne);
		mecontentement.add(mecontentement_haute);
		int qualite = this.getQualite();
		int maladie = this.getStade_maladie();
		this.setMecontentement(this.getMecontentement()+mecontentement.get(qualite-1)+maladie);
		if (maladie==0) {
			this.setMecontentement(this.getMecontentement()-5);
		}
		if (this.getMecontentement()>30) {
			double proba= Math.random();
			if (proba*this.getMecontentement()>28) {
				this.Regrouper();
			}
		}
	}
	
	
	public void MAJMaladie() { //Écrit par Antoine
		if (this.getStade_maladie() == 0) {
			double chance_maladie = Math.random();
			if (chance_maladie<=0.03) {
				double stade_maladie = Math.random();
				if (stade_maladie<=0.45) {
					this.setMaladie(1);
					this.setUt_debut_maladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.45) && (stade_maladie<=0.7)) {
					this.setMaladie(2);
					this.setUt_debut_maladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.7) && (stade_maladie<=0.85)) {
					this.setMaladie(3);
					this.setUt_debut_maladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.85) && (stade_maladie<=0.95)) {
					this.setMaladie(4);
					this.setUt_debut_maladie(Filiere.LA_FILIERE.getEtape());
				}
				if (stade_maladie>0.95) {
					this.setMaladie(5);
					this.setUt_debut_maladie(Filiere.LA_FILIERE.getEtape());
				}
			}
		}
		if ((this.getStade_maladie() == 1) && (Filiere.LA_FILIERE.getEtape() - this.getUt_debut_maladie() == 2)) {
			this.setMaladie(0);
		}
		if ((this.getStade_maladie() == 2) && (Filiere.LA_FILIERE.getEtape() - this.getUt_debut_maladie() == 5)) {
			this.setMaladie(0);
		}
		if ((this.getStade_maladie() == 3) && (Filiere.LA_FILIERE.getEtape() - this.getUt_debut_maladie() == 6)) {
			this.setMaladie(0);
		}
		if ((this.getStade_maladie() == 4) && (Filiere.LA_FILIERE.getEtape() - this.getUt_debut_maladie() == 8)) {
			this.setMaladie(0);
		}
	}
	
	public int Esperance_vie() { //Écrit par Antoine
		double écart_moyenne = Math.random();
		if (écart_moyenne<0.5) {
			int esp = 960-(int)Math.floor(écart_moyenne*240);
			return esp;
		}
		else {
			int esp = 960+(int)Math.floor((écart_moyenne-0.5)*240);
			return esp;
		}
	}
	public double Production_max() { //Écrit par Maxime
		double production = 200 + Math.random()*50; //La production de 1000 arbres
		if (this.bioequitable) {
			production = 0.8*production;
		}
		return production;
	}
	public int Age() { //Écrit par Maxime
		return Filiere.LA_FILIERE.getEtape()-this.getUt_plantation();
	}
	public double Recolte() {
		double quantite= 0;
		if (this.getBioequitable()) {
			if (this.Age()<= 75 || this.Age()>=this.getUt_esperance_vie() || this.getStade_maladie()==5) {
				return 0;
			}
			else {
				if (this.Age()<= 150) {
					quantite= this.getProductivite_max()*(this.Age()-75)/75;
				}
				else {
					quantite= this.getProductivite_max();
				}
			}
			quantite=0.8*quantite;
			if (this.getStade_maladie()==0) {
				return quantite;
			}
			else {
				if(this.getStade_maladie()==1 || this.getStade_maladie()==2) {
					return 0.85*quantite;
				}
				else {
					if(this.getStade_maladie()==3) {
						if(Filiere.LA_FILIERE.getEtape()-this.getUt_debut_maladie()<=2) {
							return 0;
						}
						else {
							
							return 0.8*quantite;
						}
					}
					else {
						if(Filiere.LA_FILIERE.getEtape()-this.getUt_debut_maladie()<=4) {
							return 0;
						}
						else {
							return 0.65*quantite;
						}
					}
				}
			}
		}
		else {
			if (this.Age()<= 60 || this.Age()>=this.getUt_esperance_vie() || this.getStade_maladie()==5) {
				return 0;
			}
			else {
				if (this.Age()<= 125) {
					quantite= this.getProductivite_max()*(this.Age()-60)/65;
				}
				else {
					quantite= this.getProductivite_max();
				}
			}
			if (this.getStade_maladie()==0) {
				return quantite;
			}
			else {
				if(this.getStade_maladie()==1 || this.getStade_maladie()==2) {
					return 0.85*quantite;
				}
				else {
					if(this.getStade_maladie()==3) {
						if(Filiere.LA_FILIERE.getEtape()-this.getUt_debut_maladie()<=2) {
							return 0;
						}
						else {
							return 0.8*quantite;
						}
					}
					else {
						if(Filiere.LA_FILIERE.getEtape()-this.getUt_debut_maladie()<=4) {
							return 0;
						}
						else {
							return 0.65*quantite;
						}
					}
				}
			}
		}
	}
	public void PasserBio() {
		if (this.getTransition_bio()==true && (Filiere.LA_FILIERE.getEtape()-this.getDate_transition())>= 72) {
			this.setBioequitable(true);
			this.setTransition_bio(false);
		}
		
	}
	public void Regrouper() {
		this.setCooperative(true);
	}
}
