package com.galvanize.gmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.gmdb.model.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GmdbSearchService {

    public GmdbSearchResponse search(String searchStr) throws IOException {
        List<Movie> matchedMovieList = new ArrayList<>();
        if (searchStr != null && !searchStr.trim().isEmpty()) {
            if (searchStr.length() >= 3 && searchStr.length() <= 20) {
                ObjectMapper mapper = new ObjectMapper();
                InputStream inputStream = new ClassPathResource("movies.json").getInputStream();
                List<Movie> movieList = Arrays.asList(mapper.readValue(inputStream, Movie[].class));

                for (Movie movie : movieList) {
                    if (matchedMovieList.size() == 10) {
                        break;
                    }
                    if (isMatch(movie.getTitle(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(1);
                    } else if (isMatch(movie.getActors(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(2);
                    } else if (isMatch(movie.getDirector(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(3);
                    } else if (isMatch(movie.getRelease(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(4);
                    } else if (isMatch(movie.getDescription(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(5);
                    } else if (isMatch(movie.getRating(), searchStr)) {
                        matchedMovieList.add(movie);
                        movie.setOrder(6);
                    }
                }

                matchedMovieList.sort(new MovieComparator());
            } else {
                return buildErrorResponse();
            }
        }

       return  buildSearchResponse(matchedMovieList);
    }

    private GmdbSearchResponse buildSearchResponse(List<Movie> searchResults) {
        GmdbSearchResponse gmdbSearchResponse=new GmdbSearchResponse();
        Payload payload = null;
        SearchResponseStatus searchResponseStatus = new SearchResponseStatus();
        searchResponseStatus.setResponseStatusEntCd("200");
        searchResponseStatus.setResponseStatusCdDesc("Success");
        List<StatusDetail> statusDetailList=new ArrayList<>();
        if (searchResults.isEmpty()){
            searchResponseStatus.setResponseDesc("No matching search results found");
        } else {
            searchResponseStatus.setResponseDesc("Found");
            payload=new Payload();
            payload.setMovie(searchResults);
        }

        searchResponseStatus.setStatusDetails(statusDetailList);
        gmdbSearchResponse.setPayload(payload);
        gmdbSearchResponse.setResponseStatus(searchResponseStatus);
        return gmdbSearchResponse;
    }

    private boolean isMatch(String attribute, String searchStr) {
        if (attribute == null) {
            return false;
        }
        return attribute.toLowerCase().contains(searchStr.toLowerCase());
    }

    private GmdbSearchResponse buildErrorResponse() {
        GmdbSearchResponse gmdbSearchResponse=new GmdbSearchResponse();
               SearchResponseStatus searchResponseStatus = new SearchResponseStatus();
        searchResponseStatus.setResponseStatusEntCd("400");
        searchResponseStatus.setResponseStatusCdDesc("Bad Request");
        List<StatusDetail> statusDetailList=new ArrayList<>();

        StatusDetail statusDetail = new StatusDetail();
        statusDetail.setStatusDetailEntCd("Invalid");
        statusDetail.setStatusDetailCdDesc("Search Criteria must be between 3 and 20 characters");

        statusDetailList.add(statusDetail);

        searchResponseStatus.setStatusDetails(statusDetailList);
        gmdbSearchResponse.setResponseStatus(searchResponseStatus);
        return gmdbSearchResponse;

    }
}
