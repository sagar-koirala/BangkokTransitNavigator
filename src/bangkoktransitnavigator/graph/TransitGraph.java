package bangkoktransitnavigator.graph;

import bangkoktransitnavigator.model.Cost;
import bangkoktransitnavigator.model.Edge;
import bangkoktransitnavigator.model.Station;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TransitGraph {

    private Map<String, Station> stations;

    public TransitGraph(String csvFilePath) {
        stations = new HashMap<>();
        buildGraph(csvFilePath);
    }

    private void buildGraph(String csvFilePath) {
        // Try-with-resources block to handle the file stream
        try (Stream<String> lines = Files.lines(Paths.get(csvFilePath))) {
            lines.forEach(line -> {
                // Ignore comments (lines starting with '#') and empty lines
                if (line.startsWith("#") || line.isEmpty()){
                    return;
                }
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String stationAName = parts[0];
                    String stationBName = parts[1];
                    String lineName = parts[2];

                    // Get or create Station objects
                    Station stationA = getOrCreateStation(stationAName);
                    Station stationB = getOrCreateStation(stationBName);
                    
                    // Determine the weight based on the line name
                    Cost cost;
                    if (lineName.trim().equalsIgnoreCase("Interchange")) {
                        cost = new Cost(10, 1); // 10 minutes, 1 transfer
                    } else {
                        cost = new Cost(3, 0);  // 3 minutes, 0 transfers
                    }
                    stationA.addNeighbor(new Edge(stationB, cost, lineName));
                    stationB.addNeighbor(new Edge(stationA, cost, lineName));
                }
            });
        } catch (IOException e) {
            // File cannot be read
            e.printStackTrace();
        }
    }

    private Station getOrCreateStation(String stationName) {
        return stations.computeIfAbsent(stationName.trim(), Station::new);
    }
    
    // getters
    public Station getStation(String name) {
        return stations.get(name);
    }

    public Map<String, Station> getAllStations() {
        return stations;
    }
}