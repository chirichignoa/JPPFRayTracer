package raytracer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * Created by maiameee on 11/07/17.
 */
public class SceneFile implements Serializable {
    private File path;
    private int rows;
    private int cols;
    private File outputPath;
    private byte[] bytes;

    public SceneFile(File path, int rows, int cols, File outputPath) throws IOException{
        this.path = path;
        this.rows = rows;
        this.cols = cols;
        this.outputPath = outputPath;
        this.bytes = Files.readAllBytes(this.path.toPath());
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public File getOutputPath() {
        return outputPath;
    }

    public File getPath() {
        return path;
    }

    public byte[] getFileAsBytes() {
        return this.bytes;
    }
}
