package com.openheimer.demo.controller;

import javax.validation.Valid;

import com.openheimer.demo.dto.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.openheimer.demo.dto.MovieDTO;
import com.openheimer.demo.model.Movie;
import com.openheimer.demo.model.User;
import com.openheimer.demo.repository.MovieRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

	@DeleteMapping("/{id}")
	public GenericResponse deleteMovie(@PathVariable Integer id) {
		try {
			Optional<Movie> movie = movieRepository.findById(id);
			if (!movie.isPresent()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found");
			}
			User userLogged = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!movie.get().getUser().getId().equals(userLogged.getId())) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not unauthorized");
			}
			movieRepository.deleteById(id);
			return new GenericResponse("Movie removed with success");
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "movie not found");
		}
	}

}
