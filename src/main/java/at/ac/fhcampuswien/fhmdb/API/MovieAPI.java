package at.ac.fhcampuswien.fhmdb.API;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class MovieAPI {
    OkHttpClient client = new OkHttpClient();
    String baseURL = "https://prog2.fh-campuswien.ac.at/movies";

    Request request = new Request.Builder()
            .url(baseURL)
            .addHeader("User-Agent", "http.agent")
            .build();
    Call call = client.newCall(request);
    Response response;

    {
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Builds URL to get specific data from API
    public String buildURL(String query, String genre, Integer releaseYear, Double ratingFrom) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseURL)).newBuilder();

        if (query != null) {
            urlBuilder.addQueryParameter("query", query);
        }
        if (genre != null) {
            urlBuilder.addQueryParameter("genre", genre);
        }
        if (releaseYear != null) {
            urlBuilder.addQueryParameter("releaseYear", releaseYear.toString());
        }
        if (ratingFrom != null) {
            urlBuilder.addQueryParameter("ratingFrom", ratingFrom.toString());
        }

        return urlBuilder.build().toString();
    }

    //Builds URL by using ID
    public String buildMovieByIdURL(String movieId) {
        return "http://prog2.fh-campuswien.ac.at/movies/" + movieId;
    }

    //Custom Deserializer to get Movie Object as we had before
// Creates a Gson object with the registered Movie deserializer
    private Gson getGsonWithMovieDeserializer() {
        // Instantiate a GsonBuilder
        GsonBuilder gsonBuilder = new GsonBuilder();
        // Register the custom Movie deserializer with the GsonBuilder
        gsonBuilder.registerTypeAdapter(Movie.class, (JsonDeserializer<Movie>) (json, typeOfT, context) -> {
            // Get the JSON object representing the movie
            JsonObject jsonObject = json.getAsJsonObject();

            // Extract fields from the JSON object
            String title = jsonObject.get("title").getAsString();
            String description = jsonObject.get("description").getAsString();
            JsonArray genreArray = jsonObject.getAsJsonArray("genres");
            String[] genre = new Gson().fromJson(genreArray, String[].class);
            int releaseYear = jsonObject.get("releaseYear").getAsInt();
            String imgURL = jsonObject.get("imgUrl").getAsString();
            double rating = jsonObject.get("rating").getAsDouble();

            // Create and return a new Movie object with the extracted fields
            return new Movie(title, description, genre, rating, releaseYear, imgURL);
        });
        // Build and return the Gson object
        return gsonBuilder.create();
    }

    public List<Movie> fetchMovies(String query, String genre, Integer releaseYear, Double ratingFrom) {
        // Build the URL using the search parameters (query, genre, releaseYear, ratingFrom)
        String url = buildURL(query, genre, releaseYear, ratingFrom);

        // Create a request object with the URL and a custom User-Agent header
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();

        // Execute the request using OkHttpClient (client) and retrieve the response
        try (Response response = client.newCall(request).execute()) {
            // Check if the response is successful
            if (response.isSuccessful()) {
                //use custom deserializer
                Gson gson = getGsonWithMovieDeserializer();
                Type movieListType = new TypeToken<List<Movie>>() {
                }.getType();
                return gson.fromJson(response.body().charStream(), movieListType);
            } else {
                // If the response is not successful, throw an IOException with an error message
                throw new IOException("Unexpected response: " + response);
            }
        } catch (IOException e) {
            // If an exception occurs, wrap it in a RuntimeException and rethrow
            throw new RuntimeException(e);
        }
    }
    // Fetches a movie by its unique ID
    public Movie fetchMovieById(String movieId) {
        // Build the URL for fetching the movie by ID
        String url = buildMovieByIdURL(movieId);
        // Create a request object with the URL and User-Agent header
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();
        // Execute the request and process the response
        try (Response response = client.newCall(request).execute()) {
            // Check if the response is successful
            if (response.isSuccessful()) {
                // Get a Gson object with the registered Movie deserializer
                Gson gson = getGsonWithMovieDeserializer();
                // Deserialize the response body into a Movie object
                return gson.fromJson(response.body().charStream(), Movie.class);
            } else {
                // If the response is not successful, throw an IOException
                throw new IOException("Unexpected response: " + response);
            }
        } catch (IOException e) {
            // If an exception occurs, wrap it in a RuntimeException and rethrow
            throw new RuntimeException(e);
        }
    }
}
