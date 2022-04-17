import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashForm extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 350;
    public static final int HEIGHT = 350;
    public static final int PANELS = 4;
    private JPanel[] panels;
    private JButton exitButton, exportButton, newButton, openButton;
    private JLabel title;

    /**
     * Adapted from the Week 5 prac.
     * @param e - event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Get event source
        Object src = e.getSource();
        //Consider the alternatives - not all active at once.
        if (src == exitButton) {
            System.exit(0);
        }

        if (src == openButton) {
            JOptionPane.showMessageDialog(this, "I do nothing, please fix me!", "Fix Me!",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (src == newButton) {
            JOptionPane.showMessageDialog(this, "I do nothing, please fix me!", "Fix Me!",
                    JOptionPane.ERROR_MESSAGE);
        }

        if (src == exportButton) {
            JOptionPane.showMessageDialog(this, "I do nothing, please fix me!", "Fix Me!",
                    JOptionPane.ERROR_MESSAGE);
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
        setTitle("Maze designer - Dashboard");
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        title = new JLabel("MazeCo");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.PLAIN, 24));

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

        // TODO Remove this when you get rid of the backgrounds.
//        southPanel.setOpaque(false);
        midPanel.setOpaque(false);

        southPanel.add(exitButton, BorderLayout.EAST);
        southPanel.add(exportButton, BorderLayout.WEST);
        midPanel.add(title);
        midPanel.add(Box.createVerticalGlue());
        midPanel.add(newButton);
        midPanel.add(Box.createVerticalGlue());
        midPanel.add(openButton);

        repaint();
//        pack();
        setVisible(true);
    }

    public JPanel createPanel() {
        JPanel myPanel = new JPanel();

        // TODO Remove colour once the layout is finished.
//        myPanel.setBackground(Color.BLACK);
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