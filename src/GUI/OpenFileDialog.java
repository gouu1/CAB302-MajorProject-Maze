package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static GUI.DashForm.source;

/**
 * Helper for opening image files
 */
public class OpenFileDialog extends JDialog implements ActionListener, Runnable {
    public static final int WIDTH = 700;
    public static final int HEIGHT = 300;
    private final Mode mode;
    private JButton getRowButton, closeButton, exportButton;
    private final JTable table;
    private final String[] columnNames = {
                                    "Title",
                                    "Author",
                                    "Date Created",
                                    "Date Last Modified"};
    private Object[][] data;

    /**
     * Creates the dialog and assigns its parent content pane
     */
    public OpenFileDialog(JFrame parent, String title, boolean modality, Mode mode) {
        super(parent, title, modality);
        this.mode = mode;
        ArrayList<String[]> temp = source.getMazeList();

        data = new Object[temp.size()][columnNames.length];

        // Populates the data variable
        int i = 0;
        for (String[] sa : temp) {
            data[i++] = sa;
        }
        table = new JTable(data, columnNames);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == closeButton) {
            dispose();
        }

        if (src == getRowButton) {
            Maze maze = source.getMaze("title");
            source.addMaze(maze);
            System.out.println(maze.getTitle() + maze.getAuthor());
        }

        if (src == exportButton) {
            System.out.println("todo");
        }

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

        if (mode == Mode.OPEN) table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only one item to be selected at a time
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        JPanel southPanel = new JPanel(new BorderLayout());

        closeButton = createButton("Close");

        Border border = BorderFactory.createLoweredSoftBevelBorder();

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.setBorder(border);
        southPanel.add(closeButton, BorderLayout.EAST);
        southPanel.add(getSelection(), BorderLayout.WEST);

        repaint();
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton myButton = new JButton(text);
        Dimension buttonDim = new Dimension(100, 30);
        myButton.setPreferredSize(buttonDim);
        myButton.addActionListener(this);
        return myButton;
    }

    private JButton getSelection() {
        return switch (mode) {
            case OPEN -> getRowButton = createButton("Open maze");
            case EXPORT -> exportButton = createButton("Export mazes");
            default -> null;
        };
    }
}
