package taskagile.infrastructure.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import taskagile.domain.common.mail.CustomMailSenderInterface;
import taskagile.domain.common.mail.MessageVariable;
import taskagile.domain.model.user.User;

@Component
public class CustomMailSender implements CustomMailSenderInterface {
	
	private Configuration configuration;	 // 템플릿 인스턴스를 가져옴
	
	public CustomMailSender(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void send(User user, String templateName, MessageVariable... messageVariable) {
		try {
			
			// 네이버일 경우 smtp.naver.com 을 입력 / Google일 경우 smtp.gmail.com 입력  /  네이버를 사용할 경우 POP3/SMTP를 먼저 사용함으로 변경해야한다.
	  	String host = "smtp.naver.com"; 
	  	final String username = "inwooxx"; 
	  	final String password = "71037960x@"; 
	  	//네이버 이메일 비밀번호를 입력해주세요. 
	  	int port = 465; //포트번호 
	  	String recipient = user.getEmailAddress(); 
	  	String subject = "가입을 축하합니다!!"; 															 // 메일 제목 
	  	//String body = user.getUsername() + "님! 가입을 축하드니다."; 				 // 메일 내용
	  	String body = createMessageBody(templateName, messageVariable);  // 메일 내용을 Freemarker 템플릿을 통해 생성
	  	Properties props = System.getProperties(); 											 // 정보를 담기 위한 객체 생성 
	  	
	  	// SMTP 서버 정보 설정 
	  	props.put("mail.smtp.host", host); 
	  	props.put("mail.smtp.port", port); 
	  	props.put("mail.smtp.auth", "true"); 
	  	props.put("mail.smtp.ssl.enable", "true"); 
	  	props.put("mail.smtp.ssl.trust", host); 
	  	
	  	//Session 생성 
	  	Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() { 
	  		String un=username; 
	  		String pw=password; 
	  		protected javax.mail.PasswordAuthentication getPasswordAuthentication() { 
	  			return new javax.mail.PasswordAuthentication(un, pw); 
	  			} 
	  	}); 
	  	session.setDebug(true); //for debug 
	  	Message mimeMessage = new MimeMessage(session); 																		// MimeMessage 생성 
	  	mimeMessage.setFrom(new InternetAddress("inwooxx@naver.com")); 											// 발신자 셋팅 , 보내는 사람의 이메일주소 
	  	mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient)); // 수신자셋팅  /  .TO 외에 .CC(참조) .BCC(숨은참조)
	  	
	  	mimeMessage.setSubject(subject); 																										// 제목셋팅 
	  	mimeMessage.setText(body); 																													// 내용셋팅 
	  	Transport.send(mimeMessage); 																												// javax.mail.Transport.send() 이용
		
		}
		catch(Exception e){
			new Exception("에러 발생: " + e.getMessage());
		}
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
      return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);				// 템플릿과 템플릿 내에 전달될 데이터를 합쳐서 String으로 변환
    } catch (Exception e) {
      return null;
    }
  }
}
