
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
  public String welcome(Model model) {
    model.addAttribute("message", "Hello, Spring Boot!!"); // Model을 통해, 반환되는 welcome 뷰에 속성을 전달
    return "welcome";
  }
}