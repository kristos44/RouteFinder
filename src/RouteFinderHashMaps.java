import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class RouteFinderHashMaps {

    public static final double BILLION = 1000000000D;
    public static final int PRINT_ALL_CITIES_LIMIT_SIZE = 100;

    private HashMap<String, String> connectionsMap;
    private int connections;
    private String[] out;

    private long start;
    private long end;

    private String[][] connectionsArr;

    private void switchExistingLoop(String key, String value, Map<String, String> connMap) {
        while(connMap.containsKey(key)) {
            String tempKey = connMap.get(key);
            String tempValue = key;
            connMap.put(key, value);
            key = tempKey;
            value = tempValue;
        }

        connMap.put(key, value);
    }

    private void printMap(Map map) {
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
    }

    private void findFirstAndLastAndPrepareConnectionsMap() {
        HashMap<String, Integer> occurrences = new HashMap<>();

        for(int i = 1; i <= connectionsArr.length; i++) {
            int j = i - 1;
            String firstCity = connectionsArr[j][0];
            String secondCity = connectionsArr[j][1];
            if(occurrences.containsKey(firstCity)) {
                occurrences.remove(firstCity);
            } else {
                occurrences.put(firstCity, i);
            }
            if(occurrences.containsKey(secondCity)) {
                occurrences.remove(secondCity);
            } else {
                occurrences.put(secondCity, -i);
            }

            switchExistingLoop(firstCity, secondCity, connectionsMap);
        }

        connections = connectionsMap.size();
        out = new String[connections + 1];

        Iterator itOccurrencesMap = occurrences.entrySet().iterator();
        int j = 0;
        while (itOccurrencesMap.hasNext()) {
            Map.Entry pair = (Map.Entry)itOccurrencesMap.next();
            if (j == 0) {
                out[0] = pair.getKey().toString();
                int value = (int) pair.getValue();
                int[] cityIndex = getCityIndex(value);
                out[1] = connectionsArr[cityIndex[0]][cityIndex[1]];
                j++;
            } else {
                out[connections] = pair.getKey().toString();
                int value = (int) pair.getValue();
                int[] cityIndex = getCityIndex(value);
                out[connections - 1] = connectionsArr[cityIndex[0]][cityIndex[1]];
                switchExistingLoop(out[connections - 1], out[connections], connectionsMap);
                connectionsMap.remove(out[connections]);
                connectionsMap.remove(out[connections - 1]);
            }
        }

        connectionsMap.remove(out[0]);

    }

    private int[] getCityIndex(int value) {
        int i, k;
        if(value > 0) {
            i = value - 1;
            k = 1;
        } else {
            i = -value - 1;
            k = 0;
        }

        int [] intArr = {i, k};
        return intArr;
    }

    private void getOutputMirror(String[] out, HashMap<String, String> connMap) {
        for(int i = 1; i < out.length - 3; i++) {
            int start = i;

            if (connMap.containsKey(out[start])) {
                String val = connMap.remove(out[start]);
                out[start + 1] = val;
            }
        }
    }

    private void resetVars() {
        start = System.nanoTime();
        connectionsMap = new HashMap<>();
    }

    public void prepareDummyData(String[][] citiesArr) {
        resetVars();
        this.connectionsArr = citiesArr;
    }

    public void prepareDataFromCitiesFile(String path) {
        resetVars();
        String fileName = path;

        HashSet<String> citesSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(s -> citesSet.add(s));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> itSet = citesSet.iterator();

        connectionsArr = new String[citesSet.size()][2];

        int l = 0;
        String first;
        String second = "";
        while (itSet.hasNext()) {
            if(l == 0) {
                first = itSet.next();
                second = itSet.next();
            } else {
                first = second;
                second = itSet.next();
            }
            if(l % 3 == 0) {
                connectionsArr[l][0] = first;
                connectionsArr[l][1] = second;
            } else {
                connectionsArr[l][0] = first;
                connectionsArr[l][1] = second;
            }
            l++;
        }
    }

    public void prepareDataFromConnectionsFile(String pathString) {
        resetVars();
        String fileName = pathString;

        String[] parts;
        try {

            File f = new File(fileName);

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine;

            Path path = Paths.get(pathString);
            connectionsArr = new String[(int) Files.lines(path).count()][2];

            int i = 0;
            while ((readLine = b.readLine()) != null) {
                parts = readLine.split("_");
                connectionsArr[i][0] = parts[0];
                connectionsArr[i][1] = parts[1];
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doIt() {
        long startOnlyAlgorithm = System.nanoTime();
        findFirstAndLastAndPrepareConnectionsMap();
        if(out.length <= PRINT_ALL_CITIES_LIMIT_SIZE) {
            System.out.println("Connections map:");
            printMap(connectionsMap);
        }
        getOutputMirror(out, connectionsMap);
        end = System.nanoTime();
        System.out.println();
        System.out.println("Time spent: " + (end - start) / BILLION + "sec");
        System.out.println("Time spent (algorithm): " + (end - startOnlyAlgorithm) / BILLION + "sec");

        if(out.length > PRINT_ALL_CITIES_LIMIT_SIZE) {
            System.out.println("First and last city:");
            System.out.println(out[0] + " " + out[out.length -1]);
        } else {
            System.out.println("Whole route:");
            System.out.println(Arrays.toString(out));
        }

        System.out.println("**********");
        System.out.println();
    }
}
