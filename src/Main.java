import GUI.DashForm;
import GUI.JDBCMazeDataSource;
import GUI.Maze;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new DashForm());
    }
}