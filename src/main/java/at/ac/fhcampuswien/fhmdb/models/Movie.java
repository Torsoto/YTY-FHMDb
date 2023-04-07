package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Movie {
    private final String title;
    private String description;
    private String[] genre;
    private String director;
    private int releaseYear;
    private List<String> mainCast;


    private static final String[] genres = {"ACTION", "ADVENTURE", "ANIMATION", "BIOGRAPHY", "COMEDY",
            "CRIME", "DRAMA", "DOCUMENTARY", "FAMILY", "FANTASY", "HISTORY", "HORROR",
            "MUSICAL", "MYSTERY", "ROMANCE", "SCI-FI", "SPORT", "THRILLER", "WAR",
            "WESTERN"};

    public Movie(String title, String description, String[] genre) {
        this.title = title;
        this.description = description;

        List<String> validGenres = new ArrayList<>();
        for (String g : genre) {
            if (Arrays.asList(genres).contains(g)) {
                validGenres.add(g);
            }
        }
        this.genre = validGenres.toArray(new String[0]);
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
        //TODO Implement MovieAPI HERE INSTEAD OF STATIC MOVIES

        Movie SHAWSHANK = new Movie("The Shawshank Redemption", "A powerful story of hope, friendship, and the human spirit set against the backdrop of a brutal prison system.",new String[]{"DRAMA","CRIME"});
        Movie LOTR1 = new Movie("The Lord of the Rings: The Fellowship of the Ring", "Frodo must destroy a powerful ring with the help of a fellowship on a dangerous journey", new String[]{"FANTASY","ADVENTURE"});
        Movie LOTR2 = new Movie("The Lord of the Rings: The Two Towers", "The fellowship is split and faces new challenges, while Frodo and his Companion Sam are pursued by Gollum", new String[]{"FANTASY","ADVENTURE"});
        Movie LOTR3 = new Movie("The Lord of the Rings: The Return of the King", "the final battle for Middle-earth begins as Frodo and Sam continue their perilous journey to destroy the ring", new String[]{"FANTASY","ADVENTURE"});
        Movie INCEPTION = new Movie("Inception", "A skilled thief is hired to plant an idea into the mind of a corporate heir. As he and his team delve deeper into the man's subconscious, they must face dangerous adversaries and navigate complex layers of reality.", new String[]{"ACTION", "THRILLER", "SCI-FI"});
        Movie PULPFICTION = new Movie("Pulp Fiction", "The lives of two mob hitmen, a boxer, a gangster's wife, and a pair of diner bandits intertwine in four tales of violence and redemption.", new String[]{"CRIME", "DRAMA"});
        Movie FORRESTGUMP = new Movie("Forrest Gump", "Forrest Gump, a simple man with a low IQ, leads an extraordinary life and becomes a witness to and participant in some of the defining moments of the 20th century.", new String[]{"DRAMA", "ROMANCE", "COMEDY"});
        Movie INTERSTELLAR = new Movie("Interstellar", "A group of explorers embark on a mission to save humanity from a dying Earth by traveling through a wormhole to find a new habitable planet in another galaxy.", new String[]{"SCI-FI", "DRAMA"});
        Movie SHUTTERISLAND = new Movie("Shutter Island", "In 1954, U.S. Marshal Teddy Daniels investigates the disappearance of a murderess from an asylum on Shutter Island and uncovers a web of deceit.", new String[]{"MYSTERY", "THRILLER"});
        Movie TAXIDRIVER = new Movie("Taxi Driver", "A mentally unstable Vietnam War veteran works as a nighttime taxi driver in New York City, where the perceived decadence and sleaze fuels his urge for violent action.", new String[]{"DRAMA", "CRIME"});
        Movie KUNGFUPANDA1 = new Movie("Kung Fu Panda", "In ancient China, a clumsy and overweight panda named Po dreams of becoming a kung fu master and is unexpectedly chosen to fulfill an ancient prophecy.", new String[]{"ANIMATION", "ACTION", "COMEDY"});
        Movie KUNGFUPANDA2 = new Movie("Kung Fu Panda 2", "Po must confront a new and powerful villain who is using a secret weapon to conquer China and destroy kung fu.", new String[]{"ANIMATION", "ACTION", "COMEDY"});
        Movie KUNGFUPANDA3 = new Movie("Kung Fu Panda 3", "Po reunites with his long-lost father and discovers a secret village of pandas, but an ancient and powerful villain threatens to destroy their peaceful existence.", new String[]{"ANIMATION", "ACTION", "COMEDY"});
        Movie PARASITE = new Movie("Parasite", "A poor family scheming to become the servants of a wealthy family gradually infiltrates their household, but an unexpected incident sets off a series of events that expose their greed and deception.", new String[]{"THRILLER", "DRAMA"});

        Movie[] movieArray = {SHAWSHANK, LOTR1, LOTR2, LOTR3, INCEPTION, PULPFICTION, FORRESTGUMP, INTERSTELLAR, SHUTTERISLAND, TAXIDRIVER, KUNGFUPANDA1, KUNGFUPANDA2, KUNGFUPANDA3, PARASITE};
        Arrays.sort(movieArray, Comparator.comparing(Movie::getTitle));
        return new ArrayList<>(Arrays.asList(movieArray));
    }

    public String[] getGenre() {
        return genre;
    }


    public static String[] getAllGenres() {
        return genres;
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
}
