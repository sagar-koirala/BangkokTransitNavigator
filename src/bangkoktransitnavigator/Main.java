package bangkoktransitnavigator;

// Import all the components we built
import bangkoktransitnavigator.algorithm.DijkstraPathfinder;
import bangkoktransitnavigator.algorithm.Pathfinder;
import bangkoktransitnavigator.graph.TransitGraph;
import bangkoktransitnavigator.model.RouteResult;
import bangkoktransitnavigator.model.Station;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // --- 1. SETUP PHASE ---
        System.out.println("Loading transit data...");
        // The path to the data file, relative to the project's root directory
        TransitGraph graph = new TransitGraph("data/connections.csv");
        System.out.println("Data loaded. Welcome to the Bangkok Transit Navigator!");

        // --- 2. INTERACTION PHASE ---
        // Use a try-with-resources block for the Scanner to ensure it's always closed
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\nEnter your starting station (or type 'exit' to quit):");
                String startStationName = scanner.nextLine();

                // Allow the user to exit the program
                if (startStationName.equalsIgnoreCase("exit")) {
                    break;
                }

                Station startStation = graph.getStation(startStationName);
                if (startStation == null) {
                    System.err.println("Error: Starting station '" + startStationName + "' not found.");
                    continue; // Restart the loop
                }

                System.out.println("Enter your destination station:");
                String endStationName = scanner.nextLine();
                
                if (endStationName.equalsIgnoreCase("exit")) {
                    break;
                }
                
                Station endStation = graph.getStation(endStationName);

                if (endStation == null) {
                    System.err.println("Error: Destination station '" + endStationName + "' not found.");
                    continue; // Restart the loop
                }

                // --- 4. EXECUTION PHASE ---
                // Find the FASTEST path
                Pathfinder fastestPathFinder = new DijkstraPathfinder(1.0, 0.0); // 100% weight on time
                RouteResult result = fastestPathFinder.findShortestPath(graph.getAllStations().values(), startStation, endStation);
                
                System.out.println("\n--- Fastest Route ---");
                if (!result.isFound()) {
                    System.out.println("No path could be found from " + startStation.getName() + " to " + endStation.getName());
                } else {
                    System.out.println("Time: " + result.getTotalCost().getTime() + " minutes");
                    System.out.println("Transfers: " + result.getTotalCost().getTransfers());
                    
                    String pathString = result.getPath().stream()
                                              .map(Station::getName) // Convert each Station object to its name
                                              .collect(Collectors.joining(" -> ")); // Join them with an arrow

                    System.out.println("Route: " + pathString);
                }
                System.out.println("--------------");

                // Find the path with FEWEST TRANSFERS
                Pathfinder fewestTransPathfinder = new DijkstraPathfinder(0.0, 1.0); // 100% weight on transfers
                result = fewestTransPathfinder.findShortestPath(graph.getAllStations().values(), startStation, endStation);

                System.out.println("\n--- Fewest Transfer Route ---");
                if (!result.isFound()) {
                    System.out.println("No path could be found from " + startStation.getName() + " to " + endStation.getName());
                } else {
                    System.out.println("Time: " + result.getTotalCost().getTime() + " minutes");
                    System.out.println("Transfers: " + result.getTotalCost().getTransfers());
                    
                    String pathString = result.getPath().stream()
                                              .map(Station::getName) // Convert each Station object to its name
                                              .collect(Collectors.joining(" -> ")); // Join them with an arrow

                    System.out.println("Route: " + pathString);
                }
                System.out.println("--------------");
                
            }
        }
        System.out.println("Thank you for using the Bangkok Transit Navigator!");
    }
}