package com.galvanize.gmdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter @Setter
public class Movie {

    private String title;
    private String director;
    private String actors;
    private String release;
    private String description;
    private String rating;

    @JsonIgnore
    private int order;
}
