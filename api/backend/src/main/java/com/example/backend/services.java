import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
public class services {
    

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final String apiEndpoint = "https://api-nba-v1.p.rapidapi.com/players?team=1&season=2021";

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void fetchDataAndSaveToDatabase() {
        RestTemplate restTemplate = new RestTemplate();
        // Make an HTTP request to the API
        // Note: You may need to set headers like "X-RapidAPI-Key" if required by the API
        Player[] players = restTemplate.getForObject(apiEndpoint, Player[].class);

        // Save players to the database
        playerRepository.saveAll(Arrays.asList(players));
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
}

}
