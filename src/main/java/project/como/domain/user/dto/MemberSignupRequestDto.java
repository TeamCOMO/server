package project.como.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.como.domain.user.model.User;

import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupRequestDto {

	@NotBlank(message = "아이디를 입력해주세요.")
	private String username;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
			message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
	private String password;

	private String checkedPassword;

	@NotBlank(message = "닉네임을 입력해주세요.")
	@Size(min = 2, message = "닉네임은 최소 2자 이상으로 입력해주세요.")
	private String nickname;

	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "이메일 형식에 맞게 입력해주세요.")
	private String email;

	private String githubUrl;

	private String blogUrl;

	@Builder
	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.nickname(nickname)
				.email(email)
				.githubUrl(githubUrl)
				.blogUrl(blogUrl)
				.roles(Collections.singletonList("USER"))
				.build();
	}
}
