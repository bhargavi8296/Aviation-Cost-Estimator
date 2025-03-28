package travel.costPrediction.system.ui;

import travel.costPrediction.system.utils.GlobalData;
import travel.costPrediction.system.utils.connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FlightDetailsGUI extends JFrame {
    private final JComboBox<String> startCity, endCity, flightModelCombo;
    private double mileageInput = 0;
    private final JTextField fuelPriceInput, mileageDisplay;
    private final JTextArea resultArea;

    public FlightDetailsGUI() {
        setTitle("Flight Cost Estimator");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // No panels, manual positioning

        // 📌 Labels & Input Fields
        JLabel titleLabel = new JLabel("Flight Cost Estimator", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setBounds(170, 35, 220, 30);
        add(titleLabel);
        
        URL imageUrl = getClass().getResource("/images/logo.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(100, 10, 80, 80); 
        add(imageLabel);

        JLabel startLabel = new JLabel("Select Start City:");
        startLabel.setBounds(50, 100, 200, 25);
        startLabel.setFont(new Font("Raleway",Font.BOLD,14));
        add(startLabel);

        startCity = new JComboBox<>(loadCities());
        startCity.setBounds(250, 100, 250, 25);
        add(startCity);

        JLabel endLabel = new JLabel("Select Destination:");
        endLabel.setBounds(50, 150, 200, 25);
        endLabel.setFont(new Font("Raleway",Font.BOLD,14));
        add(endLabel);

        endCity = new JComboBox<>(loadCities());
        endCity.setBounds(250, 150, 250, 25);
        add(endCity);

        JLabel flightLabel = new JLabel("Select Flight Model:");
        flightLabel.setBounds(50, 200, 200, 25);
        flightLabel.setFont(new Font("Raleway",Font.BOLD,14));
        add(flightLabel);

        flightModelCombo = new JComboBox<>(loadFlightModels());
        flightModelCombo.setBounds(250, 200, 250, 25);
        add(flightModelCombo);

        JLabel fuelLabel = new JLabel("Fuel Price (per liter):");
        fuelLabel.setBounds(50, 250, 200, 25);
        fuelLabel.setFont(new Font("Raleway",Font.BOLD,14));
        add(fuelLabel);

        fuelPriceInput = new JTextField();
        fuelPriceInput.setBounds(250, 250, 250, 25);
        add(fuelPriceInput);

        JLabel mileageLabel = new JLabel("Mileage (km per liter):");
        mileageLabel.setBounds(50, 300, 200, 25);
        mileageLabel.setFont(new Font("Raleway",Font.BOLD,14));
        add(mileageLabel);

        mileageDisplay = new JTextField();
        mileageDisplay.setBounds(250, 300, 250, 25);
        mileageDisplay.setEditable(false);
        add(mileageDisplay);

        // 📌 Buttons
        ImageIcon i1 = new ImageIcon(getClass().getResource("/images/calculate.png"));
        JButton calculateBtn = createStyledButton("Calculate", new Color(46, 204, 113),i1);
        calculateBtn.setBounds(120, 370, 150, 35);
        add(calculateBtn);

        ImageIcon i2 = new ImageIcon(getClass().getResource("/images/back.png"));
        JButton backBtn = createStyledButton("Back", new Color(231, 76, 60),i2);
        backBtn.setBounds(290, 370, 150, 35);
        add(backBtn);

        JLabel footerLabel = new JLabel("© 2025 Travel Cost Prediction System");
        footerLabel.setBounds(170,540,260,25);
        footerLabel.setForeground(Color.gray);
        add(footerLabel);
        
        // 📌 Result Area
        resultArea = new JTextArea(3, 30);
        resultArea.setEditable(false);
        resultArea.setBounds(75, 450, 400, 50);
        add(resultArea);

        // 📌 Action Listeners
        flightModelCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFuelEstimate();
            }
        });

        calculateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateFlightCost();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeGUI();
                dispose();
            }
        });

        setVisible(true);
    }

    private String[] loadCities() {
        List<String> cityList = new ArrayList<>();
        try {connect con = new connect();
             ResultSet rs = con.getStatement().executeQuery("SELECT city_name FROM city");
            while (rs.next()) {
                cityList.add(rs.getString("city_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityList.isEmpty() ? new String[]{"No Cities Available"} : cityList.toArray(new String[0]);
    }

    private String[] loadFlightModels() {
        List<String> modelList = new ArrayList<>();
        try {connect con = new connect();
             ResultSet rs = con.getStatement().executeQuery("SELECT model_name FROM flightModel");
            while (rs.next()) {
                modelList.add(rs.getString("model_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelList.isEmpty() ? new String[]{"No Models Available"} : modelList.toArray(new String[0]);
    }

    private void updateFuelEstimate() {
        String selectedFlight = (String) flightModelCombo.getSelectedItem();
        try {connect con = new connect();
             ResultSet rs = con.getStatement().executeQuery("SELECT mileage FROM flightModel WHERE model_name='" + selectedFlight + "'");
            if (rs.next()) {
                mileageInput = rs.getDouble("mileage");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mileageDisplay.setText(String.format("%.2f km/l", mileageInput));
    }

    private void calculateFlightCost() {
        try {
            double fuelPrice = Double.parseDouble(fuelPriceInput.getText());
            if (mileageInput <= 0) {
                resultArea.setText("Invalid mileage. Select a valid flight model.");
                return;
            }

            String start = (String) startCity.getSelectedItem();
            String end = (String) endCity.getSelectedItem();
            int distance = GlobalData.graph.dijkstra(start, end);

            if (distance == -1) {
                resultArea.setText("No route available between " + start + " and " + end);
                return;
            }

            double fuelNeeded = distance / mileageInput;
            double totalCost = fuelNeeded * fuelPrice;

            resultArea.setText("Estimated Fuel Needed: " + String.format("%.2f liters", fuelNeeded) + "\n"
                    + "Estimated Cost: $" + String.format("%.2f", totalCost));
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid fuel price input.");
        }
    }

    private JButton createStyledButton(String text, Color bgColor,ImageIcon i) {
        JButton button = new JButton(text,i);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        return button;
    }
}
