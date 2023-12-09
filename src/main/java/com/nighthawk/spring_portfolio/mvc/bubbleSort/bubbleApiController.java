package com.nighthawk.spring_portfolio.mvc.bubbleSort;

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
@RequestMapping("/api/bubble")
public class bubbleApiController {

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
    private BubbleJPARepository repository;
    
    @GetMapping("/get")
    public ResponseEntity<List<BubbleSort>> getValues() {
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
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = 0; j < list.length - i - 1; j++) {
                if (list[j] > list[j + 1]) {
                    int temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        BubbleSort bubbleSort = new BubbleSort(a, secDiffTime);
        repository.save(bubbleSort);
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }
}
