package taskagile.domain.common.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

// 로그인된 사용자 즉, 현재 사용자의 정보를 스프링 시큐리티의 SecurityContext에서 가져올 필요가 있다.
// 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여, CurrentUser 어노테이션을 만들고,
// 컨트롤러에서 이 어노테이션을 사용하여 현재 사용자를 가져온다.

@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
