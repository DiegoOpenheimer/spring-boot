package com.openheimer.demo.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.openheimer.demo.model.User;

public class UserDTO {

	@NotNull @NotEmpty
	private String name;
	
	@Email
	private String email;
	
	@NotNull @NotEmpty @Size(min = 5, max = 20)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	public UserDTO() {
		
	}
	
	public UserDTO(User user) {
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
		return new User(name, email, password);
	}
	
	public static Page<UserDTO> convertPageToPageUserDTO(Page<User> result) {
		return result.map(UserDTO::new);
	}
	
}
