package com.nighthawk.spring_portfolio.mvc.selectionSort;

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
@RequestMapping("/api/select")
public class selectApiController {
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
    private SelectJPARepository repository;
    
    @GetMapping("/get")
    public ResponseEntity<List<SelectSort>> getValues() {
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
        for(int i = 0; i < list.length - 1; i++) {
			int min_index = i;	
			
			for(int j = i + 1; j < list.length; j++) {
				if(list[j] < list[min_index]) {
					min_index = j;
				}
			}
			
			int temp = list[min_index];
            list[min_index] = list[i];
            list[i] = temp;
        }
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        SelectSort selectSort = new SelectSort(a, secDiffTime);
        repository.save(selectSort);
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }
}
