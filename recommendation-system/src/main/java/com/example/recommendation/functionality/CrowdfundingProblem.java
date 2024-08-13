package com.example.recommendation.functionality;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/crowdfunding")
public class CrowdfundingProblem {

    @GetMapping("/min-pledge")
    public ResponseEntity<Integer> findMinPledge(@RequestParam List<Integer> pledges) {
        int minPledge = findMinPledgeInternal(pledges);
        return ResponseEntity.ok(minPledge);
    }

    private int findMinPledgeInternal(List<Integer> pledges) {
        Set<Integer> pledgeSet = new HashSet<>(pledges);
        int minPledge = 1;
        while (pledgeSet.contains(minPledge)) {
            minPledge++;
        }
        return minPledge;
    }
}


