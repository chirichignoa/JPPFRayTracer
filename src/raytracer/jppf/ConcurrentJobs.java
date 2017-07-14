/*
 * JPPF.
 * Copyright (C) 2005-2015 JPPF Team.
 * http://www.jppf.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package raytracer.jppf;

import java.util.*;
import java.util.concurrent.*;

import javafx.scene.Scene;
import org.jppf.client.*;
import org.jppf.client.event.*;
import org.jppf.node.protocol.Task;
import org.jppf.utils.ExceptionUtils;
import raytracer.SceneFile;

public class ConcurrentJobs {


  /**
   * N threads, trabajos bloqueantes.
   * @throws Exception if any error occurs.
   */
  public void multipleThreadsBlockingJobs(List<SceneFile> scenes) throws Exception {
    int nbJobs = 1;
    ExecutorService executor = Executors.newFixedThreadPool(nbJobs);
    try (JPPFClient jppfClient = new JPPFClient()) {
      // make sure the client has enough connections
      ensureSufficientConnections(jppfClient, nbJobs);
      List<Future<JPPFJob>> futures = new ArrayList<>(nbJobs);
      // delegate the job submissions to separate threads
      for (int i=1; i<=nbJobs; i++) {
        JPPFJob job = createJob("multipleThreadsBlockingJob " + i, scenes);
        futures.add(executor.submit(new MyCallable(jppfClient, job)));
      }
      for (Future<JPPFJob> future: futures) {
        try {
          JPPFJob job = future.get();
          // process the job results
          processResults(job);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } finally {
      executor.shutdown();
    }
  }

  /**
   * Submit multiple non-blocking jobs from a single thread, process the results in sequence.
   * @throws Exception if any error occurs.
   */
  public void singleThreadNonBlockingJobs(List<SceneFile> scenes) throws Exception {
    int nbJobs = 1;
    try (final JPPFClient jppfClient = new JPPFClient()) {
      // make sure the client has enough connections
      ensureSufficientConnections(jppfClient, nbJobs);
      List<JPPFJob> jobs = new ArrayList<>(nbJobs);
      for (int i=0; i<nbJobs; i++) {
        // create the job and its tasks
        JPPFJob job = createJob("singleThreadNonBlockingJob " + i, scenes);
        job.setBlocking(false);
        jobs.add(job);
        jppfClient.submitJob(job);
      }
      for (JPPFJob job: jobs) {
        job.awaitResults();
        // process the job results
        processResults(job);
      }
    }
  }

  /**
   * 1 thread, N trabajos no bloqueantes.
   * @throws Exception if any error occurs.
   */
  public void asynchronousNonBlockingJobs(List<SceneFile> scenes) throws Exception {
    int nbJobs = 1;
    try (final JPPFClient jppfClient = new JPPFClient()) {
      // make sure the client has enough connections
      ensureSufficientConnections(jppfClient, nbJobs);
      // synchronization helper that tells us when all jobs have completed
      final CountDownLatch countDown = new CountDownLatch(nbJobs);
      for (int i=1; i<=nbJobs; i++) {
        JPPFJob job = createJob("asynchronousNonBlockingJob " + i, scenes);
        job.setBlocking(false);
        // results will be processed asynchronously within
        // the job listener's jobEnded() notifications
        job.addJobListener(new JobListenerAdapter() {
          @Override
          public synchronized void jobEnded(final JobEvent event) {
            // ... process the job results ...
            processResults(event.getJob());
            // decrease the jobs count down: when the count reaches 0, countDown.await() will exit immediately
            countDown.countDown();
          }
        });
        // submit the job
        jppfClient.submitJob(job);
      }
      // wait until all jobs are complete
      // i.e. until the count down reaches 0
      countDown.await();
    }
  }

  /**
   * Create a job with the specified name, number of tasks and duration for each task.
   * @param jobName the name to assign to the job.
   * @return the created job.
   */
  public static JPPFJob createJob(final String jobName, List<SceneFile> scenes) {
    JPPFJob job = new JPPFJob();
    // set the job name
    job.setName(jobName);
    int i = 0;
    for (SceneFile scene: scenes) {
      // create a new task
      SceneGenerator task = new SceneGenerator(scene);
      try {
        // add the task to the job and give it a readable id
        job.add(task).setId(jobName + " - " + "task " + i);
      } catch (Exception e) {
        e.printStackTrace();
      }
      i++;
    }
    return job;
  }

  /**
   * Process the results of a job.
   * @param job the JPPF job whose results are printed.
   */
  public static void processResults(final JPPFJob job) {
    System.out.printf("*** results for job '%s' ***%n", job.getName());
    List<Task<?>> results = job.getAllResults();
    for (Task<?> task: results) {
      if (task.getThrowable() != null) { // if the task execution raised an exception
        System.out.printf("%s raised an exception : %s%n", task.getId(), ExceptionUtils.getMessage(task.getThrowable()));
      } else { // otherwise display the task result
        System.out.printf("result of %s : %s%n", task.getId(), task.getResult());
      }
    }
  }

  /**
   * Ensure that the JPPF client has the specified number of connections.
   * @param jppfClient the jppf client.
   * @param nbConnections the desried number of connections.
   * @throws Exception if any error occurs.
   */
  private static void ensureSufficientConnections(final JPPFClient jppfClient, final int nbConnections) throws Exception {
    // wait until a connection pool is available
    JPPFConnectionPool pool = jppfClient.awaitActiveConnectionPool();
    // make sure the pool has enough connections and wait until all connections are active
    pool.awaitActiveConnections(Operator.AT_LEAST, 1);
    // alternatively with a single method call: wait until there is a connection pool with at least <nbConnections> active connections, for as long as it takes
    //jppfClient.awaitConnectionPools(Operator.AT_LEAST, nbConnections, Long.MAX_VALUE, JPPFClientConnectionStatus.ACTIVE);
  }
}
