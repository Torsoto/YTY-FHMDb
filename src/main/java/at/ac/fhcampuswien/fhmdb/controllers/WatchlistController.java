package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.API.MovieAPI;
import at.ac.fhcampuswien.fhmdb.DataLayer.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.FhmdbApplication;
import at.ac.fhcampuswien.fhmdb.Interfaces.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.DataLayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.MovieCellWatchList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WatchlistController implements Initializable {
    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public Label homeLabel;
    @FXML
    public Label watchlistLabel;
    @FXML
    public Label aboutLabel;
    private WatchlistRepository repository = new WatchlistRepository();
    public List<Movie> allMovies = Movie.initializeMovies();
    @FXML
    public VBox rootWatchlist;
    @FXML
    public Button refreshBtn;
    protected final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();  // automatically updates corresponding UI elements when underlying data changes

    private final ClickEventHandler onAddToWatchListClicked = (clickedItem) -> {

    };

    public void sort(){
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
    }

    @FXML
    private void handleRefresh() {
        observableMovies.clear();
        try {
            observableMovies.addAll(repository.getAllMovies());
            sort();
        } catch (SQLException e) {
            System.out.println("Error parsing movies from database");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            observableMovies.addAll(repository.getAllMovies());
            sort();
        } catch (SQLException e) {
            System.out.println("Error initializing movies");
        }

        refreshBtn.getStyleClass().add("Btn");

        watchlistLabel.getStyleClass().clear();
        watchlistLabel.getStyleClass().add("text-blue");

        // initialize UI stuff
        movieListView.setItems(observableMovies); // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCellWatchList(onAddToWatchListClicked)); // use custom cell factory to display data

        homeLabel.setOnMouseClicked(mouseEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("/home-view.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
                Stage stage = (Stage)rootWatchlist.getScene().getWindow();
                stage.setScene(scene);

            } catch (IOException ioe) {
                System.out.println("Error loading watchlist");
            }
        });
    }
}