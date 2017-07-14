package raytracer;

import java.io.File;
import java.io.Serializable;

/**
 * Created by maiameee on 11/07/17.
 */
public class SceneFile implements Serializable {
    private File path;
    private int rows;
    private int cols;
    private File outputPath;

    public SceneFile(File path, int rows, int cols, File outputPath) {
        this.path = path;
        this.rows = rows;
        this.cols = cols;
        this.outputPath = outputPath;
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
}
