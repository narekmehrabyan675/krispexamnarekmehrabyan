package com.example.recommendation.invoker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@ComponentScan
public class InvokerService {

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ConcurrentMap<String, List<Integer>> localCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cacheCleaner = Executors.newScheduledThreadPool(1);

    @Autowired
    public InvokerService(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        startCacheCleaner();
    }

    public List<Integer> recommend(String viewerId) {
        if (localCache.containsKey(viewerId)) {
            return localCache.get(viewerId);
        }

        List<Integer> recommendations = (List<Integer>) redisTemplate.opsForValue().get(viewerId);
        if (recommendations != null) {
            localCache.put(viewerId, recommendations);
            return recommendations;
        }

        recommendations = runcascade();
        redisTemplate.opsForValue().set(viewerId, recommendations);
        localCache.put(viewerId, recommendations);

        return recommendations;
    }

    public List<Integer> runcascade() {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<Map<String, Object>>> futures = List.of("Model1", "Model2", "Model3", "Model4", "Model5").stream()
                .map(model -> executor.submit(() -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> response = restTemplate.postForObject(
                            "http://localhost:8081/generator/generate?modelName=" + model + "&viewerId=123",
                            null,
                            (Class<Map<String, Object>>) (Class<?>) Map.class);
                    return response;
                }))
                .collect(Collectors.toList());

        return futures.stream()
                .map(future -> {
                    try {
                        return (Integer) future.get().get("result");
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private void startCacheCleaner() {
        cacheCleaner.scheduleAtFixedRate(() -> {
            if (localCache.size() > 3) {
                localCache.clear(); // Clear cache if it contains more than 3 keys
            }
        }, 10, 10, TimeUnit.SECONDS); // TTL 10 seconds
    }
}
