// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package com.ebiznext.sbt.sample.reception.service;

import com.ebiznext.sbt.sample.reception.wsclient.ReceptionGetListeCommandesClient;
import com.ebiznext.sbt.sample.reception.wsclient.ReceptionGetListeCommandesFournisseurClient;
import com.ebiznext.sbt.sample.reception.wsclient.ReceptionGetListeFournisseursClient;
import com.ebiznext.sbt.sample.reception.wsclient.ReceptionLoginClient;
import com.ebiznext.sbt.sample.reception.wsclient.ReceptionReceptionnerCommandeCLient;
import com.ebiznext.sbt.sample.reception.wsclient.ReceptionValiderCodeOIAClient;

/**
 * @see com.ebiznext.sbt.sample.reception.service.ReceptionService
 */
public class ReceptionServiceImpl
    extends com.ebiznext.sbt.sample.reception.service.ReceptionServiceBase
{

    private static final String PWD = ", pwd=";
    private static final String HIDDEN = "******";
    private static final String IDENTIFIANT = "identifiant=";
    private static final String RETURN_VALUE = "returnValue=";

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#validerCodeOIA(java.lang.String, java.lang.String, java.lang.String)
     */
    protected com.ebiznext.sbt.sample.reception.vo.ResultatRechercheCodeOIA handleValiderCodeOIA(java.lang.String identifiant, java.lang.String pwd, java.lang.String codeOIA)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN + ", codeOIA=" + codeOIA);
            com.ebiznext.sbt.sample.reception.vo.ResultatRechercheCodeOIA res = ReceptionValiderCodeOIAClient.execute(identifiant, pwd, codeOIA);
            log.info(RETURN_VALUE + res);
            return res;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#rechercherDetailCommandeAReceptionner(java.lang.String, java.lang.String, java.lang.String)
     */
    protected com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] handleRechercherDetailCommandeAReceptionner(java.lang.String identifiant, java.lang.String pwd, java.lang.String numeroCommande)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN + ", numeroCommande=" + numeroCommande);
            com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] res = ReceptionGetListeCommandesClient.execute(identifiant, pwd, numeroCommande);
            log.info(RETURN_VALUE + res.toString());
            return res;

        } catch (Exception e) {
            log.error(e);
            return new com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[0];
        }
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#receptionnerLignesDeCommande(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.ebiznext.sbt.sample.reception.vo.CommandeReceptionnee[])
     */
    protected boolean handleReceptionnerLignesDeCommande(java.lang.String identifiant, java.lang.String pwd, java.lang.String numeroDeCommande, java.lang.String bonDeLivraison, com.ebiznext.sbt.sample.reception.vo.CommandeReceptionnee[] lignesDeCommandeAReceptionner)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN + ", numeroDeCommande=" + numeroDeCommande
                    + ", bonDeLivraison=" + bonDeLivraison + ", lignesDeCommandeAReceptionner="
                    + lignesDeCommandeAReceptionner.toString());
            boolean res = ReceptionReceptionnerCommandeCLient.execute(identifiant, pwd, numeroDeCommande,
                    bonDeLivraison, lignesDeCommandeAReceptionner);
            log.info(RETURN_VALUE + res);
            return res;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#listerCommandesAReceptionnerFournisseur(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    protected com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] handleListerCommandesAReceptionnerFournisseur(java.lang.String identifiant, java.lang.String pwd, java.lang.String codeFournisseur, java.lang.String codeOIA)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN + ", codeFournisseur=" + codeFournisseur + ", codeOIA=" + codeOIA);
            com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] res = ReceptionGetListeCommandesFournisseurClient.execute(identifiant, pwd, codeFournisseur, codeOIA);
            log.info(RETURN_VALUE + res.toString());
            return res;

        } catch (Exception e) {
            log.error(e);
            return new com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[0];
        }
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#listerFournisseurs(java.lang.String, java.lang.String, java.lang.String)
     */
    protected com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] handleListerFournisseurs(java.lang.String identifiant, java.lang.String pwd, java.lang.String codeOIA)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN + ", codeOIA=" + codeOIA);
            com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[] res = ReceptionGetListeFournisseursClient.execute(identifiant, pwd, codeOIA);
            log.info(RETURN_VALUE + res.toString());
            return res;

        } catch (Exception e) {
            log.error(e);
            return new com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner[0];
        }
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#getNumeroCommandePrefix()
     */
    protected java.lang.String handleGetNumeroCommandePrefix()
        throws java.lang.Exception
    {
        return "";
    }

    /**
     * @see com.ebiznext.sbt.sample.reception.service.ReceptionService#verifierLogin(java.lang.String, java.lang.String)
     */
    protected boolean handleVerifierLogin(java.lang.String identifiant, java.lang.String pwd)
        throws java.lang.Exception
    {
        try {
            log.info(IDENTIFIANT + identifiant + PWD + HIDDEN);
            boolean res = ReceptionLoginClient.execute(identifiant, pwd);
            log.info(RETURN_VALUE + res);
            return res;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

}