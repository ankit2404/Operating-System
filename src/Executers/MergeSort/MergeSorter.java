package Executers.MergeSort;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MergeSorter implements Callable<List<Integer>> {
    private List<Integer> arrayToSort;
    private  ExecutorService executorService;
    MergeSorter(List<Integer> al, ExecutorService executorService){
        this.arrayToSort = al;
        this.executorService = executorService;
    }
    @Override
    public List<Integer> call() throws ExecutionException, InterruptedException {
        if(arrayToSort.size() <= 1){
            return arrayToSort;
        }
        int mid = arrayToSort.size()/2;
        List<Integer> leftArray = new ArrayList<>();
        for(int i =0; i < mid; i++){
            leftArray.add(arrayToSort.get(i));
        }
        List<Integer> rightArray = new ArrayList<>();
        for(int i =mid; i < arrayToSort.size(); i++){
            rightArray.add(arrayToSort.get(i));
        }
        MergeSorter leftSorter = new MergeSorter(leftArray,executorService);
        MergeSorter rightSorter = new MergeSorter(rightArray,executorService);

//        ExecutorService executorService = Executors.newCachedThreadPool();

        Future <List<Integer>> leftSortedArrayFuture = executorService.submit(leftSorter);
        Future <List<Integer>> rightSortedArrayFuture = executorService.submit(rightSorter);

//        code will not go to next line
        List<Integer> leftSortedArray = leftSortedArrayFuture.get();
        List<Integer> rightSortedArray = rightSortedArrayFuture.get();

        List<Integer> ans = new ArrayList<>();
        int i = 0;
        int j = 0;
        while(i< leftSortedArray.size() && j < rightSortedArray.size()){
            if(leftSortedArray.get(i) <= rightSortedArray.get(j)){
                ans.add(leftSortedArray.get(i));
                i++;
            }else{
                ans.add(rightSortedArray.get(j));
                j++;
            }
        }
        while(i< leftSortedArray.size()){
                ans.add(leftSortedArray.get(i));
                i++;
        }
        while(j < rightSortedArray.size()){
                ans.add(rightSortedArray.get(j));
                j++;
        }
        return ans;
    }

}
