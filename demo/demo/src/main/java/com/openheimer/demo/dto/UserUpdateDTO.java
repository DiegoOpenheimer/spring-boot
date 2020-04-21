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

public class UserUpdateDTO {
	private Integer id;

	@NotNull
	@NotEmpty
	private String name;

	@Email
	private String email;

	@Size(min = 5, max = 20)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String oldPassword;

	@Size(min = 5, max = 20)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String confirmPassword;

	public UserUpdateDTO() {

	}

	public UserUpdateDTO(User user) {
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

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public User convert(User user) {
		user.setName(name);
		if (oldPassword == null) {
			return user;
		}
		user.setPassword(new BCryptPasswordEncoder().encode(confirmPassword));
		return user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public static Page<UserDTO> convertPageToPageUserDTO(Page<User> result) {
		return result.map(UserDTO::new);
	}

	public boolean validPassword(User user) {
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		if (oldPassword == null) {
			return true;
		}
		return bcrypt.matches(oldPassword, user.getPassword()) && confirmPassword != null;
	}
}
