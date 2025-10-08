package bangkoktransitnavigator.algorithm;

import bangkoktransitnavigator.model.Cost;
import bangkoktransitnavigator.model.Edge;
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
        List<Station> path = new ArrayList<>();
        // build final path of stations
        if (distances.get(end) != Double.POSITIVE_INFINITY) {
            for (Station step = end; step != null; step = predecessors.get(step)) {
                path.add(step);
            }
            Collections.reverse(path);
        }
        
        // Analyze the path to calcualte Real cost
        int resultTime = 0;
        int resultTransfers = 0;
        String currentLine = "";
        
        if (path.size() > 1) {
            for (int i = 0; i < path.size() - 1; i++) {
                Station from = path.get(i);
                Station to = path.get(i + 1);
                // Find the edge connecting these two stations
                for (Edge edge : from.getNeighbors()) {
                    if (edge.getDestination().equals(to)) {
                        resultTime += edge.getCost().getTime(); // Always add the time cost

                        String segmentLine = edge.getLine();
                        if (segmentLine.equalsIgnoreCase("Interchange")) {
                            continue; // Don't treat the interchange helper edge as a line
                        }

                        if (currentLine.isEmpty()) {
                            // This is the first leg of the journey
                            currentLine = segmentLine;
                        } else if (!currentLine.equals(segmentLine)) {
                            // The line has changed, so this is a transfer
                            resultTransfers++;
                            currentLine = segmentLine; // Update the line we are currently on
                        }
                        break; // Found the correct edge
                    }
                }
            }
        }
        Cost resultCost = new Cost(resultTime, resultTransfers);
        return new RouteResult(path, resultCost);
    }
}