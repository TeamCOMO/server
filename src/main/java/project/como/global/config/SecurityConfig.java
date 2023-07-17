package project.como.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import project.como.global.auth.service.JwtProvider;
import project.como.global.auth.filter.JwtAuthenticationFilter;
import project.como.global.auth.service.OAuth2UserService;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtProvider jwtProvider;
	private final OAuth2UserService oAuth2UserService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
//				.cors()
//				.and()
				.httpBasic().disable()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeHttpRequests()
				.requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/sign-up")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/sign-in")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/current-user")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/user/test")).permitAll()
				.requestMatchers(new AntPathRequestMatcher("/HOST/**")).hasRole("어드민")
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

//		http.csrf().disable();
//		http.authorizeHttpRequests(config -> config.anyRequest().permitAll());
//		http.oauth2Login(oauth2Configurer -> oauth2Configurer
//				.loginPage("/login").permitAll()
//				.successHandler(successHandler())
//				.userInfoEndpoint()
//				.userService(oAuth2UserService));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return ((request, response, authentication) -> {
			DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

			String id = defaultOAuth2User.getAttribute("id");
			String body = """
                    {"id":"%s"}
					""".formatted(id);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());

			PrintWriter writer = response.getWriter();
			writer.println(body);
			writer.flush();
		});
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
