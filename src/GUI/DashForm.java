package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main GUI form which will spawn all subsequent forms and handle the event listening.
 */
public class DashForm extends JFrame implements ActionListener, Runnable {
    private static final String TITLE = "MazeCo";
    public static final int WIDTH = 350;
    public static final int HEIGHT = 350;
    public static final int PANELS = 4;
    public static final int FONT_SIZE = 24;
    private JPanel[] panels;
    private JButton exitButton, exportButton, newButton, openButton;
    public static JDBCMazeDataSource source = new JDBCMazeDataSource();

    /**
     * Adapted from the Week 5 prac. Takes an event e and checks the source of the event.
     * Different actions based on the source of the event are then executed here.
     * @param e - event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        //Consider the alternatives - not all active at once.
        if (src == exitButton) {
            source.close();
            System.exit(0);
        }

        if (src == openButton) {
            if (source.countRows() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Database is empty, please add a maze!", "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
            else SwingUtilities.invokeLater(new OpenFileDialog(this, "Open Maze", true, Mode.OPEN));
        }

        if (src == exportButton) {
            if (source.countRows() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Database is empty, please add a maze!", "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
            else SwingUtilities.invokeLater(new OpenFileDialog(this, "Export Maze", true, Mode.EXPORT));
        }

        if (src == newButton) {
            SwingUtilities.invokeLater(new NewMazeDialog(this, "New Maze", true));
        }
    }

    @Override
    public void run() {
        createGUI();
    }

    /**
     * Adapted from the week 5 prac. Generates a Java Swing form that utilises a Border layout and
     * Box layout for the center panel to create a responsive Dashboard form.
     */
    public void createGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("Dashboard");
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        panels = new JPanel[PANELS];
        String[] positions = {BorderLayout.EAST, BorderLayout.WEST,
                            BorderLayout.SOUTH, BorderLayout.CENTER};

        for (int i = 0; i < PANELS; i++) {
            panels[i] = createPanel();
            getContentPane().add(panels[i], positions[i]);
        }

        Dimension buttonBorder = new Dimension(100, 40);
        exitButton = createButton(buttonBorder, "Exit");
        exportButton = createButton(buttonBorder, "Export");

        newButton = createButton(buttonBorder, "New maze");
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        openButton = createButton(buttonBorder, "Open maze");
        openButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel southPanel = panels[PANELS - 2];
        JPanel midPanel = panels[PANELS - 1];
        southPanel.setLayout(new BorderLayout());
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));

        southPanel.add(exitButton, BorderLayout.EAST);
        southPanel.add(exportButton, BorderLayout.WEST);
        midPanel.add(title);
        midPanel.add(Box.createVerticalGlue());
        midPanel.add(newButton);
        midPanel.add(Box.createVerticalGlue());
        midPanel.add(openButton);

        repaint();
        setVisible(true);
    }

    /**
     * Creates a panel for the dash form.
     * @return A JPanel component with a predefined border
     */
    public JPanel createPanel() {
        JPanel myPanel = new JPanel();
        myPanel.setBorder(new EmptyBorder(20, 10, 30, 10));
        return myPanel;
    }

    /**
     * Creates a baseline look for all buttons on the dash form
     * @param dim - Dimensions of the button
     * @param text - The text to be display on the button
     * @return - A Button with an action listener and layout set
     */
    public JButton createButton(Dimension dim, String text) {
        JButton myButton = new JButton(text);
        myButton.setPreferredSize(dim);
        myButton.setMaximumSize(dim);
        myButton.addActionListener(this);
        return myButton;
    }
}