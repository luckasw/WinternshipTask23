import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String matchDataPath = "src/main/resources/match_data.txt";
        final String playerDataPath = "src/main/resources/player_data.txt";
        Casino casino = new Casino(0);
        List<Player> players = new ArrayList<>();
        List<Match> matches = Initialize.readMatchData(matchDataPath);
        List<Action> actions = Initialize.readPlayerData(playerDataPath);
        ActionHandler actionHandler = new ActionHandler(players, matches, casino);
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
