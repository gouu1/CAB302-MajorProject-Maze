package GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.Math;

import static GUI.DashForm.source;

/**
 * Form that allows users to edit and save mazes
 */
public class MazeEditForm extends JFrame implements ActionListener, Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 700;
    private static final int FONT_SIZE = 16;
    private String author;
    private JLabel drawIcon, eraseIcon, selectIcon;
    private final String titleString;
    private JPanel mainPanel;
    private JButton addLogoButton, returnButton, saveButton, saveAsButton, solveButton, setStartButton, setEndButton, exportButton;
    private JFileChooser fileChooser;
    private Maze maze;
    private Maze preSaveMaze;
    private JButton[][] mazeButtons;
    private final int[][] startingMaze;
    private final int mazeWidth, mazeHeight;
    private int showSolution = 0;
    private int cubeSize;
    private int[] startPoint = null;
    private int[] endPoint = null;
    private int setType = 0;
    private boolean randomCheck;
    private boolean childrensCheck;
    private final boolean preExisting;
    private ImageIcon[] Logos =  new ImageIcon[100];
    private ImageIcon[] ChildrensLogos = new ImageIcon[2];
    private int ImageIconCount = 0;
    private int AddLogo = 0;

    private ImageIcon blackSquare = createImageIcon("images/BlackSquare.png", "blackSquare");
    private ImageIcon greenSquare = createImageIcon("images/GreenSquare.png", "greenSquare");
    private ImageIcon whiteSquare = createImageIcon("images/WhiteSquare.png", "whiteSquare");
    private ImageIcon start = createImageIcon("images/Start.png", "Start");
    private ImageIcon end = createImageIcon("images/End.png", "End");

    /**
     * Constructor for use when opening a pre-existing maze from DB
     * @param mazeToOpen - maze from the database to open
     */
    public MazeEditForm(Maze mazeToOpen) { // TODO finish this
        preExisting = true;
        this.mazeHeight = mazeToOpen.getMaze().length;
        this.mazeWidth = mazeToOpen.getMaze()[0].length;
        titleString = getTitleString(mazeToOpen.getTitle());
        author = mazeToOpen.getAuthor();
        mazeButtons = new JButton[this.mazeWidth][this.mazeHeight];

        startingMaze = new int[this.mazeWidth][this.mazeHeight];
        for (int i = 0; i < this.mazeWidth; i++)
            for (int j = 0; j < this.mazeHeight; j++)
                startingMaze[i][j] = 2;

        maze = mazeToOpen;
        maze.setX(mazeWidth);
        maze.setY(mazeHeight);
        preSaveMaze = new Maze(maze.getTitle(), author); // save a copy of the maze to check if changes have been made

        setUpVisuals();
    }

    /**
     * Constructor for use when creating a brand-new maze
     * @param mazeName - name of the new maze
     * @param mazeSize - dimensions of the new maze
     */
    public MazeEditForm(String mazeName, Dimension mazeSize, boolean randomcheck, boolean childrenscheck) {
        this.mazeHeight = mazeSize.height;
        this.mazeWidth = mazeSize.width;
        this.randomCheck = randomcheck;
        this.childrensCheck = childrenscheck;
        preExisting = false;
        titleString = getTitleString(mazeName);
        mazeButtons = new JButton[this.mazeWidth][this.mazeHeight];

        while (author == null || author.isBlank()) {
            author = JOptionPane.showInputDialog(this, "Please add the author of this maze");
            if (author == null || author.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid author!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Blank maze
        startingMaze = new int[this.mazeWidth][this.mazeHeight];
        for (int i = 0; i < this.mazeWidth; i++)
            for (int j = 0; j < this.mazeHeight; j++)
                startingMaze[i][j] = 2;

        author = author.trim();
        maze = new Maze(mazeName, author);
        maze.setMaze(startingMaze);
        preSaveMaze = new Maze(mazeName, author); // save a copy of the maze to check if changes have been made

        setUpVisuals();
    }

    /**
     * Set-ups the maze scaling and gets the images for the buttons
     */
    private void setUpVisuals() {
        this.cubeSize = Math.round(640/((mazeWidth+mazeHeight)/2));
        /*
        if (mazeWidth <= 10 && mazeHeight <= 10) {
            this.cubeSize = 64;
        } else if (mazeWidth <= 20 && mazeHeight <= 20) {
            this.cubeSize = 32;
        } else if (mazeWidth <= 40 && mazeHeight <= 40) {
            this.cubeSize = 16;
        } else if (mazeWidth <= 80 && mazeHeight <= 80) {
            this.cubeSize = 8;
        } else if (mazeWidth <= 100 && mazeHeight <= 100) {
            this.cubeSize = 6;
        }
         */
        Image image = blackSquare.getImage();
        Image newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
        blackSquare = new ImageIcon(newimg);

        image = greenSquare.getImage();
        newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
        greenSquare = new ImageIcon(newimg);

        image = whiteSquare.getImage();
        newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
        whiteSquare = new ImageIcon(newimg);

        image = start.getImage();
        newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
        start = new ImageIcon(newimg);

        image = end.getImage();
        newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
        end = new ImageIcon(newimg);
    }

    private String getTitleString(String mazeName) {
        return "Maze Editor - " + mazeName + " (" + mazeWidth + ", " + mazeHeight + ")";
    }

    /**
     * Runs the main driver of the form, which is the createGUI() function
     */
    @Override
    public void run() {
        createGUI();
    }

    /**
     * Even listener that executes code based on the source of an event.
     * @param e - the event which has occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == returnButton) {
            if (!Objects.deepEquals(maze.getMaze(), preSaveMaze.getMaze())) {
                int selection = JOptionPane.showConfirmDialog(this, "Changes have been made, do you want to return without saving?");
                if (selection == JOptionPane.YES_OPTION) dispose();
            } else {
                dispose();
            }
        }

        if (src == saveButton) {
            saveMaze();
        }

        if (src == saveAsButton) {
            String newTitle = JOptionPane.showInputDialog("Please enter a new title");
            if (newTitle == null) {
                JOptionPane.showMessageDialog(this, "Save aborted!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                maze.setTitle(newTitle);
                setTitle(getTitleString(newTitle));
                saveMaze();
            }
        }

        if (src == addLogoButton) {
            int returnValue = fileChooser.showOpenDialog(getContentPane());
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(file);
                    System.out.println("Opening: " + file.getName());
                    System.out.println("Path: " + file.getAbsolutePath());
                    Logos[ImageIconCount] = new ImageIcon(bufferedImage);
                    System.out.println(Logos[ImageIconCount]);
                    ImageIconCount++;
                    AddLogo = 1;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
//
            } else {
                System.out.println("Open command cancelled by user.");
            }
        }

        if (src == setStartButton)
        {
            if (childrensCheck)
            {
                int returnValue = fileChooser.showOpenDialog(getContentPane());
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(file);
                        System.out.println("Opening: " + file.getName());
                        System.out.println("Path: " + file.getAbsolutePath());
                        ChildrensLogos[0] = new ImageIcon(bufferedImage);
                        System.out.println(ChildrensLogos[0]);
                        AddLogo = 1;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
//
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
            setType = 1;
            System.out.println("Set Start");
        }

        if (src == setEndButton)
        {
            if (childrensCheck)
            {
                int returnValue = fileChooser.showOpenDialog(getContentPane());
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(file);
                        System.out.println("Opening: " + file.getName());
                        System.out.println("Path: " + file.getAbsolutePath());
                        ChildrensLogos[1] = new ImageIcon(bufferedImage);
                        System.out.println(ChildrensLogos[1]);
                        AddLogo = 1;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
//
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
            setType = 2;
            System.out.println("Set End");
        }

        if (src == exportButton)
        {
            ExportMaze();
        }

        if (src == solveButton) {
            if(this.startPoint == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Please set a start point", "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if(this.endPoint == null)
            {
                JOptionPane.showMessageDialog(this,
                        "Please set an end point", "Error!",
                        JOptionPane.ERROR_MESSAGE);
            }
            else {
                //try{
                int[][] solvedMazeDisplay = maze.solveMaze(this.startPoint, this.endPoint);

                if (solvedMazeDisplay == null) {
                    System.out.println("Maze is unsolvable");

                    JOptionPane.showMessageDialog(this,
                            "The maze is unsolvable!", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    UpdateMazeDisplay(solvedMazeDisplay);
                }
            }
        }

        for (int i = 0; i < this.mazeWidth; i++)
        {
            for (int j = 0; j < this.mazeHeight; j++)
            {
                if (src == mazeButtons[i][j])
                {
                    if(setType == 0)
                    {
                        if (AddLogo == 1)
                        {
                            if (this.childrensCheck)
                            {
                                maze.maze[i][j] = 2;
                            }
                            else
                            {
                                Image image = Logos[ImageIconCount-1].getImage();
                                Image newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
                                Logos[ImageIconCount-1] = new ImageIcon(newimg);
                                mazeButtons[i][j].setIcon(Logos[ImageIconCount-1]);
                                AddLogo = 0;
                                maze.maze[i][j] = 0;
                            }

                        }
                        else
                        {
                            UpdateMazeBlock(mazeButtons[i][j],i,j);
                        }
                    }
                    else if(setType == 1)
                    {
                        if (this.startPoint != null)
                        {
                            mazeButtons[this.startPoint[0]][this.startPoint[1]].setIcon(whiteSquare);
                        }
                        this.startPoint = new int[] {i,j};
                        UpdateMazeBlock(mazeButtons[i][j],i,j);
                        setType = 0;
                    }
                    else if(setType == 2)
                    {
                        if (this.endPoint != null)
                        {
                            mazeButtons[this.endPoint[0]][this.endPoint[1]].setIcon(whiteSquare);
                        }
                        this.endPoint = new int[] {i,j};
                        UpdateMazeBlock(mazeButtons[i][j],i,j);
                        setType = 0;
                    }
                }
            }
        }
    }

    private void ExportMaze()
    {
        BufferedImage newImage = new BufferedImage(this.mazeWidth*this.cubeSize, this.mazeHeight*this.cubeSize,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        for (int i = 0; i < this.mazeWidth; i++)
        {
            for (int j = 0; j < this.mazeHeight; j++)
            {
                Icon icon = mazeButtons[i][j].getIcon();

                int w = icon.getIconWidth();
                int h = icon.getIconHeight();
                GraphicsEnvironment ge =
                        GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                GraphicsConfiguration gc = gd.getDefaultConfiguration();
                BufferedImage image = gc.createCompatibleImage(w, h);
                Graphics2D g = image.createGraphics();
                icon.paintIcon(null, g, 0, 0);
                g.dispose();

                g2.drawImage(image, null, i*this.cubeSize, j*this.cubeSize);
            }
        }
        g2.dispose();
        File file = new File("images/Test.jpg");
        try {
            ImageIO.write(newImage, "jpg", file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void saveMaze() {
        maze.setTimeEdited(LocalDateTime.now());
        Maze checkMaze = source.getMaze(maze.getTitle());
        if (checkMaze.getTitle().equals("na")) { // "na" means the maze does not exist
            source.addMaze(maze);
            preSaveMaze.setMaze(cloneMaze(maze.getMaze())); // Update the copy of the reference maze
            JOptionPane.showMessageDialog(this, "Saved successfully!","Success", JOptionPane.INFORMATION_MESSAGE);
        }
        else { // maze already exists
            if (checkMaze.getAuthor().equals(maze.getAuthor())) {
                source.updateMaze(maze);
                preSaveMaze.setMaze(cloneMaze(maze.getMaze()));
                JOptionPane.showMessageDialog(this, "Saved successfully!","Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cannot overwrite someone else's maze!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void UpdateMazeBlock(JButton Button,int i, int j)
    {
        if (maze.maze[i][j] == 0)
        {
            maze.maze[i][j] = 2;
            Button.setIcon(whiteSquare);
        }
        else
        {
            maze.maze[i][j] = 0;
            Button.setIcon(blackSquare);
        }
        if(setType == 2)
        {
            if (childrensCheck)
            {
                Image image = ChildrensLogos[1].getImage();
                Image newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
                ChildrensLogos[1] = new ImageIcon(newimg);
                Button.setIcon(ChildrensLogos[1]);
                AddLogo = 0;
            }
            else
            {
                Button.setIcon(end);
            }
            maze.maze[i][j] = 2;
        }
        if(setType == 1)
        {
            if (childrensCheck)
            {
                Image image = ChildrensLogos[0].getImage();
                Image newimg = image.getScaledInstance(this.cubeSize, this.cubeSize, java.awt.Image.SCALE_SMOOTH);
                ChildrensLogos[0] = new ImageIcon(newimg);
                Button.setIcon(ChildrensLogos[0]);
                AddLogo = 0;
            }
            else
            {
                Button.setIcon(start);
            }
            maze.maze[i][j] = 2;
        }
    }
    /**
     * Iterates through the maze and creates green path for maze cells that have been marked as
     * the solution of the maze.
     * @param solvedMazeDisplay - a solved maze
     */
    public void UpdateMazeDisplay(int[][] solvedMazeDisplay)
    {
        for (int i = 0; i < this.mazeWidth; i++) {
            for (int j = 0; j < this.mazeHeight; j++) {
                if (showSolution == 0) {
                    if (solvedMazeDisplay[i][j] == 1) {
                        mazeButtons[i][j].setIcon(greenSquare);
                    }
                }
                if (showSolution == 1) {
                    if (solvedMazeDisplay[i][j] != 0) {
                        mazeButtons[i][j].setIcon(whiteSquare);
                    }
                }
            }
        }
        if (childrensCheck)
        {
                mazeButtons[this.startPoint[0]][this.startPoint[1]].setIcon(ChildrensLogos[0]);
                mazeButtons[this.endPoint[0]][this.endPoint[1]].setIcon(ChildrensLogos[1]);
        }
        else
        {
            mazeButtons[this.startPoint[0]][this.startPoint[1]].setIcon(start);
            mazeButtons[this.endPoint[0]][this.endPoint[1]].setIcon(end);
        }

        if (showSolution == 0)
        {
            showSolution = 1;
        }
        else
        {
            showSolution = 0;
        }
    }

    /**
     * Runs the maze generation script and places the maze buttons on the editing panel
     */
    public void generateMaze()
    {
        if (!preExisting) { // run the random generator if the maze is new
            maze.MazeGenerator(this.startingMaze, this.mazeWidth, this.mazeHeight, randomCheck);
        }
        preSaveMaze.setMaze(cloneMaze(maze.getMaze()));

        System.out.println(maze.maze[2][2]);
        for (int i = 0; i < this.mazeWidth; i++)
        {
            for (int j = 0; j < this.mazeHeight; j++)
            {
                if (maze.maze[i][j] == 0 || maze.maze[i][j] == -1)
                {
                    mazeButtons[i][j] = new JButton(blackSquare);
                    mazeButtons[i][j].setBounds(cubeSize*i,cubeSize*j,cubeSize,cubeSize);
                }
                else
                {
                    mazeButtons[i][j] = new JButton(whiteSquare);
                    mazeButtons[i][j].setBounds(cubeSize*i,cubeSize*j,cubeSize,cubeSize);
                }
                mazeButtons[i][j].addActionListener(this);
                getContentPane().add(mazeButtons[i][j]);
            }
        }
    }

    public void createGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | InstantiationException |
                ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Initial setup for form
        setTitle(titleString);
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        generateMaze();

        fileChooser = makeFileChooser();

        // Make labels
        ImageIcon draw = createImageIcon("images/pencil.png", "Edit");
        ImageIcon erase = createImageIcon("images/eraser.png", "Erase");
        ImageIcon select = createImageIcon("images/select.png", "Select");

        JLabel titleLabel = new JLabel("Tools");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        drawIcon = new JLabel(draw);
        eraseIcon = new JLabel(erase);
        selectIcon = new JLabel(select);

        // Make separator
        JSeparator separatorTop = new JSeparator();

        // Make buttons
        addLogoButton = createButton("Add Logo");
        saveButton = createButton("Save");
        saveAsButton = createButton("Save As");
        returnButton = createButton("Return");
        solveButton = createButton("Solve");
        setStartButton = createButton("Set Start");
        setEndButton = createButton("Set End");
        exportButton = createButton("Export");

        // Setup border layout
        mainPanel = new JPanel();
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel toolsPanel = new JPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(toolsPanel, BorderLayout.EAST);

        // Setup tools layout
        GroupLayout toolsLayout = new GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsLayout);

        toolsLayout.setAutoCreateGaps(true);
        toolsLayout.setAutoCreateContainerGaps(true);

        toolsLayout.setHorizontalGroup(toolsLayout.createSequentialGroup()
                    .addComponent(drawIcon)
                    .addGroup(toolsLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addComponent(titleLabel)
                            .addComponent(eraseIcon)
                            .addComponent(addLogoButton)
                            .addComponent(solveButton)
                            .addComponent(setStartButton)
                            .addComponent(setEndButton)
                            .addComponent(separatorTop)
                            .addComponent(exportButton)
                            .addComponent(saveButton)
                            .addComponent(saveAsButton)
                            .addComponent(returnButton))
                    .addComponent(selectIcon)
        );
        toolsLayout.setVerticalGroup(toolsLayout.createSequentialGroup()
                    .addComponent(titleLabel)
                    .addGroup(toolsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(drawIcon)
                            .addComponent(eraseIcon)
                            .addComponent(selectIcon))
                    .addComponent(addLogoButton)
                    .addComponent(solveButton)
                    .addComponent(setStartButton)
                    .addComponent(setEndButton)
                    .addComponent(separatorTop)
                    .addComponent(exportButton)
                    .addComponent(saveButton)
                    .addComponent(saveAsButton)
                    .addComponent(returnButton)
        );

        toolsLayout.linkSize(SwingConstants.CENTER, eraseIcon, titleLabel);

        setVisible(true);
    }

    public JFileChooser makeFileChooser() {
        JFileChooser fc = new JFileChooser();
        Action details = fc.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);
        return fc;
    }

    private JButton createButton(String title) {
        JButton myButton = new JButton(title);
        myButton.addActionListener(this);
        return myButton;
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     *  Retrieved from https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Clones the 2d array representation of the maze
     * @param original - original array that is being cloned
     * @return - a cloned 2d array
     */
    private int[][] cloneMaze(int[][] original) {
        int[][] clone = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            clone[i] = original[i].clone();
        }
        return clone;
    }
}
