package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebiznext.sbt.sample.ServiceLocator;
import com.ebiznext.sbt.sample.reception.service.ReceptionService;
import com.ebiznext.sbt.sample.reception.vo.UserToken;

/**
 * Servlet implementation class VerifierLogin
 */
public class VerifierLoginReceptionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VerifierLoginReceptionServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public VerifierLoginReceptionServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        boolean loginOK = true;
        @SuppressWarnings("unchecked")
        final List<String> services = (List<String>) request.getAttribute("services");
        LOGGER.info("request:services=" + services);
        final String service = "edifice.reception";
        if(services.contains(service)){
            UserToken userToken = (UserToken) request.getAttribute(service);
            final ReceptionService receptionService = ServiceLocator.instance().getReceptionService();
                LOGGER.info(String.format("service=%s, identifiant=%s, deviceuuid=%s", userToken.getService(), userToken.getLogin(),
                        userToken.getUuid()));
                loginOK = loginOK && receptionService.verifierLogin(userToken.getLogin(), userToken.getPassword());
        }
        if (loginOK) {
            LOGGER.info("responseValue=" + HttpServletResponse.SC_OK);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            LOGGER.info("responseValue=" + HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
