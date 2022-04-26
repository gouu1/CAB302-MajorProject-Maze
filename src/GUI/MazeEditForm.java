package GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MazeEditForm extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int FONT_SIZE = 16;
    private JLabel drawIcon; // TODO make these labels clickable
    private JLabel eraseIcon;
    private JLabel selectIcon;
    private final String titleString;
    private final String mazeName;
    private JPanel mainPanel;
    private JButton addLogoButton, returnButton, saveButton, saveAsButton;
    private JFileChooser fileChooser;


    public MazeEditForm(String mazeName, Dimension mazeSize) {
        titleString = "Maze Editor - " + mazeName + " (" + mazeSize.width + ", " + mazeSize.height + ")";
        this.mazeName = mazeName;
    }

    @Override
    public void run() {
        createGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == returnButton) {
            dispose(); // TODO check if any changes have been made?
        }

        if (src == saveButton) {
            // TODO make this overwrite the save on the current file
            System.out.println("Saving: " + mazeName + ".someFileEnding");
        }

        if (src == saveAsButton) {
            int returnValue = fileChooser.showSaveDialog(getContentPane()); // TODO make this try and save a new copy and auto name it
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println("Saving:" + file.getName()); // TODO make this actually save a file
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }

        if (src == addLogoButton) {
            int returnValue = fileChooser.showOpenDialog(getContentPane());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println("Opening:" + file.getName());
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }
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

        fileChooser = makeFileChooser();

        // Make labels
        ImageIcon draw = createImageIcon("images/pencil.png", "Edit");
        ImageIcon erase = createImageIcon("images/eraser.png", "Erase");
        ImageIcon select = createImageIcon("images/select.png", "Select");

        JLabel titleLabel = new JLabel("Tools");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        drawIcon = new JLabel(draw);
        eraseIcon = new JLabel(erase);
        selectIcon = new JLabel(select);

        // Make separator
        JSeparator separatorTop = new JSeparator();

        // Make buttons
        addLogoButton = createButton("Add Logo");
        saveButton = createButton("Save");
        saveAsButton = createButton("Save As");
        returnButton = createButton("Return");

        // Setup border layout
        mainPanel = new JPanel(); // TODO maybe add scroll pane??
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel toolsPanel = new JPanel();
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

    public JFileChooser makeFileChooser() {
        JFileChooser fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        return fc;
    }

    private JButton createButton(String title) {
        JButton myButton = new JButton(title);
        myButton.addActionListener(this);
        return myButton;
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