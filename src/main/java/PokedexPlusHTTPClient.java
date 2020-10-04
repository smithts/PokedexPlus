import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

public class PokedexPlusHTTPClient {

    private static final Logger log = Logger.getAnonymousLogger();
    private static HttpClient client = HttpClient.newHttpClient();
    private static final String[] STAT_TYPE = {"HP", "Attack", "Defense", "Special-Attack", "Special-Defense", "Speed"};
    //private static final String[] TYPES = {};
    private static Map<String, String> pokeCache = new HashMap<String, String>();

    /**
     * returns ArrayList containing the provided pokemon's stats
     * */
    public static ArrayList<Stat> getStats(String pokemonName) throws IOException {
        String responseBody = getPokemon(pokemonName);

        //if empty, return null
        if (responseBody.isEmpty()) {
            log.warning("No results returned for Pokemon \"" + pokemonName + "\"");
            return null;
        }

        ArrayList<Stat> stats = new ArrayList<>();

        //Entire response body
        JSONObject jo = new JSONObject(responseBody);

        //Just stats array
        JSONArray arr = new JSONArray(jo.get("stats").toString());

        for (int i = 0; i < STAT_TYPE.length; i++) {

            JSONObject statObject = new JSONObject(arr.get(i).toString());
            JSONObject urlObject = new JSONObject(statObject.get("stat").toString());
            
            String statName = STAT_TYPE[i];
            int statBase = (int) statObject.get("base_stat");
            int statEffort = (int) statObject.get("effort");
            String url = (String) urlObject.get("url");
            
            Stat temp = new Stat(statName, statBase, statEffort, url);
            stats.add(temp);
        }

        return stats;
    }

    /*
    // Future
    public static ArrayList<Type> getTypes(String pokemonName) throws IOException {
        String responseBody = getPokemon(pokemonName);
        return null;
    }
    */

    /**
     * returns string of JSON form containing all Pokemon data
     * */
    private static String getPokemon(String pokemonName)  {
        pokemonName = pokemonName.toLowerCase();
        if (pokeCache.containsKey(pokemonName)) {
            return pokeCache.get(pokemonName);
        } else {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pokeapi.co/api/v2/pokemon/" + pokemonName +"/"))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.body().equals("Not Found")) {
                    log.warning("Pokemon \"" + pokemonName + "\" does not exist");
                    return "";
                } else {
                    pokeCache.put(pokemonName, response.body());
                    return response.body();
                }

            } catch (InterruptedException | IOException ex) {
                log.severe(String.valueOf(ex.getStackTrace()));
            }
            return "";
        }

    }

}
