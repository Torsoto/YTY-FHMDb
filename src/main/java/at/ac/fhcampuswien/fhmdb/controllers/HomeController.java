package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.API.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ExceptionHandling.MovieApiException;
import at.ac.fhcampuswien.fhmdb.FhmdbApplication;
import at.ac.fhcampuswien.fhmdb.Interfaces.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.DataLayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HomeController implements Initializable {
    @FXML
    public JFXButton resetBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<String> genreComboBox;

    @FXML
    public JFXComboBox<String> releaseYearComboBox;

    @FXML
    public JFXComboBox<String> ratingComboBox;
    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton filterBtn;
    @FXML
    public Label homeLabel;
    @FXML
    public Label watchlistLabel;
    @FXML
    public Label aboutLabel;
    @FXML
    public VBox root;

    public MovieAPI API = new MovieAPI();
    public List<Movie> allMovies = Movie.initializeMovies();
    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();  // automatically updates corresponding UI elements when underlying data changes
    private WatchlistRepository repository = new WatchlistRepository();

    private final ClickEventHandler onAddToWatchListClicked = (clickedItem) -> {

    };

    public HomeController() throws MovieApiException {
    }


    public void filter() throws MovieApiException {
        String searchText = searchField.getText();
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        String selectedReleaseYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        String selectedRating = ratingComboBox.getSelectionModel().getSelectedItem();

        Integer releaseYear = null;
        if (selectedReleaseYear != null) {
            releaseYear = Integer.parseInt(selectedReleaseYear);
        }
        Double ratingFrom = null;
        if (selectedRating != null) {
            ratingFrom = Double.parseDouble(selectedRating);
        }
        filtering(searchText, selectedGenre, releaseYear, ratingFrom, false);
        sort();
        sortBtn.setText("Sort Z-A");
    }

    public void filtering(String searchText, String selectedGenre, Integer releaseYear, Double ratingFrom, boolean UI) throws MovieApiException {
        MovieAPI API = new MovieAPI();
        allMovies = API.fetchMovies(searchText, selectedGenre, releaseYear, ratingFrom);

        observableMovies.clear();
        observableMovies.addAll(allMovies);
    }

    public void sort(){
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
    }
    public void sortReverse() {
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
    }

    // Initialize and populate the releaseYearComboBox and ratingComboBox
    public void initComboBoxes() {

        // Populate releaseYearComboBox
        Set<Integer> releaseYears = allMovies.stream()
                .map(Movie::getReleaseYear)
                .collect(Collectors.toSet());
        releaseYearComboBox.getItems().addAll(releaseYears.stream().map(String::valueOf).sorted(Comparator.reverseOrder()).toList());
        releaseYearComboBox.setPromptText("Filter by Year");

        // Populate ratingComboBox
        List<String> ratings = IntStream.rangeClosed(1, 9)
                .mapToObj(String::valueOf).sorted(Comparator.reverseOrder()).toList();

        ratingComboBox.getItems().addAll(ratings);
        ratingComboBox.setPromptText("Filter by Rating");
    }

    public void reset() throws MovieApiException {
        searchField.clear();
        genreComboBox.getSelectionModel().clearSelection();
        releaseYearComboBox.getSelectionModel().clearSelection();
        ratingComboBox.getSelectionModel().clearSelection();

        allMovies = API.fetchMovies(null, null, null, null);
        observableMovies.clear();
        observableMovies.addAll(allMovies);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list
        sort();

        // initialize UI stuff
        movieListView.setItems(observableMovies); // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchListClicked)); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.getAllGenres());

        initComboBoxes();

        homeLabel.getStyleClass().clear();
        homeLabel.getStyleClass().add("text-blue");

        homeLabel.setOnMouseClicked(e -> {
            observableMovies.clear();
            observableMovies.addAll(allMovies);
            sort();
        });

        watchlistLabel.setOnMouseClicked(e -> {
            loadWatchlistFXML();
        });

        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort A-Z")) {
                // sort observableMovies ascending
                sort();
                sortBtn.setText("Sort Z-A");
            } else {
                // sort observableMovies descending
                sortReverse();
                sortBtn.setText("Sort A-Z");
            }
        });
    }

    public void setWatchListView (String path) {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("/watchlist-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
            Stage stage = (Stage)root.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException ioe) {
            System.out.println("test");
        }
    }

    public void loadWatchlistFXML(){
        setWatchListView("/watchlist-view.fxml");
    }
    //returns the person who appears most often in the mainCast of the passed movies
    String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorsCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        return actorsCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // filter on the longest movie title of the given movies and return the number of letters in the title
    int getLongestMovieTitle(List<Movie> movies) {
        return movies.stream()
                .mapToInt(movie -> movie.getTitle().length())
                .max()
                .orElse(0);
    }
    //return the number of movies by a certain director
    long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirector().equals(director))
                .count();
    }

    //return the movies that were released between two given years
    List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .sorted(Comparator.comparing(Movie::getReleaseYear))
                .collect(Collectors.toList());
    }
}