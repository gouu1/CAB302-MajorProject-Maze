package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import static GUI.DashForm.source;

/**
 * Handles interacting with database
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
            }
        };
    }

    /**
     * Parses the data from the database in a way that can be used by the table component
     * @return Object[][] that contains all of the current
     */
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

    /**
     * Event listener for the Open file dialog window
     * @param e - the event which has occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == closeButton) {
            dispose();
        }

        if (src == getRowButton) {
            int row = table.getSelectedRow();
            Maze maze = source.getMaze((String) table.getValueAt(row, 0));
            SwingUtilities.invokeLater(new MazeEditForm(maze));
            dispose();
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

    /**
     * Creates button with given text and dimensions and adds listener.
     * @param text - text to be displayed on button
     * @return - Button element
     */
    private JButton createButton(String text) {
        JButton myButton = new JButton(text);
        Dimension buttonDim = new Dimension(100, 30);
        myButton.setPreferredSize(buttonDim);
        myButton.addActionListener(this);
        return myButton;
    }

    /**
     * Picks which button to display on the dialog window depending on the mode it was opened in.
     * @return - A button element to either Open mazes, or export them
     */
    private JButton getSelection() {
        return switch (mode) {
            case OPEN -> getRowButton = createButton("Open maze");
            case EXPORT -> exportButton = createButton("Export mazes");
            default -> null;
        };
    }
}
