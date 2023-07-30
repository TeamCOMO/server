package usertest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.como.domain.user.model.User;

@SpringBootTest
@Transactional
public class UserTest {
    @Test
    public void 확인(){
        User user = new User();
    }
}
