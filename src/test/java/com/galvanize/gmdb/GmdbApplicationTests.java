package com.galvanize.gmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.gmdb.model.AddMoviePayload;
import com.galvanize.gmdb.model.AddMovieRequest;
import com.galvanize.gmdb.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GmdbApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void searchWithEmptyString_returns200() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", ""))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "     \"payload\": null,\n" +
                        "     \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"No matching search results found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithParameterReturnsListofMovies() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "scientist"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "                    \"payload\": {\n" +
                        "                        \"movie\": [\n" +
                        "                            {\n" +
                        "                                \"title\": \"Steel\",\n" +
                        "                                \"director\": \"Kenneth Johnson\",\n" +
                        "                                \"actors\": \"Shaquille O'Neal, Annabeth Gish, Judd Nelson, Richard Roundtree\",\n" +
                        "                                \"release\": \"1997\",\n" +
                        "                                \"description\": \"A scientist for the military turns himself into a cartoon-like superhero when a version of one of his own weapons is being used against enemies.\",\n" +
                        "                                \"rating\": null\n" +
                        "                            }\n" +
                        "                        ]\n" +
                        "                    },\n" +
                        "                    \"responseStatus\": {\n" +
                        "                        \"responseStatusEntCd\": \"200\",\n" +
                        "                        \"responseStatusCdDesc\": \"Success\",\n" +
                        "                        \"responseDesc\": \"Found\",\n" +
                        "                        \"statusDetails\": []\n" +
                        "                    }\n" +
                        "                }"));

    }

    @Test
    void searchWithStringlessthan3char() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "a"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"400\",\n" +
                        "        \"responseStatusCdDesc\": \"Bad Request\",\n" +
                        "        \"responseDesc\": null,\n" +
                        "        \"statusDetails\": [\n" +
                        "            {\n" +
                        "                \"statusDetailEntCd\": \"Invalid\",\n" +
                        "                \"statusDetailCdDesc\": \"Search Criteria must be between 3 and 20 characters\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithStringmorethan20char() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "abcdefghiawerwpeurweoruweoriuweooiob"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"400\",\n" +
                        "        \"responseStatusCdDesc\": \"Bad Request\",\n" +
                        "        \"responseDesc\": null,\n" +
                        "        \"statusDetails\": [\n" +
                        "            {\n" +
                        "                \"statusDetailEntCd\": \"Invalid\",\n" +
                        "                \"statusDetailCdDesc\": \"Search Criteria must be between 3 and 20 characters\"\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithStringwithNumbersandSpecialchar() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "%$#@901"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"No matching search results found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithStringwithSpecialchar() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "N/A"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": {\n" +
                        "        \"movie\": [\n" +
                        "            {\n" +
                        "                \"title\": \"Rocketeer\",\n" +
                        "                \"director\": \"Jay Light\",\n" +
                        "                \"actors\": \"Christopher Coakley\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"N/A\",\n" +
                        "                \"rating\": null\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithStringWith20char() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "abcdefghifjlkswuiouy"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"No matching search results found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void searchWithPriority() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "Chris"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"payload\":{\"movie\":[{\"title\":\"Christopher-1\",\"director\":\"Chris\",\"actors\":\"Will Arnett, Michael Cera, Rosario Dawson, Ralph Fiennes\",\"release\":\"2017\",\"description\":\"A cooler-than-ever Bruce Wayne\",\"rating\":null},{\"title\":\"Christopher-2\",\"director\":\"McKay\",\"actors\":\"Will Arnett, Michael Cera, Rosario Dawson, Ralph Fiennes\",\"release\":\"2017\",\"description\":\"A cooler-than-ever Bruce Wayne\",\"rating\":null},{\"title\":\"The Avengers\",\"director\":\"Joss Whedon\",\"actors\":\"Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth\",\"release\":\"2012\",\"description\":\"Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.\",\"rating\":null},{\"title\":\"Rocketeer\",\"director\":\"Jay Light\",\"actors\":\"Christopher Coakley\",\"release\":\"2012\",\"description\":\"N/A\",\"rating\":null},{\"title\":\"The Lego Batman Movie\",\"director\":\"Chris McKay\",\"actors\":\"Will Arnett, Michael Cera, Rosario Dawson, Ralph Fiennes\",\"release\":\"2017\",\"description\":\"A cooler-than-ever Bruce Wayne must deal with the usual suspects as they plan to rule Gotham City, while discovering that he has accidentally adopted a teenage orphan who wishes to become his sidekick.\",\"rating\":null}]},\"responseStatus\":{\"responseStatusEntCd\":\"200\",\"responseStatusCdDesc\":\"Success\",\"responseDesc\":\"Found\",\"statusDetails\":[]}}"));
    }

    @Test
    void limitResultsToTen() throws Exception {
        mockMvc.perform(get("/movies").queryParam("search", "Avengers"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": {\n" +
                        "        \"movie\": [\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-1\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-2\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-3\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-4\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-5\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-6\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-7\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-8\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"Avengers-9\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr.\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes\",\n" +
                        "                \"rating\": null\n" +
                        "            },\n" +
                        "            {\n" +
                        "                \"title\": \"The Avengers\",\n" +
                        "                \"director\": \"Joss Whedon\",\n" +
                        "                \"actors\": \"Robert Downey Jr., Chris Evans, Mark Ruffalo, Chris Hemsworth\",\n" +
                        "                \"release\": \"2012\",\n" +
                        "                \"description\": \"Earth's mightiest heroes must come together and learn to fight as a team if they are going to stop the mischievous Loki and his alien army from enslaving humanity.\",\n" +
                        "                \"rating\": null\n" +
                        "            }\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void addMovie() throws Exception {
        mockMvc.perform(put("/add-movie")
                        .content(asJsonString(getMockAddMovieRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Movie details have been successfully added\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));

        mockMvc.perform(get("/movies").queryParam("search", "Jurassic"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": {\n" +
                        "        \"movie\": [\n" +
                        "            {\n" +
                        "                \"title\": \"Jurassic Park-1\",\n" +
                        "                \"director\": \"Steven Spielberg\",\n" +
                        "                \"actors\": \"Sam Neill, Laura Dern, Jeff Goldblum, Richard Attenborough\",\n" +
                        "                \"release\": \"1993\",\n" +
                        "                \"description\": \"\",\n" +
                        "                \"rating\": null\n" +
                        "            }            \n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    @Test
    void addMovie_sameMovieMultipleTimes() throws Exception {
        mockMvc.perform(put("/add-movie")
                        .content(asJsonString(getMockAddMovieRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Movie details have been successfully added\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
        mockMvc.perform(put("/add-movie")
                        .content(asJsonString(getMockAddMovieRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": null,\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Movie details have been successfully added\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));

        mockMvc.perform(get("/movies").queryParam("search", "Jurassic"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"payload\": {\n" +
                        "        \"movie\": [\n" +
                        "            {\n" +
                        "                \"title\": \"Jurassic Park-1\",\n" +
                        "                \"director\": \"Steven Spielberg\",\n" +
                        "                \"actors\": \"Sam Neill, Laura Dern, Jeff Goldblum, Richard Attenborough\",\n" +
                        "                \"release\": \"1993\",\n" +
                        "                \"description\": \"\",\n" +
                        "                \"rating\": null\n" +
                        "            }            \n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"responseStatus\": {\n" +
                        "        \"responseStatusEntCd\": \"200\",\n" +
                        "        \"responseStatusCdDesc\": \"Success\",\n" +
                        "        \"responseDesc\": \"Found\",\n" +
                        "        \"statusDetails\": []\n" +
                        "    }\n" +
                        "}"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
