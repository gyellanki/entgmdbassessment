package com.galvanize.gmdb;

import com.galvanize.gmdb.model.AddMoviePayload;
import com.galvanize.gmdb.model.AddMovieRequest;
import com.galvanize.gmdb.model.GmdbSearchResponse;
import com.galvanize.gmdb.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GmdbAddServiceTests {


    @Test
    void addMovieTest() throws IOException {
        GmdbAddService gmdbAddService = new GmdbAddService();
        GmdbSearchResponse gmdbSearchResponse = gmdbAddService.addMovie(getMockAddMovieRequest());

        Assertions.assertEquals("200",gmdbSearchResponse.getResponseStatus().getResponseStatusEntCd());
        Assertions.assertEquals( "Movie details have been successfully added"   ,gmdbSearchResponse.getResponseStatus().getResponseDesc());
    }

    private AddMovieRequest getMockAddMovieRequest() {
        AddMovieRequest movieRequest = new AddMovieRequest();
        AddMoviePayload payload = new AddMoviePayload();
        Movie movie = new Movie();
        movie.setTitle("Jurassic Park-1");
        movie.setDirector("Steven Spielberg");
        movie.setActors("Sam Neill, Laura Dern, Jeff Goldblum, Richard Attenborough");
        movie.setRelease("1993");
        movie.setDescription("");

        payload.setMovie(movie);
        movieRequest.setPayload(payload);

        return movieRequest;
    }

}
