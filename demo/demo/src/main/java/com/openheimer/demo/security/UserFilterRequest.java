package com.openheimer.demo.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.openheimer.demo.model.User;
import com.openheimer.demo.model.UserRepository;

public class UserFilterRequest extends OncePerRequestFilter {

	public static final int BEARER_LENGTH = 7;
	
	private final TokenService tokenService;
	private final UserRepository userRepository;
	
	public UserFilterRequest(TokenService tokenService, UserRepository userRepository) {
		this.tokenService = tokenService;
		this.userRepository = userRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recoverToken(request);
		if (tokenService.valid(token)) {
			Optional<User> user = userRepository.findById(tokenService.getUserIdFromToken(token));
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.get(), null);
			SecurityContextHolder.getContext().setAuthentication( authentication );
		}
		super.doFilter(request, response, filterChain);
	}
	
	private String recoverToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(BEARER_LENGTH, token.length());
	}

	
	
}
