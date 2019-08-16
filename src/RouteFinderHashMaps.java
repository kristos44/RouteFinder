import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class RouteFinderHashMaps {

    public static final double BILLION = 1000000000D;
    public static final int PRINT_ALL_CITIES_LIMIT_SIZE = 100;

    private HashMap<String, String> connectionsMap;
    private HashMap<String, String> connectionsMapMirror;
    private int connections;
    private String[] out;

    private long start;
    private long end;

    private int occ;
    private int occMirr;

    private String[][] citiesArr;

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

    private void findFirstAndLastAndPrepareMirror() {
        HashMap<String, Integer> occurrences = new HashMap<>();
//        Iterator it = connectionsMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            if(occurrences.containsKey(pair.getKey())) {
//                occurrences.remove(pair.getKey());
//            } else {
//                occurrences.put((String) pair.getKey(), 1);
//            }
//            if(occurrences.containsKey(pair.getValue())) {
//                occurrences.remove(pair.getValue());
//            } else {
//                occurrences.put((String) pair.getValue(), 1);
//            }
//
//            connectionsMapMirror.put((String) pair.getValue(), (String) pair.getKey());
//        }

        for(int i = 1; i <= citiesArr.length; i++) {
            if(occurrences.containsKey(citiesArr[i-1][0])) {
                occurrences.remove(citiesArr[i-1][0]);
            } else {
                occurrences.put(citiesArr[i-1][0], i);
            }
            if(occurrences.containsKey(citiesArr[i-1][1])) {
                occurrences.remove(citiesArr[i-1][1]);
            } else {
                occurrences.put(citiesArr[i-1][1], -i);
            }

//            for(String[] connection : citiesArr) {
//                System.out.println(Arrays.toString(connection));
//            }

//            System.out.println(citiesArr[i-1][0] + " & " + citiesArr[i-1][1]);

            switchExistingLoop(citiesArr[i-1][0], citiesArr[i-1][1], connectionsMap);
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
                int i, k;
                if(value > 0) {
                    i = value - 1;
                    k = 1;
                } else {
                    i = -value - 1;
                    k = 0;
                }
                out[1] = citiesArr[i][k];
                switchExistingLoop(out[0], out[1], connectionsMap);
                connectionsMap.remove(out[0]);
                j++;
            } else {
                out[connections] = pair.getKey().toString();
                int value = (int) pair.getValue();
                int i, k;
                if(value > 0) {
                    i = value - 1;
                    k = 1;
                } else {
                    i = -value - 1;
                    k = 0;
//                    System.out.println("Dupa " + -value + " " + i);
                }
                out[connections - 1] = citiesArr[i][k];
                switchExistingLoop(out[connections], out[connections - 1], connectionsMap);
                connectionsMap.remove(out[connections]);
            }
        }

        Iterator itConnectionsMap = connectionsMap.entrySet().iterator();
        while(itConnectionsMap.hasNext()) {
            Map.Entry pair = (Map.Entry) itConnectionsMap.next();
            connectionsMapMirror.put(pair.getValue().toString(), pair.getKey().toString());
        }
    }

    private void getOutputMirror(String[] out, HashMap<String, String> connMap, HashMap<String, String> connMapMirr) {
        for(int i = 1; i < out.length; i++) {
            int start = i;
//            int end = out.length - 1 - i;

            if (connMap.containsKey(out[start])) {
                String val = connMap.remove(out[start]);
                out[start + 1] = val;
                connMapMirr.remove(val);
                occ++;
            } else if (connMapMirr.containsKey(out[start])) {
                String val = connMapMirr.remove(out[start]);
                out[start + 1] = val;
                connMap.remove(val);
                occMirr++;
            }

//            if (connMap.containsKey(out[end])) {
//                String val = connMap.remove(out[end]);
//                out[end - 1] = val;
//                connMapMirr.remove(val);
//                occ++;
//            } else if (connMapMirr.containsKey(out[end])) {
//                String val = connMapMirr.remove(out[end]);
//                out[end - 1] = val;
//                connMap.remove(val);
//                occMirr++;
//            }
        }
    }

    public void prepareDummyData(String[][] citiesArr) {
        start = System.nanoTime();
//        for(String [] connection : citiesArr) {
//            switchExistingLoop(connection[0], connection[1], connectionsMap);
//        }
        connectionsMap = new HashMap<>();
        connectionsMapMirror = new HashMap<>();
        this.citiesArr = citiesArr;
    }

    public void prepareDataFromCitiesFile(String path) {
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
                switchExistingLoop(first, second, connectionsMap);
            } else {
                switchExistingLoop(second, first, connectionsMap);
            }
            l++;
        }
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
    }

    public void doIt() {
        occ = 0;
        occMirr = 0;
        long startOnlyAlgorithm = System.nanoTime();
        findFirstAndLastAndPrepareMirror();
        if(out.length <= PRINT_ALL_CITIES_LIMIT_SIZE) {
            printMaps();
        }
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

        System.out.println("Occurences: " + occ);
        System.out.println("Occurences mirror: " + occMirr);
        System.out.println("**********");
        System.out.println();
    }
}
