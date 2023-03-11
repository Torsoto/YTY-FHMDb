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
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox<String> genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    public void resetCategory(){
        if (genreComboBox != null){
            genreComboBox.getSelectionModel().clearSelection();
        }
        observableMovies.clear();
        observableMovies.addAll(allMovies);
    }

    public void checkGenre(){
        genreComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String searchText = searchField.getText().trim().toLowerCase();
            observableMovies.clear();
            for (Movie movie : allMovies) {
                if (Arrays.asList(movie.getGenre()).contains(newValue)
                        && (searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText)
                        || movie.getDescription().toLowerCase().contains(searchText))) {
                    observableMovies.add(movie);
                    System.out.println(observableMovies);
                }
            }
        });
    }

    public void checkSearch(){
        // Add event listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
            String searchText = newValue.trim().toLowerCase();
            observableMovies.clear();
            for (Movie movie : allMovies) {
                if ((selectedGenre == null || Arrays.asList(movie.getGenre()).contains(selectedGenre))
                        && (searchText.isEmpty() || movie.getTitle().toLowerCase().contains(searchText)
                        || movie.getDescription().toLowerCase().contains(searchText))) {
                    observableMovies.add(movie);

                }
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        observableMovies.addAll(allMovies);         // add dummy data to observable list

        //Layout of Labels
        searchField.setPrefWidth(800);
        searchBtn.setText("Reset Category");
        // initialize UI stuff
        movieListView.setItems(observableMovies);// set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.getAllGenres());

        checkGenre();
        checkSearch();

        //Sorts movies
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort A-Z")) {
                // sort observableMovies ascending
                FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
                sortBtn.setText("Sort Z-A");
            } else {
                // sort observableMovies descending
                FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
                sortBtn.setText("Sort A-Z");
            }
        });
    }
}