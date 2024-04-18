package com.galvanize.gmdb;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.galvanize.gmdb.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GmdbAddService {
    GmdbSearchResponse addMovie(AddMovieRequest request) throws IOException {
        Movie movie = request.getPayload().getMovie();
        ObjectMapper mapper = new ObjectMapper();
        ClassPathResource classPathResource = new ClassPathResource("movies.json");
        InputStream inputStream = classPathResource.getInputStream();

        List<Movie> movieList = new ArrayList<>(Arrays.asList(mapper.readValue(inputStream, Movie[].class)));
        if (!movieList.stream().filter(a -> a.getTitle().equalsIgnoreCase(movie.getTitle())).findFirst().isPresent()) {
            movieList.add(movie);
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(classPathResource.getFile(), movieList);
        }

        return buildAddResponse();
    }

    private GmdbSearchResponse buildAddResponse() {
        GmdbSearchResponse gmdbSearchResponse=new GmdbSearchResponse();
        SearchResponseStatus searchResponseStatus = new SearchResponseStatus();
        searchResponseStatus.setResponseStatusEntCd("200");
        searchResponseStatus.setResponseStatusCdDesc("Success");
        searchResponseStatus.setResponseDesc("Movie details have been successfully added");

        List<StatusDetail> statusDetailList=new ArrayList<>();
        searchResponseStatus.setStatusDetails(statusDetailList);
        gmdbSearchResponse.setResponseStatus(searchResponseStatus);
        return gmdbSearchResponse;
    }
}
