package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class NewMazeForm extends JDialog implements ActionListener, Runnable {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 175;
    private JCheckBox childrenCheck, randomCheck;
    private JTextField mazeNameField, mazeX, mazeY;
    private JButton newMazeButton, addLogoButton;
    private JLabel px1, px2, imageLabel;
    private JFileChooser fc;

    public NewMazeForm(JFrame parent, String title, boolean modality) {
        super(parent, title, modality);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        //Consider the alternatives - not all active at once.
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
            JOptionPane.showMessageDialog(this, "I do nothing, please fix me!", "Fix Me!",
                    JOptionPane.ERROR_MESSAGE);
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

        fc = new JFileChooser();

        childrenCheck = new JCheckBox("Children's maze");
        randomCheck = new JCheckBox("Randomized maze");

        newMazeButton = createButton("New Maze");
        addLogoButton = createButton("Add Logo");

        mazeNameField = new JTextField("Add maze name here...");
        mazeNameField.setMinimumSize(new Dimension(200, 20));
        mazeX = new JTextField("x");
        mazeY = new JTextField("y");
        mazeY.setMaximumSize(new Dimension(50,20));
        mazeX.setMaximumSize(new Dimension(50,20));
        mazeX.setHorizontalAlignment(SwingConstants.RIGHT);
        mazeY.setHorizontalAlignment(SwingConstants.RIGHT);

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
}
