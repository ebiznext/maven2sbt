package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebiznext.sbt.sample.ServiceLocator;
import com.ebiznext.sbt.sample.reception.service.ReceptionService;
import com.ebiznext.sbt.sample.reception.vo.CommandeReceptionnee;
import com.ebiznext.sbt.sample.reception.vo.UserToken;

/**
 * Servlet implementation class ReceptionServlet
 */
public class ReceptionnerLignesDeCommandeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ReceptionnerLignesDeCommandeServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReceptionnerLignesDeCommandeServlet() {
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
        final String[] lignes = request.getParameterValues("reception");
        final String numerocommande = request.getParameter("numerocommande");
        final String bonDeLivraison = request.getParameter("bonDeLivraison");
        LOGGER.info("request:numerocommande="+ (numerocommande != null ? numerocommande : "null"));
        LOGGER.info("request:bonDeLivraison="+ (bonDeLivraison != null ? bonDeLivraison : "null"));
        final String service = request.getParameter("service");
        UserToken userToken = (UserToken) request.getAttribute(service);
        List<CommandeReceptionnee> lignesDeCommande = new ArrayList<CommandeReceptionnee>();
        for (String ligne : lignes) {
            LOGGER.info("ligne="+ ligne);
            final String[] tab = ligne.split(",");
            final String numligne = tab[0];
            final String quantiterecue = tab[1];
            final CommandeReceptionnee ligneDeCommande = new CommandeReceptionnee(Long.parseLong(numligne),
                    quantiterecue);
            lignesDeCommande.add(ligneDeCommande);
        }
        final ReceptionService receptionService = ServiceLocator.instance().getReceptionService();
        try {
            receptionService.receptionnerLignesDeCommande(userToken.getLogin(), userToken.getPassword(), numerocommande, bonDeLivraison,
                    lignesDeCommande.toArray(new CommandeReceptionnee[0]));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            LOGGER.info("responseValue=" + HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
