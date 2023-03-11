package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    public void resetCategory(){
        genreComboBox.getSelectionModel().clearSelection();
        observableMovies.clear();
        observableMovies.addAll(allMovies);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        //Layout of Labels
        searchField.setPrefWidth(800);
        // initialize UI stuff
        movieListView.setItems(observableMovies);// set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.getAllGenres());
        genreComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            observableMovies.clear();
            for (Movie movie : allMovies) {
                if (Arrays.asList(movie.getGenre()).contains(newValue)) {
                    observableMovies.add(movie);
                    movieListView.setItems(observableMovies);
                }
            }
        });

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        // Add event listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                observableMovies.clear();
                observableMovies.addAll(allMovies);
                return;
            }
            String searchText = newValue.trim().toLowerCase();
            observableMovies.clear();
            movieListView.getItems().clear();
            for (Movie movie : allMovies) {
                if (movie.getTitle().toLowerCase().contains(searchText) || movie.getDescription().toLowerCase().contains(searchText)) {
                    observableMovies.add(movie);
                    movieListView.setItems(observableMovies);
                }
            }
        });


        //sortiert die Filme auf- und absteigend
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // sort observableMovies ascending
                FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
                sortBtn.setText("Sort (desc)");
            } else {
                // sort observableMovies descending
                FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
                sortBtn.setText("Sort (asc)");
            }
        });



    }
}