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
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.i.cis.config.ejb.dao.CisUserDao;

/**
 * Redirects client to new user registration if the user still does not exist in application.
 *
 * @author David Matějček
 */
@WebFilter(filterName = "UserFilter", urlPatterns = {"/config/*"}, dispatcherTypes = DispatcherType.REQUEST)
public class UserFilter implements Filter {

  private static final Logger LOG = LoggerFactory.getLogger(UserFilter.class);

  private static final String USER_CONTEXT = "/config/user";
  private static final String CREATE_USER_SERVLET = USER_CONTEXT + "/create.xhtml" ;

  @EJB
  private CisUserDao userDao;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
    ServletException {
    LOG.debug("doFilter(request={}, response={}, chain={})", request, response, chain);
    final HttpServletRequest httpRequest = (HttpServletRequest) request;
    final String login = httpRequest.getUserPrincipal() == null ? null : httpRequest.getUserPrincipal().getName();
    LOG.info("login={}, servletPath={}", login, httpRequest.getServletPath());
    if (login == null || httpRequest.getServletPath().startsWith(USER_CONTEXT)) {
      chain.doFilter(httpRequest, response);
      return;
    }
    if (userDao.getUser(login) != null) {
      chain.doFilter(httpRequest, response);
      return;
    }
    final String contextPath = httpRequest.getContextPath();
    ((HttpServletResponse)response).sendRedirect(contextPath + CREATE_USER_SERVLET );
    LOG.info("Forwarded to {}", CREATE_USER_SERVLET);
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
