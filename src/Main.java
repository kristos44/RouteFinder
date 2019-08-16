public class Main {

    public static void main(String[] args) {
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

        RouteFinderHashMaps routeFinderHashMaps = new RouteFinderHashMaps();

//        routeFinderHashMaps.prepareDataFromCitiesFile("/tmp/cities.csv");
//        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummpyData);
        routeFinderHashMaps.doIt();

        routeFinderHashMaps.prepareDummyData(dummyData2);
        routeFinderHashMaps.doIt();

//        routeFinderHashMaps.prepareDataFromConnectionsFile("/tmp/connections.txt");
//        routeFinderHashMaps.doIt();
    }
}
