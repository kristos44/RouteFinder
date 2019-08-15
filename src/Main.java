public class Main {

    public static void main(String[] args) {
        RouteFindHashMap routeFindHashMap = new RouteFindHashMap();

//        routeFindHashMap.prepareDataFromFile("/tmp/cities.csv");
//        routeFindHashMap.doIt();
//
//        routeFindHashMap.prepareDummyData();
//        routeFindHashMap.doIt();
//
//        routeFindHashMap.prepareDummyData2();
//        routeFindHashMap.doIt();

        routeFindHashMap.prepareDataFromConnectionsFile("/tmp/connections.txt");
        routeFindHashMap.doIt();
    }
}
