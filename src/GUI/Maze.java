package GUI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Arrays;

/*
 * recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 */
public class Maze {
    private int x;
    private int y;
    private boolean Blank;
    private String timeCreated;
    private String timeEdited;
    private String title, author;
    public int[][] maze;
    public int totalcellspathed = 0;
    private final DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter myDateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Maze() {}

    public Maze(String title, String author) {
        this.title = title;
        this.author = author;
        timeCreated = LocalDate.now().format(myDateFormat);
        timeEdited = LocalDateTime.now().format(myDateTimeFormat);
    }

    /**
     * Sets the size of the maze in the X direction
     * @param x - The size of the maze in the X direction
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * Sets the size of the maze in the Y direction
     * @param y - The size of the maze in the Y direction
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the title of the maze
     * @return - The title of the maze
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the author of the maze
     * @return - The author of the maze
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the time when the maze was created as a string
     * @return - The time when the maze was created
     */
    public String getTimeCreated() {
        return timeCreated;
    }

    /**
     * Returns the time when the maze was last edited
     * @return - The time when the maze was last edited
     */
    public String getTimeEdited() {
        return timeEdited;
    }

    /**
     * Returns the two-dimensional array that contains the maze
     * @return - The maze
     */
    public int[][] getMaze() {
        return maze;
    }

