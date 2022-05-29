import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Currently a stub class for exporting maze files to images and sizing them correctly
 */
public class FileExporter {
    private File[] toBeExported;
    private BufferedImage[] readyForExport;
    private BufferedImage[] readyForExportSolutions;

    /**
     * Constructor for the FileExporter class, populations the file array of mazes to be exported
     */
    public FileExporter() {
    }

    /**
     * Sizes an image based on its width and height
     * @param width - maze width to be resized
     * @param height - maze height to be resized
     */
    private void imageSizer(int width, int height) {

    }

    /**
     * Converts from maze file to an in between Graphic, that can then be exported to an image
     * @param file - the maze file to be converted
     */
    private void convertToGraphics(File file) {

    }

    /**
     * Create solution images for corresponding mazes
     * @param file
     */
    private void generateSolutions(File file) {

    }

    /**
     * Takes the converted image mazes and saves them to the database
     */
    public void exportMazes() {

    }
}
