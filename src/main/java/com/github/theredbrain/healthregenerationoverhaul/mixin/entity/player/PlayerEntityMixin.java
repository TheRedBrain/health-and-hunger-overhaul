package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.entity.HealthUsingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements HealthUsingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public float healthregenerationoverhaul$getRegeneratedHealth() {
        return Math.max(this.healthregenerationoverhaul$getHealthRegeneration(), (this.getServer() != null && this.getServer().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION) ? 1.0F : 0.0F));
    }

}
