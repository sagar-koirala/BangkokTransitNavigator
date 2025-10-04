package bangkoktransitnavigator.model;

public class Edge {

    private Station destination;
    private int weight; // The "cost" of this edge (e.g., time in minutes)
    private String line;

    public Edge(Station destination, int weight, String line) {
        this.destination = destination;
        this.weight = weight;
        this.line = line;
    }

    // Getter methods
    public Station getDestination() {
        return destination;
    }

    public int getWeight() {
        return weight;
    }

    public String getLine() {
        return line;
    }
}