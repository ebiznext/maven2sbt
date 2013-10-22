package com.ebiznext.sbt.sample.reception.wsclient;

import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899VALIDEOIA;
import com.ebiznext.sbt.sample.reception.wsclient.ZWSMMI899VALIDEOIA_Service;

import com.ebiznext.sbt.sample.reception.vo.ResultatRechercheCodeOIA;
import com.ebiznext.sbt.sample.utils.Configuration;

/**
 * @author stephane.manciot@ebiznext.com
 * 
 */
public final class ReceptionValiderCodeOIAClient {

    private static final Logger LOGGER = Logger.getLogger(ReceptionValiderCodeOIAClient.class.getName());

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions", "ZWS_MM_I899_VALIDE_OIA");

    private ReceptionValiderCodeOIAClient(){}

    public static ResultatRechercheCodeOIA execute(String user, String password, String codeOIA) throws java.lang.Exception {
        URL wsdlURL = Configuration.getWSDL("edifice.wsdl.reception.ValiderCodeOIA");
        ZWSMMI899VALIDEOIA_Service ss = new ZWSMMI899VALIDEOIA_Service(wsdlURL, SERVICE_NAME);
        ZWSMMI899VALIDEOIA port = ss.getZWSMMI899VALIDEOIA();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
        ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

        {
            LOGGER.info("Invoking zWSMMI899VALIDEOIA...");
            javax.xml.ws.Holder<java.lang.String> eLIBELLE = new javax.xml.ws.Holder<java.lang.String>();
            javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T> eMSGRETURN = new javax.xml.ws.Holder<com.ebiznext.sbt.sample.reception.wsclient.BAPIRET2T>();
            javax.xml.ws.Holder<java.lang.String> eRESULTAT = new javax.xml.ws.Holder<java.lang.String>();
            port.zMMI899VALIDEOIA(codeOIA, eLIBELLE, eMSGRETURN, eRESULTAT);
            LOGGER.info("zWSMMI899VALIDEOIA.eMSGRETURN=" + eMSGRETURN.value);
            LOGGER.info("zWSMMI899VALIDEOIA.eRESULTAT=" + eRESULTAT.value);
            LOGGER.info("zWSMMI899VALIDEOIA.eLIBELLE=" + eLIBELLE.value);
            return new ResultatRechercheCodeOIA(codeOIA, eLIBELLE.value,
                    eRESULTAT.value.equals("1"));
        }
    }

}
