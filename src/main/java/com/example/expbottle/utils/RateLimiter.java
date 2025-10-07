package com.example.expbottle.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RateLimiter {
    
    private final ConcurrentMap<String, Long> lastActionTime = new ConcurrentHashMap<>();
    private final long cooldownMs;
    private final ScheduledExecutorService cleanupExecutor;
    private final AtomicBoolean isShutdown = new AtomicBoolean(false);
    private static final int MAX_ENTRIES = 10000; // Prevent memory explosion
    
    public RateLimiter(long cooldownMs) {
        this.cooldownMs = cooldownMs;
        
        // Create dedicated thread pool for cleanup - prevent blocking main thread
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RateLimiter-Cleanup");
            t.setDaemon(true); // Don't prevent JVM shutdown
            return t;
        });
        
        // Schedule automatic cleanup every 5 minutes
        this.cleanupExecutor.scheduleWithFixedDelay(
            this::cleanup, 
            5, 5, 
            TimeUnit.MINUTES
        );
    }
    
    /**
     * Kiểm tra xem action có bị rate limit không
     * @param key Unique key cho action (ví dụ: player UUID)
     * @return true nếu action được phép, false nếu bị rate limit
     */
    public boolean canPerformAction(String key) {
        if (key == null || isShutdown.get()) {
            return false;
        }
        
        // Prevent memory explosion
        if (lastActionTime.size() > MAX_ENTRIES) {
            cleanup();
        }
        
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastActionTime.get(key);
        
        if (lastTime == null || (currentTime - lastTime) >= cooldownMs) {
            lastActionTime.put(key, currentTime);
            return true;
        }
        
        return false;
    }
    
    /**
     * Lấy thời gian còn lại trước khi có thể thực hiện action tiếp theo
     * @param key Unique key cho action
     * @return Thời gian còn lại (ms), 0 nếu có thể thực hiện ngay
     */
    public long getRemainingCooldown(String key) {
        if (key == null || isShutdown.get()) {
            return 0;
        }
        
        Long lastTime = lastActionTime.get(key);
        if (lastTime == null) {
            return 0;
        }
        
        long elapsed = System.currentTimeMillis() - lastTime;
        return Math.max(0, cooldownMs - elapsed);
    }
    
    /**
     * Clear old entries để tránh memory leak
     */
    public void cleanup() {
        if (isShutdown.get()) {
            return;
        }
        
        try {
            long currentTime = System.currentTimeMillis();
            long expireTime = cooldownMs * 3; // Keep entries for 3x cooldown duration
            
            // Remove expired entries efficiently
            lastActionTime.entrySet().removeIf(entry -> 
                (currentTime - entry.getValue()) > expireTime);
                
        } catch (Exception e) {
            // Log error but don't crash
            System.err.println("Error during RateLimiter cleanup: " + e.getMessage());
        }
    }
    
    /**
     * Shutdown cleanup executor to prevent memory leaks
     */
    public void shutdown() {
        if (isShutdown.compareAndSet(false, true)) {
            try {
                cleanupExecutor.shutdown();
                if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    cleanupExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                cleanupExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            } finally {
                lastActionTime.clear();
            }
        }
    }
    
    /**
     * Get current cache size for monitoring
     */
    public int getCacheSize() {
        return lastActionTime.size();
    }
}