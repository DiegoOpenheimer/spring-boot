package com.openheimer.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.openheimer.demo.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

	private Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		return Jwts.builder()
				.setIssuer("com.openheimer.demo")
				.setSubject(user.getId().toString())
				.setId(user.getId().toString())
				.setIssuedAt(new Date())
				.signWith(secret)
				.compact();
	}
	
	public boolean valid(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Integer getUserIdFromToken(String token) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return Integer.parseInt(claims.getSubject());
	}
	
	
}
