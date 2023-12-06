package com.nighthawk.spring_portfolio.mvc.poke;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // annotation to create a RESTful web services
@RequestMapping("/api/poke")  //prefix of API
public class PokeApiController {

    private static void alphabeticallySort(ArrayList<Map<String, String>> list) {
        int n = list.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                // Compare adjacent names and swap if they are in the wrong order
                if (list.get(j).get("name").compareTo(list.get(j + 1).get("name")) > 0) {
                    Map<String, String> temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
        }
    }
    private JSONObject body; //last run result
    private HttpStatus status; //last run status
    
    // GET Covid 19 Stats
    @GetMapping("/")   //added to end of prefix as endpoint
    public ResponseEntity<JSONObject> getCovid() {

         try {  //APIs can fail (ie Internet or Service down)
             
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://pokeapi.co/api/v2/pokemon?limit=5000"))
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

             //JSONParser extracts text body and parses to JSONObject
             this.body = (JSONObject) new JSONParser().parse(response.body());
             this.status = HttpStatus.OK;
         }
         catch (Exception e) {  //capture failure info
             HashMap<String, String> status = new HashMap<>();
             status.put("status", "RapidApi failure: " + e);

             //Setup object for error
             this.body = (JSONObject) status;
             this.status = HttpStatus.INTERNAL_SERVER_ERROR;
         }
     //return JSONObject in RESTful style
     return new ResponseEntity<>(body, status);
    }
    @GetMapping("/alphabet")   //added to end of prefix as endpoint
    public ResponseEntity<JSONObject> getPokemonInAlphabetOrder() {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://pokeapi.co/api/v2/pokemon?limit=151"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // JSONParser extracts text body and parses to JSONObject
            JSONObject originalBody = (JSONObject) new JSONParser().parse(response.body());

            // Extracting the list of Pokemon names
            JSONArray originalPokemonList = (JSONArray) originalBody.get("results");
            ArrayList<Map<String, String>> pokemonNames = new ArrayList<>();
            // Map<String, String> dict = new HashMap<>();
            for (Object pokemonObj : originalPokemonList) {
                Map<String, String> dict = new HashMap<>();
                JSONObject pokemon = (JSONObject) pokemonObj;
                String pokemonName = (String) pokemon.get("name");
                String pokemonUrl = (String) pokemon.get("url");
                dict.put("name", pokemonName);
                dict.put("url", pokemonUrl);
                pokemonNames.add(dict);
            }

            // Sorting Pokemon names alphabetically
            alphabeticallySort(pokemonNames);

            // Creating a new JSON object with sorted Pokemon names
            JSONObject sortedBody = new JSONObject();
            sortedBody.put("pokemon", pokemonNames);

            this.body = sortedBody;
            this.status = HttpStatus.OK;
        } catch (Exception e) {
            HashMap<String, String> status = new HashMap<>();
            status.put("status", "RapidApi failure: " + e);

            // Setup object for error
            this.body = (JSONObject) status;
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // return JSONObject in RESTful style
        return new ResponseEntity<>(body, status);
    }
}
