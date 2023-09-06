package project.como.domain.apply.controller;

import com.google.gson.Gson;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.como.domain.apply.service.ApplyService;
import project.como.domain.user.exception.UserNotEligibleForApplyException;
import project.como.global.auth.service.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplyController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ApplyControllerTest {

	@MockBean
	private ApplyService applyService;

	@MockBean
	private JPAQueryFactory queryFactory;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Gson gson;

	@MockBean
	private JwtProvider jwtProvider;

	@Test
	@WithMockUser
	@DisplayName("Apply 등록")
	void makeApply() throws Exception {
		mockMvc.perform(post("/api/v1/apply/{post_id}", 1L)
				.param("username", "test")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string("success"));
	}

/*	@Test
	@WithMockUser
	@DisplayName("Apply 등록 NotEligibleForApplyException 발생")
	void makeApplyWithNotEligibleForApplyException() throws Exception {
		doThrow(UserNotEligibleForApplyException.class)
				.when(applyService).makeApply("test", 1L);

		mockMvc.perform(post("/api/v1/apply/{post_id}", 1L)
				.param("username", "test")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.errorCode").value(105))
				.andExpect(jsonPath("$.errorType").value("NOT_ELIGIBLE_FOR_APPLICATION"))
				.andExpect(jsonPath("$.detail").value("해당 공고에 지원할 수 없습니다."));

		verify(applyService).makeApply("test", 1L);
	}*/

	@Test
	@WithMockUser
	@DisplayName("Apply 등록 후 확인")
	void getApply() throws Exception {
		mockMvc.perform(post("/api/v1/apply/{post_id}", 1L)
				.param("username", "test")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string("success"));

		mockMvc.perform(get("/api/v1/apply/{post_id}", 1L)
				.param("username", "test")
				.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(content().string("false"));
	}
}