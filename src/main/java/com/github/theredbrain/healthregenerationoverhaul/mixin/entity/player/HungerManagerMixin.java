package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.player;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {

    /**
     * @author TheRedBrain
     */
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(PlayerEntity player, CallbackInfo ci) {
        if (HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system) {
            ci.cancel();
        }
    }

    /**
     * @author TheRedBrain
     */
    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    public void addExhaustion(float exhaustion, CallbackInfo ci) {
        if (HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system) {
            ci.cancel();
        }
    }

}
