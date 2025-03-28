package travel.costPrediction.system.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.*;

import travel.costPrediction.system.utils.API;
import travel.costPrediction.system.utils.GlobalData;
import travel.costPrediction.system.utils.Graph;
import travel.costPrediction.system.utils.distanceCalculator;

public class RouteInputGUI extends JFrame {
    public RouteInputGUI(Graph graph) {
        setTitle("Add Route");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // ❌ No panels, using absolute positioning

        getContentPane().setBackground(new Color(236, 240, 241));

        // 📌 Title
        JLabel titleLabel = new JLabel("Add a New Route", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setBounds(150, 35, 250, 30);
        add(titleLabel);
        
        URL imageUrl = getClass().getResource("/images/logo.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(100, 10, 80, 80); 
        add(imageLabel);

        // 📌 Labels & Input Fields
        JLabel sourceLabel = new JLabel("Source:");
        sourceLabel.setFont(new Font("Raleway",Font.BOLD,16));
        sourceLabel.setBounds(90, 120, 150, 25);
        add(sourceLabel);

        JTextField sourceField = new JTextField();
        sourceField.setBounds(250, 120, 200, 25);
        add(sourceField);

        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(new Font("Raleway",Font.BOLD,16));
        destinationLabel.setBounds(90, 160, 150, 25);
        add(destinationLabel);

        JTextField destinationField = new JTextField();
        destinationField.setBounds(250, 160, 200, 25);
        add(destinationField);


        // 📌 Buttons
        ImageIcon i1 = new ImageIcon(getClass().getResource("/images/add.png"));
        JButton addButton = createStyledButton("Add Route", new Color(46, 204, 113),i1);
        addButton.setBounds(100, 230, 170, 35);
        add(addButton);

        ImageIcon i2 = new ImageIcon(getClass().getResource("/images/back.png"));
        JButton backButton = createStyledButton("Back", new Color(231, 76, 60),i2);
        backButton.setBounds(290, 230, 170, 35);
        add(backButton);
        
        JLabel footerLabel = new JLabel("© 2025 Travel Cost Prediction System");
        footerLabel.setBounds(170,340,260,25);
        footerLabel.setForeground(Color.gray);
        add(footerLabel);

        // 📌 Action Listeners
        addButton.addActionListener((ActionEvent e) -> {
            String source = sourceField.getText().trim();
            String destination = destinationField.getText().trim();
            double cor1[] = new API(source).re;
            double cor2[] = new API(destination).re;

            if (cor1 == null || cor2 == null) {
                JOptionPane.showMessageDialog(null, "Invalid Source or Destination", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double distance = new distanceCalculator(cor1[0], cor2[0], cor1[1], cor2[1]).distance;
                System.out.println(distance+" "+cor1[0]+" "+cor1[1]+" "+cor2[0]+" "+cor2[1]);
                if (distance < 0) throw new NumberFormatException();

                GlobalData.graph.addFlight(source, destination, (int) distance);
                JOptionPane.showMessageDialog(null, "Route added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid distance value.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception E) {
                E.printStackTrace();
            }
        });

        backButton.addActionListener((ActionEvent e) -> {
            new HomeGUI();
            dispose();
        });

        setVisible(true);
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
