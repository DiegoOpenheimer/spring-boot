package com.openheimer.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;

import com.openheimer.demo.model.Movie;
import com.openheimer.demo.model.User;

public class MovieDTO {

	private Integer id;

	@NotNull
	@NotEmpty
	private String name;
	
	public MovieDTO() {
		
	}
	
	public MovieDTO(Movie movie) {
		this.id = movie.getId();
		this.name = movie.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Movie convert(User user) {
		if (user == null) {
			return new Movie(name);
		}
		return new Movie(name, user);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public static Page<MovieDTO> asPage(Page<Movie> movie) {
		return movie.map(MovieDTO::new);
	}

}
