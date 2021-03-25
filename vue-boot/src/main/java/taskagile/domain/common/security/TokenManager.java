package taskagile.domain.common.security;

import taskagile.domain.model.user.UserId;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

// 스프링의 웹소켓 구현체는 WebSocketSession 인터페이스를 제공하는데, WebSocketSession.getPrincipal() 메서드로 인증된 사용자 정보에 접근할 수 있게 한다.
// 인증되지 않았다면 null 값일 것이기 때문에, 매우 편리하게 사용자가 인증 되었는지 여부를 확인할 수 있다.

// 하지만 실시간과 관련된 기능을 하나의 독립된 애플리케이션으로 분리할 필요가 있을 때, 스프링 세션을 활용해 세션 클러스터링을 구성하지 않고는
// 인증된 사용자를 가져오는데 WebSocketSession.getPrincipal()을 사용할 수 없다.
// 하지만 다른 방법도 있다. 인증을 수행하는데 JWT를 활용하는 것이다.

// 이 애플리케이션에서는 웹소켓 연결에 대한 인증을 수행하기 위해, JWT 실시간 토큰을 생성한다.
// JWT를 생성하는 로직을 애플리케이션의 나머지 부분과 분리하기 위해 TokenManager를 만든다.

@Component
public class TokenManager {

  private Key secretKey;

  public TokenManager(@Value("${app.token-secret-key}") String secretKey) {
    this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  // 실시간 토큰인 JWT 문자열을 생성한다.
  public String jwt(UserId userId) {
    return Jwts.builder()
      .setSubject(String.valueOf(userId.value()))
      .signWith(secretKey).compact();
  }

  // JWT 문자열에서 User ID를 추출한다. (검증)
  public UserId verifyJwt(String jws) {
    String userIdValue = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jws).getBody().getSubject();
    return new UserId(Long.valueOf(userIdValue));
  }
}
