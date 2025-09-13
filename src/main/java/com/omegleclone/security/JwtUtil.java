package com.omegleclone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import com.omegleclone.dto.LoginResponse;
import com.omegleclone.model.AppUser;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

	// Secure random key, store in Vault/Env in real projects
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	// Token validity: 24 hours
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 1;

	// Generate JWT
	public LoginResponse generateToken(AppUser foundUser) {
		LoginResponse loginResponse = new LoginResponse();

		// set issue & expiration dates
		Date issuedAt = new Date(System.currentTimeMillis());
		Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

		loginResponse.setUserId(foundUser.getId());
		loginResponse.setIssueAt(issuedAt);
		loginResponse.setExpireIn(expiration);

		// build JWT token
		String token = Jwts.builder().setSubject(foundUser.getUsername()).setIssuedAt(issuedAt)
				.setExpiration(expiration).signWith(key) // key must be SecretKey (HMAC) or PrivateKey
				.compact();

		loginResponse.setToken(token);

		return loginResponse;
	}

	// Extract username
	public String extractUsername(String token) {
		return parseClaims(token).getSubject();
	}

	// Validate token
	public boolean validateToken(String token, String username) {
		return username.equals(extractUsername(token)) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return parseClaims(token).getExpiration().before(new Date());
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
