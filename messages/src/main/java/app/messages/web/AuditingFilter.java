package app.messages.web;

import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

// 필터는 자바 EE의 기술 중에 하나이며, 디자인 패턴인 책임 연쇄 패턴을 구현한 것,
// 서블릿에 도달하기 전에 HTTP 요청에 대한 필터링 작업을 수행할 때 유용하다.
// 경과 시간을 계산하고 디버그 모드로 로그를 출력하는 필터 
public class AuditingFilter extends GenericFilterBean {
  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    long start = new Date().getTime();
    chain.doFilter(req, res);
    long elapsed = new Date().getTime() - start;
    HttpServletRequest request = (HttpServletRequest) req;
    logger.info("[Filter] Request uri=" + request.getRequestURI() + ", method=" + request.getMethod() + " completed in "
        + elapsed + " ms");
  }
}
