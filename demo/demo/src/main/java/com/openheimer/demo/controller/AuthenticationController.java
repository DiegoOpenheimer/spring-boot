package com.openheimer.demo.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.openheimer.demo.dto.AuthDTO;
import com.openheimer.demo.dto.UserAuthDTO;
import com.openheimer.demo.model.User;
import com.openheimer.demo.model.UserRepository;
import com.openheimer.demo.security.TokenService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	public AuthDTO auth(@RequestBody @Valid UserAuthDTO body) {
		try {
			Optional<User> user = userRepository.findByEmail(body.getEmail());
			if (!user.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
			}
			Authentication authentication = body.convert();
			authenticationManager.authenticate(authentication);
			return tokenService.generateToken(user.get());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
	}

}
