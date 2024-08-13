package com.example.recommendation.generator;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/generator")
public class GeneratorController {

    @PostMapping("/generate")
    public Map<String, Object> generateRecommendation(@RequestParam String modelName, @RequestParam String viewerId) {
        Random random = new Random();
        int randomNumber = random.nextInt(100); // Генерация случайного числа

        Map<String, Object> response = new HashMap<>();
        response.put("reason", modelName);
        response.put("result", randomNumber);

        return response;
    }
}
