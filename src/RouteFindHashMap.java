import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class RouteFindHashMap {

    public static final double BILLION = 1000000000D;
    public static final int PRINT_ALL_CITIES_LIMIT_SIZE = 100;

    HashMap<String, String> connectionsMap;
    HashMap<String, String> connectionsMapMirror;
    int connections;
    String[] out;

    long start;
    long end;

    private void switchExisting(String key, String value, Map<String, String> connMap) {
        if(connMap.containsKey(key)) {
            String tempKey = connMap.get(key);
            String tempValue = key;
            connMap.put(key, value);
            switchExisting(tempKey, tempValue, connMap);
        } else {
            connMap.put(key, value);
        }
    }

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

    private void printMaps() {
        System.out.println("Connections map:");
        printMap(connectionsMap);
        System.out.println();
        System.out.println("Connections mirror map:");
        printMap(connectionsMapMirror);
    }

    private void prepareMirror(Map<String, String> map, Map<String, String> mapMirr) {
        Iterator it = map.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            mapMirr.put((String) pair.getValue(), (String) pair.getKey());
        }
    }

    public void prepareDummyData() {
        start = System.nanoTime();
        connectionsMap = new HashMap<>();
        connectionsMapMirror = new HashMap<>();
        switchExistingLoop("Wrocław", "Poznań", connectionsMap);
        switchExistingLoop("Legnica", "Opole", connectionsMap);
        switchExistingLoop("Poznań", "Szczecin", connectionsMap);
        switchExistingLoop("Opole", "Kluczbork", connectionsMap);
        switchExistingLoop("Wrocław", "Legnica", connectionsMap);

        prepareMirror(connectionsMap, connectionsMapMirror);

        printMaps();
    }

    public void prepareDummyData2() {
        start = System.nanoTime();
        connectionsMap = new HashMap<>();
        connectionsMapMirror = new HashMap<>();
        switchExistingLoop("Krosno", "Rzeszów", connectionsMap);
        switchExistingLoop("Chełm", "Zamość", connectionsMap);
        switchExistingLoop("Krosno", "Przemyśl", connectionsMap);
        switchExistingLoop("Puławy", "Radom", connectionsMap);
        switchExistingLoop("Zamość", "Przemyśl", connectionsMap);
        switchExistingLoop("Rzeszów", "Tarnów", connectionsMap);
        switchExistingLoop("Kielce", "Sandomierz", connectionsMap);
        switchExistingLoop("Kielce", "Tarnów", connectionsMap);
        switchExistingLoop("Chełm", "Lublin", connectionsMap);
        switchExistingLoop("Puławy", "Sandomierz", connectionsMap);

        prepareMirror(connectionsMap, connectionsMapMirror);

        printMaps();
    }

    public void prepareDataFromFile(String path) {
        start = System.nanoTime();
        connectionsMap = new HashMap<>();
        connectionsMapMirror = new HashMap<>();
        String fileName = path;

        HashSet<String> citesSet = new HashSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(s -> citesSet.add(s));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> itSet = citesSet.iterator();

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
                switchExisting(first, second, connectionsMap);
            } else {
                switchExisting(second, first, connectionsMap);
            }
            l++;
        }

        prepareMirror(connectionsMap, connectionsMapMirror);
    }

    public void prepareDataFromConnectionsFile(String path) {
        start = System.nanoTime();
        connectionsMap = new HashMap<>();
        connectionsMapMirror = new HashMap<>();
        String fileName = path;

        String[] parts;
        try {

            File f = new File(fileName);

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            while ((readLine = b.readLine()) != null) {
                parts = readLine.split("_");
                switchExistingLoop(parts[0], parts[1], connectionsMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareMirror(connectionsMap, connectionsMapMirror);
    }

    private void findFirstAndLast() {
        HashMap<String, Integer> occurrences = new HashMap<>();
        Iterator it = connectionsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if(occurrences.containsKey(pair.getKey())) {
                occurrences.remove(pair.getKey());
            } else {
                occurrences.put((String) pair.getKey(), 1);
            }
            if(occurrences.containsKey(pair.getValue())) {
                occurrences.remove(pair.getValue());
            } else {
                occurrences.put((String) pair.getValue(), 1);
            }
        }

        connections = connectionsMap.size();
        out = new String[connections + 1];

        Iterator itMap = occurrences.entrySet().iterator();
        int j = 0;
        while (itMap.hasNext()) {
            Map.Entry pair = (Map.Entry)itMap.next();
            if (j == 0) {
                out[0] = (String) pair.getKey();
                j++;
            } else {
                out[connections] = (String) pair.getKey();
            }
        }
    }

    private void getOutputMirror(String[] out, HashMap<String, String> connMap, HashMap<String, String> connMapMirr) {

        for(int i = 0; i < out.length/2; i++) {
            int start = i;
            int end = out.length - 1 - i;

            if (connMap.containsKey(out[start])) {
                String val = connMap.remove(out[start]);
                out[start + 1] = val;
                connMapMirr.remove(val);
            } else if (connMapMirr.containsKey(out[start])) {
                String val = connMapMirr.remove(out[start]);
                out[start + 1] = val;
                connMap.remove(val);
            }

            if (connMap.containsKey(out[end])) {
                String val = connMap.remove(out[end]);
                out[end - 1] = val;
                connMapMirr.remove(val);
            } else if (connMapMirr.containsKey(out[end])) {
                String val = connMapMirr.remove(out[end]);
                out[end - 1] = val;
                connMap.remove(val);
            }
        }
    }

    public void doIt() {
        long startOnlyAlgorithm = System.nanoTime();
        findFirstAndLast();
        getOutputMirror(out, connectionsMap, connectionsMapMirror);
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
