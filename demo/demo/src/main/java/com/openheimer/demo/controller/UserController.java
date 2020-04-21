package com.openheimer.demo.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.openheimer.demo.dto.UserDTO;
import com.openheimer.demo.model.User;
import com.openheimer.demo.model.UserRepository;

@RestController
@RequestMapping("users")
public class UserController {
	
	UserRepository userRepository;
	
	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	public Page<UserDTO> getUsers(Pageable pageable) {
		Page<User> result = (Page<User>) userRepository.findAll(pageable);
		return UserDTO.convertPageToPageUserDTO(result);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
		Optional<User> user = userRepository.findByEmail(userDTO.getEmail());
		if (user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "user already registered");
		}
		userRepository.save(userDTO.convert());
		return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
	}
	
}
