package taskagile;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest					// TaskagileApplication을 찾을 수 있도록, 이 어노테이션을 적용
@ActiveProfiles("test")			// test 프로파일을 활성화하여 테스트 시 test/resources에 있는 application.yml을 읽도록 한다. 
class TaskagileApplicationTests {

	@Test
	void contextLoads() {
	}

}
