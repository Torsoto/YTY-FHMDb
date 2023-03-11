package at.ac.fhcampuswien.fhmdb;

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
        controller.observableMovies.clear();
        controller.resetCategory();
        assertEquals(allMovies.size(), controller.observableMovies.size());
        assertTrue(controller.observableMovies.containsAll(allMovies));
    }
}