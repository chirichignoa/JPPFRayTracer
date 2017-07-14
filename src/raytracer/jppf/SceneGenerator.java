
package raytracer.jppf;

import org.jppf.node.protocol.AbstractTask;
import raytracer.RayTracer;
import raytracer.SceneFile;

import java.io.IOException;
import java.util.Scanner;


/**
 * A simple task used in the demo.
 */
public class SceneGenerator extends AbstractTask<String> {

  private SceneFile scene;

  public SceneGenerator(SceneFile scene) {
    this.scene = scene;
  }

  @Override
  public void run() {
    RayTracer rayTracer = new RayTracer(this.scene.getCols(), this.scene.getRows());

    try {
      rayTracer.readScene(this.scene.getPath());
      rayTracer.draw(scene.getOutputPath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
