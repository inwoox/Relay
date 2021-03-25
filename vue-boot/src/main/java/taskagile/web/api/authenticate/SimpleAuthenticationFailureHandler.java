package taskagile.web.api.authenticate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import taskagile.utils.JsonUtils;
import taskagile.web.result.ApiResult;

public class SimpleAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    ApiResult failure;
    if (exception instanceof BadCredentialsException) {
      failure = ApiResult.message("Invalid credentials");
    } else if (exception instanceof InsufficientAuthenticationException) {
      failure = ApiResult.message("Invalid authentication request");
    } else {
      failure = ApiResult.message("Authentication failure");
    }
    JsonUtils.write(response.getWriter(), failure);
  }
}
