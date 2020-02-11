package net.leirahjelle.natas;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

abstract public class NatasLevel {

    protected final ThreadPoolExecutor executor;

    public NatasLevel(int threads) {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    }

    abstract public String findPassword() throws InterruptedException, ExecutionException;

    protected String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }

    protected String runTasks(List<NatasTask> natasTasks) throws ExecutionException, InterruptedException {
        List<Future<String>> futures = new LinkedList<>();
        for (NatasTask natasTask : natasTasks) {
            futures.add(executor.submit(natasTask));
        }
        String nextChar = "";
        boolean isAllTasksFinished;
        do {
            isAllTasksFinished = true;
//            System.out.println("While top-loop, active executors: "+executor.getActiveCount());
            for (Future<String> future : futures) {

                if (future.isDone()) {
                    String result = future.get();
                    if (!result.isEmpty()) {
                        nextChar = result;
                        cancelRemainingTasks(futures);
                        break;
                    }
                }

                if (!future.isCancelled() && !future.isDone()) {
//                    System.out.println("All tasks not finished yet, active executors: "+executor.getActiveCount());
                    isAllTasksFinished = false;
                }
            }
            Thread.sleep(10);
        } while(nextChar.isEmpty() && !isAllTasksFinished);
        return nextChar;
    }

    private void cancelRemainingTasks(List<Future<String>> futures) {
//        System.out.println("Cancelling remaining tasks");
        for (Future<String> future : futures) {
            if (!future.isDone() && !future.isCancelled()) {
                future.cancel(true);
            }
        }
    }
}
