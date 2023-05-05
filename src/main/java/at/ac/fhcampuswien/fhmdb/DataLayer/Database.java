package at.ac.fhcampuswien.fhmdb.DataLayer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class Database {
     private static final String DB_URL = "jdbc:h2:file: ./db/watchlistdb";
     private static final String user = "user";
     private static final String password = "pass";

     private static ConnectionSource connectionSource;

     Dao<WatchlistMovieEntity, Long> dao;

     private static Database instance;

     private Database() {
          try {
               createConnectionSource();
               dao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
               createTables();
          } catch (SQLException e){
               System.out.println(e.getMessage());
          }
     }

     public void testDB() throws SQLException {
          WatchlistMovieEntity entity = new WatchlistMovieEntity("1","abc","wasamk","horror",2019,"www.oasch.com",22,8);
          dao.create(entity);
     }

     public static Database getDatabase(){
          if (instance == null) {
               instance = new Database();
          }
          return instance;
     }

     private static void createTables() throws SQLException {
          TableUtils.createTableIfNotExists(connectionSource,WatchlistMovieEntity.class);
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
