package bangkoktransitnavigator.graph;

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
                    int weight = getWeightForLine(lineName);
                    
                    // Add edges in both directions (A -> B and B -> A)
                    stationA.addNeighbor(new Edge(stationB, weight, lineName));
                    stationB.addNeighbor(new Edge(stationA, weight, lineName));
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
    
    private int getWeightForLine(String lineName) {
        // Check if this is an interchange connection
        if (lineName.trim().equalsIgnoreCase("Interchange")) {
            return 10; // 10-minute penalty for a transfer
        } else {
            return 3; // 3-minute travel time
        }
    }
    
    // getters
    public Station getStation(String name) {
        return stations.get(name);
    }

    public Map<String, Station> getAllStations() {
        return stations;
    }
}