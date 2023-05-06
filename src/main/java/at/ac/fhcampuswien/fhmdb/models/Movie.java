package at.ac.fhcampuswien.fhmdb.models;

import at.ac.fhcampuswien.fhmdb.API.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ExceptionHandling.MovieApiException;

import java.util.List;

public class Movie {
    private final String title;
    private final String description;
    private final String[] genre;
    private String director;
    private final int releaseYear;
    private List<String> mainCast;
    private double rating;
    private String ID;
    private String imgURL;
    private List<String> writers;
    private int length;

    private static final String[] genres = {"ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
            "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR",
            "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE FICTION", "SPORT", "THRILLER", "WAR",
            "WESTERN"};


    public static List<Movie> initializeMovies() throws MovieApiException {
        MovieAPI movieAPI = new MovieAPI();
        return movieAPI.fetchMovies(null, null, null, null);
    }

    //Constructor with every possible value from JSON
    public Movie(String title, String description, String[] genre, double rating, int releaseYear, List<String> mainCast, String director, String ID, String imgURL, List<String> writers, int length) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.mainCast = mainCast;
        this.director = director;
        this.ID = ID;
        this.imgURL = imgURL;
        this.writers = writers;
        this.length = length;
        this.releaseYear = releaseYear;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public List<String> getMainCast() {
        return mainCast;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public List<String> getWriters() {
        return writers;
    }

    public void setWriters(List<String> writers) {
        this.writers = writers;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public static String[] getAllGenres(){
        return genres;
    }
}
