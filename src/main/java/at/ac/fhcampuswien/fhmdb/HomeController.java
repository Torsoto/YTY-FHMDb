package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Movie> allMovies = Movie.initializeMovies();

    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    public void resetCategory(String search){
        if (genreComboBox != null){
            genreComboBox.getSelectionModel().clearSelection();
        } else {
            filterByGenreAndSearch(null, search);
        }
    }

    public void filterByGenreAndSearch(String selectedGenre, String searchText){
        observableMovies.clear();
        for (Movie movie : allMovies) {
            if ((selectedGenre == null || Arrays.asList(movie.getGenre()).contains(selectedGenre))
                    && (searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText.toLowerCase())
                    || movie.getDescription().toLowerCase().contains(searchText.toLowerCase()))) {
                observableMovies.add(movie);
            }
        }
    }

    public void releaseYearComboBox(){

    }
    public void ratingComboBox(){

    }

    public void searchMovies(String searchText){
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        filterByGenreAndSearch(selectedGenre, searchText);
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
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // Populate ratingComboBox
        Set<Double> ratings = allMovies.stream()
                .map(Movie::getRating)
                .collect(Collectors.toSet());
        ratingComboBox.getItems().addAll(ratings.stream().map(String::valueOf).sorted(Comparator.reverseOrder()).toList());
        ratingComboBox.setPromptText("Filter by Rating");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies); // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.getAllGenres());

        initComboBoxes();

        //Listener that checks for changes in the genreComboBox
        genreComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = searchField.getText().trim().toLowerCase();
            filterByGenreAndSearch(newValue, searchText);
        });

        releaseYearComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    updateFilter();
        });

        ratingComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateFilter();
        });

        //releaseYearComboBox.setPromptText("Filter by Release Year");

        //ratingComboBox.setPromptText("Filter by Rating");


        //Listener that checks for changes in the searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim().toLowerCase();
            searchMovies(searchText);
        });

        resetBtn.setOnAction(actionEvent -> resetCategory(searchField.getText().trim().toLowerCase()));

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

    public void updateFilter() {
        String searchText = searchField.getText().trim().toLowerCase();
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        String selectedYear = releaseYearComboBox.getSelectionModel().getSelectedItem();
        String selectedRating = ratingComboBox.getSelectionModel().getSelectedItem();

        observableMovies.setAll(
                allMovies.stream()
                        .filter(movie -> selectedGenre == null || Arrays.asList(movie.getGenre()).contains(selectedGenre))
                        .filter(movie -> selectedYear == null || movie.getReleaseYear() == Integer.parseInt(selectedYear))
                        .filter(movie -> selectedRating == null || movie.getRating() == Double.parseDouble(selectedRating))
                        .filter(movie -> searchText.isEmpty() ||
                                movie.getTitle().toLowerCase().contains(searchText) ||
                                movie.getDescription().toLowerCase().contains(searchText))
                        .collect(Collectors.toList())
        );
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