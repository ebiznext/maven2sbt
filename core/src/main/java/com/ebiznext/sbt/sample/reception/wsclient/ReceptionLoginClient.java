package com.ebiznext.sbt.sample.reception.wsclient;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.net.URL;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import com.ebiznext.sbt.sample.reception.wsclient.ZBCLOGINVERIF;
import com.ebiznext.sbt.sample.reception.wsclient.ZBCLOGINVERIF_Service;
import com.ebiznext.sbt.sample.utils.Configuration;


/**
 * This class was generated by Apache CXF 2.6.1 2012-11-02T20:43:07.215+01:00
 * Generated source version: 2.6.1
 * 
 */
public final class ReceptionLoginClient {

    private static final Logger LOGGER = Logger.getLogger(ReceptionLoginClient.class.getName());

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions", "Z_BC_LOGIN_VERIF");

    private ReceptionLoginClient(){}

    public static boolean execute(String user, String password) throws java.lang.Exception {
        URL wsdlURL = Configuration.getWSDL("edifice.wsdl.reception.LoginReception");
        ZBCLOGINVERIF_Service ss = new ZBCLOGINVERIF_Service(wsdlURL, SERVICE_NAME);
        ZBCLOGINVERIF port = ss.getZBCLOGINVERIF();
        ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, user);
        ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);

        {
            LOGGER.info("Invoking zBCLOGINVERIF...");
            java.lang.String _zBCLOGINVERIF__return = port.zBCLOGINVERIF(password, user);
            LOGGER.info("zBCLOGINVERIF.result=" + _zBCLOGINVERIF__return);
            return _zBCLOGINVERIF__return.equals("1");

        }
    }

}
