package com.github.theredbrain.healthandhungeroverhaul.mixin.client.gui.hud;

import com.github.theredbrain.healthandhungeroverhaul.HealthAndHungerOverhaulClient;
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

    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Unique
    private static final Identifier BARS_TEXTURE = new Identifier("textures/gui/bars.png");

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void foodoverhaul$renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        var clientConfig = HealthAndHungerOverhaulClient.clientConfig;
        if (clientConfig.enable_alternative_health_bar) {
            health = MathHelper.ceil(player.getHealth());
            maxHealth = MathHelper.ceil(player.getMaxHealth());

            int attributeBarX = this.scaledWidth / 2 + clientConfig.health_bar_x_offset;
            int attributeBarY = this.scaledHeight + clientConfig.health_bar_y_offset;
            int health_bar_additional_length = clientConfig.health_bar_additional_length;
            int attributeBarNumberX;
            int attributeBarNumberY;
            int normalizedHealthRatio = (int) (((double) health / Math.max(maxHealth, 1)) * (5 + clientConfig.health_bar_additional_length + 5));

            if (maxHealth > 0 && (health < maxHealth || clientConfig.show_full_health_bar)) {

                // background
                context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 20, 5, 5, 256, 256);
                if (health_bar_additional_length > 0) {
                    for (int i = 0; i < health_bar_additional_length; i++) {
                        context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + i, attributeBarY, 5, 20, 1, 5, 256, 256);
                    }
                }
                context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + health_bar_additional_length, attributeBarY, 177, 20, 5, 5, 256, 256);

                // foreground
                if (normalizedHealthRatio > 0) {
                    context.drawTexture(BARS_TEXTURE, attributeBarX, attributeBarY, 0, 25, Math.min(5, normalizedHealthRatio), 5, 256, 256);
                    if (normalizedHealthRatio > 5) {
                        if (health_bar_additional_length > 0) {
                            for (int i = 5; i < Math.min(5 + health_bar_additional_length, normalizedHealthRatio); i++) {
                                context.drawTexture(BARS_TEXTURE, attributeBarX + i, attributeBarY, 5, 25, 1, 5, 256, 256);
                            }
                        }
                    }
                    if (normalizedHealthRatio > (5 + health_bar_additional_length)) {
                        context.drawTexture(BARS_TEXTURE, attributeBarX + 5 + health_bar_additional_length, attributeBarY, 177, 25, Math.min(5, normalizedHealthRatio - 5 - health_bar_additional_length), 5, 256, 256);
                    }
                }

                if (clientConfig.show_health_bar_number) {
                    this.client.getProfiler().swap("health_bar_number");
                    String string = String.valueOf(health);
                    attributeBarNumberX = (this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2 + clientConfig.health_bar_number_x_offset;
                    attributeBarNumberY = this.scaledHeight + clientConfig.health_bar_number_y_offset;
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
    public int foodoverhaul$redirect_getHeartCount(InGameHud instance, LivingEntity entity) {
        return 1;
    }



}
