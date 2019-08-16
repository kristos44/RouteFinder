import java.util.LinkedList;
import java.util.*;

public class Main {

    public static void main(String[] args) {
//        LinkedList<LinkedList<String>> data = new LinkedList<>();
//        data.add(new LinkedList(Arrays.asList("Wrocław", "Poznań")));
//        data.add(new LinkedList(Arrays.asList("Legnica", "Opole")));
//        data.add(new LinkedList(Arrays.asList("Poznań", "Szczecin")));
//        data.add(new LinkedList(Arrays.asList("Opole", "Kluczbork")));
//        data.add(new LinkedList(Arrays.asList("Wrocław", "Legnica")));
//        System.out.println(data.get(0).get(0));

        String[][] dummpyData = {
                {"Wrocław", "Poznań"},
                {"Legnica", "Opole"},
                {"Poznań", "Szczecin"},
                {"Opole", "Kluczbork"},
                {"Wrocław", "Legnica"}
        };

        String[][] dummyData2 = {
                {"Krosno", "Rzeszów"},
                {"Chełm", "Zamość"},
                {"Krosno", "Przemyśl"},
                {"Puławy", "Radom"},
                {"Zamość", "Przemyśl"},
                {"Rzeszów", "Tarnów"},
                {"Kielce", "Sandomierz"},
                {"Kielce", "Tarnów"},
                {"Chełm", "Lublin"},
                {"Puławy", "Sandomierz"}
        };

        String[][] dummyData3 = {
                {"Krosno", "Rzeszów"},
                {"Chełm", "Zamość"},
                {"Przemyśl", "Krosno"},
                {"Puławy", "Radom"},
                {"Zamość", "Przemyśl"},
                {"Rzeszów", "Tarnów"},
                {"Kielce", "Sandomierz"},
                {"Tarnów", "Kielce"},
                {"Lublin", "Chełm"},
                {"Puławy", "Sandomierz"}
        };

        String[][] dummyData4 = {
                {"Radom", "Łódź"},
                {"Łomża", "Radom"},
                {"Kielce", "Łomża"}
        };

        RouteFinderHashMaps routeFinderHashMaps = new RouteFinderHashMaps();

        routeFinderHashMaps.prepareDataFromCitiesFile("/tmp/cities.csv");
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummpyData);
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummyData2);
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummyData3);
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDataFromConnectionsFile("/tmp/connections.txt");
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummyData4);
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummyData4);
        routeFinderHashMaps.doIt();
    }
}
