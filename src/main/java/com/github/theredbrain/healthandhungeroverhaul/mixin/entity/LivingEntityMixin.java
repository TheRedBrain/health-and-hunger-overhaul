package com.github.theredbrain.healthandhungeroverhaul.mixin.entity;

import com.github.theredbrain.healthandhungeroverhaul.HealthAndHungerOverhaul;
import com.github.theredbrain.healthandhungeroverhaul.entity.HealthUsingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements HealthUsingEntity {

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract double getAttributeValue(EntityAttribute attribute);

    @Shadow public abstract void heal(float amount);

    @Unique
    private int healthTickTimer = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void betteradventuremode$createLivingAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(HealthAndHungerOverhaul.HEALTH_REGENERATION)
                .add(HealthAndHungerOverhaul.HEALTH_TICK_THRESHOLD)
        ;
    }

    @Inject(method = "heal", at = @At("RETURN"))
    public void betteradventuremode$heal(float amount, CallbackInfo ci) {
        if (amount < 0) {
            this.healthTickTimer = 0;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void betteradventuremode$tick(CallbackInfo ci) {
        if (!this.getWorld().isClient) {

            this.healthTickTimer++;

            if (this.healthTickTimer >= this.healthandhungeroverhaul$getHealthTickThreshold()) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(this.healthandhungeroverhaul$getRegeneratedHealth());
                } else if (this.getHealth() > this.getMaxHealth()) {
                    this.setHealth(this.getMaxHealth());
                }
                this.healthTickTimer = 0;
            }
        }
    }

    @Override
    public int healthandhungeroverhaul$getHealthTickThreshold() {
        return (int) this.getAttributeValue(HealthAndHungerOverhaul.HEALTH_TICK_THRESHOLD);
    }

    @Override
    public float healthandhungeroverhaul$getRegeneratedHealth() {
        return this.healthandhungeroverhaul$getHealthRegeneration();
    }

    @Override
    public float healthandhungeroverhaul$getHealthRegeneration() {
        return (float) this.getAttributeValue(HealthAndHungerOverhaul.HEALTH_REGENERATION);
    }
}
