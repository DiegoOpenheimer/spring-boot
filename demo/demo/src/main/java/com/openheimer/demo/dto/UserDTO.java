package com.openheimer.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.openheimer.demo.model.User;

public class UserDTO {

	private Integer id;

	@NotNull
	@NotEmpty
	private String name;

	@Email
	private String email;

	@NotNull
	@NotEmpty
	@Size(min = 5, max = 20)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	public UserDTO() {

	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User convert() {
		return new User(name, email, new BCryptPasswordEncoder().encode(password));
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static Page<UserDTO> convertPageToPageUserDTO(Page<User> result) {
		return result.map(UserDTO::new);
	}
}
