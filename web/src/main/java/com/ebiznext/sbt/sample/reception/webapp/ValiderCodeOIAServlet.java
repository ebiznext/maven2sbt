package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebiznext.sbt.sample.ServiceLocator;
import com.ebiznext.sbt.sample.reception.service.ReceptionService;
import com.ebiznext.sbt.sample.reception.vo.ResultatRechercheCodeOIA;
import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.ebiznext.sbt.sample.utils.Utils;

/**
 * Servlet implementation class ReceptionServlet
 */
public class ValiderCodeOIAServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ValiderCodeOIAServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ValiderCodeOIAServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    // reception=numligne1,quantite1&reception=numligne2,quantite2&reception=numligne3,quantite3&numerocommande=CMD123
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        final String codeOIA = request.getParameter("codeOIA");
        LOGGER.info("request:codeOIA="+ (codeOIA != null ? codeOIA : "null"));
        final String service = request.getParameter("service");
        UserToken userToken = (UserToken) request.getAttribute(service);
        final ReceptionService receptionService = ServiceLocator.instance().getReceptionService();
        try {
            ResultatRechercheCodeOIA ret = receptionService.validerCodeOIA(userToken.getLogin(), userToken.getPassword(), codeOIA);
            String json = Utils.jsonOutput(ret);
            LOGGER.info("responseValue=" + json);
            response.setContentType("application/json");
            final ServletOutputStream outputStream = response.getOutputStream();
            outputStream.print(json);
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            LOGGER.info("responseValue=" + HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
