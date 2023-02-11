package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final DirectorRepository directorRepository;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    @Override
    public List<Movie> listAllMovies() {
        return this.movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return this.movieRepository.findById(id).get();
    }

    @Override
    public Movie create(String name, String description, Double rating, Genre genre, Long director) {
        Director d = this.directorRepository.findById(director).get();
        Movie movie = new Movie(name, description, rating, genre, d);
        return this.movieRepository.save(movie);
    }

    @Override
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director) {
        Movie movie = findById(id);
        Director d = this.directorRepository.findById(director).get();
        movie.setName(name);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setDirector(d);
        return this.movieRepository.save(movie);


    }

    @Override
    public Movie delete(Long id) {
        Movie movie = this.movieRepository.findById(id).get();
        this.movieRepository.delete(movie);
        return movie;
    }

    @Override
    public Movie vote(Long id) {
        return this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
    }

    @Override
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre) {
        List<Movie> newMovies = new ArrayList<>();
        if (rating == null && genre == null) {
            newMovies = this.movieRepository.findAll();
        }
        if (rating != null && genre != null) {
            newMovies = this.movieRepository.findByRatingLessThanAndGenre(rating, genre);
        }
        if (rating != null && genre == null) {
            newMovies = this.movieRepository.findByRatingLessThan(rating);
        }
        if (rating == null && genre != null) {
            newMovies = this.movieRepository.findByGenre(genre);
        }
        return newMovies;
    }
}
