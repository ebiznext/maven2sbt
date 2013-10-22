package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.ebiznext.sbt.sample.utils.Utils;

/**
 * Servlet implementation class ListerCommandesAReceptionnerFournisseur
 */
public class ListerCommandesAReceptionnerFournisseurServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ListerCommandesAReceptionnerFournisseurServlet.class.getName());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListerCommandesAReceptionnerFournisseurServlet() {
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
        final String codeFournisseur = request.getParameter("codeFournisseur");
        final String codeOIA = request.getParameter("codeOIA");
        @SuppressWarnings("unchecked")
        final List<String> services = (List<String>) request.getAttribute("services");
        List<CommandeAReceptionner> commandesAReceptionner = new ArrayList<CommandeAReceptionner>();
        try {
            final String service = "edifice.reception";
            if(services.contains(service)){
                UserToken userToken = (UserToken) request.getAttribute(service);
                final ReceptionService receptionService = ServiceLocator.instance().getReceptionService();
                final CommandeAReceptionner[] commandes = receptionService.listerCommandesAReceptionnerFournisseur(
                        userToken.getLogin(), userToken.getPassword(), codeFournisseur, codeOIA);
                List<CommandeAReceptionner> cmds = Arrays.asList(commandes);
                commandesAReceptionner.addAll(cmds);
            }

            Collections.sort(commandesAReceptionner, new Comparator<CommandeAReceptionner>(){
                @Override
                public int compare(CommandeAReceptionner o1, CommandeAReceptionner o2) {
                    final long date1 = o1.getLignes() != null && o1.getLignes().length > 0 ? o1
                            .getLignes()[0].getDateLivraison().getTime() : 0;
                    final long date2 = o2.getLignes() != null && o2.getLignes().length > 0 ? o2
                            .getLignes()[0].getDateLivraison().getTime() : 0;
                    long compare = date1 - date2;
                    return (int) compare;
                }

            });

            String json = Utils.jsonOutput(commandesAReceptionner);
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