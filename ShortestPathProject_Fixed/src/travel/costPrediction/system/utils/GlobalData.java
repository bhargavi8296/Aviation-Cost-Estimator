package travel.costPrediction.system.utils;

import travel.costPrediction.system.models.FlightModel;
import java.sql.ResultSet;
import java.util.*;

public class GlobalData {
    public static List<String> cities = new ArrayList<>();
    public static Graph graph;
    public static Map<String, FlightModel> flightModels = new HashMap<>();

    static {
        // ✅ Initialize flight models FIRST
        flightModels = new HashMap<>();
        flightModels.put("Boeing 737", new FlightModel("Boeing 737", 5.0));
        flightModels.put("Airbus A320", new FlightModel("Airbus A320", 4.8));
        flightModels.put("Boeing 747", new FlightModel("Boeing 747", 3.5));
        flightModels.put("Airbus A380", new FlightModel("Airbus A380", 3.2));
        

        cities = new ArrayList<>();

        try {connect con = new connect();
            // ✅ Load all cities from database
            String cityQuery = "SELECT city_name FROM city";
            ResultSet cityResult = con.getStatement().executeQuery(cityQuery);
            while (cityResult.next()) {
                cities.add(cityResult.getString("city_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ Initialize graph with cities
        graph = new Graph(cities);

        try {connect con = new connect();
            // ✅ Load routes from database into the graph
            String routeQuery = "SELECT source, destination, distance FROM route";
            ResultSet routeResult = con.getStatement().executeQuery(routeQuery);

            while (routeResult.next()) {
                String source = routeResult.getString("source");
                String destination = routeResult.getString("destination");
                int distance = routeResult.getInt("distance");

                graph.addFlight(source, destination, distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
