package com.example.recommendation.invoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoker")
public class InvokerController {

    private final InvokerService invokerService;

    @Autowired
    public InvokerController(InvokerService invokerService) {
        this.invokerService = invokerService;
    }

    @GetMapping("/recommend")
    public List<Integer> recommend(@RequestParam String viewerId) {
        return invokerService.recommend(viewerId);
    }
}
