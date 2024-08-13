package com.example.recommendation.functionality;

import com.example.recommendation.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("/config")
    public Map<String, Object> getConfig(@RequestParam Map<String, String> params) {
        Map.Entry<String, Map<String, Object>> config = configService.configFromDict(params);
        Map<String, Object> response = new HashMap<>();
        response.put("dag_name", config.getKey());
        response.put("config", config.getValue());
        return response;
    }
}

