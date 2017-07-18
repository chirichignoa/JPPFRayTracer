
package raytracer.jppf;

import org.jppf.node.protocol.AbstractTask;
import raytracer.RayTracer;
import raytracer.SceneFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;


/**
 * A simple task used in the demo.
 */
public class SceneGenerator extends AbstractTask<String> {

  private SceneFile scene;
  private byte[] img;

  public SceneGenerator(SceneFile scene) {
    this.scene = scene;
  }

  public String getOutputPath() {
    return this.scene.getOutputPath().getPath();
  }

  public byte[] getImageAsBytes() {
    return this.img;
  }

  @Override
  public void run() {
    RayTracer rayTracer = new RayTracer(this.scene.getCols(), this.scene.getRows());

    try {
      System.out.println("MAMAAAAAA");
      rayTracer.readScene(this.scene.getFileAsBytes());
      System.out.println("CORTASTEEE");
      this.img = rayTracer.draw(scene.getOutputPath());
      System.out.println("TODA LA LOOOOOZ");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
