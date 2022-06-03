package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.function.DoubleToIntFunction;

/**
 * Helper for opening image files
 */
public class OpenFileDialog extends JDialog implements ActionListener, Runnable {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 300;
    public static boolean saveFlag;
    private JButton getRowButton, closeButton;
    private JTable table = new JTable(data, columnNames);

    private static final String[] columnNames = {
                                    "Title",
                                    "Author",
                                    "Date Created",
                                    "Date Last Modified"};

    private static final Object[][] data = { // dummy data
            {"Kathy", "Smith", "Snowboarding", 5},
            {"John", "Doe", "Rowing", 3},
            {"Sue", "Black", "Knitting",2},
            {"Jane", "White", "Speed reading", 20},
            {"Joe", "Brown", "Pool", 10}
    };


    /**
     * Creates the dialog and assigns its parent content pane
     */
    public OpenFileDialog(JFrame parent, String title, boolean modality, boolean save) {
        super(parent, title, modality);
        saveFlag = save;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
    }

    @Override
    public void run() {
        createGUI();
    }

    public void createGUI() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getContentPane());

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only one item to be selected at a time
        table.setSize(new Dimension(WIDTH, (int)(HEIGHT * 0.7))); // TODO this might not do anything

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel southPanel = new JPanel(new BorderLayout());


        closeButton = createButton("Close");
        getRowButton = createButton("Open maze");

        Border border = BorderFactory.createLoweredSoftBevelBorder();

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.setBorder(border);
        southPanel.add(closeButton, BorderLayout.EAST);
        southPanel.add(getRowButton, BorderLayout.WEST);

        repaint();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton myButton = new JButton(text);
        Dimension buttonDim = new Dimension(90, 30);
        myButton.setPreferredSize(buttonDim);
        return myButton;
    }

    /**
     * Checks if the file is a valid image file.
     * @return - true if the file is an image file, false otherwise.
     */
    private boolean isImageFile() {
        return false;
    }

    /**
     * Gets the user selected file
     * @return - The file opened by the user.
     */
    public File getFile() {
        return null;
    }

}
