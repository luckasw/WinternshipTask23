import enums.ActionType;
import objects.*;

import java.util.List;
import java.util.UUID;

public class ActionHandler {
    final private List<Player> players;
    final private List<Match> matches;
    final private Casino casino;

    public ActionHandler(List<Player> players, List<Match> matches, Casino casino) {
        this.players = players;
        this.matches = matches;
        this.casino = casino;
    }
    
    public void handleAction(Action action) {
        Player player = getPlayerById(action.getPlayerId(), players);
        final int coinNumber = action.getCoinAmount();
        
        if (player == null) {
            handleNullPlayer(action);
        } else if (player.isLegitimate()) {
            handleLegitimatePlayer(action, player, coinNumber);
        } else {
            illegitimateAction(action, player);
        }
    }

    private void handleLegitimatePlayer(Action action, Player player, int coinNumber) {
        switch (action.getActionType()) {
            case BET -> handleBetAction(action, player, coinNumber);
            case WITHDRAW -> handleWithdrawAction(action, player, coinNumber);
            case DEPOSIT -> handleDepositAction(player, coinNumber);
            default -> illegitimateAction(action, player);
        }
    }

    private void handleDepositAction(Player player, int coinNumber) {
        player.setBalance(player.getBalance() + coinNumber);
    }

    private void handleWithdrawAction(Action action, Player player, int coinNumber) {
        if (player.getBalance() < coinNumber) {
            illegitimateAction(action, player);
        } else {
            player.setBalance(player.getBalance() - coinNumber);
        }
    }

    private void handleBetAction(Action action, Player player, int coinNumber) {
        if (player.getBalance() < action.getCoinAmount()) {
            illegitimateAction(action, player);
        } else {
            processBetAction(action, player, coinNumber);
        }
    }

    private void processBetAction(Action action, Player player, int coinNumber) {
        String playerSide = action.getPlayerSide();
        Match match = getMatchById(action.getMatchId(), matches);
        if (match == null || match.isMatchEnded()) {
            illegitimateAction(action, player);
            return;
        }
        if (match.getResult().equals(playerSide)) { // player wins
            handleWin(player, coinNumber, match, playerSide);
        } else if (match.getResult().equals("DRAW")) { // draw
            handleDraw(player, match);
        } else { // player loses
            handleLoss(player, coinNumber, match);
        }
        match.setMatchEnded(true);
    }

    private void handleLoss(Player player, int coinNumber, Match match) {
        player.setBalance(player.getBalance() - coinNumber);
        player.calculateWinRate(false);
        casino.addBalance(coinNumber);
        casino.addChanges(new Transaction(player.getId(), coinNumber, match.getMatchId()));
    }

    private void handleDraw(Player player, Match match) {
        player.calculateWinRate(false);
        casino.addChanges(new Transaction(player.getId(), 0, match.getMatchId()));
    }

    private void handleWin(Player player, int coinNumber, Match match, String playerSide) {
        int winAmount = (int) Math.round(coinNumber * match.getReturnRate(playerSide));
        player.setBalance(player.getBalance() + winAmount);
        player.calculateWinRate(true);
        casino.subtractBalance((winAmount));
        casino.addChanges(new Transaction(player.getId(), (winAmount) * -1L, match.getMatchId()));
    }

    private void handleNullPlayer(Action action) {
        if (action.getActionType().equals(ActionType.DEPOSIT)) {
            Player player = new Player(action.getPlayerId(), action.getCoinAmount(), true);
            players.add(player);
        } else {
            Player player = new Player(action.getPlayerId(), 0, false);
            players.add(player);
            illegitimateAction(action, player);
        }
    }

    private static Player getPlayerById(UUID playerId, List<Player> players) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    private static Match getMatchById(UUID matchId, List<Match> matches) {
        for (Match match : matches) {
            if (match.getMatchId().equals(matchId)) {
                return match;
            }
        }
        return null;
    }

    private void illegitimateAction(Action action, Player player) {
        if (player.isLegitimate()) {
            player.setIllegitimateReason(action);
            player.setIllegitimate();
            casino.rollbackIllegitimateChanges(player.getId());
        }
    }

}
