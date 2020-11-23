package app.messages;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈을 정의하기 위한 것임을 스프링에 알려주기 위해
@ComponentScan("app.messages") // 컴포넌트를 스캔할 기본 패키지를 알려주기 위해
public class AppConfig {

}
