package com.github.theredbrain.healthregenerationoverhaul.registry;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.config.ClientConfig;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import com.github.theredbrain.resourcebarapi.ResourceBarAPIClient;
import me.fzzyhmstrs.fzzy_config.api.ConfigApi;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ClientEventsRegistry {
	private static final String RESOURCE_BAR_IDENTIFIER_STRING = HealthRegenerationOverhaul.MOD_ID + ":health";

	public static void initializeClientEvents() {
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			PlayerEntity playerEntity = minecraftClient.player;
			ClientConfig clientConfig = HealthRegenerationOverhaulClient.CLIENT_CONFIG;

			if (playerEntity != null) {
				double health = playerEntity.getHealth();
				double maxHealth = playerEntity.getMaxHealth();

				ResourceBarAPIClient.drawResourceBar(
						minecraftClient,
						minecraftClient.textRenderer,
						matrixStack,
						RESOURCE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-91,
								-45,
								5,
								182,
								5,
								182,
								5,
								182,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_background.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_decrease_animation.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_animation.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_value.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_reserved.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_overlay.png"),
								null
						},
						clientConfig.enable_alternative_health_bar && maxHealth > 0 && (health < maxHealth || clientConfig.show_full_health_bar) && !playerEntity.isCreative(),
						health,
						maxHealth,
						MathHelper.ceil(((HealthRegeneratingEntity) playerEntity).healthregenerationoverhaul$getRegeneratedHealth()),
						MathHelper.ceil(((HealthRegeneratingEntity) playerEntity).healthregenerationoverhaul$getUnreservedHealth()),
						clientConfig.positionSettings.origin,
						clientConfig.positionSettings.offsets_x,
						clientConfig.positionSettings.offsets_y,
						0,
						0,
						clientConfig.fill_direction,
						clientConfig.textureSettings.backgroundTextureSettings.texture_heights,
						clientConfig.textureSettings.backgroundTextureSettings.texture_widths,
						clientConfig.textureSettings.backgroundTextureSettings.texture_ids,
						clientConfig.textureSettings.progressTextureSettings.offset_x,
						clientConfig.textureSettings.progressTextureSettings.offset_y,
						clientConfig.textureSettings.progressTextureSettings.texture_heights,
						clientConfig.textureSettings.progressTextureSettings.texture_widths,
						clientConfig.textureSettings.progressTextureSettings.progress_decrease_animation_texture_ids,
						clientConfig.textureSettings.progressTextureSettings.progress_increase_animation_texture_ids,
						clientConfig.textureSettings.progressTextureSettings.progress_increase_value_texture_ids,
						clientConfig.textureSettings.progressTextureSettings.progress_texture_ids,
						clientConfig.textureSettings.reservedTextureSettings.offset_x,
						clientConfig.textureSettings.reservedTextureSettings.offset_y,
						clientConfig.textureSettings.reservedTextureSettings.texture_heights,
						clientConfig.textureSettings.reservedTextureSettings.texture_widths,
						clientConfig.textureSettings.reservedTextureSettings.texture_ids,
						clientConfig.show_current_value_overlay,
						clientConfig.textureSettings.overlayTextureSettings.offset_x,
						clientConfig.textureSettings.overlayTextureSettings.offset_y,
						clientConfig.textureSettings.overlayTextureSettings.texture_heights,
						clientConfig.textureSettings.overlayTextureSettings.texture_widths,
						clientConfig.textureSettings.overlayTextureSettings.texture_ids,
						clientConfig.show_icon && maxHealth > 0,
						clientConfig.iconTextureSettings.offset_x,
						clientConfig.iconTextureSettings.offset_y,
						clientConfig.iconTextureSettings.texture_heights,
						clientConfig.iconTextureSettings.texture_widths,
						clientConfig.iconTextureSettings.texture_ids,
						clientConfig.enable_smooth_animation,
						clientConfig.animationSettings.animation_interval,
						clientConfig.animationSettings.max_value_change_is_animated,
						clientConfig.show_number && maxHealth > 0 && (health < maxHealth || clientConfig.numberSettings.show_when_health_full),
						clientConfig.numberSettings.show_max_value,
						clientConfig.numberSettings.offset_x,
						clientConfig.numberSettings.offset_y,
						clientConfig.numberSettings.color.toInt()
				);
			}
		});
		ConfigApi.event().onUpdateClient((identifier, config) -> {
			if (identifier.equals(Identifier.of(HealthRegenerationOverhaul.MOD_ID, "client"))) {
				ResourceBarAPIClient.clearCache(
						RESOURCE_BAR_IDENTIFIER_STRING,
						new double[]{
								-1,
								-1,
								0,
								-91,
								-45,
								5,
								182,
								5,
								182,
								5,
								182,
								5,
								5,
								0,
								0
						},
						new Identifier[]{
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_background.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_decrease_animation.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_animation.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress_increase_value.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_progress.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_reserved.png"),
								Identifier.of("healthregenerationoverhaul", "textures/gui/sprites/hud/horizontal_health_overlay.png"),
								null
						}
				);
			}
		});
	}
}
