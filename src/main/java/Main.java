import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        final String matchDataPath = "src/main/resources/match_data.txt";
        final String playerDataPath = "src/main/resources/player_data.txt";
        Casino casino = new Casino(0);
        List<Player> players = new ArrayList<>();
        List<Match> matches = readMatchData(matchDataPath);
        List<Action> actions = readPlayerData(playerDataPath);
        for (Action action : actions) {
            System.out.println("--------------------------------------------------");
            System.out.println(action);
            Player player = getPlayerById(action.getPlayerId(), players);
            final int coinNumber = action.getCoinAmount();
            if (player == null) {
                if (action.getActionType().equals(ActionType.DEPOSIT)) {
                    player = new Player(action.getPlayerId(), coinNumber, true);
                    players.add(player);
                } else {
                    player = new Player(action.getPlayerId(), 0, false);
                    players.add(player);
                    illegitimateAction(action, player);
                }
            } else if (player.isLegitimate()) {
                switch (action.getActionType()) {
                    case BET -> {
                        if (player.getBalance() < action.getCoinAmount()) {
                            illegitimateAction(action, player);
                        } else {
                            String playerSide = action.getPlayerSide();
                            Match match = getMatchById(action.getMatchId(), matches);
                            if (match == null || match.isMatchEnded()) {
                                illegitimateAction(action, player);
                                continue;
                            }
                            if (match.getResult().equals(playerSide)) { // player wins
                                int winAmount = (int) Math.round(coinNumber * match.getReturnRate(playerSide));
                                player.setBalance(player.getBalance() + winAmount);
                                player.calculateWinRate(true);
                                casino.subtractBalance((winAmount));
                                casino.addChanges(new Changes(player.getId(), (winAmount) * -1L, match.getMatchId()));
                            } else if (match.getResult().equals("DRAW")) { // draw
                                player.calculateWinRate(false);
                                casino.addChanges(new Changes(player.getId(), 0, match.getMatchId()));
                            } else { // player loses
                                player.setBalance(player.getBalance() - coinNumber);
                                player.calculateWinRate(false);
                                casino.addBalance(coinNumber);
                                casino.addChanges(new Changes(player.getId(), coinNumber, match.getMatchId()));
                            }
                            match.setMatchEnded(true);
                        }
                    }
                    case WITHDRAW -> {
                        if (player.getBalance() < coinNumber) {
                            illegitimateAction(action, player);
                        } else {
                            player.setBalance(player.getBalance() - coinNumber);
                        }
                    }
                    case DEPOSIT -> player.setBalance(player.getBalance() + coinNumber);
                    default -> illegitimateAction(action, player);
                }
            }
            if (!player.isLegitimate()) {
                casino.rollbackIllegitimateChanges(player.getId());
            }
        }
        writeResults(players, casino);
    }

    private static void illegitimateAction(Action action, Player player) {
        System.out.println("Illegitimate action: " + action);
        if (player.isLegitimate()) {
            player.setIllegitimateReason(action);
            player.setIllegitimate();
        }
    }

    public static Player getPlayerById(UUID playerId, List<Player> players) {
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null;
    }

    public static Match getMatchById(UUID matchId, List<Match> matches) {
        for (Match match : matches) {
            if (match.getMatchId().equals(matchId)) {
                return match;
            }
        }
        return null;
    }

    public static ArrayList<String> readFile(String fileName) {
        ArrayList<String> data = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                data.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return data;
    }

    public static List<Match> readMatchData(final String matchDataPath) {
        List<Match> matches = new ArrayList<>();
        ArrayList<String> match_data = readFile(matchDataPath);
        for (String match : match_data) {
            String[] matchSplit = match.split(",");
            matches.add(new Match(UUID.fromString(matchSplit[0]), Double.parseDouble(matchSplit[1]),
                    Double.parseDouble(matchSplit[2]), matchSplit[3]));
        }
        return matches;
    }

    private static List<Action> readPlayerData(final String playerDataPath) {
        ArrayList<String> player_data = readFile(playerDataPath);
        List<Action> actions = new ArrayList<>();
        for (String line : player_data) {
            String[] lineSplit = line.split(",", -1);
            if (lineSplit[1].equals("BET")) {
                actions.add(new Action(UUID.fromString(lineSplit[0]), lineSplit[1], UUID.fromString(lineSplit[2]),
                        Integer.parseInt(lineSplit[3]), lineSplit[4]));
            } else if (lineSplit[1].equals("WITHDRAW") || lineSplit[1].equals("DEPOSIT")) {
                actions.add(new Action(UUID.fromString(lineSplit[0]), lineSplit[1], Integer.parseInt(lineSplit[3])));
            }
        }
        return actions;
    }

    public static void writeResults(List<Player> players, Casino casino) {
        Collections.sort(players);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/result.txt"));
            for (Player player : players) {
                if (player.isLegitimate()) {
                    writer.write(player.toString());
                    writer.newLine();
                }
            }
            writer.newLine();
            for (Player player : players) {
                if (!player.isLegitimate()) {
                    writer.write(player.getIllegitimateReason().toString());
                    writer.newLine();
                }
            }
            writer.newLine();
            writer.write(String.valueOf(casino.getBalance()));
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
