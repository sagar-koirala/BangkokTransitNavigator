package bangkoktransitnavigator.model;

public class Edge {

    private Station destination;
    private Cost cost;           // The "cost" of this edge (e.g., time, transfer)
    private String line;

    public Edge(Station destination, Cost cost, String line) {
        this.destination = destination;
        this.cost = cost;
        this.line = line;
    }

    // Getter methods
    public Station getDestination() {
        return destination;
    }

    public Cost getCost() {
        return cost;
    }

    public String getLine() {
        return line;
    }
}