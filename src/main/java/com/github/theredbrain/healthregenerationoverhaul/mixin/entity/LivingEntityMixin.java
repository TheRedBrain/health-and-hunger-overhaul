package com.github.theredbrain.healthregenerationoverhaul.mixin.entity;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements HealthRegeneratingEntity {

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();

    @Shadow public abstract void heal(float amount);

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    @Unique
    private int healthTickTimer = 0;

    @Unique
    private int healthRegenerationDelayTimer = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void healthregenerationoverhaul$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(HealthRegenerationOverhaul.HEALTH_REGENERATION)
                .add(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD)
                .add(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD)
        ;
    }

    @Inject(method = "heal", at = @At("RETURN"))
    public void healthregenerationoverhaul$heal(float amount, CallbackInfo ci) {
        if (amount < 0) {
            this.healthRegenerationDelayTimer = 0;
            this.healthTickTimer = 0;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void healthregenerationoverhaul$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {

            this.healthTickTimer++;

            if (this.healthRegenerationDelayTimer <= this.healthregenerationoverhaul$getHealthRegenerationDelayThreshold()) {
                this.healthRegenerationDelayTimer++;
            }

            if (
                    this.healthTickTimer >= this.healthregenerationoverhaul$getHealthTickThreshold()
                    && this.healthRegenerationDelayTimer > this.healthregenerationoverhaul$getHealthRegenerationDelayThreshold()
            ) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(this.healthregenerationoverhaul$getRegeneratedHealth());
                } else if (this.getHealth() > this.getMaxHealth()) {
                    this.setHealth(this.getMaxHealth());
                }
                this.healthTickTimer = 0;
            }
        }
    }

    @Override
    public int healthregenerationoverhaul$getHealthRegenerationDelayThreshold() {
        return (int) this.getAttributeValue(HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD);
    }

    @Override
    public int healthregenerationoverhaul$getHealthTickThreshold() {
        return (int) this.getAttributeValue(HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD);
    }

    @Override
    public float healthregenerationoverhaul$getRegeneratedHealth() {
        return this.healthregenerationoverhaul$getHealthRegeneration();
    }

    @Override
    public float healthregenerationoverhaul$getHealthRegeneration() {
        return (float) this.getAttributeValue(HealthRegenerationOverhaul.HEALTH_REGENERATION);
    }
}
