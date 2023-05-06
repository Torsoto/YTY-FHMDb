package at.ac.fhcampuswien.fhmdb.ExceptionHandling;

public class MovieApiException extends Exception {
    public MovieApiException(String message) {
        super(message);
    }

    public MovieApiException(String message, Throwable cause) {
        super(message, cause);
    }
}