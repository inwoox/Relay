package taskagile.config;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;


// 보통 클래스 내에 @Bean과 같이 사용해 클래스 안에서 등록할 빈을 정의하는 컴포넌트를 의미하지만, 여기서는 단지 Spring에게 환경 설정 파일이라는 것을 알린다
// 이 클래스 안에서 @Bean 어노테이션을 사용한 함수가 리턴하는 객체는 자체적으로 Singleton으로 관리 된다.
@Configuration							
@ConfigurationProperties(prefix="app") // app 접두어를 가지는 프로퍼티를 정의한다는 것을 알려준다.
// @Valid를 적용시킬때는 제약조건을 달아놓은 속성에 대해 전부 유효성 검사를 하게 되는데, 
// 만약 제약조건은 그대로 선언해놓되 원하는 속성만 유효성 검사를 하고싶은 경우에 사용하는 것이 @Validated (원하는 속성들만 유효성 검사를 하려면 먼저 그룹핑 필요)
// Validated, Email, NotBlank로 인해 스프링부트는 애플리케이션 시작 단계에서 해당 프로퍼티에 대한 검증을 수행한다.
// app.mail-from 프로퍼티의 값이 유효한 이메일 주소가 아니면 검증 에러 때문에 애플리케이션은 실패한다.
@Validated
public class ApplicationProperties {
	
	@Email
	@NotBlank
	private String mailFrom;
	
	@NotBlank
  @NotEmpty
  private String tokenSecretKey;

  @NotBlank
  @NotEmpty
  private String realTimeServerUrl;

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public String getTokenSecretKey() {
    return tokenSecretKey;
  }

  public void setTokenSecretKey(String tokenSecretKey) {
    this.tokenSecretKey = tokenSecretKey;
  }

  public String getRealTimeServerUrl() {
    return realTimeServerUrl;
  }

  public void setRealTimeServerUrl(String realTimeServerUrl) {
    this.realTimeServerUrl = realTimeServerUrl;
  }
}
