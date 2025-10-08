package bangkoktransitnavigator.model;

public class RouteLeg {
    private final Station station;
    private final String lineToGetHere;

    public RouteLeg(Station station, String lineToGetHere) {
        this.station = station;
        this.lineToGetHere = lineToGetHere;
    }

    public Station getStation() {
        return station;
    }

    /**
     * The name of the transit line used to travel to this station from the previous one.
     * Will be null for the starting station.
     */
    public String getLineToGetHere() {
        return lineToGetHere;
    }
}