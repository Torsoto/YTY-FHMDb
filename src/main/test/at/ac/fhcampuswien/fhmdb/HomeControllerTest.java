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
        @Test
        void testFiltering_withSearchText() {
            controller.filtering("Lord", null, null, null, true);

            for (Movie movie : controller.allMovies){
                boolean found = movie.getTitle().contains("Lord");
                assertTrue(found);
            }
        }

        @Test
        public void should_filter_by_selected_genre() {
            String selectedGenre = "COMEDY";
            controller.filtering(null, selectedGenre, null, null, true);
            // Verify that all movies are of the selected genre
            //controller.allMovies.add(new Movie("test", "test", new String[]{"DRAMA"}, 5.5, 2001, null)); <- add dummy movie after filtering -> test should fail
            for (Movie movie : controller.allMovies) {
                boolean found = false;
                for (String genre : movie.getGenre()) {
                    if (genre.equals(selectedGenre)) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }

        @Test
        public void should_filter_by_release_year() {
            controller.filtering(null, null, 2000, null, true);
            // Verify that all movies were released in the year 2000
            for (Movie movie : controller.allMovies) {
                assertEquals(2000, movie.getReleaseYear());
            }
        }

        @Test
        public void should_filter_by_rating_from() {
            controller.filtering(null, null, null, 7.0, true);
            // Verify that all movies have a rating of 7.0 or higher
            for (Movie movie : controller.allMovies) {
                assertTrue(movie.getRating() >= 7.0);
            }
        }

        @Test
        public void should_filter_by_genre_and_searchText(){
            controller.filtering(null, "ANIMATION", null, null, true);

            assertEquals(4, controller.allMovies.size());

            controller.filtering("n", "ANIMATION", null, null, true);

            for (Movie movie : controller.allMovies){
                boolean found = movie.getTitle().contains("n");
                assertTrue(found);
            }
            assertEquals(2, controller.allMovies.size());
        }

        @Test
        public void should_filter_by_genre_and_rating(){
            controller.filtering(null, "DRAMA", null, 7.0, true);
            boolean found = false;
            // Verify that all movies have a rating of 7.0 or higher
            for (Movie movie : controller.allMovies) {
                assertTrue(movie.getRating() >= 7.0);
                for (String genre : movie.getGenre()) {
                    if (genre.equals("DRAMA")) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }
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
