import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefenseCalcGUIOPT {

    // Define components
    private JFrame frame;
    private JTextField defField, leadSkillField, defPassField, defSupportField, defPLinksField, defFLinksField, buDefPassField, attackDefenseField, saDefenseField, saDefense2Field, saTimesField;
    private JLabel sotDefLabel, fullBuiltDefLabel, maxDefLabel;
    private JPanel superDefPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DefenseCalcGUIOPT::new);
    }

    public DefenseCalcGUIOPT() {
        // Set up the frame
        frame = new JFrame("Defense Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setLayout(new GridLayout(0, 2, 5, 5));

        // Create input fields and labels
        createInputField("Unit's Defense Stat:", defField = new JTextField());
        createInputField("Leader Skill Percentage:", leadSkillField = new JTextField());
        createInputField("Start of Turn Defense:", defPassField = new JTextField());
        createInputField("Support Defense:", defSupportField = new JTextField());
        createInputField("Percentage from Links:", defPLinksField = new JTextField());
        createInputField("Flat Defense from Links:", defFLinksField = new JTextField());
        createInputField("Defense from Build-ups:", buDefPassField = new JTextField());
        createInputField("Defense on Super/Attack:", attackDefenseField = new JTextField());
        createInputField("Defense in SA Effect:", saDefenseField = new JTextField());
        createInputField("LR 12 Ki SA Effect (if applicable):", saDefense2Field = new JTextField());
        createInputField("Number of Supers per Turn:", saTimesField = new JTextField());

        // Calculate button
        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new CalculateButtonListener());
        frame.add(calculateButton);
        frame.add(new JLabel(""));

        // Result labels
        sotDefLabel = new JLabel("SoT Defense: ");
        frame.add(sotDefLabel);
        fullBuiltDefLabel = new JLabel("Fully Built-up SoT Defense: ");
        frame.add(fullBuiltDefLabel);
        maxDefLabel = new JLabel("Max Possible Defense: ");
        frame.add(maxDefLabel);

        // Panel for dynamic labels
        superDefPanel = new JPanel();
        superDefPanel.setLayout(new BoxLayout(superDefPanel, BoxLayout.Y_AXIS));
        frame.add(new JLabel("Defense after each Super:"));
        frame.add(superDefPanel);

        // Set frame visible
        frame.setVisible(true);
    }

    private void createInputField(String labelText, JTextField textField) {
        frame.add(new JLabel(labelText));
        frame.add(textField);
    }

    private class CalculateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Parse input fields
                int def = Integer.parseInt(defField.getText());
                int leadSkill = Integer.parseInt(leadSkillField.getText());
                int defPass = Integer.parseInt(defPassField.getText()) + Integer.parseInt(defSupportField.getText());
                int defPLinks = Integer.parseInt(defPLinksField.getText());
                int defFLinks = Integer.parseInt(defFLinksField.getText());
                int buDefPass = Integer.parseInt(buDefPassField.getText());
                int attackDefense = Integer.parseInt(attackDefenseField.getText());
                int saDefense = Integer.parseInt(saDefenseField.getText());
                int saDefense2 = Integer.parseInt(saDefense2Field.getText());
                int saTimes = Integer.parseInt(saTimesField.getText());
                if (saTimes == 0) saTimes = 1;

                // Calculate defenses
                int sotDef = calculateSoTDefense(def, leadSkill, defPass, defPLinks, defFLinks);
                int fullBuiltUpDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
                int maxDef = calculateMaxDefense(sotDef, buDefPass, attackDefense, saDefense, saDefense2, saTimes);

                // Calculate defense after each super
                int[] defenseAfterSupers = calculateDefenseAfterEachSuper(sotDef, buDefPass, attackDefense, saDefense, saDefense2, saTimes);

                // Display the results
                sotDefLabel.setText("SoT Defense: " + sotDef);
                fullBuiltDefLabel.setText("Fully Built-up SoT Defense: " + fullBuiltUpDef);
                maxDefLabel.setText("Max Possible Defense: " + maxDef);

                // Clear previous labels
                superDefPanel.removeAll();

                // Add new labels based on the number of supers
                for (int i = 0; i < saTimes; i++) {
                    JLabel superDefLabel = new JLabel("Defense after " + (i + 1) + " Super(s): " + defenseAfterSupers[i]);
                    superDefPanel.add(superDefLabel);
                }

                // Refresh the panel
                superDefPanel.revalidate();
                superDefPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Calculate SoT Defense
    private int calculateSoTDefense(int def, int leadSkill, int defPass, int defPLinks, int defFLinks) {
        int sotDef = def;
        sotDef = (int) (sotDef * ((leadSkill + 100) / 100.0));
        sotDef = (int) (sotDef * ((defPass + 100) / 100.0));
        sotDef = (int) (sotDef * ((defPLinks + 100) / 100.0));
        sotDef += defFLinks;
        return sotDef;
    }

    // Calculate Fully Built-up SoT Defense
    private int calculateFullBuiltUpSoTDefense(int sotDef, int buDefPass) {
        return (int) (sotDef * ((buDefPass + 100) / 100.0));
    }

    // Calculate Max Defense after supering
    private int calculateMaxDefense(int sotDef, int buDefPass, int attackDefense, int saDefense, int saDefense2, int satimesperTurn) {
        int maxDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
        maxDef = (int) (maxDef * ((attackDefense + 100) / 100.0));

        if (satimesperTurn == 1) {
            maxDef = (int) (maxDef * ((saDefense + 100) / 100.0));
        } else if (saDefense2 > 0) {
            maxDef = (int) (maxDef * (saDefense + (saDefense2 * (satimesperTurn - 1)) + 100) / 100.0);
        } else {
            maxDef = (int) (maxDef * ((saDefense * satimesperTurn) + 100) / 100.0);
        }
        return maxDef;
    }

    // Calculate defense after each super
    private int[] calculateDefenseAfterEachSuper(int sotDef, int buDefPass, int attackDefense, int saDefense, int saDefense2, int satimesperTurn) {
        int currentDef = calculateFullBuiltUpSoTDefense(sotDef, buDefPass);
        currentDef = (int) (currentDef * ((attackDefense + 100) / 100.0));
        int staticDef = currentDef;

        int[] defenseAfterSupers = new int[satimesperTurn];
        for (int i = 0; i < satimesperTurn; i++) {
            if (i == 0) {
                currentDef = (int) (staticDef * (saDefense + 100) / 100.0);
            } else if (saDefense2 > 0) {
                currentDef = (int) (staticDef * (saDefense + (saDefense2 * i) + 100) / 100.0);
            } else {
                currentDef = (int) (staticDef * ((saDefense * (i + 1)) + 100) / 100.0);
            }
            defenseAfterSupers[i] = currentDef;
        }
        return defenseAfterSupers;
    }
}
