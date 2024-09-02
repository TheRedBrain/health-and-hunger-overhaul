package com.github.theredbrain.healthregenerationoverhaul.entity;

public interface HealthRegeneratingEntity {
    int healthregenerationoverhaul$getHealthTickThreshold();
    float healthregenerationoverhaul$getRegeneratedHealth();
    float healthregenerationoverhaul$getHealthRegeneration();
    int healthregenerationoverhaul$getHealthRegenerationDelayThreshold();
    void healthregenerationoverhaul$resetTickCounters();
}
