package com.galvanize.gmdb;

import com.galvanize.gmdb.model.Movie;

import java.util.Comparator;

public class MovieComparator implements Comparator<Movie> {

    @Override
    public int compare(Movie o1, Movie o2) {
        return (o1.getOrder() - o2.getOrder());
    }
}
