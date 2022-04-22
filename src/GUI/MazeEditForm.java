package GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeEditForm extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int FONT_SIZE = 16;
    private JLabel titleLabel, drawIcon, eraseIcon, selectIcon;
    private JSeparator separatorTop;
    private final String titleString;
    private JPanel mainPanel, toolsPanel;
    private JButton addLogoButton, returnButton, saveButton, saveAsButton;


    public MazeEditForm(String mazeName, Dimension mazeSize) {
        titleString = "Maze Editor - " + mazeName + " (" + mazeSize.width + ", " + mazeSize.height + ")";
    }

    @Override
    public void run() {
        createGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void createGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Initial setup for form
        setTitle(titleString);
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        makeFileChooser();

        // Make labels
        ImageIcon draw = createImageIcon("images/pencil.png", "Edit");
        ImageIcon erase = createImageIcon("images/eraser.png", "Erase");
        ImageIcon select = createImageIcon("images/select.png", "Select");

        titleLabel = new JLabel("Tools");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        drawIcon = new JLabel(draw);
        eraseIcon = new JLabel(erase);
        selectIcon = new JLabel(select);

        // Make separator
        separatorTop = new JSeparator();

        // Make buttons
        addLogoButton = new JButton("Add Logo");
        saveButton = new JButton("Save");
        saveAsButton = new JButton("Save As");
        returnButton = new JButton("Return");

        // Setup border layout
        mainPanel = new JPanel(); // TODO maybe add scroll pane??
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        toolsPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(toolsPanel, BorderLayout.EAST);

        // Setup tools layout
        GroupLayout toolsLayout = new GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsLayout);

        toolsLayout.setAutoCreateGaps(true);
        toolsLayout.setAutoCreateContainerGaps(true);

        toolsLayout.setHorizontalGroup(toolsLayout.createSequentialGroup()
                    .addComponent(drawIcon)
                    .addGroup(toolsLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(titleLabel)
                            .addComponent(eraseIcon)
                            .addComponent(addLogoButton)
                            .addComponent(separatorTop)
                            .addComponent(saveButton)
                            .addComponent(saveAsButton)
                            .addComponent(returnButton))
                    .addComponent(selectIcon)
        );
        toolsLayout.setVerticalGroup(toolsLayout.createSequentialGroup()
                    .addComponent(titleLabel)
                    .addGroup(toolsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(drawIcon)
                            .addComponent(eraseIcon)
                            .addComponent(selectIcon))
                    .addComponent(addLogoButton)
                    .addComponent(separatorTop)
                    .addComponent(saveButton)
                    .addComponent(saveAsButton)
                    .addComponent(returnButton)
        );

        toolsLayout.linkSize(SwingConstants.CENTER, eraseIcon, titleLabel);

        setVisible(true);
    }

    public void makeFileChooser() {
        JFileChooser fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     *  Retrieved from https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
