package com.ebiznext.sbt.sample.reception.wsclient;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.ebiznext.sbt.sample.reception.wsclient.ZSMMI899CMDDATA;
import com.ebiznext.sbt.sample.reception.wsclient.ZSMMI899ITEMDATA;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETCMDLISTEBELN;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899GETCMDLISTEBELN_Service;

import com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner;
import com.ebiznext.sbt.sample.reception.vo.EnteteCommande;
import com.ebiznext.sbt.sample.reception.vo.LigneDeCommande;
import com.ebiznext.sbt.sample.utils.Configuration;

/**
 * This class was generated by Apache CXF 2.6.1 2012-11-02T20:43:08.846+01:00
 * Generated source version: 2.6.1
 * 
 */
public final class ReceptionGetListeCommandesClient {

    private static final Logger LOGGER = Logger.getLogger(ReceptionGetListeCommandesClient.class.getName());

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions",
            "ZWS_MM_I899_GET_CMD_LIST_EBELN");

    private ReceptionGetListeCommandesClient(){}

    public static CommandeAReceptionner[] execute(String user, String password, String critere)
            throws java.lang.Exception {
        URL wsdlURL = Configuration.getWSDL("edifice.wsdl.reception.ListerReceptions");
        ZWSMMI899GETCMDLISTEBELN_Service ss = new ZWSMMI899GETCMDLISTEBELN_Service(wsdlURL, SERVICE_NAME);
        ZWSMMI899GETCMDLISTEBELN port = ss.getZWSMMI899GETCMDLISTEBELN();

        {
            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

            LOGGER.info("Invoking zMMI899GETCMDLISTEBELN...");
            java.lang.String _zMMI899GETCMDLISTEBELN_iSTRGENERAL = critere;
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899CMDDATA> _zMMI899GETCMDLISTEBELN_eCMDTABLE = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.ZTMMI899CMDDATA>();
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T> _zMMI899GETCMDLISTEBELN_eMSGRETURN = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T>();
            port.zMMI899GETCMDLISTEBELN(_zMMI899GETCMDLISTEBELN_iSTRGENERAL, _zMMI899GETCMDLISTEBELN_eCMDTABLE,
                    _zMMI899GETCMDLISTEBELN_eMSGRETURN);

            LOGGER.info("zMMI899GETCMDLISTEBELN._zMMI899GETCMDLISTEBELN_eCMDTABLE=" + _zMMI899GETCMDLISTEBELN_eCMDTABLE.value);
            LOGGER.info("zMMI899GETCMDLISTEBELN._zMMI899GETCMDLISTEBELN_eMSGRETURN=" + _zMMI899GETCMDLISTEBELN_eMSGRETURN.value);
            List<CommandeAReceptionner> cmds = new ArrayList<CommandeAReceptionner>();
            for (ZSMMI899CMDDATA item : _zMMI899GETCMDLISTEBELN_eCMDTABLE.value.getItem()) {
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
