/**
 *
 */
package cz.i.cis.config.web.filter;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;
import cz.i.cis.config.jpa.CisUser;

/**
 * Redirects client to new user registration if the user still does not exist in application.
 *
 * @author David Matějček
 */
@WebFilter(filterName = "UserFilter", urlPatterns = {"/config/*"}, dispatcherTypes = DispatcherType.REQUEST)
public class UserFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(UserFilter.class);

  private static final String USER_CONTEXT = "/config/user";
  private static final String CREATE_USER_SERVLET = USER_CONTEXT + "/create.xhtml";
  private static final String REMOVED_CONTEXT_SERVLET = "/removed.xhtml";

  @EJB
  private CisUserDao userDao;


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {
    LOG.debug("doFilter(request={}, response={}, chain={})", request, response, chain);

    final long start = System.currentTimeMillis();
    try {
      final HttpServletRequest httpRequest = (HttpServletRequest) request;
      final boolean filter = processUser(httpRequest, response, chain);
      if (filter) {
        chain.doFilter(httpRequest, response);
      }
    } finally {
      LOG.info("Request processed in {} ms", System.currentTimeMillis() - start);
    }
  }


  private boolean processUser(HttpServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    LOG.trace("processUser(request, response, chain)");

    final String login = request.getUserPrincipal() == null ? null : request.getUserPrincipal().getName();
    LOG.info("login={}, servletPath={}", login, request.getServletPath());
    if (login == null || request.getServletPath().equals(CREATE_USER_SERVLET)) {
      chain.doFilter(request, response);
      return false;
    }

    final CisUser user = userDao.getUser(login);
    if (user == null) {
      final String contextPath = request.getContextPath();
      ((HttpServletResponse) response).sendRedirect(contextPath + CREATE_USER_SERVLET);
      LOG.info("Forwarded to {}", CREATE_USER_SERVLET);
      return false;
    }

    if (user.isDeleted()) {
      final String contextPath = request.getContextPath();
      ((HttpServletResponse) response).sendRedirect(contextPath + REMOVED_CONTEXT_SERVLET);
      LOG.info("Forwarded to {}", REMOVED_CONTEXT_SERVLET);

      return false;
    }
    return true;
  }


  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOG.debug("init(filterConfig={})", filterConfig);
  }


  @Override
  public void destroy() {
    LOG.debug("destroy()");
  }
}
