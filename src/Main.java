import raytracer.RayTracer;
import raytracer.SceneFile;
import raytracer.jppf.ConcurrentJobs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {

		ArrayList<File> filesList = new ArrayList<>();

		filesList.add(new File("test01.txt"));
		filesList.add(new File("test02.txt"));
		filesList.add(new File("test03.txt"));
		filesList.add(new File("test04.txt"));
		filesList.add(new File("test01.txt"));

		List<SceneFile> scenesList = new ArrayList<>();
		for(int i = 0; i < filesList.size(); i++){
			scenesList.add(new SceneFile(new File("test01.txt"), 1000,1000, new File("maiame" + i + ".bmp")));
		}

		ConcurrentJobs jobs = new ConcurrentJobs();

		try {
			//Sequential
			sequentialProcessing(filesList,false);
			//Multithreaded
			sequentialProcessing(filesList,true);
			//Distributed - Multithreaded
			jobs.multipleThreadsBlockingJobs(scenesList);
			jobs.singleThreadNonBlockingJobs(scenesList);
			jobs.asynchronousNonBlockingJobs(scenesList);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private static void sequentialProcessing(ArrayList<File> files, boolean multi) throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		for(int j =0; j < 4; j++) {
			for (int i = 0; i < files.size(); i++) {
				RayTracer rayTracer = new RayTracer(1000,1000, multi);
				rayTracer.readScene(files.get(i));
				rayTracer.drawLocally(new File("maiame" + i + ".bmp"));
			}
		}
		if(multi){
            System.out.println("multithreadingProcessing: " + ((System.currentTimeMillis() - startTime)* 1E-3));
        }else{
            System.out.println("sequentialProcessing: " + ((System.currentTimeMillis() - startTime)* 1E-3));
        }
	}
}
