package com.github.theredbrain.healthregenerationoverhaul.mixin.entity.attribute;

import com.github.theredbrain.healthregenerationoverhaul.HealthRegenerationOverhaul;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityAttributes.class)
public class EntityAttributesMixin {
	static {
		HealthRegenerationOverhaul.HEALTH_REGENERATION = Registry.registerReference(Registries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("generic.health_regeneration"), new ClampedEntityAttribute("attribute.name.generic.health_regeneration", 0.0, 0.0, 1024.0).setTracked(true));
		HealthRegenerationOverhaul.HEALTH_TICK_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("generic.health_tick_threshold"), new ClampedEntityAttribute("attribute.name.generic.health_tick_threshold", 100.0, 0.0, 1024.0).setTracked(true));
		HealthRegenerationOverhaul.HEALTH_REGENERATION_DELAY_THRESHOLD = Registry.registerReference(Registries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("generic.health_regeneration_delay_threshold"), new ClampedEntityAttribute("attribute.name.generic.health_regeneration_delay_threshold", 100.0, 0.0, 1024.0).setTracked(true));
		HealthRegenerationOverhaul.RESERVED_HEALTH = Registry.registerReference(Registries.ATTRIBUTE, HealthRegenerationOverhaul.identifier("generic.reserved_health"), new ClampedEntityAttribute("attribute.name.generic.reserved_health", 0.0, 0.0, 100.0).setTracked(true));
	}
}
