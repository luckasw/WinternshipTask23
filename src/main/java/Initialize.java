import objects.Action;
import objects.Match;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Initialize {
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

    public static List<Action> readPlayerData(final String playerDataPath) {
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
}
