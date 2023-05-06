package at.ac.fhcampuswien.fhmdb.DataLayer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {
     public static final String DB_URL = "jdbc:h2:file: ./db/watchlistdb"; //URL for connection to h2 database
     public static final String user = "user"; //USERNAME for connection to h2 database
     public static final String password = "pass"; //PASSWORD for connection to h2 database

     private static ConnectionSource connectionSource;

     private Dao<WatchlistMovieEntity, Long> dao;

     private static Database instance;

     //Constructor for Database -> create Connection -> create Dao -> create Tables
     private Database() {
          try {
               createConnectionSource();
               dao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
               createTables();
          } catch (SQLException e){
               System.out.println(e.getMessage());
          }
     }

     //Singleton for only one access at a time
     public static Database getDatabase(){
          if (instance == null) {
               instance = new Database();
          }
          return instance;
     }

     private static void createTables() throws SQLException {
          TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
     }


     private static void createConnectionSource() throws SQLException {
          connectionSource  = new JdbcConnectionSource(DB_URL, user, password);
     }

     public static ConnectionSource getConnectionSource() {
          return connectionSource;
     }

     public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
          return dao;
     }
}
