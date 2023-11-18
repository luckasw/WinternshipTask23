import objects.Action;
import objects.Casino;
import objects.Match;
import objects.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    final static String matchDataPath = "src/main/resources/match_data.txt";
    final static String playerDataPath = "src/main/resources/player_data.txt";
    public static void main(String[] args) {
        final Casino casino = new Casino(0);
        final List<Player> players = new ArrayList<>();
        final List<Match> matches = Initialize.readMatchData(matchDataPath);
        final List<Action> actions = Initialize.readPlayerData(playerDataPath);
        final ActionHandler actionHandler = new ActionHandler(players, matches, casino);
        for (Action action : actions) {
            actionHandler.handleAction(action);
        }
        writeResults(players, casino);
    }



    public static void writeResults(List<Player> players, Casino casino) {
        Collections.sort(players);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/result.txt"));
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
