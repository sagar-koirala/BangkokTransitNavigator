package bangkoktransitnavigator.algorithm;

import bangkoktransitnavigator.model.RouteResult;
import bangkoktransitnavigator.model.Station;
import java.util.Collection;

// This interface defines the contract for any pathfinding algorithm.
public interface Pathfinder{
    /**
     * Finds the best route between a start and end station.
     *
     * @param allStations The complete collection of all stations in the graph.
     * @param start The starting station for the path.
     * @param end The destination station for the path.
     * @return A RouteResult object containing the path and the total cost.
     */
    RouteResult findShortestPath(Collection<Station> allStations, Station start, Station end);
}