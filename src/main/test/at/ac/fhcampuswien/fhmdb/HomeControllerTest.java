package at.ac.fhcampuswien.fhmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
        public void should_reset_category_filter_to_show_all_movies() {
            // Loop through allMovies and filter by genre
            List<Movie> filteredMovies = new ArrayList<>();
            for (Movie movie : allMovies) {
                for (String genre : movie.getGenre()) {
                    if (genre.equals("ACTION")) {
                        filteredMovies.add(movie);
                        break;
                    }
                }
            }

            //Use Filter Method from homeController and look for the same genre as Loop before
            controller.filterByGenreAndSearch("ACTION", "");

            // Check if the filtered list has the same size as the observable list in the controller
            assertEquals(filteredMovies.size(), controller.observableMovies.size());
            // Check if the filtered list contains all movies in the observable list in the controller
            assertTrue(controller.observableMovies.containsAll(filteredMovies));

            // Reset category and check if all movies are displayed
            controller.resetCategory("");
            assertEquals(allMovies.size(), controller.observableMovies.size());
            assertTrue(controller.observableMovies.containsAll(allMovies));
        }

        @Test
        public void should_reset_category_filter_to_show_all_movies_fitting_search_query(){
            // Add some movies to allMovies
            controller.allMovies.clear();

            controller.allMovies.add(new Movie("Movie 1","Description",new String[]{"ACTION", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("Movie 2","Description",new String[]{"CRIME","DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("Movie 3","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("Movie 4","Description",new String[]{"ACTION"},0,0,null));
            controller.allMovies.add(new Movie("Movie 4","Description",new String[]{"SCI-FI"},0,0,null));

            controller.filterByGenreAndSearch("ACTION", "4");

            assertEquals(1, controller.observableMovies.size());

            controller.resetCategory("4");
            assertEquals(2, controller.observableMovies.size());
            assertTrue(controller.observableMovies.contains(allMovies.get(3)));
            assertTrue(controller.observableMovies.contains(allMovies.get(4)));
        }

        @Test
        public void should_filter_movies_by_genre() {
            // Add some movies to allMovies
            controller.allMovies.clear();

            controller.allMovies.add(new Movie("Movie 1","Description",new String[]{"ACTION", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("Movie 2","Description",new String[]{"CRIME","DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("Movie 3","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("Movie 4","Description",new String[]{"ACTION"},0,0,null));
            controller.allMovies.add(new Movie("Movie 5","Description",new String[]{"SCI-FI"},0,0,null));
            controller.allMovies.add(new Movie("Movie 6","Description",new String[]{"THRILLER"},0,0,null));

            // Filter by "Action"
            controller.filterByGenreAndSearch("ACTION", "");

            // Check how many Movies are being displayed => has to equal 2 because only 2 Movies have the ACTION genre
            assertEquals(2, controller.observableMovies.size());

            // Check that observableMovies now only contains "Movie 1" and "Movie 4"
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(0)));
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(3)));
        }

        @Test
        public void should_filter_movies_by_search_query(){
            //Clear allMovies from this controller
            controller.allMovies.clear();

            //add Dummy Movies so that we can test the searchField
            controller.allMovies.add(new Movie("ABC","Description",new String[]{"ACTION", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("DEF","Description",new String[]{"CRIME","DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("GHI","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("JKL","Description",new String[]{"ACTION", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("MNO","Description",new String[]{"CRIME","DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("PQR","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("J","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("K","Description",new String[]{"FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("L","Description",new String[]{"FANTASY"},0,0,null));

            //Filter the movies through searchField ( genre = null -> we are only testing the searchField)
            controller.filterByGenreAndSearch(null, "JKL");

            //observableMovies should onl display 1 Movie as only 1 Movie has the String "JKL" in their title/description
            assertEquals(1, controller.observableMovies.size()); //same size
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(3))); //checks specific Movie
        }

        @Test
        public void should_filter_movies_by_genre_and_search_query() {
            // Add some movies to allMovies
            controller.allMovies.clear();

            controller.allMovies.add(new Movie("The Dark Knight","Description",new String[]{"ACTION", "CRIME", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("Inception","The Description",new String[]{"ACTION", "ADVENTURE", "SCI-FI"},0,0,null));
            controller.allMovies.add(new Movie("The Shawshank Redemption","Description",new String[]{"DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("The Godfather","Description",new String[]{"CRIME", "DRAMA"},0,0,null));
            controller.allMovies.add(new Movie("The Lord of the Rings: The Fellowship of the Ring","Description",new String[]{"ACTION", "ADVENTURE", "FANTASY"},0,0,null));
            controller.allMovies.add(new Movie("Matrix","Description",new String[]{"ACTION", "SCI-FI"},0,0,null));
            controller.allMovies.add(new Movie("The Silence of the Lambs","Description",new String[]{"CRIME", "DRAMA", "THRILLER"},0,0,null));
            controller.allMovies.add(new Movie("Jurassic Park","Description",new String[]{"ADVENTURE", "SCI-FI", "ACTION"},0,0,null));
            controller.allMovies.add(new Movie("Forrest Gump","Description",new String[]{"DRAMA", "ROMANCE"},0,0,null));

            // Genre selected + Search Query
            controller.filterByGenreAndSearch("ACTION", "The");

            // Check that observableMovies now only contains "The Dark Knight","Inception" and "The Lord of the Rings: The Fellowship of the Ring"
            assertEquals(3, controller.observableMovies.size());
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(0)));
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(1)));
            assertTrue(controller.observableMovies.contains(controller.allMovies.get(4)));
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
        public void should_sort_movies_by_title_in_descending_order(){
            controller.sortReverse();
            // Verify that observableMovies is sorted in reverse alphabetical order by title
            for (int i = 0; i < controller.observableMovies.size() - 1; i++) {
                assertTrue(controller.observableMovies.get(i).getTitle().compareTo(controller.observableMovies.get(i + 1).getTitle()) >= 0);
            }
        }

        @Test
        public void should_sort_movies_by_title_while_category_selected(){
            controller.filterByGenreAndSearch("ACTION", "");

            controller.sort();

            List<Movie> sortedMovies = new ArrayList<>(controller.observableMovies);
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
            assertEquals(sortedMovies, controller.observableMovies);

            controller.sortReverse();
            sortedMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            assertEquals(sortedMovies, controller.observableMovies);
        }

        @Test
        public void should_sort_movies_by_title_with_search_query(){
            controller.filterByGenreAndSearch(null, "The");

            controller.sort();

            List<Movie> sortedMovies = new ArrayList<>(controller.observableMovies);
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
            assertEquals(sortedMovies, controller.observableMovies);

            controller.sortReverse();
            sortedMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            assertEquals(sortedMovies, controller.observableMovies);
        }

        @Test
        public void should_sort_movies_by_title_while_category_selected_and_search_query(){
            controller.filterByGenreAndSearch("ACTION", "The");

            controller.sort();

            List<Movie> sortedMovies = new ArrayList<>(controller.observableMovies);
            sortedMovies.sort(Comparator.comparing(Movie::getTitle));
            assertEquals(sortedMovies, controller.observableMovies);

            controller.sortReverse();
            sortedMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
            assertEquals(sortedMovies, controller.observableMovies);
        }
    }

    @Nested
    @DisplayName("Stream Tests")
    public class StreamTests{
        @Test
        public void should_return_most_popular_Actor(){
            String all = controller.getMostPopularActor(api.fetchMovies(null,null,null,null,false));
            String drama = controller.getMostPopularActor(api.fetchMovies(null,"DRAMA",null,null,false));


            assertEquals(drama, "Leonardo DiCaprio");
            assertEquals(all, "Tom Hanks");
        }

        @Test
        public void should_return_longest_movie_Title(){
            int all = controller.getLongestMovieTitle(api.fetchMovies(null,null,null,null,false));

            assertEquals(all,46);
        }

        @Test
        public void should_return_movieCount_from_Director(){
            long d1 = controller.countMoviesFrom(api.fetchMovies(null,null,null,null,false),"Christopher Nolan");
            long d2 = controller.countMoviesFrom(api.fetchMovies(null,null,null,null,false),"Quentin Tarantino");
            long d3 = controller.countMoviesFrom(api.fetchMovies(null,null,null,null,false),"Martin Scorsese");

            assertEquals(2, d1);
            assertEquals(3, d2);
            assertEquals(2, d3);
        }

        @Test
        public void should_return_movies_between_year_span() {
            // create some test movies
            List<String> mainCast1 = Arrays.asList("Actor1", "Actor2");
            List<String> mainCast2 = Arrays.asList("Actor3", "Actor4");
            List<String> mainCast3 = Arrays.asList("Actor5", "Actor6");
            List<String> mainCast4 = Arrays.asList("Actor7", "Actor8");

            Movie movie1 = new Movie("Movie1", "Description1", new String[]{"Genre1"}, 8.0, 2000, mainCast1, "Director1", "ID1", "URL1", Arrays.asList("Writer1"), 120);
            Movie movie2 = new Movie("Movie2", "Description2", new String[]{"Genre2"}, 7.5, 2010, mainCast2, "Director2", "ID2", "URL2", Arrays.asList("Writer2"), 110);
            Movie movie3 = new Movie("Movie3", "Description3", new String[]{"Genre1", "Genre2"}, 9.0, 2015, mainCast3, "Director3", "ID3", "URL3", Arrays.asList("Writer3"), 130);
            Movie movie4 = new Movie("Movie4", "Description4", new String[]{"Genre3"}, 6.5, 1999, mainCast4, "Director4", "ID4", "URL4", Arrays.asList("Writer4"), 100);

            // create a list of movies to test
            List<Movie> movieList = new ArrayList<>(Arrays.asList(movie1, movie2, movie3, movie4));

            // get the movies between 2000 and 2015
            List<Movie> result = controller.getMoviesBetweenYears(movieList, 2000, 2015);

            // check that the result contains the correct movies in the correct order
            assertEquals(3, result.size());
            assertEquals("Movie1", result.get(0).getTitle());
            assertEquals("Movie2", result.get(1).getTitle());
            assertEquals("Movie3", result.get(2).getTitle());
        }

    }
}
