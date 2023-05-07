package at.ac.fhcampuswien.fhmdb.DataLayer;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> dao;
    WatchlistMovieEntity entityMethodCall = new WatchlistMovieEntity();

    public WatchlistRepository() {
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    public void addToWatchlist(Movie movie) throws SQLException {
        dao.create(movieToEntity(movie));
    }

    public List<WatchlistMovieEntity> readAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public void resetWatchlist() throws SQLException {
        // Delete all movies from the watchlist
        dao.delete(readAllMovies());

        // Reset the watchlist ID
        dao.executeRaw("TRUNCATE TABLE WATCHLIST RESTART IDENTITY;");
    }

    public WatchlistMovieEntity readMovieById(long id) throws SQLException {
        return dao.queryForId(id);
    }

    public boolean isMovieInWatchlist(Movie movie) throws SQLException {
        List<WatchlistMovieEntity> entities = dao.queryForAll();
        return entities.stream()
                .anyMatch(entity -> entity.getApiId() != null && entity.getApiId().equals(movie.getID()));
    }

    public List<Movie> getAllMovies() throws SQLException {
        List<WatchlistMovieEntity> entities = readAllMovies();
        return entities.stream()
                .map(this::entityToMovie)
                .collect(Collectors.toList());
    }

    public void removeFromWatchlist(Movie movie) throws SQLException {
        String title = movie.getTitle().replace("'", "''");
        List<WatchlistMovieEntity> movies = dao.queryForEq("title", title);
        if (!movies.isEmpty()) {
            dao.delete(movies);
            System.out.println("Deleted " + movie.getTitle() + " from Watchlist");
        }
    }

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