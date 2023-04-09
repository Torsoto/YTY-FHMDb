package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MovieCell extends ListCell<Movie> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final Label releaseYear = new Label();
    private final Label rating = new Label();
    private final Label genre = new Label();
    private final VBox layout = new VBox(title, detail, genre, releaseYear);
    private final StackPane stackPane = new StackPane(layout, rating);

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (empty || movie == null) {
            setText(null);
            title.setText(null);
            detail.setText(null);
            genre.setText(null);
            releaseYear.setText(null);
            rating.setText(null);
            setBackground(null);
            setGraphic(null);
        } else {
            this.getStyleClass().add("movie-cell");
            title.setText(movie.getTitle());
            detail.setText(
                    movie.getDescription() != null
                            ? movie.getDescription()
                            : "No description available"
            );
            genre.setText(String.join(", ", movie.getGenre()));
            releaseYear.setText("Release Year: " + movie.getReleaseYear());
            rating.setText(String.valueOf(movie.getRating()));
            rating.setPadding(new Insets(5));

            // color scheme
            title.getStyleClass().add("text-movieTitle");
            detail.getStyleClass().add("text-white");
            genre.getStyleClass().add("text-genre");
            releaseYear.getStyleClass().add("text-releaseYear");
            rating.getStyleClass().add("text-rating");
            layout.setBackground(new Background(new BackgroundFill(Color.web("#00454c"), null, null)));

            // layout
            title.fontProperty().set(title.getFont().font(20));
            detail.setMaxWidth(this.getScene().getWidth() - 30);
            detail.setWrapText(true);
            layout.setPadding(new Insets(15));
            layout.spacingProperty().set(10);
            layout.alignmentProperty().set(Pos.CENTER_LEFT);

            StackPane.setAlignment(rating, Pos.TOP_RIGHT);
            setGraphic(stackPane);
        }
    }
}
