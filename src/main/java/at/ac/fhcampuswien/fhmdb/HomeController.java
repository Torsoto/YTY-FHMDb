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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    public void resetCategory(){
        if (genreComboBox != null){
            genreComboBox.getSelectionModel().clearSelection();
        } else {
            filterByGenreAndSearch(null, "");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);// set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.getAllGenres());

        //Listener that checks for changes in the genreComboBox
        genreComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = searchField.getText().trim().toLowerCase();
            filterByGenreAndSearch(newValue, searchText);
        });

        //Listener that checks for changes in the searchField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = newValue.trim().toLowerCase();
            searchMovies(searchText);
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
}