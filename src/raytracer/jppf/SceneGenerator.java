
package raytracer.jppf;

import org.jppf.node.protocol.AbstractTask;
import raytracer.RayTracer;
import raytracer.SceneFile;


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
      System.out.println("Llego una escena");
      rayTracer.readScene(this.scene.getFileAsBytes());
      System.out.println("Se leyo la escena");
      this.img = rayTracer.draw();
      System.out.println("Se proceso la escena");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
