package bangkoktransitnavigator.algorithm;

import bangkoktransitnavigator.model.Edge;
import bangkoktransitnavigator.model.RouteResult;
import bangkoktransitnavigator.model.Station;

import java.util.*;

// An implementation of the Pathfinder interface using Dijkstra's algorithm.
public class DijkstraPathfinder implements Pathfinder{

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
                int newDist = currentDistance + edge.getWeight();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }
        
        // 3. RECONSTRUCT THE PATH
        List<Station> path = new ArrayList<>();
        Integer totalCost = distances.get(end);

        if (totalCost != null && totalCost != Integer.MAX_VALUE) {
            Station step = end;
            while (step != null) {
                path.add(step);
                step = predecessors.get(step);
            }
            Collections.reverse(path);
        }
        
        return new RouteResult(path, (totalCost == null || totalCost == Integer.MAX_VALUE) ? -1 : totalCost);
    }
}