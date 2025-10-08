package bangkoktransitnavigator.model;

import java.util.List;

// A simple class to hold the results of a pathfinding operation.
public class RouteResult {

    private final List<Station> path;
    private final Cost totalCost;

    public RouteResult(List<Station> path, Cost totalCost) {
        this.path = path;
        this.totalCost = totalCost;
    }

    public List<Station> getPath() {
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
}