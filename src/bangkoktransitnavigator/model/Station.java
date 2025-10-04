package bangkoktransitnavigator.model;

import java.util.List;
import java.util.ArrayList;

// Represents a single station (a node/vertex in the graph)
public class Station {
    
    private String name;
    private List<Edge> neighbors;

    // The constructor initializes the station
    public Station(String name) {
        this.name = name;
        this.neighbors = new ArrayList<>(); 
    }

    // A method to add a connection to another station
    public void addNeighbor(Edge edge) {
        this.neighbors.add(edge);
    }

    // getter methods to access the private variables
    public String getName() {
        return name;
    }

    public List<Edge> getNeighbors() {
        return neighbors;
    }
}