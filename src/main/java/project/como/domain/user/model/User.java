package project.como.domain.user.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails {

	@Id
	@Column(name = "user_id", updatable = false, unique = true, nullable = false)
	private String userId;

	@Column(nullable = false)
	private String password;

	@Column(name = "username", unique = true, nullable = false)
	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(SimpleGrantedAuthority::new).toList();
	}

	@Column(nullable = false)
	private String email;

	private String githubUrl;

	private String blogUrl;

	public void encodePassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(password);
	}

	public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
		return passwordEncoder.matches(password, getPassword());
	}

	public void updatePassword(PasswordEncoder passwordEncoder, String password) {
		this.password = passwordEncoder.encode(password);
	}

//	public void updateRefreshToken(String refreshToken) {
//		this.refreshToken = refreshToken;
//	}

	public void updateName(String name) {
		this.name = name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
