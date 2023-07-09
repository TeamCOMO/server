package project.como.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.como.global.auth.JwtProvider;
import project.como.global.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeHttpRequests()
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/login")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/test")).hasRole("USER")
				.requestMatchers(new AntPathRequestMatcher("/HOST/**")).hasRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
