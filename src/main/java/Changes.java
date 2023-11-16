import java.util.UUID;

public class Changes {
    private final UUID matchId;
    private final UUID playerId;
    private final long balanceChange;
    private boolean isRolledback;

    public Changes(UUID playerId, long balanceChange, UUID matchId) {
        this.playerId = playerId;
        this.balanceChange = balanceChange;
        this.matchId = matchId;
        this.isRolledback = false;
    }

    public void setRolledback(boolean rollbacked) {
        isRolledback = rollbacked;
    }

    public boolean isRolledback() {
        return isRolledback;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public long getBalanceChange() {
        return balanceChange;
    }

    public String toString(){
        return this.playerId + " " + this.balanceChange + " " + this.matchId;
    }

}
