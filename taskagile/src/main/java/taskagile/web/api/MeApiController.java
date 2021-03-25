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
  @GetMapping("/api/me")
  public ResponseEntity<ApiResult> getMyData(@CurrentUser SimpleUser currentUser) {
    User user = userService.findById(currentUser.getUserId());
    return MyDataResult.build(user);
  }
}
