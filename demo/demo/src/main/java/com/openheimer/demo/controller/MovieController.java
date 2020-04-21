package com.openheimer.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.openheimer.demo.dto.MovieDTO;
import com.openheimer.demo.model.Movie;
import com.openheimer.demo.model.User;
import com.openheimer.demo.repository.MovieRepository;

@RestController
@RequestMapping("movies")
public class MovieController {

	@Autowired
	MovieRepository movieRepository;

	@GetMapping
	public Page<MovieDTO> getMovies(Pageable pageable) {
		return MovieDTO.asPage(movieRepository.findAll(pageable));
	}

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public MovieDTO createMovie(@RequestBody @Valid MovieDTO body) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Movie movie = movieRepository.save(body.convert(user));
		body.setId(movie.getId());
		return body;
	}

}
