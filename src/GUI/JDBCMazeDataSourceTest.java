package GUI;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JDBCMazeDataSourceTest {
    private Maze maze;
    private int count;
    JDBCMazeDataSource source = new JDBCMazeDataSource();
    DateTimeFormatter myDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter myDateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    @BeforeEach
    public void init() {
        int[][] testArr = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        maze = new Maze("testMaze", "Jamie");
        maze.setMaze(testArr);
        source.addMaze(maze);
        count = 1;
    }

    @AfterEach
    public void tearDown() {
        ArrayList<String[]> list = source.getMazeList();

        for (String[] sa : list) { // delete all mazes in db
            source.deleteMaze(sa[0]);
        }
    }

    @Test
    @Order(1)
    public void TestUpdateMaze() {
        int[][] newArr = {
                {1, 2, 3}
        };
        maze.setMaze(newArr); // update array
        source.updateMaze(maze); // send update to DB

        Maze newMaze = source.getMaze(maze.getTitle()); // get maze from db into new object

        assertAll("Maze fields",
                () -> assertArrayEquals(newArr, newMaze.getMaze()),
                () -> assertEquals(LocalDateTime.now().format(myDateTimeFormat), newMaze.getTimeEdited())
        );
    }

    @Test
    @Order(2)
    public void TestAddMaze() {
        source.addMaze(maze); // add this new maze

        assertEquals(count + 1, source.countRows()); // check if the number of records has gone up
    }

    @Test
    @Order(3)
    public void TestGetMaze() {
        Maze newMaze = source.getMaze(maze.getTitle()); // get default maze
        Maze noMaze = source.getMaze("nothing");

        assertAll("Maze fields",
                () -> assertEquals(maze.getTitle(), newMaze.getTitle()),
                () -> assertArrayEquals(maze.getMaze(), newMaze.getMaze()),
                () -> assertEquals(maze.getAuthor(), newMaze.getAuthor()),
                () -> assertEquals(maze.getTimeCreated(), newMaze.getTimeCreated()),
                () -> assertEquals(maze.getTimeEdited(), newMaze.getTimeEdited())
        );

        assertEquals("na", noMaze.getTitle()); // check the get failure returns the "na" title
    }

    @Test
    @Order(4)
    public void TestGetMazeList() {
        ArrayList<String[]> arrayList = new ArrayList<>();
        arrayList.add(
                new String[]{
                        maze.getTitle(),
                        maze.getAuthor(),
                        maze.getTimeEdited(),
                        maze.getTimeCreated()
                }
        );

        assertArrayEquals(arrayList.get(0), source.getMazeList().get(0)); // test 1 maze

        for (int j = 0; j < 10; j++) { // generate more mazes, add them to the db and to our list
            source.addMaze(new Maze(String.valueOf(j), String.valueOf(j)));
            arrayList.add(
                    new String[]{
                            String.valueOf(j),
                            String.valueOf(j),
                            LocalDateTime.now().format(myDateTimeFormat),
                            LocalDate.now().format(myDateFormat)
                    }
            );
        }

        int i = 0;
        for (String[] sa : arrayList) { // test 11 mazes
            assertArrayEquals(sa, source.getMazeList().get(i));
            i++;
        }
    }

    @Test
    @Order(5)
    public void TestDeleteMaze() {
        source.deleteMaze(maze.getTitle());

        assertEquals(count - 1, source.countRows());
        assertEquals("na", source.getMaze(maze.getTitle()).getTitle()); // check get maze returns appropriate value for deleted maze
    }

    @Test
    @Order(6)
    public void TestCountRows() {
        assertAll("count",
                () -> assertEquals(count, source.countRows()),
                () -> {
                    source.addMaze(new Maze("test", "test"));
                    count++;
                    assertEquals(count, source.countRows());
                },
                () -> {
                    for (int i = 0; i < 10; i++) {
                        count++;
                        source.addMaze(new Maze(String.valueOf(i), String.valueOf(i)));
                    }
                    assertEquals(count, source.countRows());
                }
        );
    }
}
