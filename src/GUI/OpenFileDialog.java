package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
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
    private JButton getRowButton, closeButton, deleteButton, exportButton;
    private final JTable table;
    private final DefaultTableModel model;
    private static final String[] COLUMN_NAMES = {
            "Title",
            "Author",
            "Date Last Modified",
            "Date Created"
    };

    /**
     * Creates the dialog and assigns its parent content pane
     */
    public OpenFileDialog(JFrame parent, String title, boolean modality, Mode mode) {
        super(parent, title, modality);
        this.mode = mode;

        model = new DefaultTableModel(initData(), COLUMN_NAMES);
        table = new JTable(model) {

            // Prevents cells from being editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
    }

    private Object[][] initData() {
        ArrayList<String[]> temp = source.getMazeList(); // gets the data from the database
        Object[][] data = new Object[temp.size()][COLUMN_NAMES.length];

        // Populates the data variable
        int i = 0;
        for (String[] sa : temp) {
            data[i++] = sa;
        }
        return data;
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

        if (src == deleteButton) {
            int[] rows = table.getSelectedRows();

            for (int i = 0; i < rows.length; i++) {
                source.deleteMaze((String) table.getValueAt(rows[i] - i, 0));
                model.removeRow(rows[i] - i);
            }
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
        JPanel southPanel = new JPanel(new FlowLayout());

        closeButton = createButton("Close");
        deleteButton = createButton("Delete Maze");

        Border border = BorderFactory.createLoweredSoftBevelBorder();

        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        southPanel.setBorder(border);
        southPanel.add(deleteButton);
        southPanel.add(closeButton);
        southPanel.add(getSelection());

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
