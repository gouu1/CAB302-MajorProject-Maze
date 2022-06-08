import GUI.DashForm;
import GUI.JDBCMazeDataSource;
import GUI.Maze;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

//        JFrame jFrame = new JFrame();
//
//        final JFileChooser fc = new JFileChooser();
//
//        int returnval = fc.showOpenDialog(jFrame);
//
//        File fileP = null;
//        if (returnval == JFileChooser.APPROVE_OPTION) {
//            fileP = fc.getSelectedFile();
//        }
//
//        File file = new File("C:\\Users\\jamie\\Pictures\\wallhaven-dpw6wg.jpg");
//        BufferedImage bufferedImage = ImageIO.read(fileP);
//
//        ImageIcon imageIcon = new ImageIcon(bufferedImage);
//
//        jFrame.setLayout(new FlowLayout());
//
//        jFrame.setSize(500, 500);
//        JLabel jLabel = new JLabel();
//
//        jLabel.setIcon(imageIcon);
//        jFrame.add(jLabel);
//        jFrame.setVisible(true);
//
//        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new DashForm());
    }
}