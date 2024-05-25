package com.github.theredbrain.healthandhungeroverhaul.mixin.entity.attribute;

import com.github.theredbrain.healthandhungeroverhaul.HealthAndHungerOverhaul;
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
        HealthAndHungerOverhaul.HEALTH_REGENERATION = register(HealthAndHungerOverhaul.MOD_ID + ":generic.health_regeneration", new ClampedEntityAttribute("attribute.name.generic.health_regeneration", 0.0, 0.0, 1024.0).setTracked(true));
        HealthAndHungerOverhaul.HEALTH_TICK_THRESHOLD = register(HealthAndHungerOverhaul.MOD_ID + ":generic.health_tick_threshold", new ClampedEntityAttribute("attribute.name.generic.health_tick_threshold", 100.0, 0.0, 1024.0).setTracked(true));
    }
}
