package com.galvanize.gmdb;

import com.galvanize.gmdb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GmdbController {

    GmdbSearchService gmdbSearchService;
    GmdbAddService gmdbAddService;
    public GmdbController(GmdbSearchService gmdbSearchService, GmdbAddService gmdbAddService){
        this.gmdbSearchService = gmdbSearchService;
        this.gmdbAddService = gmdbAddService;
    }

    @GetMapping("/movies")
    public GmdbSearchResponse getMovies(@RequestParam String search) throws IOException {
        GmdbSearchResponse gmdbSearchResponse= gmdbSearchService.search(search);
        return gmdbSearchResponse;
    }

    @PutMapping("/add-movie")
    public GmdbSearchResponse addMovie(@RequestBody AddMovieRequest request) throws IOException {
        GmdbSearchResponse gmdbSearchResponse= gmdbAddService.addMovie(request);
        return gmdbSearchResponse;
    }


}
