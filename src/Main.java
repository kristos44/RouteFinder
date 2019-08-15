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

        RouteFindHashMap routeFindHashMap = new RouteFindHashMap();

        routeFindHashMap.prepareDataFromCitiesFile("/tmp/cities.csv");
        routeFindHashMap.doIt();

        routeFindHashMap.prepareDummyData(dummpyData);
        routeFindHashMap.doIt();

        routeFindHashMap.prepareDummyData(dummyData2);
        routeFindHashMap.doIt();

        routeFindHashMap.prepareDataFromConnectionsFile("/tmp/connections.txt");
        routeFindHashMap.doIt();
    }
}
