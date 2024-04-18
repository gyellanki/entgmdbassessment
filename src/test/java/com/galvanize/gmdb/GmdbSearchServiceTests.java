package com.galvanize.gmdb;

import com.galvanize.gmdb.model.GmdbSearchResponse;
import com.galvanize.gmdb.model.StatusDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.galvanize.gmdb.model.Movie;

import java.io.IOException;
import java.util.List;

public class GmdbSearchServiceTests {

    @Test
    void movieSearchTest() throws IOException {
        GmdbSearchService gmdbSearchService = new GmdbSearchService();
        GmdbSearchResponse gmdbSearchResponse = gmdbSearchService.search("The");

        validateSearchResults(gmdbSearchResponse.getPayload().getMovie());
    }

    @Test
    void movieSearchUpperCaseTest() throws IOException {
        GmdbSearchService gmdbSearchService = new GmdbSearchService();
        GmdbSearchResponse gmdbSearchResponse = gmdbSearchService.search("THE");

        validateSearchResults(gmdbSearchResponse.getPayload().getMovie());
    }

    @Test
    void movieSearchLowerCaseTest() throws IOException {
        GmdbSearchService gmdbSearchService = new GmdbSearchService();
        GmdbSearchResponse gmdbSearchResponse = gmdbSearchService.search("the");
        validateSearchResults(gmdbSearchResponse.getPayload().getMovie());
    }

    private static void validateSearchResults(List<Movie> movieList) {
        Assertions.assertEquals(5, movieList.size());
        Movie movie1 = movieList.get(0);
        Assertions.assertEquals("The Avengers", movie1.getTitle());
        Assertions.assertEquals("Joss Whedon", movie1.getDirector());
        Assertions.assertEquals("Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth", movie1.getActors());
        Assertions.assertEquals("2012", movie1.getRelease());
        Assertions.assertEquals("Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.", movie1.getDescription());
        Assertions.assertNull( movie1.getRating());

        Movie movie2 = movieList.get(1);
        Assertions.assertEquals("The Incredibles", movie2.getTitle());
        Assertions.assertEquals("Brad Bird", movie2.getDirector());
        Assertions.assertEquals("Craig T. Nelson, Holly Hunter, Samuel L. Jackson, Jason Lee", movie2.getActors());
        Assertions.assertEquals("2004", movie2.getRelease());
        Assertions.assertEquals("A family of undercover superheroes, while trying to live the quiet suburban life, are forced into action to save the world.", movie2.getDescription());
        Assertions.assertNull(movie2.getRating());

        Movie movie3 = movieList.get(2);
        Assertions.assertEquals("The Lego Batman Movie", movie3.getTitle());
        Assertions.assertEquals("Chris McKay", movie3.getDirector());
        Assertions.assertEquals("Will Arnett, Michael Cera, Rosario Dawson, Ralph Fiennes", movie3.getActors());
        Assertions.assertEquals("2017", movie3.getRelease());
        Assertions.assertEquals("A cooler-than-ever Bruce Wayne must deal with the usual suspects as they plan to rule Gotham City, while discovering that he has accidentally adopted a teenage orphan who wishes to become his sidekick.", movie3.getDescription());
        Assertions.assertNull(movie3.getRating());

        Movie movie4 = movieList.get(3);
        Assertions.assertEquals("Superman Returns", movie4.getTitle());
        Assertions.assertEquals("Bryan Singer", movie4.getDirector());
        Assertions.assertEquals("Brandon Routh, Kate Bosworth, Kevin Spacey, James Marsden", movie4.getActors());
        Assertions.assertEquals("2006", movie4.getRelease());
        Assertions.assertEquals("Superman returns to Earth after spending five years in space examining his homeworld Krypton. But he finds things have changed while he was gone, and he must once again prove himself important to the world.", movie4.getDescription());
        Assertions.assertNull(movie4.getRating());

        Movie movie5 = movieList.get(4);
        Assertions.assertEquals("Steel", movie5.getTitle());
        Assertions.assertEquals("Kenneth Johnson", movie5.getDirector());
        Assertions.assertEquals("Shaquille O'Neal, Annabeth Gish, Judd Nelson, Richard Roundtree", movie5.getActors());
        Assertions.assertEquals("1997", movie5.getRelease());
        Assertions.assertEquals("A scientist for the military turns himself into a cartoon-like superhero when a version of one of his own weapons is being used against enemies.", movie5.getDescription());
        Assertions.assertNull(movie5.getRating());
    }
}
