package project.como.domain.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record MemberModifyRequestDto(
        @Nullable
        @URL
        String blog_url,
        @Nullable
        @URL
        String github_url,
        @Nullable
        @Size(min = 2, message = "닉네임은 최소 2자 이상으로 입력해주세요.")
        String nickname,
        @Nullable
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+|<>?:{}])[A-Za-z\\d~!@#$%^&*()_+|<>?:{}]{8,30}$",
                message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야 합니다.")
        String password
) {
}
