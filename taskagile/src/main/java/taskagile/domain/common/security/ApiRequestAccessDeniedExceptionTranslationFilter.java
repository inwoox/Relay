package taskagile.domain.common.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiRequestAccessDeniedExceptionTranslationFilter extends GenericFilterBean {

  private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    try {
      chain.doFilter(request, response);
    } catch (IOException ex) {
      throw ex;
    } catch (Exception ex) {
      if (!request.getRequestURI().startsWith("/api/")) {
        throw ex;
      }

      Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
      RuntimeException ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(
        AccessDeniedException.class, causeChain);

      if (ase == null) {
        throw ex;
      }

      if (response.isCommitted()) {
        throw new ServletException("Unable to translate AccessDeniedException because the response" +
          " of this API request is already committed.", ex);
      }

      if (request.getUserPrincipal() == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      } else {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      }
    }
  }

  private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
    /**
     * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
     */
    protected void initExtractorMap() {
      super.initExtractorMap();
      registerExtractor(ServletException.class, throwable -> {
        ThrowableAnalyzer.verifyThrowableHierarchy(throwable,
          ServletException.class);
        return ((ServletException) throwable).getRootCause();
      });
    }
  }
}
