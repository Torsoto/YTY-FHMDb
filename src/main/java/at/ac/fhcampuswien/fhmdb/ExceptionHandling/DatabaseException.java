package at.ac.fhcampuswien.fhmdb.ExceptionHandling;

public class DatabaseException extends Exception{
    public DatabaseException(String message) {
        super(message);
    }
}
