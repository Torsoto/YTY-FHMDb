package at.ac.fhcampuswien.fhmdb;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import static org.junit.jupiter.api.Assertions.*;

public class HomeControllerTest {

    private HomeController controller;
    private List<Movie> allMovies;

    @BeforeEach
    public void setUp() {
        controller = new HomeController();
        allMovies = Movie.initializeMovies();
        controller.allMovies = allMovies;
    }

    @Test
    public void testResetCategory() {
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
        controller.resetCategory();
        assertEquals(allMovies.size(), controller.observableMovies.size());
        assertTrue(controller.observableMovies.containsAll(allMovies));
    }

    @Test
    public void testFilterByGenre() {
        // Add some movies to allMovies
        controller.allMovies.clear();

        controller.allMovies.add(new Movie("Movie 1","Description",new String[]{"ACTION", "DRAMA"}));
        controller.allMovies.add(new Movie("Movie 2","Description",new String[]{"CRIME","DRAMA"}));
        controller.allMovies.add(new Movie("Movie 3","Description",new String[]{"FANTASY"}));
        controller.allMovies.add(new Movie("Movie 4","Description",new String[]{"ACTION"}));
        controller.allMovies.add(new Movie("Movie 5","Description",new String[]{"SCI-FI"}));
        controller.allMovies.add(new Movie("Movie 6","Description",new String[]{"THRILLER"}));

        // Filter by "Action"
        controller.filterByGenreAndSearch("ACTION", "");

        // Check how many Movies are being displayed => has to equal 2 because only 2 Movies have the ACTION genre
        assertEquals(2, controller.observableMovies.size());

        // Check that observableMovies now only contains "Movie 1" and "Movie 4"
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(0)));
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(3)));
    }

    @Test
    public void testSearchField(){
        //Clear allMovies from this controller
        controller.allMovies.clear();

        //add Dummy Movies so that we can test the searchField
        controller.allMovies.add(new Movie("ABC","Description",new String[]{"ACTION", "DRAMA"}));
        controller.allMovies.add(new Movie("DEF","Description",new String[]{"CRIME","DRAMA"}));
        controller.allMovies.add(new Movie("GHI","Description",new String[]{"FANTASY"}));
        controller.allMovies.add(new Movie("JKL","Description",new String[]{"ACTION", "DRAMA"}));
        controller.allMovies.add(new Movie("MNO","Description",new String[]{"CRIME","DRAMA"}));
        controller.allMovies.add(new Movie("PQR","Description",new String[]{"FANTASY"}));
        controller.allMovies.add(new Movie("J","Description",new String[]{"FANTASY"}));
        controller.allMovies.add(new Movie("K","Description",new String[]{"FANTASY"}));
        controller.allMovies.add(new Movie("L","Description",new String[]{"FANTASY"}));

        //Filter the movies through searchField ( genre = null -> we are only testing the searchField)
        controller.filterByGenreAndSearch(null, "JKL");

        //observableMovies should onl display 1 Movie as only 1 Movie has the String "JKL" in their title/description
        assertEquals(1, controller.observableMovies.size()); //same size
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(3))); //checks specific Movie
    }

    @Test
    public void testGenreAndSearchFilter() {
        // Add some movies to allMovies
        controller.allMovies.clear();

        controller.allMovies.add(new Movie("The Dark Knight","Description",new String[]{"ACTION", "CRIME", "DRAMA"}));
        controller.allMovies.add(new Movie("Inception","The Description",new String[]{"ACTION", "ADVENTURE", "SCI-FI"}));
        controller.allMovies.add(new Movie("The Shawshank Redemption","Description",new String[]{"DRAMA"}));
        controller.allMovies.add(new Movie("The Godfather","Description",new String[]{"CRIME", "DRAMA"}));
        controller.allMovies.add(new Movie("The Lord of the Rings: The Fellowship of the Ring","Description",new String[]{"ACTION", "ADVENTURE", "FANTASY"}));
        controller.allMovies.add(new Movie("Matrix","Description",new String[]{"ACTION", "SCI-FI"}));
        controller.allMovies.add(new Movie("The Silence of the Lambs","Description",new String[]{"CRIME", "DRAMA", "THRILLER"}));
        controller.allMovies.add(new Movie("Jurassic Park","Description",new String[]{"ADVENTURE", "SCI-FI", "ACTION"}));
        controller.allMovies.add(new Movie("Forrest Gump","Description",new String[]{"DRAMA", "ROMANCE"}));

        // Genre selected + Search Query
        controller.filterByGenreAndSearch("ACTION", "The");

        // Check that observableMovies now only contains "The Dark Knight","Inception" and "The Lord of the Rings: The Fellowship of the Ring"
        assertEquals(3, controller.observableMovies.size());
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(0)));
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(1)));
        assertTrue(controller.observableMovies.contains(controller.allMovies.get(4)));
    }
    @Test
    public void testSortMoviesByTitle() {
        controller.sort();
        // Verify that observableMovies is sorted alphabetically by title
        for (int i = 0; i < controller.observableMovies.size() - 1; i++) {
            assertTrue(controller.observableMovies.get(i).getTitle().compareTo(controller.observableMovies.get(i + 1).getTitle()) <= 0);
        }

        controller.sortReverse();
        // Verify that observableMovies is sorted in reverse alphabetical order by title
        for (int i = 0; i < controller.observableMovies.size() - 1; i++) {
            assertTrue(controller.observableMovies.get(i).getTitle().compareTo(controller.observableMovies.get(i + 1).getTitle()) >= 0);
        }
    }
}
