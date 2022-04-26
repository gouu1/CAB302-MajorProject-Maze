package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class DashForm extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 350;
    public static final int HEIGHT = 350;
    public static final int PANELS = 4;
    public static final int FONT_SIZE = 24;
    private JPanel[] panels;
    private JButton exitButton, exportButton, newButton, openButton;
    private JLabel title;
    private JFileChooser fc;

    /**
     * Adapted from the Week 5 prac.
     * @param e - event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        //Consider the alternatives - not all active at once.
        if (src == exitButton) {
            System.exit(0);
        }

        // TODO much code to be added here
        if (src == openButton) {
            if (fc.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                System.out.println("Opening: " + file.getName());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }

        // TODO do some actual file I/O
        if (src == exportButton) {
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(getContentPane()) == JFileChooser.APPROVE_OPTION) {
                File[] file = fc.getSelectedFiles();
                String msg = "File: " + "MultiSelection not enabled yet :(" + " has been exported!";
                JOptionPane.showMessageDialog(this, msg, "Success!",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                System.out.println("Open command cancelled by user.");
            }
            fc.setMultiSelectionEnabled(false);
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
     * Adapted from the week 5 prac
     */
    public void createGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setTitle("Maze designer - Dashboard");
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creates a file chooser that opens on detail view by default
        fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        title = new JLabel("MazeCo");
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

    public JPanel createPanel() {
        JPanel myPanel = new JPanel();
        myPanel.setBorder(new EmptyBorder(20, 10, 30, 10));
        return myPanel;
    }

    public JButton createButton(Dimension dim, String text) {
        JButton myButton = new JButton(text);
        myButton.setPreferredSize(dim);
        myButton.setMaximumSize(dim);
        myButton.addActionListener(this);
        return myButton;
    }
}