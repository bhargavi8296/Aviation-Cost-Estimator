package travel.costPrediction.system.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.net.URL;
import java.sql.ResultSet;
import travel.costPrediction.system.utils.connect;

public class FlightModelInputGUI extends JFrame {
    public FlightModelInputGUI() {
        setTitle("Add Flight Model");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null); // No panels, manual positioning

        getContentPane().setBackground(new Color(236, 240, 241));

        // 📌 Title Label
        JLabel titleLabel = new JLabel("Add a New Flight Model", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setBounds(170, 35, 260, 30);
        add(titleLabel);

        // 📌 Image Label
        URL imageUrl = getClass().getResource("/images/logo.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setBounds(100, 10, 80, 80); 
        add(imageLabel);

      

        // 📌 Labels & Input Fields
        JLabel modelLabel = new JLabel("Flight Model:");
        modelLabel.setFont(new Font("Raleway",Font.BOLD,16));
        modelLabel.setBounds(90, 130, 170, 25);
        add(modelLabel);

        JTextField modelField = new JTextField();
        modelField.setBounds(280, 130, 200, 25);
        add(modelField);

        JLabel mileageLabel = new JLabel("Mileage (km per liter):");
        mileageLabel.setFont(new Font("Raleway",Font.BOLD,16));
        mileageLabel.setBounds(90, 170, 170, 25);
        add(mileageLabel);

        JTextField mileageField = new JTextField();
        mileageField.setBounds(280, 170, 200, 25);
        add(mileageField);

        // 📌 Buttons
        ImageIcon i1 = new ImageIcon(getClass().getResource("/images/add.png"));
        JButton addButton = createStyledButton("Add Model", new Color(46, 204, 113),i1);
        addButton.setBounds(100, 240, 170, 35);
        add(addButton);

        ImageIcon i2 = new ImageIcon(getClass().getResource("/images/back.png"));
        JButton backButton = createStyledButton("Back", new Color(231, 76, 60),i2);
        backButton.setBounds(310, 240, 170, 35);
        add(backButton);

        // 📌 Footer Label
        JLabel footerLabel = new JLabel("© 2025 Travel Cost Prediction System", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.DARK_GRAY);
        footerLabel.setBounds(140, 330, 250, 25);
        add(footerLabel);

        // 📌 Action Listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String model = modelField.getText().trim();
                String mileageText = mileageField.getText().trim();

                try {
                    double mileage = Double.parseDouble(mileageText);
                    if (mileage < 0) throw new NumberFormatException();

                   connect con = new connect();
                         ResultSet result = con.getStatement().executeQuery("SELECT * FROM flightModel WHERE model_name='" + model + "'");

                        if (!result.next()) {
                            String q = "INSERT INTO flightModel VALUES('" + model + "', '" + mileage + "' )";
                            con.getStatement().executeUpdate(q);
                            JOptionPane.showMessageDialog(null, "Flight model added successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Model already exists.", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid mileage value.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomeGUI();
                dispose();
            }
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
