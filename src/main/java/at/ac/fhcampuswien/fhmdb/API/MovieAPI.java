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
    private OkHttpClient client = new OkHttpClient();

    private Gson createGson(JsonDeserializer<Movie> movieDeserializer) {
        return new GsonBuilder()
                .registerTypeAdapter(Movie.class, movieDeserializer)
                .create();
    }


    private Gson getGsonWithMovieDeserializerForUI() {
        return createGson((json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();

            String title = jsonObject.get("title").getAsString();
            String description = jsonObject.get("description").getAsString();
            JsonArray genreArray = jsonObject.getAsJsonArray("genres");
            String[] genre = new Gson().fromJson(genreArray, String[].class);
            int releaseYear = jsonObject.get("releaseYear").getAsInt();
            String imgURL = jsonObject.get("imgUrl").getAsString();
            double rating = jsonObject.get("rating").getAsDouble();

            return new Movie(title, description, genre, rating, releaseYear, imgURL);
        });
    }

    private Gson getGsonWithMovieDeserializer() {
        return createGson((json, typeOfT, context) -> {
            JsonObject jsonObject = json.getAsJsonObject();

            String id = jsonObject.get("id").getAsString();
            String title = jsonObject.get("title").getAsString();
            String description = jsonObject.get("description").getAsString();
            JsonArray genreArray = jsonObject.getAsJsonArray("genres");
            String[] genre = new Gson().fromJson(genreArray, String[].class);
            int releaseYear = jsonObject.get("releaseYear").getAsInt();
            String imgURL = jsonObject.get("imgUrl").getAsString();
            int lengthInMinutes = jsonObject.get("lengthInMinutes").getAsInt();
            JsonArray directorsArray = jsonObject.getAsJsonArray("directors");
            String director = new Gson().fromJson(directorsArray.get(0), String.class);
            JsonArray writersArray = jsonObject.getAsJsonArray("writers");
            List<String> writers = new Gson().fromJson(writersArray, new TypeToken<List<String>>() {}.getType());
            JsonArray mainCastArray = jsonObject.getAsJsonArray("mainCast");
            List<String> mainCast = new Gson().fromJson(mainCastArray, new TypeToken<List<String>>() {}.getType());
            double rating = jsonObject.get("rating").getAsDouble();

            return new Movie(title, description, genre, rating, releaseYear, mainCast, director, id, imgURL, writers, lengthInMinutes);
        });
    }

    private Request createRequest(String url) {
        return new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "http.agent")
                .build();
    }

    private <T> T executeRequest(Request request, Type type, Gson gson) {
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return gson.fromJson(response.body().charStream(), type);
            } else {
                throw new IOException("Unexpected response: " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String buildURL(String query, String genre, Integer releaseYear, Double ratingFrom) {
        String baseURL = "https://prog2.fh-campuswien.ac.at/movies";
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

    public List<Movie> fetchMovies(String query, String genre, Integer releaseYear, Double ratingFrom, boolean UI) {
        String url = buildURL(query, genre, releaseYear, ratingFrom);
        Request request = createRequest(url);
        Gson gson = UI ? getGsonWithMovieDeserializerForUI() : getGsonWithMovieDeserializer();
        Type movieListType = new TypeToken<List<Movie>>() {}.getType();

        return executeRequest(request, movieListType, gson);
    }

    public Movie fetchMovieById(String movieId, boolean UI) {
        String url = buildMovieByIdURL(movieId);
        Request request = createRequest(url);
        Gson gson = UI ? getGsonWithMovieDeserializerForUI() : getGsonWithMovieDeserializer();

        return executeRequest(request, Movie.class, gson);
    }
}
