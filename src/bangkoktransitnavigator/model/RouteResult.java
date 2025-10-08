package bangkoktransitnavigator.model;

import java.util.List;

// A simple class to hold the results of a pathfinding operation.
public class RouteResult {

    private final List<RouteLeg> path;
    private final Cost totalCost;

    public RouteResult(List<RouteLeg> path, Cost totalCost) {
        this.path = path;
        this.totalCost = totalCost;
    }

    public List<RouteLeg> getPath() {
        return path;
    }

    public Cost getTotalCost() {
        return totalCost;
    }

    public boolean isFound() {
        // A path is found if the list is not empty and contains more than one station,
        // or if start and end are the same.
        return path != null && !path.isEmpty();
    }

    public void printRoute() {
        if (!isFound()) {
            System.out.println("No path could be found");
        } else {
            System.out.println("Time: " + totalCost.getTime() + " minutes");
            System.out.println("Transfers: " + totalCost.getTransfers());
            System.out.println("--- Detailed Route ---");

            String currentLine = "";
            for (RouteLeg leg : path) {
                String stationName = leg.getStation().getName();
                String lineToHere = leg.getLineToGetHere();

                // The line is null only for the start station
                if (lineToHere == null) {
                    System.out.println("  Start at: " + stationName);
                    continue;
                }

                // If the line changes (and isn't an interchange), print a header
                if (!lineToHere.equalsIgnoreCase(currentLine) && !lineToHere.equalsIgnoreCase("Interchange")) {
                    currentLine = lineToHere;
                    System.out.println("  -> Take " + currentLine);
                }
                
                System.out.println("     - " + stationName);
            }
        }
    }
}