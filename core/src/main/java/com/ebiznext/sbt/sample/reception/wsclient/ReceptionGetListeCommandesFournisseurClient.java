package com.ebiznext.sbt.sample.reception.wsclient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.ebiznext.sbt.sample.reception.wsclient.ZSMMI899CMDDATA;
import com.ebiznext.sbt.sample.reception.wsclient.ZSMMI899ITEMDATA;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETCMDLISTLIFNR;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETCMDLISTLIFNR_Service;

import com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner;
import com.ebiznext.sbt.sample.reception.vo.EnteteCommande;
import com.ebiznext.sbt.sample.reception.vo.LigneDeCommande;
import com.ebiznext.sbt.sample.utils.Configuration;

/**
 * @author stephane.manciot@ebiznext.com
 * 
 */
public final class ReceptionGetListeCommandesFournisseurClient {

    private static final Logger LOGGER = Logger.getLogger(ReceptionGetListeCommandesFournisseurClient.class.getName());

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions",
            "ZWS_MM_I899_GET_CMD_LIST_LIFNR");

    private ReceptionGetListeCommandesFournisseurClient(){}

    public static CommandeAReceptionner[] execute(String user, String password, String codeFournisseur, String codeOIA)
            throws java.lang.Exception {
        URL wsdlURL = Configuration.getWSDL("edifice.wsdl.reception.ListerReceptionsFournisseur");
        ZWSMMI899GETCMDLISTLIFNR_Service ss = new ZWSMMI899GETCMDLISTLIFNR_Service(wsdlURL, SERVICE_NAME);
        ZWSMMI899GETCMDLISTLIFNR port = ss.getZWSMMI899GETCMDLISTLIFNR();

        {
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

            LOGGER.info("Invoking zMMI899GETCMDLISTLIFNR...");
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899CMDDATA> eCMDTABLE = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899CMDDATA>();
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T> eMSGRETURN = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T>();
            port.zMMI899GETCMDLISTLIFNR(codeFournisseur, codeOIA, eCMDTABLE, eMSGRETURN);

            LOGGER.info("zMMI899GETCMDLISTLIFNR.eCMDTABLE=" + eCMDTABLE.value);
            LOGGER.info("zMMI899GETCMDLISTLIFNR.eMSGRETURN=" + eMSGRETURN.value);
            List<CommandeAReceptionner> cmds = new ArrayList<CommandeAReceptionner>();
            for (ZSMMI899CMDDATA item : eCMDTABLE.value.getItem()) {
                List<LigneDeCommande> details = new ArrayList<LigneDeCommande>();
                for (ZSMMI899ITEMDATA detail : item.getLINEDETAIL().getItem()) {
                    Date eindt = detail.getEINDT() != null ? new Date(detail.getEINDT().toGregorianCalendar().getTimeInMillis()) : new Date();
                    details.add(new LigneDeCommande(Long.parseLong(detail.getEBELP()), eindt, detail.getNETWR()
                            .toString(), detail.getTXZ01(), detail.getTXZ01(), detail.getMEINS(), detail.getKNTXT(),
                            detail.getMENGE().toString(), detail.getMENGERCP().toString(), detail.getMENGERAL()
                                    .toString(), detail.getWAERS()));
                }
                cmds.add(new CommandeAReceptionner("edifice.reception", new EnteteCommande("", item.getNAME1(), item.getEBELN(),
                        item.getBUKRS(), item.getNETWR().toString(), item.getLIFNR(), item.getWAERS(), "", item.getOIATEXT()),
                        details.toArray(new LigneDeCommande[details.size()])));
            }
            return cmds.toArray(new CommandeAReceptionner[cmds.size()]);
        }
    }

}
