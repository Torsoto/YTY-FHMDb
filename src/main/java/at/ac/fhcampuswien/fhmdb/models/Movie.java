package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;

    private String genre;

    private static final String[] genres = {"ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
            "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR",
            "MUSICAL", "MYSTERY", "ROMANCE", "SCIENCE_FICTION", "SPORT", "THRILLER", "WAR",
            "WESTERN"};

    public Movie(String title, String description, String genre) {
        this.title = title;
        this.description = description;

        for (String s : genres) {
            if (genre.equals(s)) {
                System.out.println("genre already exists");
                break;
            }
            else {
                this.genre = genre;
            }
        }
    }

    public  Movie(String title, String description){
        this.title = title;
        this.description = description;
    }

    public  Movie(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        Movie test = new Movie("The Shawshank Redemption", "In 1947, Andy Dufresne (Tim Robbins), a banker in Maine, is convicted of murdering his wife and her lover, a golf pro. Since the state of Maine has no death penalty, he is given two consecutive life sentences and sent to the notoriously harsh Shawshank Prison.","yo");
        movies.add(test);


        return movies;
    }

    public String getGenre() {
        return genre;
    }

    public static String[] getAllGenres() {
        return genres;
    }
}
