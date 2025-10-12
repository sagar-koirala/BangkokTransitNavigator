package bangkoktransitnavigator;

// Import all the components we built
import bangkoktransitnavigator.algorithm.DijkstraPathfinder;
import bangkoktransitnavigator.algorithm.Pathfinder;
import bangkoktransitnavigator.graph.TransitGraph;
import bangkoktransitnavigator.model.RouteResult;
import bangkoktransitnavigator.model.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // --- 1. SETUP PHASE ---
        System.out.println("Loading transit data...");
        // The path to the data file, relative to the project's root directory
        TransitGraph graph = new TransitGraph("data/connections.csv");
        System.out.println("Data loaded. Welcome to the Bangkok Transit Navigator!");

        // --- 2. INTERACTION PHASE ---
        // Try-with-resources block for the Scanner to ensure it's always closed
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
                // Find the FASTEST path first
                Pathfinder fastestPathFinder = new DijkstraPathfinder(1.0, 0.0); // 100% weight on time
                RouteResult fastestResult = fastestPathFinder.findShortestPath(graph.getAllStations().values(), startStation, endStation);
                
                System.out.println("\n--- Fastest Route ---");
                fastestResult.printRoute();
                System.out.println("--------------");

                // --- 5. OPTIONS MENU ---
                if (fastestResult.isFound()) {
                    RouteResult currentRoute = fastestResult; // Track the current route for detour validation
                    List<Station> excludedStations = new ArrayList<>(); // Track all excluded stations
                    while (true) {
                        System.out.println("\nOptions:");
                        System.out.println("1. Find other route with lowest transfer");
                        System.out.println("2. Exclude station and find detour");
                        System.out.println("3. Restart (new journey)");
                        System.out.println("4. Exit");
                        System.out.print("Choose an option (1-4): ");
                        
                        String choice = scanner.nextLine().trim();
                        
                        if (choice.equals("1")) {
                            // Find the path with FEWEST TRANSFERS
                            Pathfinder fewestTransPathfinder = new DijkstraPathfinder(0.0, 1.0); // 100% weight on transfers
                            RouteResult fewestTransfersResult = fewestTransPathfinder.findShortestPath(graph.getAllStations().values(), startStation, endStation);

                            System.out.println("\n--- Fewest Transfer Route ---");
                            fewestTransfersResult.printRoute();
                            System.out.println("--------------");
                            
                        } else if (choice.equals("2")) {
                            // Exclude station and find detour
                            System.out.println("\nEnter a station from the current route to exclude for a detour:");
                            String excludedStationName = scanner.nextLine().trim();

                            Station excludedStation = graph.getStation(excludedStationName);

                            // Validate the station for exclusion
                            if (excludedStation == null) {
                                System.err.println("Warning: Station to exclude was not found.");
                                continue;
                            }
                            if (excludedStation.equals(startStation) || excludedStation.equals(endStation)) {
                                System.err.println("Warning: Cannot exclude the start or destination station.");
                                continue;
                            }
                            boolean isOnPath = currentRoute.getPath().stream()
                                                            .anyMatch(leg -> leg.getStation().equals(excludedStation));
                            if (!isOnPath) {
                                System.err.println("Warning: " + excludedStation.getName() + " is not on the current route.");
                                continue;
                            }

                            // Calculate and display detour
                            excludedStations.add(excludedStation); // Add to cumulative excluded list
                            System.out.println("\n(Calculating detour while excluding " + excludedStation.getName() + "...)");
                            List<Station> stationsForDetour = new ArrayList<>(graph.getAllStations().values());
                            stationsForDetour.removeAll(excludedStations); // Remove all previously excluded stations

                            RouteResult detourResult = fastestPathFinder.findShortestPath(
                                stationsForDetour, startStation, endStation
                            );

                            System.out.println("--- Detour Route ---");
                            detourResult.printRoute();
                            System.out.println("--------------");
                            
                            // Update current route to the new detour for subsequent detours
                            currentRoute = detourResult;
                            
                        } else if (choice.equals("3")) {
                            // Restart - break out of options loop to start new journey
                            break;
                            
                        } else if (choice.equals("4")) {
                            // Continue (exit program)
                            System.out.println("Thank you for using the Bangkok Transit Navigator!");
                            return;
                            
                        } else {
                            System.err.println("Invalid choice. Please enter 1, 2, 3, or 4.");
                        }
                    }
                }
            }
        }
        System.out.println("Thank you for using the Bangkok Transit Navigator!");
    }
}