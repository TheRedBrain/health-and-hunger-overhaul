package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HealthRegeneratingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V"))
    protected void healthregenerationoverhaul$redirect_setHealth(PlayerEntity instance, float v, @Local(argsOnly = true) float amount) {
        instance.heal(-amount);
    }

    @Override
    public float healthregenerationoverhaul$getRegeneratedHealth() {
        return Math.max(this.healthregenerationoverhaul$getHealthRegeneration(), (this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) ? 1.0F : 0.0F));
    }

}
