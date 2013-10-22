package com.ebiznext.sbt.sample.reception.webapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.ebiznext.sbt.sample.reception.vo.UserToken;
import com.ebiznext.sbt.sample.utils.Utils;

/**
 * Servlet Filter implementation class SampleFilter
 */
public class SampleFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(SampleFilter.class.getName());

    /**
     * Default constructor.
     */
    public SampleFilter() {
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        try {
            boolean secure = true;
            LOGGER.info("security is active =" + secure);
            final String[] tokens = request.getParameterValues("token");
            final List<String> serviceList = new ArrayList<String>();
            if (tokens != null) {
                for (String token : tokens) {
                    LOGGER.info("token=" + token);
                    final UserToken userToken = Utils.decrypt(token);
                    LOGGER.info(String.format("service=%s, identifiant=%s, devicedeviceuuid=%s", userToken.getService(),
                            userToken.getLogin(), userToken.getUuid()));
                    request.setAttribute(userToken.getService(), userToken);
                    serviceList.add(userToken.getService());
                }
                request.setAttribute("services", serviceList);
                chain.doFilter(request, response);
            } else if (!secure) {
                    request.setAttribute("services", serviceList);
                    chain.doFilter(request, response);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
    }

}
