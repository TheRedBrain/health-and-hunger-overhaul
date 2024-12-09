package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.attribute;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
    @Shadow
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        throw new AssertionError();
    }

    static {
        HealthRegenerationOverhaul.HEALTH_REGENERATION = register(HealthRegenerationOverhaul.MOD_ID + ":generic.health_regeneration", new ClampedEntityAttribute("attribute.name.generic.health_regeneration", 0.0, -1024.0, 1024.0).setTracked(true));
        HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD = register(HealthRegenerationOverhaul.MOD_ID + ":generic.health_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.health_tick_threshold", 100.0, 0.0, 1024.0).setTracked(true));
        HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD = register(HealthRegenerationOverhaul.MOD_ID + ":generic.health_regeneration_delay_threshold", new ClampedEntityAttribute("attribute.name.generic.health_regeneration_delay_threshold", 100.0F, 0.0F, 1024.0F).setTracked(true));
        HealthRegenerationOverhaul.RESERVED_HEALTH = register(HealthRegenerationOverhaul.MOD_ID + ":generic.reserved_health", new ClampedEntityAttribute("attribute.name.generic.reserved_health", 0.0F, 0.0F, 100.0F).setTracked(true));
    }
}
