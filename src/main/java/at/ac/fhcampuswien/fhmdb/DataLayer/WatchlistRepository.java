package at.ac.fhcampuswien.fhmdb.DataLayer;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {
    Dao<WatchlistMovieEntity, Long> dao;
    WatchlistMovieEntity entityMethodCall = new WatchlistMovieEntity();
    public WatchlistRepository(){
        this.dao = Database.getDatabase().getWatchlistDao();
    }

    public void addToWatchlist(Movie movie) throws SQLException {
        dao.create(movieToEntity(movie));
    }

    public List<WatchlistMovieEntity> readAllMovies() throws SQLException{
        return dao.queryForAll();
    }

    public void removeFromWatchlist(Movie movie) throws SQLException {
        dao.create(movieToEntity(movie));
    }

    private WatchlistMovieEntity movieToEntity(Movie movie){
        return new WatchlistMovieEntity(movie.getID(), movie.getTitle(), movie.getDescription(), entityMethodCall.genreToString(movie.getGenre()),
                movie.getReleaseYear(), movie.getImgURL(),movie.getLength(), movie.getRating());
    }
}