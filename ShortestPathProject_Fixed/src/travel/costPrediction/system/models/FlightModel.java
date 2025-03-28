package travel.costPrediction.system.models;

public class FlightModel {
    private final String modelName;
    private final double mileage; 

    public FlightModel(String modelName, double mileage) {
        this.modelName = modelName;
        this.mileage = mileage;
    }

    public double getMileage() {
        return mileage;
    }

    public String getModelName() {
        return modelName;
    }
}
