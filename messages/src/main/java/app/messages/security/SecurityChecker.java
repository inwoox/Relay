package app.messages.security;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityChecker {
  private static final Logger logger = LoggerFactory.getLogger(SecurityChecker.class);

  // 포인트컷 지정자로 특정 애노테이션을 사용하는, 어드바이스의 범위를 가지는 포인트컷
  // @Around("execution(* 패키지.interface.메서드)")
  @Pointcut("@annotation(SecurityCheck)")
  public void checkMethodSecurity() {
  } // 포인트컷 시그니처

  // 위의 메서드를 포인트컷 표현식으로 사용하는 (조인 포인트를 둘러싸는 가장 강력한) Around 어드바이스
  @Around("checkMethodSecurity()")
  public Object checkSecurity(ProceedingJoinPoint joinPoint) throws Throwable {
    logger.debug("[AOP] Checking method security.. ");
    // TODO 보안 검사 로직
    Object result = joinPoint.proceed();
    return result;
  }
}
