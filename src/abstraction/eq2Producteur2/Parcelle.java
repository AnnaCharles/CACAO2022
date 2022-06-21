package abstraction.eq2Producteur2;

import java.util.Random;

import abstraction.eq8Romu.filiere.Filiere;

//auteur : Fiona Martin 

public class Parcelle {
	
	//auteur : Fiona
	
	private Arbre TypeArbre;
	private int Age;
	private int NbArbres;
	private double RendementProgressif;
	private int StadeMaladie;
	private boolean Cooperative;
	private int StadeTransition;
	private int DebutMaladie;
	private int StadeTensionGeopolitique;
	private double ImpactRendementParasite;
	private boolean AleaClimatique;
	private int DebutAleaClimatique; 

	
	
	
	
	public Parcelle(Arbre typearbre, int age) {
		
		//auteur : Fiona	
		
		this.setTypeArbre(typearbre);
		this.setAge(age);
		this.setRendementProgressif(0.2 + Math.random()*0.5);
		this.setStadeMaladie(0);
		this.setNbArbres(10000);
		this.setImpactRendementParasite(1);
		
		// Attention : pour le moment on a 40 millions d'arbres et pas 400 millions car 1 parcelle = 10 000 arbres
		
		
		
	}
	
	public void next() {
		this.MAJMaladie();
		this.MAJTensionGeopo();
		//this.setImpactRendementParasite(0);
		//this.MAJParasite();
		this.MAJAleaClimatique();
	}

	public Arbre getTypeArbre() {
		return this.TypeArbre;
	}

	public void setTypeArbre(Arbre typeArbre) {
		this.TypeArbre = typeArbre;
	}

	public int getAge() {
		return this.Age;
	}

	public void setAge(int age) {
		this.Age = age;
	}

	public int getNbArbres() {
		return this.NbArbres;
	}

	public void setNbArbres(int nbArbres) {
		this.NbArbres = nbArbres;
	}

	public int getStadeMaladie() {
		return this.StadeMaladie;
	}

	public void setStadeMaladie(int stadeMaladie) {
	this.StadeMaladie =stadeMaladie;
	}

	public boolean isCooperative() {
		return this.Cooperative;
	}

	public void setCooperative(boolean cooperative) {
		this.Cooperative = cooperative;
	}

	public int getStadeTransition() {
		return this.StadeTransition;
	}

	public void setStadeTransition(int stadeTransition) {
		this.StadeTransition = stadeTransition;
	}

	public double getRendementProgressif() {
		return this.RendementProgressif;
	}

	public void setRendementProgressif(double rendementProgressif) {
		this.RendementProgressif = rendementProgressif;
	}
	
