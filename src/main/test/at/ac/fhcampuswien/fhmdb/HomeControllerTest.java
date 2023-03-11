package at.ac.fhcampuswien.fhmdb;

import static org.junit.jupiter.api.Assertions.*;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class HomeControllerTest {

    HomeController homeController;
    ObservableList<Movie> observableMovies;

    @BeforeEach
    void setUp() throws Exception {
        homeController = new HomeController();
        observableMovies = homeController.observableMovies;
    }

    @Test
    void testSearchFilter() {
        List<Movie> movies = Arrays.asList(
                new Movie("The Godfather","test", new String[]{"DRAMA","CRIME"}),
                new Movie("The Dark Knight","test", new String[]{"ACTION"}),
                new Movie("12 Angry Men","test", new String[]{"DRAMA","CRIME"}),
                new Movie("Fight Club","test", new String[]{"DRAMA"}),
                new Movie("The Hobbit: An Unexpected Journey","test",new String[]{"FANTASY"})
        );

        observableMovies.clear();
        observableMovies.addAll(movies);

        homeController.searchField.setText("The Godfather");
        homeController.checkSearch();
        assertEquals(1, observableMovies.size());
    }
}