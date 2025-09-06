package com.omegleclone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CheckSecurity {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests.requestMatchers("/h2-console/**").permitAll() // allow H2
																										// console
				.anyRequest().authenticated()).csrf((csrf) -> csrf.ignoringRequestMatchers("/h2-console/**")) // disable
																												// CSRF
																												// for
																												// H2
				.headers((headers) -> headers.frameOptions().sameOrigin()); // allow frames from same origin

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
