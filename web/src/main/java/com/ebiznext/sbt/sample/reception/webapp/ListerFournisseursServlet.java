package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ebiznext.sbt.sample.ServiceLocator;
import com.ebiznext.sbt.sample.reception.service.ReceptionService;
import com.ebiznext.sbt.sample.reception.vo.CommandeAReceptionner;
import com.ebiznext.sbt.sample.reception.vo.Fournisseur;
import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.ebiznext.sbt.sample.utils.Utils;

/**
 * Servlet implementation class RechercherCommandeServlet
 */
public class ListerFournisseursServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListerFournisseursServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListerFournisseursServlet() {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        final String codeOIA = request.getParameter("codeOIA");
        List<CommandeAReceptionner> commandesAReceptionner = new ArrayList<CommandeAReceptionner>();
        List<Fournisseur> commandesAReceptionnerParFournisseur = new ArrayList<Fournisseur>();
        @SuppressWarnings("unchecked")
        final List<String> services = (List<String>) request.getAttribute("services");
        try {
            final String service = "edifice.reception";
            if(services.contains(service)){
                UserToken userToken = (UserToken) request.getAttribute(service);
                final ReceptionService receptionService = ServiceLocator.instance().getReceptionService();
                final CommandeAReceptionner[] commandes = receptionService.listerFournisseurs(
                        userToken.getLogin(), userToken.getPassword(), codeOIA);
                List<CommandeAReceptionner> cmds = Arrays.asList(commandes);
                commandesAReceptionner.addAll(cmds);
            }

            // on regroupe les commandes par fournisseur
            Map<String, Fournisseur> fournisseurs = new HashMap<String, Fournisseur>();
            for (CommandeAReceptionner commandeAReceptionner : commandesAReceptionner) {
                String codeFournisseur = commandeAReceptionner.getEntete().getCodeFournisseur();
                String raisonSociale = commandeAReceptionner.getEntete().getRaisonSociale();
                int nbCommandes = 0;
                if(fournisseurs.containsKey(codeFournisseur)){
                    nbCommandes = fournisseurs.get(codeFournisseur).getNbCommandes();
                }
                nbCommandes++;
                fournisseurs.put(codeFournisseur, new Fournisseur(codeFournisseur, raisonSociale, nbCommandes));
            }
            commandesAReceptionnerParFournisseur.addAll(fournisseurs.values());

            String json = Utils.jsonOutput(commandesAReceptionnerParFournisseur);
            LOGGER.info("responseValue=" + json);
            response.setContentType("application/json");
            final ServletOutputStream outputStream = response.getOutputStream();
            outputStream.print(json);
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
            LOGGER.info("responseValue=" + HttpServletResponse.SC_BAD_REQUEST);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}