	public void MAJMaladie() { //Écrit par Antoine
		//System.out.println("Alea climatique : " + this.AleaClimatique);
		//System.out.println("stade maladie : " + this.StadeMaladie);
		//System.out.println("stade tension géopolitique : "+ this.StadeTensionGeopolitique);
		//System.out.println("impact rendement parasite : " + this.ImpactRendementParasite);
		if (StadeMaladie == 0) {
			double d = Math.random();
			if (d<=0.03) {
				double stade_maladie = Math.random();
				if (stade_maladie<=0.45) {
					this.setStadeMaladie(1);
					this.setDebutMaladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.45) && (stade_maladie<=0.7)) {
					this.setStadeMaladie(2);
					this.setDebutMaladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.7) && (stade_maladie<=0.85)) {
					this.setStadeMaladie(3);
					this.setDebutMaladie(Filiere.LA_FILIERE.getEtape());
				}
				if ((stade_maladie>0.85) && (stade_maladie<=0.95)) {
					this.setStadeMaladie(4);
					this.setDebutMaladie(Filiere.LA_FILIERE.getEtape());
				}
				if (stade_maladie>0.95) {
					this.setStadeMaladie(5);
					this.setDebutMaladie(Filiere.LA_FILIERE.getEtape());
				}
			}
		}
		
		
		else {
			
			if ((this.StadeMaladie == 1) && (Filiere.LA_FILIERE.getEtape() - this.getDebutMaladie() == 2)) {
				this.setStadeMaladie(0);
			}
			if ((this.StadeMaladie == 2) && (Filiere.LA_FILIERE.getEtape() - this.getDebutMaladie() == 5)) {
				this.setStadeMaladie(0);
			}
			if ((this.StadeMaladie == 3) && (Filiere.LA_FILIERE.getEtape() - this.getDebutMaladie() == 6)) {
				this.setStadeMaladie(0);
			}
			if ((this.StadeMaladie == 4) && (Filiere.LA_FILIERE.getEtape() - this.getDebutMaladie() == 8)) {
				this.setStadeMaladie(0);
			}
			
		}
	
	}

	public int getDebutMaladie() {
		return this.DebutMaladie;
	}

	public void setDebutMaladie(int debutMaladie) {
		this.DebutMaladie = debutMaladie;
	}

	public int getStadeTensionGeopolitique() {
		return this.StadeTensionGeopolitique;
	}

	public void setStadeTensionGeopolitique(int STG) {
		this.StadeTensionGeopolitique = STG;
	}
	
	public void MAJTensionGeopo() {
		if (this.StadeTensionGeopolitique != 0) {
			this.StadeTensionGeopolitique = this.StadeTensionGeopolitique - 1;
		}
		
		else {
			double d = Math.random();
			if (d < 0.05) {
				
				double d2 = Math.random();
				while (d2 == 0) {
					d2 = Math.random();
				}
				int dureeTG = (int) Math.ceil(d2*6);
				this.StadeTensionGeopolitique = dureeTG;
			}
		}
	}

	public double MAJParasite() {
		// auteur : Fiona 
		
		double proba = Math.random();
		if (this.getTypeArbre() == Arbre.ARBRE_HGB || this.getTypeArbre() == Arbre.ARBRE_MGB) {
			if (proba < 0.02) {
				double proba_impact = Math.random();
				if (proba_impact<=0.7) {
					this.setImpactRendementParasite(0.1);
					return 0.1;
				}
				else if (proba_impact<0.2) {
					this.setImpactRendementParasite(0.5);
					return 0.5;
				
				}
				else if (proba_impact<0.1) {
					this.setImpactRendementParasite(0.8);
					return 0.8;
					
				}
			}
			else {
				
			}
		}
		
		else {
			if (proba < 0.1) {
				double proba_impact_ = Math.random();
				if (proba_impact_<=0.7 ) {
					this.setImpactRendementParasite(0.1);
					return 0.1;
				}
				else if (proba_impact_<0.2) {
					this.setImpactRendementParasite(0.5);
					return 0.5;
				
				}
				else if (proba_impact_<0.1) {
					this.setImpactRendementParasite(0.8);
					return 0.8;
					
				}
			}
			else {
				
			}
		}
			return 1;
		}

	


	public double getImpactRendementParasite() {
		return ImpactRendementParasite;
	}

	public void setImpactRendementParasite(double impactRendementParasite) {
		ImpactRendementParasite = impactRendementParasite;
	}

	public boolean isAleaClimatique() {
		return AleaClimatique;
	}

	public void setAleaClimatique(boolean aleaClimatique) {
		AleaClimatique = aleaClimatique;
	}

	public int getDebutAleaClimatique() {
		return DebutAleaClimatique;
	}

	public void setDebutAleaClimatique(int debutAleaClimatique) {
		DebutAleaClimatique = debutAleaClimatique;
	}
	
	public void MAJAleaClimatique() {
		double dureeAleaClim = 0;
		if (Filiere.LA_FILIERE.getEtape()%24==8) {
			double proba_duree = Math.random();
			if ( proba_duree <= 0.33) {
				dureeAleaClim = 1;
				this.setDebutAleaClimatique(Filiere.LA_FILIERE.getEtape());
				this.setAleaClimatique(true);
			}
			else if (proba_duree <= 0.67){
				dureeAleaClim = 2;
				this.setDebutAleaClimatique(Filiere.LA_FILIERE.getEtape());
				this.setAleaClimatique(true);
			}
			else  {
				dureeAleaClim = 3;
				this.setDebutAleaClimatique(Filiere.LA_FILIERE.getEtape());
				this.setAleaClimatique(true);
			}
		}
		else {
			this.setAleaClimatique(false);
		}
		
		if (this.AleaClimatique == true) {
			while (Filiere.LA_FILIERE.getEtape() - this.getDebutAleaClimatique() <= dureeAleaClim) {
				dureeAleaClim = dureeAleaClim - 1; 
			}
			this.setAleaClimatique(false); 
		}
	}


}
