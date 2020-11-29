package taskagile.web.api;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import taskagile.domain.application.UserService;
import taskagile.domain.model.user.EmailAddressExistsException;
import taskagile.domain.model.user.UsernameExistsException;
import taskagile.utils.JsonUtils;
import taskagile.web.payload.RegistrationPayload;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationApiController.class)
public class RegistrationApiControllerTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserService serviceMock;

  @Test		// 비어있는 요청 바디와 함께 POST 요청을 수행하여, 400 에러를 반환하는지 체크
  public void register_blankPayload_shouldFailAndReturn400() throws Exception {
    mvc.perform(post("/api/registrations"))
      .andExpect(status().is(400));
  }

  @Test
  public void register_existedUsername_shouldFailAndReturn400() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("exist");
    payload.setEmailAddress("test@taskagile.com");
    payload.setPassword("MyPassword!");

    doThrow(UsernameExistsException.class) // serviceMock은 register(RegistrationCommand) 메서드가 호출될 때 오류를 던진다.
      .when(serviceMock) 
      .register(payload.toCommand());

    mvc.perform(						   // 목을 준비하고, 이미 존재하는 사용자 이름을 페이로드로 전달해 API 호출시 400 에러 및 메시지가 발생하는지 체크
      post("/api/registrations")
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtils.toJson(payload)))
      .andExpect(status().is(400))
      .andExpect(jsonPath("$.message").value("Username already exists"));
  }

  @Test
  public void register_existedEmailAddress_shouldFailAndReturn400() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("test");
    payload.setEmailAddress("exist@taskagile.com");
    payload.setPassword("MyPassword!");

    doThrow(EmailAddressExistsException.class)    // serviceMock은 register(RegistrationCommand) 메서드가 호출될 때 오류를 던진다.
      .when(serviceMock)
      .register(payload.toCommand());

    mvc.perform(								  // 이미 등록되어 있는 이메일 주소를 페이로드로 전달해 API를 호출하여, 400 에러 및 메시지 발생하는지 체크
      post("/api/registrations")
        .contentType(MediaType.APPLICATION_JSON) 
        .content(JsonUtils.toJson(payload)))
      .andExpect(status().is(400))
      .andExpect(jsonPath("$.message").value("Email address already exists"));
  }

  @Test
  public void register_validPayload_shouldSucceedAndReturn201() throws Exception {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername("sunny");
    payload.setEmailAddress("sunny@taskagile.com");
    payload.setPassword("MyPassword!");

    doNothing().when(serviceMock)				 // serviceMock은 register(RegistrationCommand) 메서드가 호출될 때 아무것도 하지 않는다.
      .register(payload.toCommand());

    mvc.perform(								 // 정상적인 페이로드를 전달해 API를 호출하여 201 응답 (정상적으로 생성)이 발생하는지 체크
      post("/api/registrations")
        .contentType(MediaType.APPLICATION_JSON) 
        .content(JsonUtils.toJson(payload)))
      .andExpect(status().is(201));
  }
}
