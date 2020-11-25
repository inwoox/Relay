
package app.messages;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {
  private MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping("/welcome")
  public String welcome(Model model) {
    model.addAttribute("message", "Hello, Spring Boot!!"); // Model을 통해, 반환되는 welcome 뷰에 속성을 전달
    return "welcome";
  }

  @PostMapping
  @ResponseBody
  public ResponseEntity<Message> saveMessage(@RequestBody MessageData data) { // HTTP 요청 본문에 전달된 JSON string을 data로 받는다.
    Message saved = messageService.save(data.getText());
    if (saved == null) {
      return ResponseEntity.status(500).build();
    }
    return ResponseEntity.ok(saved);
  }
}