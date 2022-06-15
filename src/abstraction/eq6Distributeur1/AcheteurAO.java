package abstraction.eq6Distributeur1;


import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abstraction.eq8Romu.appelsOffres.IAcheteurAO;
import abstraction.eq8Romu.appelsOffres.OffreVente;
import abstraction.eq8Romu.appelsOffres.PropositionAchatAO;
import abstraction.eq8Romu.contratsCadres.Echeancier;
import abstraction.eq8Romu.filiere.Filiere;
import abstraction.eq8Romu.general.Journal;
import abstraction.eq8Romu.produits.ChocolatDeMarque;


/**
 * @author Nathan
 */
public class AcheteurAO extends AcheteurContrat implements IAcheteurAO {

    private final double MAX_PRIX_ACHAT = 11;
    private Journal journalAO;
    private Map<ChocolatDeMarque, Double> prixAchat;
    
    public AcheteurAO() {
        super();
        journalAO = new Journal("Journal pour les AO", this);
    }

    @Override
    public void initialiser() {
        super.initialiser();
        
        prixAchat = new HashMap<ChocolatDeMarque, Double>();
        for (ChocolatDeMarque choco : Filiere.LA_FILIERE.getChocolatsProduits()) {
            prixAchat.put(choco, 7.0);
        }
    }
    
    /** 
     * @author Nathan
     */
    @Override
    public double proposerPrix(OffreVente offre) {
        // if (achat.containsKey(offre.getChocolat()) && ! achat.get(offre.getChocolat())) {
        //     return 0.0;
        // }
        journalAO.ajouter(Color.CYAN, Color.BLACK, "Nouvelle appel d'offre de "  + offre.getVendeur() + " pour " + offre.getQuantiteKG() + " de " + offre.getChocolat());
        if (NotreStock.seuilSecuFaillite() == true) {   //EmmaHumeau
    		return 0.0;
    	}
        int etCourant = Filiere.LA_FILIERE.getEtape();
        double aAjouter = partDuMarcheVoulu(offre.getChocolat().getChocolat()) * Filiere.LA_FILIERE.getVentes(offre.getChocolat(), (etCourant%24)-24) - NotreStock.getStock(offre.getChocolat()); 
        if (offre.getQuantiteKG() < aAjouter * 1.2 ) {
            double res = prixAchat.get(offre.getChocolat());
            if (offre.enTG()) {
                journalAO.ajouter("L'offre demande de mettre le chocolat en tête de gondole");
                res *= 0.7;
            }
            journalAO.ajouter("Nous proposons un prix de " + res);
            return res;
        }
        journalAO.ajouter(Color.RED, Color.BLACK, "La quantité proposée est trop élevée, nous refusons");
        return 0.0;
    }

    /**
     * @author Nathan
     */
    @Override
    public void notifierAchatAO(PropositionAchatAO propositionRetenue) {
        this.prixAchat.put(propositionRetenue.getOffre().getChocolat(), propositionRetenue.getPrixKg()*0.98);
        journalAO.ajouter(Color.GREEN, Color.BLACK, "La proposition a été accepté");
        this.getNotreStock().addQte(propositionRetenue.getOffre().getChocolat(), (Double) propositionRetenue.getOffre().getQuantiteKG());
        this.setPrixVente(propositionRetenue.getOffre().getChocolat(), propositionRetenue.getPrixKg());
    }

    /**
     * @author Nathan
     * 
     */
    @Override
    public void notifierPropositionNonRetenueAO(PropositionAchatAO propositionNonRetenue) {
        double nvPrix = (propositionNonRetenue.getPrixKg() * 1.02 > MAX_PRIX_ACHAT * facteurPrixChocolat(propositionNonRetenue.getOffre().getChocolat().getChocolat())) ? propositionNonRetenue.getPrixKg() : propositionNonRetenue.getPrixKg() * 1.02;
        this.prixAchat.put(propositionNonRetenue.getOffre().getChocolat(), nvPrix);
        journalAO.ajouter(Color.RED, Color.BLACK, "Notre proposition n'a pas été accepté.");
    }

    /**
     * @author Nathan
     * @return La liste des journaux renvoyée dans acheteurContrat et le journal d'appel d'offre
     */
    @Override
    public List<Journal> getJournaux() {
        List<Journal> j = super.getJournaux();
        j.add(journalAO);
        return j;
    }

}
