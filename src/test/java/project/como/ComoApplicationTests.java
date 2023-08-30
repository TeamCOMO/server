package project.como;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ComoApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("333");
	}

}
