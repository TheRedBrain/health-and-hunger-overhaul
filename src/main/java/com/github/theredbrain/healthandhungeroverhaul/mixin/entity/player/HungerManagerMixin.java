package com.github.theredbrain.healthandhungeroverhaul.mixin.entity.player;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    /**
     * @author TheRedBrain
     * @reason the hunger system got removed
     */
    @Overwrite
    public void update(PlayerEntity player) {
    }

    /**
     * @author TheRedBrain
     * @reason the hunger system got removed
     */
    @Overwrite
    public void addExhaustion(float exhaustion) {
    }

}
