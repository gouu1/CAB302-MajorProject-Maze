import GUI.DashForm;
import GUI.JDBCMazeDataSource;
import GUI.Maze;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
//        JDBCMazeDataSource source = new JDBCMazeDataSource();
//
//        Maze maze1 = new Maze("title", "author");
//
//        maze1.setMaze(new int[][] {
//                {1,1}
//        });
//        maze1.setTimeCreated(LocalDate.now());
//        maze1.setTimeEdited(LocalDateTime.now());
//        source.addMaze(maze1);
        SwingUtilities.invokeLater(new DashForm());
    }
}