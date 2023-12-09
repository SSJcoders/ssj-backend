package com.nighthawk.spring_portfolio.mvc.mergeSort;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/merge")
public class mergeApiController {

    public static void shuffle(int[] array, int count){
		int temp, temp2, randomNum1, randomNum2;
		
		for(int i=0; i<count; i++){
			randomNum1 = (int)(Math.random()*array.length);
			temp = array[randomNum1];
			randomNum2 = (int)((Math.random()*array.length));
			temp2 = array[randomNum2];
			array[randomNum1] = temp2;
			array[randomNum2] = temp;
		}
	}

    public static void sort(int[] arr, int left, int right) {
        mergeSort(arr, left, right);
    }

    private static void mergeSort(int[] arr, int left, int right) {
        int mid = 0;
        if (left < right) {
            mid = (left + right) / 2; 
            mergeSort(arr, left, mid); 
            mergeSort(arr, mid + 1, right); 
            merge(arr, left, mid, right); 
        }
    }

    private static void merge(int[] arr, int left, int mid, int right) {
        int leftIndex = left;
        int rightIndex = mid + 1;
        int sortedIndex = left;
        int[] tmpSortedArray = new int[right + 1];
        while(leftIndex <= mid && rightIndex <= right) {
            if (arr[leftIndex] <= arr[rightIndex]) {
                tmpSortedArray[sortedIndex++] = arr[leftIndex++];
            }
            else {
                tmpSortedArray[sortedIndex++] = arr[rightIndex++];
            }
        }
        if (leftIndex > mid) {
            for(int i=rightIndex; i<=right; i++) {
                tmpSortedArray[sortedIndex++] = arr[i];
            }
        }
        else {
            for(int i=leftIndex; i<=mid; i++) {
                tmpSortedArray[sortedIndex++] = arr[i];
            }
        }
        for(int i=left; i<=right; i++) {
            arr[i] = tmpSortedArray[i];
        }
    }

    @Autowired
    private MergeJPARepository repository;
    
    @GetMapping("/get")
    public ResponseEntity<List<MergeSort>> getValues() {
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<?> postNumber(@RequestBody Map<String, Integer> requestBody) {
        Integer a = requestBody.get("number");
        if (a == null) {
            return new ResponseEntity<>("error", HttpStatus.CREATED);
        }
        int[] list = new int[a];
        for (int i = 1; i <= a; i++) {
            list[i - 1] = i;
        }
        shuffle(list, list.length);
        long beforeTime = System.nanoTime();
        sort(list, 0, list.length - 1);
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        MergeSort mergeSort = new MergeSort(a, secDiffTime);
        repository.save(mergeSort);
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }
}
