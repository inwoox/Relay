package app.messages.web;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import app.messages.model.Message;
import app.messages.service.MessageService;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageControllerTest {
  @Autowired
  private MockMvc mvc; // 서버측 스프링 MVC 테스트 지원을 위한 주요 진입점

  @MockBean
  private MessageService service; // MessageService의 목을 생성

  @Test // JUnit에게 public void 메서드가 테스트 케이스로 실행될 수 있음을 알려준다.
  public void getMessages_existingMessages_shouldReturnJsonArray() throws Exception {
    Message firstMessage = new Message("First Message");
    List<Message> allMessages = Arrays.asList(firstMessage);

    // service 목이 AllMessages를 리턴하도록 설정한다.
    when(service.getMessages()).thenReturn(allMessages);

    // 사전 조건이 준비되면 get 요청을 수행하고, API 응답이 application/json인지, 상태 코드가 200인지 확인
    // JsonPath를 사용해 결과 Json에 하나의 항목만 있고, text 필드의 값이 First Message인지 확인한다.
    mvc.perform(get("/api/messages").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].text", is(firstMessage.getText())));
  }
}
