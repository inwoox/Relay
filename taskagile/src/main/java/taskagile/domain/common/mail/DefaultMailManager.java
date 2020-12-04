package taskagile.domain.common.mail;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import freemarker.template.Configuration;
import freemarker.template.Template;


// MailManager의 구현체인 DefaultMailManager

// 템플릿을 기반으로, 문자열로 변환하여 메시지 바디를 생성
// FreeMarker를 사용 

// 메시지 바디를 포함해 메시지를 인스턴스화
// Message 인터페이스, 구현체인 SimpleMessage 

// 메시지를 전송 
// 메일 서버 API (Mailer)를 활용 / Mailer 인터페이스의 구현체는 AsyncMailer 

// AsyncMailer는 Mailer의 send를 오버라이딩하고, 
// 그 안에서 JavaMailSender의 send (내부적으로 MailSender의 send)를 호출하여 메시지를 발송 


@Component
public class DefaultMailManager implements MailManager {

  private final static Logger log = LoggerFactory.getLogger(DefaultMailManager.class);

  private String mailFrom;							 // 출발지 이메일 주소 구성 속성을 가져옴
  private Mailer mailer;								 // send 메서드를 통해, 메일 발송
  private Configuration configuration;	 // 템플릿 인스턴스를 가져옴

  public DefaultMailManager(@Value("${app.mail-from}") String mailFrom, // application.yml에 설정한 app.mail-from 프로퍼티의 값을 할당
                            Mailer mailer,
                            Configuration configuration) {
    this.mailFrom = mailFrom;
    this.mailer = mailer;
    this.configuration = configuration;
  }

  @Override
  public void send(String emailAddress, String subject, String template, MessageVariable... variables) {
    Assert.hasText(emailAddress, "Parameter `emailAddress` must not be blank");
    Assert.hasText(subject, "Parameter `subject` must not be blank");
    Assert.hasText(template, "Parameter `template` must not be blank");

    String messageBody = createMessageBody(template, variables);  											// 메시지 바디 생성	
    Message message = new SimpleMessage(emailAddress, subject, messageBody, mailFrom);	// 메시지를 인스턴스화
    mailer.send(message);																																// 메시지 전송
  }

  private String createMessageBody(String templateName, MessageVariable... variables) {
    try {
      Template template = configuration.getTemplate(templateName);											// 템플릿 인스턴스 가져오기
      Map<String, Object> model = new HashMap<>();
      if (variables != null) {
        for (MessageVariable variable : variables) {
          model.put(variable.getKey(), variable.getValue());
        }
      }
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);				// 템플릿을 문자열로 변환
    } catch (Exception e) {
      log.error("Failed to create message body from template `" + templateName + "`", e);
      return null;
    }
  }

}
