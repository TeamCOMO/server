package project.como.domain.user.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import project.como.domain.user.dto.request.MemberLoginRequestDto;
import project.como.domain.user.dto.request.MemberSignupRequestDto;
import project.como.domain.user.model.User;
import project.como.domain.user.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

	@Autowired
	UserServiceImpl userServiceImpl;

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("회원가입")
	void signUp() throws Exception {
		MemberSignupRequestDto dto = new MemberSignupRequestDto(
				"test",
				"test1234",
				"test1234",
				"tester",
				"test@google.com",
				"",
				"");

		userServiceImpl.signUp(dto);

		User user = em.find(User.class, 1);
		assertThat(user.getUsername()).isEqualTo("test");
		assertThat(user.getEmail()).isEqualTo("test@google.com");
	}

	@Test
	void signIn() {
		MemberLoginRequestDto dto = new MemberLoginRequestDto("test", "test1234");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("X-Forwarded-For", "1.1.1.1");
		String token = userServiceImpl.signIn(request, dto);
		assertThat(token).isNotBlank();
	}

	@Test
	void checkDuplicate() {
		boolean test = userServiceImpl.checkDuplicate("test");
		assertThat(test).isTrue();
	}
}