package bangkoktransitnavigator.algorithm;

import bangkoktransitnavigator.model.Cost;
import bangkoktransitnavigator.model.Edge;
import bangkoktransitnavigator.model.RouteLeg;
import bangkoktransitnavigator.model.RouteResult;
import bangkoktransitnavigator.model.Station;

import java.util.*;

// An implementation of the Pathfinder interface using Dijkstra's algorithm.
public class DijkstraPathfinder implements Pathfinder{

    private final double timeWeight;
    private final double transferWeight;

    // The constructor takes the weights
    public DijkstraPathfinder(double timeWeight, double transferWeight) {
        this.timeWeight = timeWeight;
        this.transferWeight = transferWeight;
    }

    @Override
    public RouteResult findShortestPath(Collection<Station> allStations, Station start, Station end) {
        
        // 1. INITIALIZATION
        Map<Station, Integer> distances = new HashMap<>();
        Map<Station, Station> predecessors = new HashMap<>();
        PriorityQueue<Station> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        
        // Initialize distances and predecessors for all stations
        for (Station station : allStations) {
            distances.put(station, Integer.MAX_VALUE);
            predecessors.put(station, null);
        }
        
        // Set start station distance to 0
        distances.put(start, 0);
        priorityQueue.add(start);

        // 2. THE MAIN LOOP
        while (!priorityQueue.isEmpty()) {
            Station current = priorityQueue.poll();

            if (current.equals(end)) {
                break;
            }

            int currentDistance = distances.get(current);
            if (currentDistance == Integer.MAX_VALUE) {
                continue;
            }

            for (Edge edge : current.getNeighbors()) {
                Station neighbor = edge.getDestination();
                Cost edgeCost = edge.getCost();
                double weightedCost = (timeWeight * edgeCost.getTime()) + (transferWeight * edgeCost.getTransfers());
                Integer newDist = (int) (currentDistance + weightedCost);

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }
        
        // 3. RECONSTRUCT THE PATH
        List<RouteLeg> path = new ArrayList<>();
        int finalTime = 0;
        int finalTransfers = 0;

        if (distances.get(end) != Double.POSITIVE_INFINITY) {
            Station currentStation = end;

            while (currentStation != null) {
                // Find the station that came before the current one
                String lineToCurrent = null;
                Station previousStation = predecessors.get(currentStation);

                if (previousStation != null) {
                    // Find the edge that connects them to get the line name and cost
                    for (Edge edge : previousStation.getNeighbors()) {
                        if (edge.getDestination().equals(currentStation)) {
                            lineToCurrent = edge.getLine();
                            // Add the real time cost for this segment
                            finalTime += edge.getCost().getTime(); 
                            break;
                        }
                    }
                }
                
                // Add the new detailed leg to the front of the list
                path.add(0, new RouteLeg(currentStation, lineToCurrent));

                // Move to the next station in the path
                currentStation = previousStation;
            }
            
            // Now that we have the full, detailed path, we can correctly count transfers
            String currentLine = "";
            for(RouteLeg leg : path){
                String legLine = leg.getLineToGetHere();
                if(legLine == null || legLine.equalsIgnoreCase("Interchange")) continue;
                
                if(currentLine.isEmpty()){
                    currentLine = legLine;
                } else if (!currentLine.equals(legLine)){
                    finalTransfers++;
                    currentLine = legLine;
                }
            }
        }

        Cost finalCost = new Cost(finalTime, finalTransfers);
        // Note: We now pass the List<RouteLeg> to the RouteResult constructor
        return new RouteResult(path, finalCost);
    }
}