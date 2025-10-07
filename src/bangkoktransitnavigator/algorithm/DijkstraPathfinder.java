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
        int totalTime = 0;

        if (distances.get(end) != Double.POSITIVE_INFINITY) {
            Station current = end;
            while (current != null) {
                path.add(current);
                Station previous = predecessors.get(current);
                if (previous != null) {
                    for (Edge edge : previous.getNeighbors()) {
                        if (edge.getDestination().equals(current)) {
                            totalTime += edge.getCost().getTime(); // calculate the real time cost
                            break;
                        }
                    }
                }
                current = previous;
            }
            Collections.reverse(path);
        }
        
        return new RouteResult(path, totalTime);
    }
}