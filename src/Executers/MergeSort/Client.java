package Executers.MergeSort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> al = List.of(7, 3, 4, 1, 9, 8, 2, 6);
        ExecutorService executorService = Executors.newCachedThreadPool();
        MergeSorter merge = new MergeSorter(al,executorService);
        Future <List<Integer>> sortedListFuture = executorService.submit(merge);

        List<Integer> ans = sortedListFuture.get();

        System.out.println(ans);
        executorService.shutdown();
    }
}
