package GUI;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;

public class JDBCMazeDataSource {
    private final Connection connection;
    private PreparedStatement addMaze;
    private PreparedStatement getMaze;
    private PreparedStatement getAll;
    private PreparedStatement deleteMaze;
    private PreparedStatement countRows;
    private PreparedStatement updateMaze;

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " +
                    "mazes (idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," +
                    "mazeData VARCHAR(50000)," +
                    "title VARCHAR(30)," +
                    "author VARCHAR(30)," +
                    "dateLastModified VARCHAR(30)," +
                    "dateCreated VARCHAR(30)" + ");";

    private static final String INSERT_MAZE = "INSERT INTO mazes (mazeData, title, author, dateLastModified, dateCreated) VALUES (?, ?, ?, ?, ?);";
    private static final String GET_MAZE = "SELECT * FROM mazes WHERE title=?";
    private static final String GET_ALL = "SELECT title, author, dateLastModified, dateCreated FROM mazes";
    private static final String DELETE_MAZE = "DELETE FROM mazes WHERE title=?";
    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM mazes";
    private static final String UPDATE_MAZE = "UPDATE mazes SET mazeData=?, dateLastModified=? WHERE title=?";

    public JDBCMazeDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            addMaze = connection.prepareStatement(INSERT_MAZE);
            getMaze = connection.prepareStatement(GET_MAZE);
            getAll = connection.prepareStatement(GET_ALL);
            deleteMaze = connection.prepareStatement(DELETE_MAZE);
            countRows = connection.prepareStatement(COUNT_ROWS);
            updateMaze = connection.prepareStatement(UPDATE_MAZE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateMaze(Maze m) {
        try {
            updateMaze.setString(1, stringify(m.getMaze()));
            updateMaze.setString(2, m.getTimeEdited());
            updateMaze.setString(3, m.getTitle());
            updateMaze.execute();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void addMaze(Maze m) {
        try {
            addMaze.setString(1, stringify(m.getMaze()));
            addMaze.setString(2, m.getTitle());
            addMaze.setString(3, m.getAuthor());
            addMaze.setString(4, m.getTimeEdited());
            addMaze.setString(5, m.getTimeCreated());
            addMaze.executeUpdate();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public Maze getMaze(String title) {
        Maze maze = new Maze();
        ResultSet rs = null;
        try {
            getMaze.setString(1, title);
            rs = getMaze.executeQuery();
            if (rs.next()) {
                maze.setMaze((int[][]) deStringify(rs.getString("mazeData")));
                maze.setTitle(rs.getString("title"));
                maze.setAuthor(rs.getString("author"));
                maze.setTimeCreated(rs.getString("dateCreated"));
                maze.setTimeEdited(rs.getString("dateLastModified"));
            } else maze.setTitle("na");
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return maze;
    }

    public ArrayList<String[]> getMazeList() {
        ArrayList<String[]> list = new ArrayList<>();
        ResultSet rs = null;

        try {
            rs = getAll.executeQuery();
            while(rs.next()) {
                list.add(new String[] {
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Read the object from Base64 string.
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string*/
    private static Object deStringify(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string.
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string*/
    private static String stringify(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public void deleteMaze(String title) {
        try {
            deleteMaze.setString(1, title);
            deleteMaze.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countRows() {
        int rows = 0;
        ResultSet rs = null;

        try {
            rs = countRows.executeQuery();
            if (rs.next()) rows = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rows;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}