import raytracer.SceneFile;
import raytracer.jppf.ConcurrentJobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	private static final String USAGE = "Usage:\n"+
			"java -cp src Main infile bmpfile width height [-options]\n"+
			"\n"+
			"    where:\n"+
			"        infile    - input file name\n"+
			"        bmpfile   - bmp output file name\n"+
			"        width     - image width (in pixels)\n"+
			"        height    - image hreight (in pixels)\n"+
//			"        -test     - run in test mode (see below)\n"+
//			"        -noshadow - don't compute shadows\n"+
//			"        -noreflec - don't do reflections\n"+
//			"        -notrans  - don't do transparency\n"+
			"        -aa       - use anti-aliasing (~4x slower)\n"+
			"        -multi    - use multi-threading (good for large, anti-aliased images)";
//			"        -nocap    - cylinders and cones are infinite";

	public static boolean DEBUG = false;
	public static boolean ANTI_ALIAS = true;
	public static boolean MULTI_THREAD = true;


	private static void printUsage() {
		System.out.println(USAGE);
	}

	public static void main(String[] args) throws IOException {

		SceneFile s1 = new SceneFile(new File("test01.txt"), 1000,1000, new File("maiame1.bmp"));
		SceneFile s2 = new SceneFile(new File("test02.txt"), 1000,1000, new File("maiame2.bmp"));
		SceneFile s3 = new SceneFile(new File("test03.txt"), 1000,1000, new File("maiame3.bmp"));
		SceneFile s4 = new SceneFile(new File("test04.txt"), 1000,1000, new File("maiame4.bmp"));
		SceneFile s5 = new SceneFile(new File("test01.txt"), 1000,1000, new File("maiame5.bmp"));
/*		SceneFile s6 = new SceneFile(new File("test02.txt"), 1000,1000, new File("maiame6.bmp"));
		SceneFile s7 = new SceneFile(new File("test03.txt"), 1000,1000, new File("maiame7.bmp"));
		SceneFile s8 = new SceneFile(new File("test04.txt"), 1000,1000, new File("cortastetodalaloz1.bmp"));
		SceneFile s9 = new SceneFile(new File("test05.txt"), 1000,1000, new File("cortastetodalaloz2.bmp"));
		SceneFile s10 = new SceneFile(new File("test05.txt"), 1000,1000, new File("cortastetodalaloz3.bmp"));*/


		List<SceneFile> scenes = new ArrayList<>();
		scenes.add(s1);
		scenes.add(s2);
		scenes.add(s3);
		scenes.add(s4);
		scenes.add(s5);
/*		scenes.add(s6);
		scenes.add(s7);
		scenes.add(s8);
		scenes.add(s9);
		scenes.add(s10);*/

		ConcurrentJobs jobs = new ConcurrentJobs();

		try {
			jobs.multipleThreadsBlockingJobs(scenes);
			//jobs.singleThreadNonBlockingJobs(scenes);
			//jobs.asynchronousNonBlockingJobs(scenes);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
