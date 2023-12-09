package com.nighthawk.spring_portfolio.mvc.insertionSort;
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

import com.nighthawk.spring_portfolio.mvc.mergeSort.MergeJPARepository;
import com.nighthawk.spring_portfolio.mvc.mergeSort.MergeSort;

@RestController
@RequestMapping("/api/insert")
public class insertApiController {
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
        for(int i = 1; i < list.length; i++) {
			int target = list[i];
			int j = i - 1;
			while(j >= 0 && target < list[j]) {
				list[j + 1] = list[j];	
				j--;
			}
			list[j + 1] = target;	
		}
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        MergeSort mergeSort = new MergeSort(a, secDiffTime);
        repository.save(mergeSort);
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }
}
