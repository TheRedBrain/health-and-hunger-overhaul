package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.entity.HealthRegeneratingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow protected abstract int getHeartCount(LivingEntity entity);

    @Unique
    private static final Identifier BOSS_BAR_RED_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/red_progress.png");

    @Unique
    private static final Identifier BOSS_BAR_RED_BACKGROUND_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/red_background.png");

    @Unique
    private static final Identifier NOTCHED_20_PROGRESS_TEXTURE = Identifier.ofVanilla("textures/gui/sprites/boss_bar/notched_20_progress.png");

    @Unique
    private int oldNormalizedHealthRatio = -1;

    @Unique
    private int oldMaxHealth = -1;

    @Unique
    private int healthBarAnimationCounter = 0;

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void healthregenerationoverhaul$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        var clientConfig = HealthRegenerationOverhaulClient.clientConfig;
        if (clientConfig.enable_alternative_health_bar) {
            health = MathHelper.ceil(player.getHealth());
            maxHealth = MathHelper.ceil(player.getMaxHealth());

            int attributeBarX = context.getScaledWindowWidth() / 2 + clientConfig.health_bar_x_offset;
            int attributeBarY = context.getScaledWindowHeight() + clientConfig.health_bar_y_offset;
            int health_bar_additional_length = clientConfig.health_bar_additional_length;
            int attributeBarNumberX;
            int attributeBarNumberY;
            int normalizedHealthRatio = (int) (((double) health / Math.max(maxHealth, 1)) * (5 + clientConfig.health_bar_additional_length + 5));

            if (this.oldMaxHealth != (int) maxHealth) {
                this.oldMaxHealth = (int) maxHealth;
                this.oldNormalizedHealthRatio = normalizedHealthRatio;
            }

            this.healthBarAnimationCounter = this.healthBarAnimationCounter + Math.max(1, MathHelper.ceil(((HealthRegeneratingEntity) player).healthregenerationoverhaul$getRegeneratedHealth()));

            if (this.oldNormalizedHealthRatio != normalizedHealthRatio && this.healthBarAnimationCounter > Math.max(0, clientConfig.health_bar_animation_interval)) {
                boolean reduceOldRatio = this.oldNormalizedHealthRatio > normalizedHealthRatio;
                this.oldNormalizedHealthRatio = this.oldNormalizedHealthRatio + (reduceOldRatio ? -1 : 1);
                this.healthBarAnimationCounter = 0;
            }

            if (maxHealth > 0 && (health < maxHealth || clientConfig.show_full_health_bar)) {

                // background
                context.drawTexture(BOSS_BAR_RED_BACKGROUND_TEXTURE, attributeBarX, attributeBarY, 0, 0, 5, 5, 182, 5);
                if (health_bar_additional_length > 0) {
                    for (int i = 0; i < health_bar_additional_length; i++) {
                        context.drawTexture(BOSS_BAR_RED_BACKGROUND_TEXTURE, attributeBarX + 5 + i, attributeBarY, 5, 0, 1, 5, 182, 5);
                    }
                }
                context.drawTexture(BOSS_BAR_RED_BACKGROUND_TEXTURE, attributeBarX + 5 + health_bar_additional_length, attributeBarY, 177, 0, 5, 5, 182, 5);

                // foreground
                int displayRatio = clientConfig.enable_smooth_animation ? this.oldNormalizedHealthRatio : normalizedHealthRatio;
                if (displayRatio > 0) {
                    this.client.getProfiler().swap("health_bar_foreground");
                    context.drawTexture(BOSS_BAR_RED_PROGRESS_TEXTURE, attributeBarX, attributeBarY, 0, 25, Math.min(5, displayRatio), 5, 182, 5);
                    if (displayRatio > 5) {
                        if (health_bar_additional_length > 0) {
                            for (int i = 5; i < Math.min(5 + health_bar_additional_length, displayRatio); i++) {
                                context.drawTexture(BOSS_BAR_RED_PROGRESS_TEXTURE, attributeBarX + i, attributeBarY, 5, 25, 1, 5, 182, 5);
                            }
                        }
                    }
                    if (displayRatio > (5 + health_bar_additional_length)) {
                        context.drawTexture(BOSS_BAR_RED_PROGRESS_TEXTURE, attributeBarX + 5 + health_bar_additional_length, attributeBarY, 177, 25, Math.min(5, displayRatio - 5 - health_bar_additional_length), 5, 182, 5);
                    }
                }

                // overlay
                if (clientConfig.enable_smooth_animation && clientConfig.show_current_value_overlay) {
                    if (health > 0 && health < maxHealth) {
                        this.client.getProfiler().swap("health_bar_overlay");
                        context.drawTexture(NOTCHED_20_PROGRESS_TEXTURE, attributeBarX + normalizedHealthRatio - 2, attributeBarY + 1, 7, 1, 5, 3, 182, 5);
                    }
                }

                if (clientConfig.show_health_bar_number) {
                    this.client.getProfiler().swap("health_bar_number");
                    String string = String.valueOf(health);
                    attributeBarNumberX = (context.getScaledWindowWidth() - this.getTextRenderer().getWidth(string)) / 2 + clientConfig.health_bar_number_x_offset;
                    attributeBarNumberY = context.getScaledWindowHeight() + clientConfig.health_bar_number_y_offset;
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX + 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX - 1, attributeBarNumberY, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY + 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY - 1, 0, false);
                    context.drawText(this.getTextRenderer(), string, attributeBarNumberX, attributeBarNumberY, clientConfig.health_bar_number_color, false);
                }
            }
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
        return HealthRegenerationOverhaul.serverConfig.disable_vanilla_food_system ? 1 : this.getHeartCount(entity);
    }
}
