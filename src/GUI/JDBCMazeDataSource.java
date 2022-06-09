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

    /**
     * Initialises the connection to the database and allows data flow to and from the DB
     */
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

    /**
     * Updates a maze records given the maze object
     * @param m - maze to be updated
     */
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

    /**
     * Creates a new maze record given a maze object
     * @param m - maze to be created
     */
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

    /**
     * Gets a maze given the title
     * @param title - title to be searched
     * @return - Maze with title given or "na" if maze is not found
     */
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

    /**
     * Generates a list of all the entries in the DB
     * @return - An arraylist of all the mazes in the DB in string form
     */
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

    /**
     * Takes a serialised object and returns the original format.
     *
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string */
    private static Object deStringify(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Serialises an object and encodes that serialisation into a string.
     *
     * https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string */
    private static String stringify(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * Removes maze from the DB
     * @param title - title of maze to be deleted
     */
    public void deleteMaze(String title) {
        try {
            deleteMaze.setString(1, title);
            deleteMaze.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the number of rows in the DB
     * @return - The number of rows
     */
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

    /**
     * Closes the connection to the DB
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}