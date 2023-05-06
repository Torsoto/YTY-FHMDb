package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.DataLayer.WatchlistRepository;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.sql.SQLException;


public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label description = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final Label genre = new Label();
    private final Button openDetailsBtn = new Button();
    private final Button addToWatchlistBtn = new Button();
    private final Label mainCast = new Label();
    private final Label director = new Label();
    private final Label ID = new Label();
    private final Label writers = new Label();
    private final Label length = new Label();
    private final Label imgURL = new Label();
    private final HBox Buttons = new HBox(openDetailsBtn, addToWatchlistBtn);
    private final VBox layout = new VBox(title, description, genre, releaseYear);
    private final StackPane stackPane = new StackPane(layout, rating, Buttons);

    WatchlistRepository repository = new WatchlistRepository();

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            title.setText(null);
            description.setText(null);
            genre.setText(null);
            releaseYear.setText(null);
            rating.setText(null);
            imgURL.setText(null);
            mainCast.setText(null);
            writers.setText(null);
            ID.setText(null);
            length.setText(null);
            director.setText(null);
            setBackground(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            description.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            try {
                genre.setText(String.join(", ", movie.getGenre()));
                releaseYear.setText("Release Year: " + movie.getReleaseYear());
                rating.setText(String.valueOf(movie.getRating()));
                mainCast.setText("Main-Cast: " + String.join(", ", movie.getMainCast()));
                director.setText("Director: " + movie.getDirector());
                writers.setText("writers: " + String.join(", ", movie.getWriters()));
                length.setText("Length: " + String.valueOf(movie.getLength()) + "m");
                imgURL.setText("Image URL: " + movie.getImgURL());
                ID.setText("ID: " + movie.getID());
            }catch (NullPointerException e){
                System.out.println(e.getMessage());
            }

            openDetailsBtn.setOnAction(event -> {
                if (openDetailsBtn.getText().equals("Show Details")) {
                    openDetailsBtn.setText("Hide Details");
                    layout.getChildren().addAll(mainCast, director, ID, writers, length, imgURL);
                } else {
                    openDetailsBtn.setText("Show Details");
                    layout.getChildren().removeAll(mainCast, director, ID, writers, length, imgURL);
                }
            });

            addToWatchlistBtn.setOnAction(event -> {
                if (addToWatchlistBtn.getText().equals("Add to Watchlist")) {
                    addToWatchlistBtn.setText("Remove from Watchlist");
                    //TODO Add code so it adds from DATABASE

                    addToWatchlistBtn.setOnMouseClicked(mouseEvent -> {
                        try {
                            repository.addToWatchlist(getItem());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        //Database.getDatabase().dao.create();
                    });
                } else {
                    addToWatchlistBtn.setText("Add to Watchlist");
                    //TODO Add code so it removes from DATABASE
                }
            });

            // color scheme
            mainCast.getStyleClass().add("text-genre");
            writers.getStyleClass().add("text-genre");
            ID.getStyleClass().add("text-genre");
            director.getStyleClass().add("text-genre");
            length.getStyleClass().add("text-genre");
            mainCast.getStyleClass().add("text-genre");
            title.getStyleClass().add("text-movieTitle");
            description.getStyleClass().add("text-white");
            imgURL.getStyleClass().add("text-genre");
            genre.getStyleClass().add("text-genre");
            releaseYear.getStyleClass().add("text-releaseYear");
            rating.getStyleClass().add("text-rating");
            openDetailsBtn.getStyleClass().add("Btn");
            addToWatchlistBtn.getStyleClass().add("Btn");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#00454c"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            description.setMaxWidth(this.getScene().getWidth() - 225);
            description.setWrapText(true);
            layout.setPadding(new Insets(15));
            layout.spacingProperty().set(15);
            layout.alignmentProperty().set(Pos.CENTER_LEFT);

            openDetailsBtn.setText("Show Details");
            addToWatchlistBtn.setText("Add to Watchlist");
            StackPane.setAlignment(rating, Pos.TOP_RIGHT);
            Buttons.setSpacing(10);
            rating.setPadding(new Insets(5));
            Buttons.setPadding(new Insets(10));
            Buttons.setAlignment(Pos.CENTER_RIGHT);
            setGraphic(stackPane);
        }
    }
}
