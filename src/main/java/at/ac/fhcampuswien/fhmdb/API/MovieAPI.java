package at.ac.fhcampuswien.fhmdb.API;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MovieAPI {
    OkHttpClient client = new OkHttpClient();
    String baseURL = "https://prog2.fh-campuswien.ac.at/movies";

    Request request = new Request.Builder()
            .url(baseURL)
            .addHeader("YTY-Agent", "http.agent")
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

    public List<Movie> fetchMovies(String query, String genre, Integer releaseYear, Double ratingFrom) {
        String url = buildURL(query, genre, releaseYear, ratingFrom);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Gson gson = new Gson();
                Type movieListType = new TypeToken<List<Movie>>() {
                }.getType();
                List<Movie> movies = gson.fromJson(response.body().charStream(), movieListType);

                // Filter the movie list based on the provided parameters
                return movies.stream()
                        .filter(movie -> query == null || movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                        .filter(movie -> genre == null || Arrays.stream(movie.getGenre()).anyMatch(g -> g.equalsIgnoreCase(genre)))
                        //.filter(movie -> releaseYear == null || movie.getReleaseYear().equals(releaseYear))
                        //.filter(movie -> ratingFrom == null || movie.getRating() >= ratingFrom)
                        .collect(Collectors.toList());
            } else {
                throw new IOException("Unexpected response: " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
