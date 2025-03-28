package travel.costPrediction.system.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import travel.costPrediction.system.models.FlightModel;

public class Graph {
    public final Map<String, Integer> cityIndex = new HashMap<>();
    private final List<String> cityNames;
    private final List<List<Integer>> flightMatrix;

    public Graph(List<String> cities) {
        this.cityNames = new ArrayList<>(cities);

        for (int i = 0; i < cities.size(); i++) {
            cityIndex.put(cities.get(i), i);
        }

        // ✅ Initialize adjacency matrix
        flightMatrix = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            flightMatrix.add(new ArrayList<>(Collections.nCopies(cities.size(), Integer.MAX_VALUE)));
        }
    }

    public void addCity(String city) {
        if (!cityIndex.containsKey(city)) {
            cityIndex.put(city, cityNames.size());
            cityNames.add(city);
            GlobalData.cities.add(city);

            for (List<Integer> row : flightMatrix) {
                row.add(Integer.MAX_VALUE);
            }

            flightMatrix.add(new ArrayList<>(Collections.nCopies(cityNames.size(), Integer.MAX_VALUE)));

            // ✅ Add city to database
            try {
            	connect con = new connect();
            	ResultSet result=con.getStatement().executeQuery("Select * from city where city_name='"+city+"'");
            	if(!result.next())
                {
            		PreparedStatement stmt = con.getConnection()
                        .prepareStatement("INSERT INTO city (city_name) VALUES (?)");
	                stmt.setString(1, city);
	                stmt.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addFlight(String city1, String city2, int distance) {
        addCity(city1);
        addCity(city2);

        int i = cityIndex.get(city1);
        int j = cityIndex.get(city2);

        flightMatrix.get(i).set(j, distance);
        flightMatrix.get(j).set(i, distance);

        // ✅ Add to database safely
        try {
        	
        	
        	connect con = new connect();
        	ResultSet result=con.getStatement().executeQuery("Select * from route where source='"+city1+"' and destination='"+city2+"'");
        	if(!result.next())
            {
        		PreparedStatement stmt = con.getConnection()
                    .prepareStatement("INSERT INTO route (source, destination, distance) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE distance = LEAST(distance, ?)");
	            stmt.setString(1, city1);
	            stmt.setString(2, city2);
	            stmt.setInt(3, distance);
	            stmt.setInt(4, distance);
	            stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int dijkstra(String startCity, String endCity) {
    	
        if (!cityIndex.containsKey(startCity) || !cityIndex.containsKey(endCity)) {
            throw new IllegalArgumentException("One or both cities not found.");
        }

        int V = cityNames.size();
        int[] distances = new int[V];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[cityIndex.get(startCity)] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{cityIndex.get(startCity), 0});

        boolean[] visited = new boolean[V];

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int city = current[0];

            if (visited[city]) continue;
            visited[city] = true;

            for (int neighbor = 0; neighbor < V; neighbor++) {
                if (neighbor < flightMatrix.get(city).size()) {
                    int distance = flightMatrix.get(city).get(neighbor);
                    if (distance != Integer.MAX_VALUE && !visited[neighbor]) {
                        int newDist = distances[city] + distance;
                        if (newDist < distances[neighbor]) {
                            distances[neighbor] = newDist;
                            pq.add(new int[]{neighbor, newDist});
                        }
                    }
                }
            }
        }

        return (distances[cityIndex.get(endCity)] == Integer.MAX_VALUE) ? -1 : distances[cityIndex.get(endCity)];
    }
}
