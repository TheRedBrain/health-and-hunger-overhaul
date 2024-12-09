package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

	@Shadow
	protected abstract int getHeartCount(LivingEntity entity);

	@Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
	private void healthregenerationoverhaul$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		var clientConfig = HealthRegenerationOverhaulClient.CLIENT_CONFIG;
		if (clientConfig.enable_alternative_health_bar) {
			ci.cancel();
		}
	}

	// effectively disables rendering of the food bar
	@Redirect(
			method = "renderStatusBars",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/hud/InGameHud;getHeartCount(Lnet/minecraft/entity/LivingEntity;)I"
			)
	)
	public int healthregenerationoverhaul$redirect_getHeartCount(InGameHud instance, LivingEntity entity) {
		return HealthRegenerationOverhaul.SERVER_CONFIG.disable_vanilla_food_system ? 1 : this.getHeartCount(entity);
	}
}