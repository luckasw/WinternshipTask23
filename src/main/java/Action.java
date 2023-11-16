import java.util.UUID;

public class Action {
    private final String actionType;
    private final UUID playerId;
    private final int coinAmount;
    private final UUID matchId;
    private final String playerSide;

    public Action(UUID playerId, String actionType, UUID matchId, int coinAmount, String playerSide) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.matchId = matchId;
        this.coinAmount = coinAmount;
        this.playerSide = playerSide;
    }

    public Action(UUID playerId, String actionType, int coinAmount) {
        this.playerId = playerId;
        this.actionType = actionType;
        this.matchId = null;
        this.coinAmount = coinAmount;
        this.playerSide = null;
    }

    public String getActionType() {
        return actionType;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getCoinAmount() {
        return coinAmount;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String getPlayerSide() {
        return playerSide;
    }

    @Override
    public String toString() {
        return this.playerId + " " + this.actionType + " " + this.matchId + " " + this.coinAmount + " " + this.playerSide;
    }
}
