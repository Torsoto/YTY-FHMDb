package at.ac.fhcampuswien.fhmdb.DataLayer;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> dao;
    WatchlistMovieEntity entityMethodCall = new WatchlistMovieEntity();

    //initializes the dao object, that is used for the database access
    public WatchlistRepository() {
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    //Adds a new entry to the watchlist if it does not already exist in the database. The method converts the passed Movie into a WatchlistMovieEntity and adds it to the database.
    public void addToWatchlist(Movie movie) throws SQLException {
        dao.create(movieToEntity(movie));
    }

    //Returns all WatchlistMovieEntity entries from the database.
    public List<WatchlistMovieEntity> readAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    //Deletes all entries from the watchlist and resets the ID to the initial value
    public void resetWatchlist() throws SQLException {
        // Delete all movies from the watchlist
        dao.delete(readAllMovies());

        // Reset the watchlist ID
        dao.executeRaw("TRUNCATE TABLE WATCHLIST RESTART IDENTITY;");
    }

    //returns WatchlistEntity entry from the database that corresponds to the passed ID
    public WatchlistMovieEntity readMovieById(long id) throws SQLException {
        return dao.queryForId(id);
    }

    //Returns true if the passed movie already exists in the watchlist, false otherwise. The method searches the database for an entry with the API ID of the movie
    public boolean isMovieInWatchlist(Movie movie) throws SQLException {
        List<WatchlistMovieEntity> entities = dao.queryForAll();
        return entities.stream()
                .anyMatch(entity -> entity.getApiId() != null && entity.getApiId().equals(movie.getID()));
    }

    //returns a List of all films to the watchlist
    public List<Movie> getAllMovies() throws SQLException {
        List<WatchlistMovieEntity> entities = readAllMovies();
        return entities.stream()
                .map(this::entityToMovie)
                .collect(Collectors.toList());
    }

    //removes the WatchlistMovieEntity entry from the database
    public void removeFromWatchlist(Movie movie) throws SQLException {
        String title = movie.getTitle().replace("'", "''");
        List<WatchlistMovieEntity> movies = dao.queryForEq("title", title);
        if (!movies.isEmpty()) {
            dao.delete(movies);
            System.out.println("Deleted " + movie.getTitle() + " from Watchlist");
        }
    }

    //converts a movie into a WatchlistMovieEntity
    private WatchlistMovieEntity movieToEntity(Movie movie) {
        return new WatchlistMovieEntity(
                movie.getID(),
                movie.getTitle(),
                movie.getDescription(),
                entityMethodCall.genreToString(movie.getGenre()),
                movie.getReleaseYear(),
                movie.getImgURL(),
                movie.getLength(),
                movie.getRating());
    }

    //converts a WatchlistMovieEntity into a movie
    public Movie entityToMovie(WatchlistMovieEntity entity) {
        return new Movie(
                entity.getTitle(),
                entity.getDescription(),
                entity.stringToGenreArray(entity.getGenres()),
                entity.getRating(),
                entity.getReleaseYear(),
                null,
                null,
                entity.getApiId(),
                entity.getImgUrl(),
                null,
                entity.getLengthInMinutes()
        );
    }
}