package com.ebiznext.sbt.sample.reception.wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.ebiznext.sbt.sample.reception.wsclient.ZSMMI899LIFLIST;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETFOURNISSEUR;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETFOURNISSEUR_Service;

import com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner;
import com.ebiznext.sbt.sample.reception.vo.EnteteCommande;
import com.ebiznext.sbt.sample.reception.vo.LigneDeCommande;
import com.ebiznext.sbt.sample.utils.Configuration;

/**
 * @author stephane.manciot@ebiznext.com
 * 
 */
public final class ReceptionGetListeFournisseursClient {

    private static final Logger LOGGER = Logger.getLogger(ReceptionGetListeFournisseursClient.class.getName());

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions",
            "ZWS_MM_I899_GET_FOURNISSEUR");

    private ReceptionGetListeFournisseursClient(){}

    public static CommandeAReceptionner[] execute(String user, String password, String codeOIA)
            throws java.lang.Exception {
        URL wsdlURL = Configuration.getWSDL("edifice.wsdl.reception.ListerFournisseurs");
        ZWSMMI899GETFOURNISSEUR_Service ss = new ZWSMMI899GETFOURNISSEUR_Service(wsdlURL, SERVICE_NAME);
        ZWSMMI899GETFOURNISSEUR port = ss.getZWSMMI899GETFOURNISSEUR();

        {
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

            LOGGER.info("Invoking zWSMMI899GETCMDLISTLIFNR...");
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899LIFLIST> eLIFLIST = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899LIFLIST>();
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T> eMSGRETURN = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T>();
            port.zMMI899GETFOURNISSEUR(codeOIA, eLIFLIST, eMSGRETURN);

            LOGGER.info("zWSMMI899GETCMDLISTLIFNR.eCMDTABLE=" + eLIFLIST.value);
            LOGGER.info("zWSMMI899GETCMDLISTLIFNR.eMSGRETURN=" + eMSGRETURN.value);
            List<CommandeAReceptionner> cmds = new ArrayList<CommandeAReceptionner>();
            for (ZSMMI899LIFLIST item : eLIFLIST.value.getItem()) {
                cmds.add(new CommandeAReceptionner("edifice.reception", new EnteteCommande("", item.getNAME1(), item.getEBELN(),
                        null, null, item.getLIFNR(), null, "", null),
                        new LigneDeCommande[0]));
            }
            return cmds.toArray(new CommandeAReceptionner[cmds.size()]);
        }
    }

}
