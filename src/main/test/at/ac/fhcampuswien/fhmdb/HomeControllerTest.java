package at.ac.fhcampuswien.fhmdb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @BeforeEach
    public void setUp() {
        controller = new HomeController();
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

        //TODO: WRITE TESTS FOR THE 4 ADDED FUNCTIONS
    }
}
