package taskagile.web.api;

import taskagile.domain.application.UserService;
import taskagile.domain.application.command.RegisterCommand;
import taskagile.domain.model.user.EmailAddressExistsException;
import taskagile.domain.model.user.RegistrationException;
import taskagile.domain.model.user.UsernameExistsException;
import taskagile.web.payload.RegistrationPayload;
import taskagile.web.result.ApiResult;
import taskagile.web.result.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class RegistrationApiController extends AbstractBaseController {

  private UserService service;
  public RegistrationApiController(UserService service) {
    this.service = service;
  }

  // @Valid 어노테이션을 통해 이 메서드에 페이로드를 전달하기 전에, 페이로드의 입력 값 유효성을 검증한다
  // @RequestBody 어노테이션을 통해 POST 요청의 페이로드를 받아, RegistrationPayload 객체를 만든다. 
  @PostMapping("/api/registrations") 
  public ResponseEntity<ApiResult> register(@Valid @RequestBody RegistrationPayload payload, HttpServletRequest request) {
    try {
      RegisterCommand command = payload.toCommand();
      addTriggeredBy(command, request);
      service.register(command);
      return Result.created();				 // created() 메서드를 호출해 201 응답을 반환
    } 
    
    catch (RegistrationException e) {		 // 오류 발생시 에러 메시지와 함께 400 에러를 반환
      String errorMessage = "Registration failed";
      if (e instanceof UsernameExistsException) {
        errorMessage = "Username already exists";
      } else if (e instanceof EmailAddressExistsException) {
        errorMessage = "Email address already exists";
      }
      return Result.failure(errorMessage);	 
    }
  }
}
