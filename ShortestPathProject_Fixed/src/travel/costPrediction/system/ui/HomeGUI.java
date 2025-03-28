package travel.costPrediction.system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;

import travel.costPrediction.system.utils.GlobalData;

public class HomeGUI extends JFrame {
    private final GlobalData gb;

    public HomeGUI() {
        gb = new GlobalData();
        setTitle("Travel Cost Prediction System");
        setSize(550, 500);
        setLocationRelativeTo(null); // Centers window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🟡 HEADER PANEL (Title + Image)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // ✈️ Image Icon
        URL imageUrl = getClass().getResource("/images/logo.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        // 🏠 Title Label
        JLabel titleLabel = new JLabel("Welcome to Travel Cost Estimator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(imageLabel);
        headerPanel.add(titleLabel);

        // 📝 MAIN PANEL (Buttons)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel label1 = new JLabel("Check the Flight Cost:");
        label1.setFont(new Font("Calibri", Font.BOLD, 18));
        label1.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon i1 = new ImageIcon(getClass().getResource("/images/plane.png"));
        JButton flightButton = createStyledButton("Flight Cost Estimator", new Color(46, 204, 113),i1);

        // 🔹 PANEL FOR SIDE-BY-SIDE BUTTONS
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        ImageIcon i2 = new ImageIcon(getClass().getResource("/images/journey.png"));
        JButton addJourneyButton = createStyledButton("Add Journey", new Color(241, 196, 15),i2);
        
        ImageIcon i3 = new ImageIcon(getClass().getResource("/images/journey.png"));
        JButton addFlightButton = createStyledButton("Add Flight Models", new Color(230, 126, 34),i3);
        buttonPanel.add(addJourneyButton);
        buttonPanel.add(addFlightButton);

        // 🎯 ACTION LISTENERS
        flightButton.addActionListener((ActionEvent e) -> {
            new FlightDetailsGUI();
            dispose();
        });

        addJourneyButton.addActionListener((ActionEvent e) -> {
            new RouteInputGUI(GlobalData.graph);
            dispose();
        });

        addFlightButton.addActionListener((ActionEvent e) -> {
            new FlightModelInputGUI();
            dispose();
        });

        mainPanel.add(label1);
        mainPanel.add(flightButton);
        mainPanel.add(buttonPanel); // Adds both buttons side by side

        // 🔻 FOOTER PANEL
        JPanel footerPanel = new JPanel();
        JLabel footerLabel = new JLabel("© 2025 Travel Cost Prediction System");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(footerLabel);

        // 🔹 ADD PANELS TO FRAME
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 🎨 METHOD TO CREATE STYLED BUTTONS
    private JButton createStyledButton(String text, Color bgColor,ImageIcon icon) {
        JButton button = new JButton(text,icon);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }

    public static void main(String[] args) {
        new HomeGUI();
    }
}
