package bangkoktransitnavigator;

import bangkoktransitnavigator.graph.TransitGraph;
import bangkoktransitnavigator.model.Edge;
import bangkoktransitnavigator.model.Station;

public class Main {

    public static void main(String[] args) {
        System.out.println("Bangkok Transit Navigator");

        TransitGraph graph = new TransitGraph("data/connections.csv");

        // Show number of stations
        int stationCount = graph.getAllStations().size();
        System.out.println("Total stations: " + stationCount);

        // Test neighbors of one station
        Station asok = graph.getStation("Asok");
        if (asok != null) {
            System.out.println("\nNeighbors of " + asok.getName() + ":");
            for (Edge edge : asok.getNeighbors()) {
                System.out.printf("  -> %s (%s)\n", 
                    edge.getDestination().getName(), edge.getLine());
            }
        }
    }
}