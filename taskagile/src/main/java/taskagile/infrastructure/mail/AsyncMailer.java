package taskagile.infrastructure.mail;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import taskagile.domain.common.mail.Mailer;
import taskagile.domain.common.mail.Message;


// JavaMailSender에 의존해서 메시지를 전송 (MailSender 인터페이스를 상속하는 JavaMailSender 인터페이스
// Mailer의 send 메서드를 오버라이딩하고 , 그 안에서 mailSender.send 메서드 수행

@Component
public class AsyncMailer implements Mailer {

  private static final Logger log = LoggerFactory.getLogger(AsyncMailer.class);

  private JavaMailSender mailSender;

  public AsyncMailer(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async	   // 비동기로 실행되어야함을 알린다.
  @Override  // Mailer의 send 메서드를 오버라이딩 , 이 안에서 JavaMailSender.send 메서드 수행 / 내부적으로 MailSender.send를 통해 메시지 전
  public void send(Message message) {
    Assert.notNull(message, "Parameter `message` must not be null");

    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();

      if (StringUtils.isNotBlank(message.getFrom())) {
        mailMessage.setFrom(message.getFrom());
      }
      if (StringUtils.isNotBlank(message.getSubject())) {
        mailMessage.setSubject(message.getSubject());
      }
      if (StringUtils.isNotEmpty(message.getBody())) {
        mailMessage.setText(message.getBody());
      }
      if (message.getTo() != null) {
        mailMessage.setTo(message.getTo());
      }

      mailSender.send(mailMessage);
    } catch (MailException e) {
      log.error("Failed to send mail message", e);
    }
  }
}
