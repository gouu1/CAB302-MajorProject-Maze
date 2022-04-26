package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class NewMazeDialog extends JDialog implements ActionListener, Runnable {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 175;
    private JCheckBox childrenCheck, randomCheck;
    private JTextField mazeNameField, mazeX, mazeY;
    private JButton newMazeButton, addLogoButton;
    private JLabel px1, px2, imageLabel;
    private JFileChooser fc;

    public NewMazeDialog(JFrame parent, String title, boolean modality) {
        super(parent, title, modality);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == addLogoButton) {
            int returnValue = fc.showOpenDialog(getContentPane());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("Opening:" + file.getName() + ".");
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }

        if (src == newMazeButton) {
            if (mazeNameField.getText().isBlank() || mazeX.getText().isBlank() || mazeY.getText().isBlank() ||
                   checker(mazeX.getText()) || checker(mazeY.getText())) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid maze name and/or dimensions!", "Error!",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                SwingUtilities.invokeLater(new MazeEditForm(mazeNameField.getText(),
                        new Dimension(Integer.parseInt(mazeX.getText()), Integer.parseInt(mazeY.getText())))); // yuck
                dispose();
            }
        }
    }

    @Override
    public void run() {
        createGUI();
    }

    public void createGUI() {
        GroupLayout layout = new GroupLayout(getContentPane());
        setLayout(layout);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getContentPane());

        fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        childrenCheck = new JCheckBox("Children's maze");
        randomCheck = new JCheckBox("Randomized maze");

        newMazeButton = createButton("New Maze");
        addLogoButton = createButton("Add Logo");

        mazeNameField = new JTextField("Add maze name here...");
        mazeNameField.setMinimumSize(new Dimension(200, 20));
        mazeX = createTextField("x");
        mazeY = createTextField("y");

        px1 = new JLabel("px");
        px2 = new JLabel("px");
        imageLabel = new JLabel("No image selected.");

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(mazeNameField)
                        .addComponent(childrenCheck)
                        .addComponent(randomCheck)
                        .addComponent(newMazeButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(mazeX)
                                .addComponent(px1)
                                .addComponent(mazeY)
                                .addComponent(px2))
                        .addComponent(addLogoButton)
                        .addComponent(imageLabel))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(mazeNameField)
                        .addComponent(mazeX)
                        .addComponent(px1)
                        .addComponent(mazeY)
                        .addComponent(px2))
                .addComponent(childrenCheck)
                .addComponent(randomCheck)

                .addGroup(layout.createParallelGroup()
                        .addComponent(newMazeButton)
                        .addComponent(addLogoButton))
                .addComponent(imageLabel)
        );
        repaint();
        setVisible(true);
    }

    public JButton createButton(String text) {
        JButton myButton = new JButton(text);

        myButton.addActionListener(this);
        return myButton;
    }

    public JTextField createTextField(String text) {
        JTextField myField = new JTextField(text);
        myField.setMaximumSize(new Dimension(50,20));
        myField.setHorizontalAlignment(SwingConstants.RIGHT);

        myField.addActionListener(this);
        return myField;
    }

    private boolean checker(String str) {
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}