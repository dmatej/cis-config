package cz.i.cis.config.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet to logout current user.
 */
@WebServlet("/login/logout")
public class LogoutServlet extends HttpServlet {

  /** Serial version UID. */
  private static final long serialVersionUID = 1L;
  /** Logger object used for logging. */
  private static final Logger LOG = LoggerFactory.getLogger(LogoutServlet.class);


  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    LOG.info("service(request, response)");
    request.logout();
    response.sendRedirect("/cis");
  }
}
