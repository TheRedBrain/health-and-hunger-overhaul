package com.github.theredbrain.healthregenerationoverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaulClient;
import com.github.theredbrain.healthregenerationoverhaul.config.ClientConfig;
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
    private static final Identifier[] BURN_TEXTURES = {
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_background.png"),
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_progress.png"),
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/horizontal_health_overlay.png"),
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/vertical_health_background.png"),
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/vertical_health_progress.png"),
            HealthRegenerationOverhaul.identifier("textures/gui/sprites/hud/vertical_health_overlay.png")
    };

    @Unique
    private int oldNormalizedHealthRatio = -1;

    @Unique
    private int oldMaxHealth = -1;

    @Unique
    private int healthBarAnimationCounter = 0;

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void healthregenerationoverhaul$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        if (HealthRegenerationOverhaulClient.clientConfig.enable_alternative_health_bar) {
            this.healthregenerationoverhaul$renderAlternativeHealthBar(context, player);
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

    @Unique
    private void healthregenerationoverhaul$renderAlternativeHealthBar(DrawContext context, PlayerEntity player) {

        var clientConfig = HealthRegenerationOverhaulClient.clientConfig;

        int health = MathHelper.ceil(player.getHealth());
        int maxHealth = MathHelper.ceil(player.getMaxHealth());

        int healthBarX = context.getScaledWindowWidth() / 2 + clientConfig.x_offset;
        int healthBarY = context.getScaledWindowHeight() + clientConfig.y_offset;
        String healthBarNumberString = String.valueOf(health);
        int healthBarNumberX = (context.getScaledWindowWidth() - this.getTextRenderer().getWidth(healthBarNumberString)) / 2 + clientConfig.number_x_offset;
        int healthBarNumberY = context.getScaledWindowHeight() / 2 + clientConfig.number_y_offset;

        if (maxHealth > 0 && (health < maxHealth || clientConfig.show_full_health_bar)) {

            this.drawEffectBuildUpElement(
                    context,
                    health,
                    maxHealth,
                    MathHelper.ceil(((HealthRegeneratingEntity) player).healthregenerationoverhaul$getRegeneratedHealth()),
                    healthBarX,
                    healthBarY,
                    clientConfig.fill_direction,
                    clientConfig.background_middle_segment_amount,
                    clientConfig.horizontal_background_left_end_width,
                    clientConfig.horizontal_background_middle_segment_width,
                    clientConfig.horizontal_background_right_end_width,
                    clientConfig.horizontal_background_height,
                    clientConfig.vertical_background_width,
                    clientConfig.vertical_background_top_end_height,
                    clientConfig.vertical_background_middle_segment_height,
                    clientConfig.vertical_background_bottom_end_height,
                    clientConfig.progress_offset_x,
                    clientConfig.progress_offset_y,
                    clientConfig.progress_middle_segment_amount,
                    clientConfig.horizontal_progress_left_end_width,
                    clientConfig.horizontal_progress_middle_segment_width,
                    clientConfig.horizontal_progress_right_end_width,
                    clientConfig.horizontal_progress_height,
                    clientConfig.vertical_progress_width,
                    clientConfig.vertical_progress_top_end_height,
                    clientConfig.vertical_progress_middle_segment_height,
                    clientConfig.vertical_progress_bottom_end_height,
                    clientConfig.show_current_value_overlay,
                    clientConfig.overlay_offset_x,
                    clientConfig.overlay_offset_y,
                    clientConfig.horizontal_overlay_width,
                    clientConfig.horizontal_overlay_height,
                    clientConfig.vertical_overlay_width,
                    clientConfig.vertical_overlay_height,
                    clientConfig.enable_smooth_animation,
                    clientConfig.animation_interval
            );

            if (clientConfig.show_number) {
                this.client.getProfiler().swap("health_number");
                this.drawEffectBuildUpNumber(
                        context,
                        healthBarNumberString,
                        healthBarNumberX,
                        healthBarNumberY,
                        clientConfig.number_color
                );
            }
        }
    }

    @Unique
    private void drawEffectBuildUpElement(
            DrawContext context,
            int current_build_up,
            int max_build_up,
            int build_up_reduction,
            int build_up_element_x,
            int build_up_element_y,
            ClientConfig.FillDirection fill_direction,
            int background_additional_middle_segment_amount,
            int horizontal_background_left_end_width,
            int horizontal_background_middle_segment_width,
            int horizontal_background_right_end_width,
            int horizontal_background_height,
            int vertical_background_width,
            int vertical_background_top_end_height,
            int vertical_background_middle_segment_height,
            int vertical_background_bottom_end_height,
            int progress_offset_x,
            int progress_offset_y,
            int progress_additional_middle_segment_amount,
            int horizontal_progress_left_end_width,
            int horizontal_progress_middle_segment_width,
            int horizontal_progress_right_end_width,
            int horizontal_progress_height,
            int vertical_progress_width,
            int vertical_progress_top_end_height,
            int vertical_progress_middle_segment_height,
            int vertical_progress_bottom_end_height,
            boolean show_current_value_overlay,
            int overlay_offset_x,
            int overlay_offset_y,
            int horizontal_overlay_width,
            int horizontal_overlay_height,
            int vertical_overlay_width,
            int vertical_overlay_height,
            boolean enable_smooth_animation,
            int animation_interval
    ) {

//		int backgroundBarLength;
        int progressBarLength;
        int backgroundTextureHeight;
        int backgroundTextureWidth;
        int backgroundMiddleSectionLength;
        int progressTextureHeight;
        int progressTextureWidth;
        int progressMiddleSectionLength;

        // region variable calculation
        if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
            backgroundTextureHeight = vertical_background_top_end_height + vertical_background_middle_segment_height + vertical_background_bottom_end_height;
            backgroundTextureWidth = vertical_background_width;
            progressTextureHeight = vertical_progress_top_end_height + vertical_progress_middle_segment_height + vertical_progress_bottom_end_height;
            progressTextureWidth = vertical_progress_width;
            backgroundMiddleSectionLength = background_additional_middle_segment_amount * vertical_background_middle_segment_height;
            progressMiddleSectionLength = progress_additional_middle_segment_amount * vertical_progress_middle_segment_height;
//			backgroundBarLength = vertical_background_top_end_height + backgroundMiddleSectionLength + vertical_background_bottom_end_height;
            progressBarLength = vertical_progress_top_end_height + progressMiddleSectionLength + vertical_progress_bottom_end_height;
        } else {
            backgroundTextureHeight = horizontal_background_height;
            backgroundTextureWidth = horizontal_background_left_end_width + horizontal_background_middle_segment_width + horizontal_background_right_end_width;
            progressTextureHeight = horizontal_progress_height;
            progressTextureWidth = horizontal_progress_left_end_width + horizontal_progress_middle_segment_width + horizontal_progress_right_end_width;
            backgroundMiddleSectionLength = background_additional_middle_segment_amount * horizontal_background_middle_segment_width;
            progressMiddleSectionLength = progress_additional_middle_segment_amount * horizontal_progress_middle_segment_width;
//			backgroundBarLength = horizontal_background_left_end_width + backgroundMiddleSectionLength + horizontal_background_right_end_width;
            progressBarLength = horizontal_progress_left_end_width + progressMiddleSectionLength + horizontal_progress_right_end_width;
        }
        // endregion variable calculation

        int normalizedBuildUpRatio = (int) (((double) current_build_up / Math.max(max_build_up, 1)) * (progressBarLength));

        if (this.oldMaxHealth != max_build_up) {
            this.oldMaxHealth = max_build_up;
            this.oldNormalizedHealthRatio = normalizedBuildUpRatio;
        }

        this.healthBarAnimationCounter = this.healthBarAnimationCounter + Math.max(1, build_up_reduction);

        if (this.oldNormalizedHealthRatio != normalizedBuildUpRatio && this.healthBarAnimationCounter > Math.max(0, animation_interval)) {
            boolean reduceOldRatio = this.oldNormalizedHealthRatio > normalizedBuildUpRatio;
            this.oldNormalizedHealthRatio = this.oldNormalizedHealthRatio + (reduceOldRatio ? -1 : 1);
            this.healthBarAnimationCounter = 0;
        }

        // region background
        // background
        if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP || fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
            context.drawTexture(BURN_TEXTURES[3], build_up_element_x, build_up_element_y, 0, 0, backgroundTextureWidth, vertical_background_top_end_height, backgroundTextureWidth, backgroundTextureHeight);
            if (background_additional_middle_segment_amount > 0) {
                for (int i = 0; i < background_additional_middle_segment_amount; i++) {
                    context.drawTexture(BURN_TEXTURES[3], build_up_element_x, build_up_element_y + vertical_background_top_end_height + (i * vertical_background_middle_segment_height), 0, vertical_background_top_end_height, backgroundTextureWidth, vertical_background_middle_segment_height, backgroundTextureWidth, backgroundTextureHeight);
                }
            }
            context.drawTexture(BURN_TEXTURES[3], build_up_element_x, build_up_element_y + vertical_background_top_end_height + backgroundMiddleSectionLength, 0, vertical_background_top_end_height + vertical_background_middle_segment_height, backgroundTextureWidth, vertical_background_bottom_end_height, backgroundTextureWidth, backgroundTextureHeight);
        } else {
            context.drawTexture(BURN_TEXTURES[0], build_up_element_x, build_up_element_y, 0, 0, horizontal_background_left_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
            if (background_additional_middle_segment_amount > 0) {
                for (int i = 0; i < background_additional_middle_segment_amount; i++) {
                    context.drawTexture(BURN_TEXTURES[0], build_up_element_x + horizontal_background_left_end_width + (i * horizontal_background_middle_segment_width), build_up_element_y, horizontal_background_left_end_width, 0, horizontal_background_middle_segment_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
                }
            }
            context.drawTexture(BURN_TEXTURES[0], build_up_element_x + horizontal_background_left_end_width + backgroundMiddleSectionLength, build_up_element_y, horizontal_background_left_end_width + horizontal_background_middle_segment_width, 0, horizontal_progress_right_end_width, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
        }
        // endregion background

        // progress
        int displayRatio = enable_smooth_animation ? this.oldNormalizedHealthRatio : normalizedBuildUpRatio;
        if (displayRatio > 0) {
            int ratioFirstPart;
            int ratioLastPart;
            int progressElementX = build_up_element_x + progress_offset_x;
            int progressElementY = build_up_element_y + progress_offset_y;

            if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
                // 1: bottom to top

                ratioFirstPart = Math.min(vertical_progress_bottom_end_height, displayRatio);
                ratioLastPart = Math.min(vertical_progress_top_end_height, displayRatio - vertical_progress_bottom_end_height - progressMiddleSectionLength);

                context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY + progressBarLength - ratioFirstPart, 0, progressTextureHeight - ratioFirstPart, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
                if (displayRatio > vertical_progress_bottom_end_height && background_additional_middle_segment_amount > 0) {
                    boolean breakDisplay = false;
                    for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
                        for (int j = 1; j <= vertical_progress_middle_segment_height; j++) {
                            int currentTextureY = vertical_progress_bottom_end_height + (i * vertical_progress_middle_segment_height) + j;
                            if (currentTextureY > displayRatio) {
                                breakDisplay = true;
                                break;
                            }
                            context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY + progressBarLength - currentTextureY, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height - j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
                        }
                        if (breakDisplay) {
                            break;
                        }
                    }

                }
                if (displayRatio > (vertical_progress_bottom_end_height + progressMiddleSectionLength)) {
                    context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY + vertical_progress_top_end_height - ratioLastPart, 0, vertical_progress_top_end_height - ratioLastPart, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
                }
            }
            else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
                // 2: right to left

                ratioFirstPart = Math.min(horizontal_progress_right_end_width, displayRatio);
                ratioLastPart = Math.min(horizontal_progress_left_end_width, displayRatio - horizontal_progress_right_end_width - progressMiddleSectionLength);

                context.drawTexture(BURN_TEXTURES[1], progressElementX + progressBarLength - ratioFirstPart, progressElementY, progressTextureWidth - ratioFirstPart, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                if (displayRatio > horizontal_progress_right_end_width && background_additional_middle_segment_amount > 0) {
                    boolean breakDisplay = false;
                    for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
                        for (int j = 1; j <= horizontal_progress_middle_segment_width; j++) {
                            int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
                            if (currentTextureX > displayRatio) {
                                breakDisplay = true;
                                break;
                            }
                            context.drawTexture(BURN_TEXTURES[1], progressElementX + progressBarLength - currentTextureX, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width - j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                        }
                        if (breakDisplay) {
                            break;
                        }
                    }

                }
                if (displayRatio > (horizontal_progress_right_end_width + progressMiddleSectionLength)) {
                    context.drawTexture(BURN_TEXTURES[1], progressElementX + horizontal_progress_left_end_width - ratioLastPart, progressElementY, horizontal_progress_left_end_width - ratioLastPart, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                }
            }
            else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
                // 3: top to bottom

                ratioFirstPart = Math.min(vertical_progress_top_end_height, displayRatio);
                ratioLastPart = Math.min(vertical_progress_bottom_end_height, displayRatio - vertical_progress_top_end_height - progressMiddleSectionLength);

                context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY, 0, 0, progressTextureWidth, ratioFirstPart, progressTextureWidth, progressTextureHeight);
                if (displayRatio > vertical_progress_top_end_height && background_additional_middle_segment_amount > 0) {
                    boolean breakDisplay = false;
                    for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
                        for (int j = 0; j < vertical_progress_middle_segment_height; j++) {
                            int currentTextureY = vertical_progress_top_end_height + (i * vertical_progress_middle_segment_height) + j;
                            if (currentTextureY > displayRatio) {
                                breakDisplay = true;
                                break;
                            }
                            context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY + currentTextureY, 0, vertical_progress_top_end_height + j, progressTextureWidth, 1, progressTextureWidth, progressTextureHeight);
                        }
                        if (breakDisplay) {
                            break;
                        }
                    }
                }
                if (displayRatio > (vertical_progress_top_end_height + progressMiddleSectionLength)) {
                    context.drawTexture(BURN_TEXTURES[4], progressElementX, progressElementY + vertical_progress_top_end_height + progressMiddleSectionLength, 0, vertical_progress_top_end_height + vertical_progress_middle_segment_height, progressTextureWidth, ratioLastPart, progressTextureWidth, progressTextureHeight);
                }
            }
            else {
                // 0: left to right

                ratioFirstPart = Math.min(horizontal_progress_left_end_width, displayRatio);
                ratioLastPart = Math.min(horizontal_progress_right_end_width, displayRatio - horizontal_progress_left_end_width - progressMiddleSectionLength);

                context.drawTexture(BURN_TEXTURES[1], progressElementX, progressElementY, 0, 0, ratioFirstPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                if (displayRatio > horizontal_progress_left_end_width && background_additional_middle_segment_amount > 0) {
                    boolean breakDisplay = false;
                    for (int i = 0; i < progress_additional_middle_segment_amount; i++) {
                        for (int j = 0; j < horizontal_progress_middle_segment_width; j++) {
                            int currentTextureX = horizontal_progress_left_end_width + (i * horizontal_progress_middle_segment_width) + j;
                            if (currentTextureX > displayRatio) {
                                breakDisplay = true;
                                break;
                            }
                            context.drawTexture(BURN_TEXTURES[1], progressElementX + currentTextureX, progressElementY, horizontal_progress_left_end_width + j, 0, 1, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                        }
                        if (breakDisplay) {
                            break;
                        }
                    }
                }
                if (displayRatio > (horizontal_progress_left_end_width + progressMiddleSectionLength)) {
                    context.drawTexture(BURN_TEXTURES[1], progressElementX + horizontal_progress_left_end_width + progressMiddleSectionLength, progressElementY, horizontal_progress_left_end_width + horizontal_progress_middle_segment_width, 0, ratioLastPart, progressTextureHeight, progressTextureWidth, progressTextureHeight);
                }
            }

            // overlay
            if (show_current_value_overlay) {
                int overlayElementX = progressElementX + overlay_offset_x;
                int overlayElementY = progressElementY + overlay_offset_y;
                if (fill_direction == ClientConfig.FillDirection.BOTTOM_TO_TOP) {
                    // 1: bottom to top
                    if (current_build_up > 0 && current_build_up < max_build_up) {
                        context.drawTexture(BURN_TEXTURES[5], overlayElementX, overlayElementY + progressBarLength - normalizedBuildUpRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
                    }
                } else if (fill_direction == ClientConfig.FillDirection.RIGHT_TO_LEFT) {
                    // 2: right to left
                    if (current_build_up > 0 && current_build_up < max_build_up) {
                        context.drawTexture(BURN_TEXTURES[2], overlayElementX + progressBarLength - normalizedBuildUpRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
                    }
                } else if (fill_direction == ClientConfig.FillDirection.TOP_TO_BOTTOM) {
                    // 3: top to bottom
                    if (current_build_up > 0 && current_build_up < max_build_up) {
                        context.drawTexture(BURN_TEXTURES[5], overlayElementX, overlayElementY + normalizedBuildUpRatio, 0, 0, vertical_overlay_width, vertical_overlay_height, vertical_overlay_width, horizontal_overlay_height);
                    }
                } else {
                    // 0: left to right
                    if (current_build_up > 0 && current_build_up < max_build_up) {
                        context.drawTexture(BURN_TEXTURES[2], overlayElementX + normalizedBuildUpRatio, overlayElementY, 0, 0, horizontal_overlay_width, horizontal_overlay_height, horizontal_overlay_width, horizontal_overlay_height);
                    }
                }
            }
        }
    }

    @Unique
    private void drawEffectBuildUpNumber(
            DrawContext context,
            String buildUpBarNumberString,
            int build_up_bar_number_x,
            int build_up_bar_number_y,
            int build_up_bar_number_color
    ) {
        context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x + 1, build_up_bar_number_y, 0, false);
        context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x - 1, build_up_bar_number_y, 0, false);
        context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y + 1, 0, false);
        context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y - 1, 0, false);
        context.drawText(this.getTextRenderer(), buildUpBarNumberString, build_up_bar_number_x, build_up_bar_number_y, build_up_bar_number_color, false);
    }
}
