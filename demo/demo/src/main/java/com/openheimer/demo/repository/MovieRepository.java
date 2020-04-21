package com.openheimer.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.openheimer.demo.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

}
