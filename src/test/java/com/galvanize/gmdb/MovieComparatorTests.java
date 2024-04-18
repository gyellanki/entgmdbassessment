package com.galvanize.gmdb;

import com.galvanize.gmdb.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class MovieComparatorTests {

    @Test
    void titleCompare(){
        MovieComparator movieComparator=new MovieComparator();

        Movie movie1=new Movie();
        movie1.setTitle("Christmas");
        movie1.setActors("XYZ");
        movie1.setDirector("GGG");
        movie1.setRelease("2010");
        movie1.setOrder(1);

        Movie movie2=new Movie();
        movie2.setTitle("Happy Days");
        movie2.setActors("Christmas");
        movie2.setDirector("GGG");
        movie2.setRelease("2010");
        movie2.setOrder(2);

        List<Movie> movieList = new ArrayList<>();
        movieList.add(movie1);
        movieList.add(movie2);
        movieList.sort(movieComparator);

        Assertions.assertTrue(movieComparator.compare(movie1,movie2)<0);
        Assertions.assertEquals("Christmas", movieList.get(0).getTitle());
        Assertions.assertEquals("Happy Days", movieList.get(1).getTitle());
    }
}
