package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.DataLayer.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FhmdbApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FhmdbApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(FhmdbApplication.class.getResource("styles.css")).toExternalForm());
        stage.setTitle("YTY-FHMDb");
        stage.setScene(scene);
        stage.show();

     /* try {
            Database.getDatabase().testDB();
        }catch (SQLException e){
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args) {
        launch();
    }
}