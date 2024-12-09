package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HealthRegeneratingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V", shift = At.Shift.AFTER))
    protected void healthregenerationoverhaul$applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        if (amount < 0) {
            this.healthregenerationoverhaul$resetTickCounters();
        }
    }

    @Override
    public float healthregenerationoverhaul$getRegeneratedHealth() {
        return this.healthregenerationoverhaul$getHealthRegeneration() + (this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) ? 1.0F : 0.0F);
    }

}
