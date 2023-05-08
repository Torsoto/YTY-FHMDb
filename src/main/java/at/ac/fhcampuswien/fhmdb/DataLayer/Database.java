package at.ac.fhcampuswien.fhmdb.DataLayer;

import at.ac.fhcampuswien.fhmdb.ExceptionHandling.DatabaseException;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database{
     public static final String DB_URL = "jdbc:h2:file: ./db/watchlistdb"; //URL for connection to h2 database
     public static final String user = "user"; //USERNAME for connection to h2 database
     public static final String password = "pass"; //PASSWORD for connection to h2 database

     private static ConnectionSource connectionSource;

     private Dao<WatchlistMovieEntity, Long> dao; //Interface between the application and the database through which database accesses such as creating, reading, updating and deleting records can be performed.

     private static Database instance;

     //Constructor for Database -> create Connection -> create Dao -> create Tables
     private Database() {
          try {
               createConnectionSource(); //create Connection to h2 database
               dao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class); //create Dao to connect to h2 database
               createTables();
          } catch (SQLException e) {
               MovieCell.showExceptionDialog(new DatabaseException("Database problem: already in use!"));
          }
     }

     //Singleton for only one access at a time
     public static Database getDatabase(){
          if (instance == null) {
               instance = new Database();
          }
          return instance;
     }

     ////tables in h2 database are created
     private static void createTables() throws SQLException {
          TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
     }


     //This method is used to create a connection to the database. Here the JdbcConnectionSource object is created which represents the connection to the H2 database.
     private static void createConnectionSource() throws SQLException {
          connectionSource  = new JdbcConnectionSource(DB_URL, user, password);
     }

     //This method returns the connectionSource object that represents the connection to the database
     public static ConnectionSource getConnectionSource() {
          return connectionSource;
     }

     //This method returns the dao object used for database access.
     public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
          return dao;
     }
}
