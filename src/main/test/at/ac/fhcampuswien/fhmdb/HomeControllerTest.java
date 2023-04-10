package at.ac.fhcampuswien.fhmdb;

import java.util.List;

import at.ac.fhcampuswien.fhmdb.API.MovieAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("HomeController Tests")
public class HomeControllerTest {

    private HomeController controller;
    private List<Movie> allMovies;
    private MovieAPI api;

    @BeforeEach
    public void setUp() {
        controller = new HomeController();
        api = new MovieAPI();
        allMovies = Movie.initializeMovies();
        controller.allMovies = allMovies;
    }

    @Nested
    @DisplayName("Filtering Tests")
    class FilteringTests {

    }

    @Nested
    @DisplayName("Sorting Tests")
    class SortingTests {
        @Test
        public void should_sort_movies_by_title_in_ascending_order() {
            controller.sort();
            // Verify that observableMovies is sorted alphabetically by title
            for (int i = 0; i < controller.observableMovies.size() - 1; i++) {
                assertTrue(controller.observableMovies.get(i).getTitle().compareTo(controller.observableMovies.get(i + 1).getTitle()) <= 0);
            }
        }

        @Test
        public void should_sort_movies_by_title_in_descending_order() {
            controller.sortReverse();
            // Verify that observableMovies is sorted in reverse alphabetical order by title
            for (int i = 0; i < controller.observableMovies.size() - 1; i++) {
                assertTrue(controller.observableMovies.get(i).getTitle().compareTo(controller.observableMovies.get(i + 1).getTitle()) >= 0);
            }
        }
    }
    @Nested
    @DisplayName("Stream Tests")
    public class StreamTests {
        @Test
        public void should_return_most_popular_Actor() {
            String all = controller.getMostPopularActor(api.fetchMovies(null, null, null, null, false));
            String drama = controller.getMostPopularActor(api.fetchMovies(null, "DRAMA", null, null, false));


            assertEquals(drama, "Leonardo DiCaprio");
            assertEquals(all, "Tom Hanks");
        }

        @Test
        public void should_return_longest_movie_Title() {
            int all = controller.getLongestMovieTitle(api.fetchMovies(null, null, null, null, false));

            assertEquals(all, 46);
        }

        @Test
        public void should_return_movieCount_from_Director() {
            long d1 = controller.countMoviesFrom(api.fetchMovies(null, null, null, null, false), "Christopher Nolan");
            long d2 = controller.countMoviesFrom(api.fetchMovies(null, null, null, null, false), "Quentin Tarantino");
            long d3 = controller.countMoviesFrom(api.fetchMovies(null, null, null, null, false), "Martin Scorsese");

            assertEquals(2, d1);
            assertEquals(3, d2);
            assertEquals(2, d3);
        }

        @Test
        public void should_return_movies_between_year_span() {
            List<Movie> movies = controller.getMoviesBetweenYears(allMovies, 1999, 2006);
            assertEquals(5, movies.size());
            for (Movie movie : movies) {
                int releaseYear = movie.getReleaseYear();
                assertTrue(releaseYear >= 1999 && releaseYear <= 2006, "Movie release year is not in the specified range");
            }
        }
    }
}
