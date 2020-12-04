package taskagile.domain.common.mail;

public interface MailManager {

  // 목적지 이메일 주소, 제목, 메일 본문 (템플릿), 템플릿에서 사용될 데이터
  void send(String emailAddress, String subject, String template, MessageVariable... variables);
}
