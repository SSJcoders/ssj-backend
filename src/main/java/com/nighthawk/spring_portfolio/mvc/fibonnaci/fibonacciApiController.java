package com.nighthawk.spring_portfolio.mvc.fibonnaci;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/fibo")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class fibonacciApiController {

    private int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }


    @PostMapping("/cal")
    public ResponseEntity<?> calculation(@RequestBody Map<String, Integer> requestBody) {
        Integer a = requestBody.get("number");
        double [] list = new double[a];
        if (a == null) {
            return new ResponseEntity<>("error", HttpStatus.CREATED);
        }
        long beforeTime = System.nanoTime();
        for (int i=0; i < a; i++) {
            double sqrt5 = Math.sqrt(5);
            double term1 = Math.pow(1 + sqrt5, i);
            double term2 = Math.pow(1 - sqrt5, i);

            double result = (term1 - term2) / sqrt5;
            list[i] = result;
        }
        
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }

    @PostMapping("/rec")
    public ResponseEntity<?> Recursion(@RequestBody Map<String, Integer> requestBody) {
        Integer a = requestBody.get("number");
        int [] list = new int[a];
        if (a == null) {
            return new ResponseEntity<>("error", HttpStatus.CREATED);
        }
        long beforeTime = System.nanoTime();
        for (int i=1; i <= a; i++) {
            int result = fibonacci(a);
            list[i] = result;
        }
        
        long afterTime = System.nanoTime();
        double secDiffTime = (afterTime - beforeTime) / 1_000_000.0;
        return new ResponseEntity<>(secDiffTime, HttpStatus.CREATED);
    }
}
