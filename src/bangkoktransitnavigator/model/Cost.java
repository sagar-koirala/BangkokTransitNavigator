package bangkoktransitnavigator.model;

public class Cost {
    private final int time;       // in minutes
    private final int transfers;  // 1 for an interchange, 0 otherwise

    public Cost(int time, int transfers) {
        this.time = time;
        this.transfers = transfers;
    }

    // Getters
    public int getTime() { return time; }
    public int getTransfers() { return transfers; }
}
