1. Make sure that test files included in repo (cities.csv and connections.txt) are placed in /tmp dir

2. In console cd to dir {dir_where_repo_cloned}/src

3. Run commands:
    javac Main.java RouteFindHashMap.java
    
    jar -cvf routefinder.jar Main.class RouteFindHashMap.class
    
    java -cp ./routefinder.jar Main
    
4. Program output should be printed on the screen


Test files:
cities.csv - list of cities base on which pairs of cities connections are created
connections.txt - actual list of connections containing around million of items, _ sign was used to delimit cities
as some of cities have - sign as part of the name   
    