    /**
     * Sets the title of the maze
     * @param title - The title of the maze
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeCreated(LocalDate timeCreated) {
        this.timeCreated = timeCreated.format(myDateFormat);
    }

    /**
     * Sets the time when the maze was created
     * @param timeCreated   - A string containing the time when the maze was created
     */
    // Overload of other function for use in DB
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }


    public void setTimeEdited(LocalDateTime timeEdited) {
        this.timeEdited = timeEdited.format(myDateTimeFormat);
    }

    /**
     * Sets the time when the maze was last edited
     * @param timeEdited - A string containing the time when the maze was last edited
     */
    // Overload of other function for use in DB
    public void setTimeEdited(String timeEdited) {
        this.timeEdited = timeEdited;
    }

    /**
     * Sets the author of the maze
     * @param author - The author of the maze
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the two-dimensional array that contains the maze itself
     * @param maze - A two-dimensional int array
     */
    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    /**
     * Generates a maze from a base map, this base map would indicate where the logos would be placed for the
     * maze to generate around
     * @param startMaze The base map used, dimensions (x,y)
     * @param x The x dimension of the maze
     * @param y The y dimension of the maze
     */
    public void MazeGenerator(int[][] startMaze, int x, int y, boolean blank) {
        this.x = x;
        this.y = y;
        this.Blank = blank;
        maze = new int[this.x][this.y];
        startUp(startMaze);
        if (blank)
        {
            generateMaze(0, 0);
        }

    }

    /**
     * Generates the grid used by the maze generation algorithm, indicates where paths should come from and go to
     * @param startMaze The base map used, dimensions (x,y)
     */
    private void startUp(int[][] startMaze)
    {
        for (int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                if (Blank) {
                    if (startMaze[j][i] != -1) {
                        if (i % 2 == 0 && j % 2 == 0) {
                            maze[j][i] = 1;
                        } else {
                            maze[j][i] = 0;
                        }
                    } else {
                        maze[j][i] = 0;
                    }
                }
                else
                {
                    maze[j][i] = 2;
                }
            }
        }
    }

    /**
     * Iterates over the grid map to fill it up with paths, once all paths are filled it stops iterating
     * @param cx The current x location
     * @param cy The current y location
     */
    private void generateMaze(int cx, int cy) {
        DIR[] dirs = DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (DIR dir : dirs) {
            int nx = cx + dir.dx*2;
            int ny = cy + dir.dy*2;

            if (between(nx, x) && between(ny, y) && (maze[nx][ny] == 1))
            {
                maze[nx][ny] = 2;
                maze[cx + dir.dx][cy + dir.dy] = 2;
                generateMaze(nx, ny);
            }
        }
    }

    /**
     * Iterates over the generated maze to produce a solution path
     * @param startPoint The start point to solve from
     * @param endPoint The end point to solve to
     * @return A solution map with indicates the path to take to solve the maze
     */
    public int[][] solveMaze(int[] startPoint, int[] endPoint) {
        int[][] solvedMaze = new int[this.x][this.y];
        int[][] solvedMazeDisplay = new int[this.x][this.y];
        int Max_Iterations = this.x*this.y;

        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++) {
                solvedMaze[i][j] = -maze[i][j];
                solvedMazeDisplay[i][j] = -maze[i][j];
            }
        }

        solvedMaze[startPoint[0]][startPoint[1]] = 1;

        while(solvedMaze[endPoint[0]][endPoint[1]] == -2 && Max_Iterations > 0)
        {
            for (int i = 0; i < x; i++)
            {
                for (int j = 0; j < y; j++)
                {
                    if (solvedMaze[i][j] == -2)
                    {
                        int max = 0;
                        if (i > 0)
                        {
                            if (max < solvedMaze[i-1][j])
                            {
                                max = solvedMaze[i-1][j];
                            }
                        }
                        if (i < x-1)
                        {
                            if (max < solvedMaze[i+1][j])
                            {
                                max = solvedMaze[i+1][j];
                            }
                        }
                        if (j > 0)
                        {
                            if (max < solvedMaze[i][j-1])
                            {
                                max = solvedMaze[i][j-1];
                            }
                        }
                        if (j < y-1)
                        {
                            if (max < solvedMaze[i][j+1])
                            {
                                max = solvedMaze[i][j+1];
                            }
                        }
                        if (max > 0) {
                            solvedMaze[i][j] = max + 1;
                        }
                    }
                }
            }
            Max_Iterations = Max_Iterations -1;
        }

        if (Max_Iterations > 0) {
            this.totalcellspathed = solvedMaze[endPoint[0]][endPoint[1]];

            int[] nextPoint = {endPoint[0], endPoint[1]};
            int choice = 0;
            for (int i = 0; i < solvedMaze[endPoint[0]][endPoint[1]]; i++) {
                choice = 0;
                solvedMazeDisplay[nextPoint[0]][nextPoint[1]] = 1;
                if (nextPoint[0] > 0 && choice == 0) {
                    if (solvedMaze[nextPoint[0] - 1][nextPoint[1]] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0] - 1][nextPoint[1]] > 0) {
                        nextPoint[0] = nextPoint[0] - 1;
                        choice = 1;
                    }
                }
                if (nextPoint[0] < x - 1 && choice == 0) {
                    if (solvedMaze[nextPoint[0] + 1][nextPoint[1]] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0] + 1][nextPoint[1]] > 0) {
                        nextPoint[0] = nextPoint[0] + 1;
                        choice = 1;
                    }
                }
                if (nextPoint[1] > 0 && choice == 0) {
                    if (solvedMaze[nextPoint[0]][nextPoint[1] - 1] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]][nextPoint[1] - 1] > 0) {
                        nextPoint[1] = nextPoint[1] - 1;
                        choice = 1;
                    }
                }
                if (nextPoint[1] < y - 1 && choice == 0) {
                    if (solvedMaze[nextPoint[0]][nextPoint[1] + 1] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]][nextPoint[1] + 1] > 0) {
                        nextPoint[1] = nextPoint[1] + 1;
                    }
                }
            }
            return solvedMazeDisplay;
        }
        else
        {
            return null;
        }
    }

    /**
     * Indicates whether a value is between 0 and some upper value
     * @param v - value to be checked
     * @param upper - upper bound of the check (exclusive)
     * @return - true is v is between 0 and upper, false otherwise.
     */
    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    /**
     * Enum describing directions
     */
    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        private final int bit;
        private final int dx;
        private final int dy;
        private DIR opposite;

        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    }
}
