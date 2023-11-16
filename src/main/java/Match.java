import java.util.UUID;

public class Match {
    private final UUID matchId;
    private final double returnRateA;
    private final double returnRateB;
    private final String result;
    private boolean matchEnded;

    public Match(UUID matchId, double returnRateA, double returnRateB, String result) {
        this.matchId = matchId;
        this.returnRateA = returnRateA;
        this.returnRateB = returnRateB;
        this.result = result;
        this.matchEnded = false;
    }

    public boolean isMatchEnded() {
        return matchEnded;
    }

    public void setMatchEnded(boolean matchEnded) {
        this.matchEnded = matchEnded;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public double getReturnRateA() {
        return returnRateA;
    }

    public double getReturnRateB() {
        return returnRateB;
    }

    public String getResult() {
        return result;
    }

    public double getReturnRate(String playerSide) {
        if (playerSide.equals("A")) {
            return returnRateA;
        } else {
            return returnRateB;
        }
    }

}
