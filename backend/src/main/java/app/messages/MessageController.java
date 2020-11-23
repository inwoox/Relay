
package app.messages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/messages")
public class MessageController {
  @GetMapping("/welcome")
  @ResponseBody // 이거 없이 @Controller만 @RestController로 바꿔도 동일, 반환 값을 뷰가 아니라 HTTP 응답으로
  public String welcome() {
    return "Hello, Welcome to Spring Boot!";
  }
}