package GUI;

import java.util.Collections;
import java.util.Arrays;

/*
 * recursive backtracking algorithm
 * adapted from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 */
public class Maze {

    private int x;
    private int y;
    public int[][] maze;

    /**
     *
     * This is the function called to generate new mazes.
     * @param startMaze The base maze provided to the maze generator. This would include areas blocked out by logos
     * @param x The size of the maze in the x axis
     * @param y The size of the maze in the y axis
     */
    public void MazeGenerator(int[][] startMaze, int x, int y) {
        this.x = x;
        this.y = y;
        maze = new int[this.x][this.y];
        startUp(startMaze);
        generateMaze(0, 0);
    }

    /**
     * This function produces the maze grid from an existing base maze
     * @param startMaze The base maze provided to the maze generator. This would include areas blocked out by logos
     */
    private void startUp(int[][] startMaze)
    {
        for (int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                if (startMaze[j][i] != -1)
                {
                    if (i % 2 == 0 && j% 2 == 0)
                    {
                        maze[j][i] = 1;
                    }
                    else
                    {
                        maze[j][i] = 0;
                    }
                }
                else
                {
                    maze[j][i] = 0;
                }
            }
        }
    }

    /**
     * This function iterates over the maze grid produced in the startUp method and connects the paths together
     * @param cx The current x position used
     * @param cy The current y position used
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
     * This function iterates over a generated maze and creates a solution path
     * @param startPoint The starting point to solve from
     * @param endPoint The ending point to solve to
     * @return This function returns a solved maze path in the same dimensions as the input maze
     */
    public int[][] solveMaze(int[] startPoint, int[] endPoint) {
        int[][] solvedMaze = new int[this.x][this.y];
        int[][] solvedMazeDisplay = new int[this.x][this.y];

        for (int i = 0; i < x; i++)
        {
            for (int j = 0; j < y; j++) {
                solvedMaze[i][j] = -maze[i][j];
                solvedMazeDisplay[i][j] = -maze[i][j];
            }
        }

        solvedMaze[startPoint[0]][startPoint[1]] = 1;

        while(solvedMaze[endPoint[0]][endPoint[1]] == -2)
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
        }

        System.out.println(solvedMaze[endPoint[0]][endPoint[1]]);

        int[] nextPoint = {endPoint[0], endPoint[1]};
        int choice = 0;
        for (int i = 0; i < solvedMaze[endPoint[0]][endPoint[1]]; i++)
        {
            choice = 0;
            solvedMazeDisplay[nextPoint[0]][nextPoint[1]] = 1;
            if (nextPoint[0] > 0 && choice == 0)
            {
                if (solvedMaze[nextPoint[0]-1][nextPoint[1]] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]-1][nextPoint[1]] > 0)
                {
                    nextPoint[0] = nextPoint[0]-1;
                    choice = 1;
                }
            }
            if (nextPoint[0] < x-1 && choice == 0)
            {
                if (solvedMaze[nextPoint[0]+1][nextPoint[1]] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]+1][nextPoint[1]] > 0)
                {
                    nextPoint[0] = nextPoint[0]+1;
                    choice = 1;
                }
            }
            if (nextPoint[1] > 0 && choice == 0)
            {
                if (solvedMaze[nextPoint[0]][nextPoint[1]-1] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]][nextPoint[1]-1] > 0)
                {
                    nextPoint[1] = nextPoint[1]-1;
                    choice = 1;
                }
            }
            if (nextPoint[1] < y-1 && choice == 0)
            {
                if (solvedMaze[nextPoint[0]][nextPoint[1]+1] < solvedMaze[nextPoint[0]][nextPoint[1]] && solvedMaze[nextPoint[0]][nextPoint[1]+1] > 0)
                {
                    nextPoint[1] = nextPoint[1]+1;
                }
            }
        }
        return solvedMazeDisplay;
    }

    /**
     * This function checks if the given point is on a border or not
     * @param v The current point
     * @param upper The upper boundary
     * @return True if it is not on a border and false if it is
     */
    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

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
