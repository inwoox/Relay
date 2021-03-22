package taskagile.web.api;

import taskagile.domain.application.UserService;
import taskagile.domain.common.security.CurrentUser;
import taskagile.domain.model.user.SimpleUser;
import taskagile.domain.model.user.User;
import taskagile.web.result.ApiResult;
import taskagile.web.result.MyDataResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MeApiController {
  private UserService userService;

  public MeApiController(UserService userService) {
    this.userService = userService;
  }

  // 로그인 사용자 정보를 가져오기 위하여 @AuthenticationPrincipal 어노테이션을 사용하여,
  // CurrentUser 어노테이션을 만들고, 이 어노테이션을 사용하여 현재 사용자를 가져온다.

  // 현재 사용자가 접근할 수 있는 모든 보드와 팀을 가져와서, 현재 유저, 팀, 보드를 가지고 ResponseEntity를 만들어 프론트에 전달한다.
  @GetMapping("/api/me")
  public ResponseEntity<ApiResult> getMyData(@CurrentUser SimpleUser currentUser) {
    User user = userService.findById(currentUser.getUserId());

    // 실시간 토큰인 JWT 문자열을 생성한다.
    // 실시간 클라이언트가 이 토큰을 활용해 연결을 초기화하면, WebSocketHandler 구현체의 afterConnectionEstablished() 메서드에서 요청을 받는다.
    // 실시간 토큰은 이 API의 응답에 포함되어 클라이언트로 전송된다. 
    return MyDataResult.build(user);
  }
}
