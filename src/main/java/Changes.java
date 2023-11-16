import java.util.UUID;

public class Changes {
    private final UUID matchId;
    private final UUID playerId;
    private final long balanceChange;
    private boolean isRollbacked;

    public Changes(UUID playerId, long balanceChange, UUID matchId) {
        this.playerId = playerId;
        this.balanceChange = balanceChange;
        this.matchId = matchId;
        this.isRollbacked = false;
    }

    public void setRollbacked(boolean rollbacked) {
        isRollbacked = rollbacked;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getBalanceChange() {
        return balanceChange;
    }

    public boolean isRollbacked() {
        return isRollbacked;
    }

    public String toString(){
        return this.playerId + " " + this.balanceChange + " " + this.matchId;
    }

}
