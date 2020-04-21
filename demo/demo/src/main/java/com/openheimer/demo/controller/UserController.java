package com.openheimer.demo.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.openheimer.demo.dto.GenericResponse;
import com.openheimer.demo.dto.UserDTO;
import com.openheimer.demo.dto.UserUpdateDTO;
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
		User userCreated = userRepository.save(userDTO.convert());
		return new ResponseEntity<UserDTO>(new UserDTO(userCreated), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserUpdateDTO> editUser(@RequestBody @Valid UserUpdateDTO body, @PathVariable Integer id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
		User userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!userLogged.getEmail().equals(user.get().getEmail())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not unauthorized");
		}
		if (!body.validPassword(userLogged)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error Password");
		}
		User userSaved = userRepository.save(body.convert(user.get()));
		return ResponseEntity.ok(new UserUpdateDTO(userSaved));
	}
	
	@DeleteMapping("/{id}")
	public GenericResponse deleteUser(@PathVariable Integer id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if (!user.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
			}
			User userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!userLogged.getEmail().equals(user.get().getEmail())) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not unauthorized");
			}
			userRepository.deleteById(id);
			return new GenericResponse("User removed with success");
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
		}
	}
	
}
